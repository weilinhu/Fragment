# Fragment

============
### Feagment常见问题以及正确使用姿势
============
 1.重影问题
   原因：使用 Fragment 的状态保存，当系统内存不足，Fragment 的宿主 Activity 回收的时候，Fragment 的实例并没有随之被回收。Activity 被系统回收时，会主动调用 onSaveInstance() 方法来保存视图层（View Hierarchy），所以当 Activity 通过导航再次被重建时，之前被实例化过的 Fragment 依然会出现在 Activity 中，此时的 FragmentTransaction 中的相当于又再次 add 了 fragment   进去的，hide()和show()方法对之前保存的fragment已经失效了。综上这些因素导致了多个Fragment重叠在一起。
   <br>
解决方案一:
- 给每个 Fragment 加一个 Tag
- 在 onCreate(Bundle savedInstanceState) 中判断 Bundle savedInstanceState 是否不为空
- 不为空则进行 find Tag，重新给几个 frament 赋值
这样子仍是对之前保存的 fragment 操作，成功解决了重叠的问题。
方案二:
Activity 中的 onSaveInstanceState() 里面有一句super.onSaveInstanceState(outState);，Google 对于这句话的解释是 “Always call the superclass so it can save the view hierarchy state”，大概意思是“总是执行这句代码来调用父类去保存视图层的状态”。通过注释掉这句话，这样主 Activity 因为种种原因被回收的时候就不会保存之前的 fragment state，也可以成功解决重叠的问题。

- 避免错误操作导致Fragment的视图重叠
  视图重叠是因为fragment已经发生了内存泄漏。activity的创建、销毁完全托管到systemserver（ams），而fragment一般是手动new一个对象再add到systemserver，所以这里就有问题了。fragment生命周期开始于add，结束于remove。不管app中间怎么变化，崩溃、进程被回收，只要没有remove，android框架都会自动创建、恢复fragment的状态。

***
Fragment的使用原理如下：<br/>
1.获得FragmentTransaction<br/>
2.使用FragmentTransacion进行add、replace、show、hide...一个fragment<br/>
3.操作完毕之后，调用FragmentTransaction.commit()方法便可完成操作<br/>

***
# Fragment的使用
## 封装BaseFragment基类
     例如为了实例化View,抽象一个getLayoutId方法，子类无需关心具体的创建操作，父类来做View的创建处理。同时可以提供一个afterCreate抽象函数，在初始化完成之后调用,子类可以做一些初始化的操作，你也可以添加一些常用的方法在基类，例如ShowToast().

     public abstract class BaseFragment extends Fragment {
     protected View mRootView;
     
     @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null == mRootView){
           mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        return mRootView;
     }
     
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        afterCreate(savedInstanceState);
    }
     
     protected abstract int getLayoutId();
    
     protected abstract void afterCreate(Bundle savedInstanceState); 
     }

## 使用静态工厂方法newInstance(...)来获取Fragment实例
  好处是接收确切的参数，返回一个Fragment实例，避免了在创建Fragment的时候无法在类外部知道所需参数的问题，在合作开发的时候特别有用。还有就是Fragment推荐使用setArguments来传递参数，避免在横竖屏切换的时候Fragment自动调用自己的无参构造函数，导致数据丢失。
  

    public static Fragment newInstance(String cityName) {
     Bundle args = new Bundle();
    args.putString(cityName,"cityName");
    Fragment fragment = new Fragment();
    fragment.setArguments(args);
    return fragment;
    }

##Fragment状态保存/现场恢复
为了让你的代码更加清晰和稳定，最好区分清楚fragment状态保存和view状态保存，如果某个属性属于View，则不要在Fragment中做它的状态保存，除非属性属于Fragment。每一个自定义View都有义务实现状态的保存，可以像EditText一样，设置一个开关来选择是否保存比如说：`android:freezeText="true/false"。`

    public class CustomView extends View {
     
     ...
     
     @Override
     public Parcelable onSaveInstanceState() {
         Bundle bundle = new Bundle();
        // 在这里保存当前状态
        return bundle;
    }
     
     @Override
     public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        // 恢复保存的状态
      }
     
     ...
     
     }
 处理fragment状态保存，例如保存从服务器获取的数据。

    private String serverData;
      
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("data", serverData);
    }
     
     @Override
     public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);
        serverData = savedInstanceState.getString("data");
     }

