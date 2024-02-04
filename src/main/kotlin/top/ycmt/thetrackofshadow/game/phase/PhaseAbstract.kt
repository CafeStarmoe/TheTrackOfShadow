package top.ycmt.thetrackofshadow.game.phase

import top.ycmt.thetrackofshadow.game.Game

// 阶段抽象类
abstract class PhaseAbstract : PhaseInterface {
    abstract val game: Game // 游戏对象
    var isDone: Boolean = false
        private set

    // 完成该阶段
    fun done() {
        isDone = true
    }
}