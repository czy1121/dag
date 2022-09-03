# dag

有向无环图(DAG)，可用来调度应用初始化任务


## 引入

``` groovy
repositories {
    maven { url "https://gitee.com/ezy/repo/raw/cosmo/"}
}
dependencies {
    implementation "me.reezy.cosmo:dag:0.7.0"
}
```

## 使用

```kotlin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        val app = this
        val processName = resolveCurrentProcessName() ?: ""

        TaskDag.launch(packageName, processName, BuildConfig.DEBUG) {
            add("task1") {
            }
            add("task2", depends = setOf("task1")) {
            }
            add("task3", depends = setOf("task1"), background = true) {
            }
            add("task4", background = true) {
            }
            add("task5") {
            }
        }
    }
}
```

## 核心类

- `Task` 任务
- `TaskList` 负责持有和添加任务
- `TaskDag` 负责调度任务，支持添加开关任务(没有业务仅作为开关，可手动触发完成，并偿试执行其子任务)
  - 无依赖的异步任务，在子线程并行执行
  - 无依赖的同步任务，在主线程顺序执行
  - 有依赖的任务，确保无循环依赖，且被依赖的任务先执行

```kotlin
class Task(
    val name: String,
    val leading: Boolean = false,       // 是否前置任务，前置任务全部执行完成后才开始调度其它任务
    val background: Boolean = false,    // 是否在工作线程执行任务
    val priority: Int = 0,              // 任务运行的优先级，值小的先执行
    val depends: Set<String> = setOf(), // 依赖的任务列表
    val block: () -> Unit = {},
) {
    val children: MutableSet<Task> = mutableSetOf()
}
```

## LICENSE

The Component is open-sourced software licensed under the [Apache license](LICENSE).