## 避免错误操作导致Fragment的视图重叠
在add或者replace的时候，调用含有TAG参数的那个方法，之后再add相同TAG的Fragment的话，之前的会被替换掉，也就不会同时出现多个相同的Fragment了。

    public class WeatherFragment extends Fragment {
     //TAG
    public static final String TAG = WeatherFragment.class.getSimpleName();

不过为了最大限度的重用，可以在Activity的onCreate(Bundle savedInstanceState)中判断savedInstanceState是否不为空；不为空的话，先用getSupportFragmentManager(). findFragmentByTag()找一下，找到实例就不用再次创建。

     fragment = null;
    if(savedInstanceState!=null){
    fragment = getSupportFragmentManager().findFragmentByTag(Fragment.TAG);
     }
     
     if(fragment == null){
     fragment = Fragment.newInstance(...);
     }
## Fragment里监听虚拟按键和实体按键的返回事件

      mRootView.setFocusable(true);
     mRootView.setFocusableInTouchMode(true);
    mRootView.setOnKeyListener(new View.OnKeyListener() {
     @Override
     public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //不一定是要触发返回栈，可以做一些其他的事情，我只是举个栗子。
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }
     });

## 使用最大化的DialogFragment来实现浮动层级视图

使用最大化的DialogFragment来显示详情页，此时并不需要提供一个具体的ContainerId即可显示，只需要FragementManager，因为详情页一般情况下在Phone上都是占据满屏幕的，用DialogFragment即可。<br/>

     EvaluateDialog dialog = new EvaluateDialog();  
                //注意setTargetFragment  
                dialog.setTargetFragment(ContentFragment.this, REQUEST_EVALUATE);  
                dialog.show(getFragmentManager(), EVALUATE_DIALOG);
                 // 判断是否设置了targetFragment  
        if (getTargetFragment() == null)  
            return;  
     
        Intent intent = new Intent();  
        intent.putExtra(RESPONSE_EVALUATE, mEvaluteVals[which]);  
        getTargetFragment().onActivityResult(ContentFragment.REQUEST_EVALUATE,  
                Activity.RESULT_OK, intent);  
                
##Fragment与Activity通信的最佳实践
 
 通过instanceof 多态实现或者暴露接口
 
 
      //返回数据
     getActivity().setResult(ListTitleFragment.REQUEST_DETAIL, intent); 
     //接收数据调用onActivityResult();


      if (getActivity() instanceof FOneBtnClickListener)  
         {  
             ((FOneBtnClickListener) getActivity()).onFOneBtnClick();  
        }  

## 判断一个页面该使用Fragment还是Activity

如果后一个页面不需要用到前一个页面的太多数据，推荐用Activity展示，否则最好用Fragment（ 当然这也不是绝对的）。
* * *
# 封装好的项目
<br>
 * [FragmentStack](https://github.com/Mr-wangyong/FragmentStack)

====
# 参考资料
- [ Android Fragment 真正的完全解析（上）](http://blog.csdn.net/lmj623565791/article/details/37970961)
- [ Android Fragment 真正的完全解析（下）](http://blog.csdn.net/lmj623565791/article/details/37992017#comments)
- [从Fragment被销毁看replace和add的区别](http://jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0324/2639.html)
- [Android Fragment完全解析，关于碎片你所需知道的一切](http://blog.csdn.net/guolin_blog/article/details/8881711)
- [Android Fragment应用实战，使用碎片向ActivityGroup说再见](http://blog.csdn.net/guolin_blog/article/details/13171191)
- [Android 屏幕旋转 处理 AsyncTask 和 ProgressDialog 的最佳方案](http://blog.csdn.net/lmj623565791/article/details/37936275)
- [Android 官方推荐 : DialogFragment 创建对话框](http://blog.csdn.net/lmj623565791/article/details/37815413)
- [ Android Fragment 你应该知道的一切](http://blog.csdn.net/lmj623565791/article/details/42628537#comments)
- [Android实战技巧：Fragment的那些坑](http://toughcoder.net/blog/2015/04/30/android-fragment-the-bad-parts/)
- [Android：Activity 与 Fragment 通信 (99%) 完美解决方案](http://gold.xitu.io/entry/56a87b2b2e958a0051906227?utm_source=leopost&utm_medium=20160128&utm_campaign=weibo)
- [微信ANDROID客户端-会话速度提升70%的背后](https://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=207548094&idx=1&sn=1a277620bc28349368b68ed98fbefebe&scene=1&srcid=lbTtbXQRHO0soAYOzzD2&key=dffc561732c22651cf13a2ee4f45620137344a21f83167444e033088f26e812fa6307ca51e115edcf9bacf54184fd6b1&ascene=0&uin=Mjc4MjU3MDQw&devicetype=iMac+MacBookPro11%2C2+OSX+OSX+10.10.5+build(14F27)&version=11020201&pass_ticket=kNXrPWwF37WmkcrIR%2FjG2Gj%2BPznLc1gxbd9eWs3zqQgSXUbTHFWZvA7pwPeW36Sp)
- [关于 Android，用多个 activity](https://www.zhihu.com/question/39662488)
-[Android项目Tab类型主界面大总结 Fragment+TabPageIndicator+ViewPager](http://blog.csdn.net/lmj623565791/article/details/24740977)
