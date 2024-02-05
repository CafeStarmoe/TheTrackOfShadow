package top.ycmt.thetrackofshadow.game.task

// 任务抽象类
abstract class TaskAbstract : TaskInterface {
    override var isCancelled = false // 是否已被取消
    override fun cancel() {
        isCancelled = true
    }
}