package me.reezy.cosmo.dag

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