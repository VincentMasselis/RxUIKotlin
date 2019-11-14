## FragmentManager

`rxFragmentsLifecycle` extension to listen fragment updates for a `FragmentManager`.

Our examples uses an `Activity`, you can replace this by a `Fragment`, it works the same way.

### Download

`implementation "com.vincentmasselis.rxuikotlin:rxuikotlin-fragmentmanager:$rx_ui_kotlin_version"`

### FragmentManager.rxFragmentsLifecycle()
```kotlin
class MyActivity : AppCompatActivity() {
  // Your code here
  fun onCreate() {
    // Your code here
    supportFragmentManager.rxFragmentsLifecycle(false)
      .subscribe { (state, fragment) ->
        // Every next emission will fire Pair<FragmentState, Fragment>
        // By using this pair, you can remotly listen to the fragments states events
      }
      .disposeOnState(ActivityState.DESTROY, this)
  }
}
```
