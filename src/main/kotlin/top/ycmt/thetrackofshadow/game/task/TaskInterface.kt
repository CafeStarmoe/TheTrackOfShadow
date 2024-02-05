package top.ycmt.thetrackofshadow.game.task

// 任务接口
interface TaskInterface {
    var isCancelled: Boolean  // 是否已被取消
    fun run() // 执行任务
    fun cancel() // 取消任务
}