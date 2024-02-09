package top.ycmt.thetrackofshadow.game.phase

import top.ycmt.thetrackofshadow.game.Game

// 还原地图阶段
class ResetPhase (private val game: Game) : PhaseAbstract() {
    override fun onTick() {
        this.done()
    }
}