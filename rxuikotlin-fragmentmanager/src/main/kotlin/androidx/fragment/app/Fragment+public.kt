package androidx.fragment.app

/**
 * [Fragment.mPerformedCreateView] is the only property which matches exactly the behavior of [Fragment.onCreateView] and [Fragment.onDestroyView]. This field is set to true when
 * [Fragment.performCreateView] is called and set to false when [Fragment.performDestroyView] is called.
 *
 * Using [Fragment.getView] is a bad idea since [Fragment.mView] is not set to null when [Fragment.onDestroyView] is called.
 */
fun Fragment.isViewCreated() = mPerformedCreateView

fun Fragment.isCreated() = mIsCreated

val FragmentManager.activeFragments: MutableList<Fragment> get() = (this as FragmentManagerImpl).activeFragments