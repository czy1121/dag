package me.reezy.cosmo.dag


class TaskList internal constructor(
    private val packageName: String,
    private val currentProcessName: String,
    private val isDebuggable: Boolean,
) {
    internal val items: MutableList<Task> = mutableListOf()

    fun add(name: String, process: String = "all", leading: Boolean = false, background: Boolean = false, debugOnly: Boolean = false, priority: Int = 0, depends: Set<String> = setOf(), block: () -> Unit) {
        if (debugOnly && !isDebuggable) {
            TaskDag.log("===> $name SKIPPED : debug only")
            return
        }
        when (process) {
            "all" -> {}
            "main" -> {
                if (currentProcessName != packageName) {
                    TaskDag.log("===> $name SKIPPED : main process only")
                    return
                }
            }
            else -> {
                if (currentProcessName != "${packageName}:${process}") {
                    TaskDag.log("===> $name SKIPPED : process [$currentProcessName] [${packageName}:${process}] only")
                    return
                }
            }
        }
        items.add(Task(name, leading, background, priority, depends, block))
    }
}