# Fragment
测试Fragment各种方法的Demo
###Fragment各种问题以及解决方法
 1.重影问题
   原因：使用 Fragment 的状态保存，当系统内存不足，Fragment 的宿主 Activity 回收的时候，Fragment 的实例并没有随之被回收。Activity 被系统回收时，会主动调用 onSaveInstance() 方法来保存视图层（View Hierarchy），所以当 Activity 通过导航再次被重建时，之前被实例化过的 Fragment 依然会出现在 Activity 中，此时的 FragmentTransaction 中的相当于又再次 add 了 fragment   进去的，hide()和show()方法对之前保存的fragment已经失效了。综上这些因素导致了多个Fragment重叠在一起。
   ============
解决方案一:
- 给每个 Fragment 加一个 Tag
- 在 onCreate(Bundle savedInstanceState) 中判断 Bundle savedInstanceState 是否不为空
- 不为空则进行 find Tag，重新给几个 frament 赋值
这样子仍是对之前保存的 fragment 操作，成功解决了重叠的问题。
方案二:
Activity 中的 onSaveInstanceState() 里面有一句super.onSaveInstanceState(outState);，Google 对于这句话的解释是 “Always call the superclass so it can save the view hierarchy state”，大概意思是“总是执行这句代码来调用父类去保存视图层的状态”。通过注释掉这句话，这样主 Activity 因为种种原因被回收的时候就不会保存之前的 fragment state，也可以成功解决重叠的问题。
