# Fragment
测试Fragment各种方法的Demo
============
### Fragment[各种问题](http://toughcoder.net/blog/2015/04/30/android-fragment-the-bad-parts/)以及解决方法
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

============
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










