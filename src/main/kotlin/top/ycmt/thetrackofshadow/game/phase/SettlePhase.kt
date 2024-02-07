package top.ycmt.thetrackofshadow.game.phase

import top.ycmt.thetrackofshadow.game.Game

// 结算阶段
class SettlePhase(private val game: Game) : PhaseAbstract() {
    override fun onTick() {
        // 清除所有子任务
        game.subTaskModule.cancelAllTask()
        this.done()
    }
}