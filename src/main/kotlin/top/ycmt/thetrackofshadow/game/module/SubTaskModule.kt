package top.ycmt.thetrackofshadow.game.module

import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.task.TaskInterface

// 子任务管理模块
class SubTaskModule(private val game: Game) {
    private val platformTasks: MutableList<PlatformExecutor.PlatformTask> = mutableListOf() // 平台任务列表

    // 提交任务
    fun submitTask(
        now: Boolean = false,
        async: Boolean = false,
        delay: Long = 0,
        period: Long = 0,
        task: TaskInterface
    ) {
        platformTasks.add(submit(now, async, delay, period) {
            // 运行任务
            task.run()
            // 如果任务取消了平台任务也取消
            if (task.isCancelled) {
                this.cancel()
                // 清除任务记录
                platformTasks.remove(this)
            }
        })
    }

    // 取消所有任务
    fun cancelAllTask() {
        platformTasks.forEach {
            it.cancel()
        }
        platformTasks.clear()
    }
}