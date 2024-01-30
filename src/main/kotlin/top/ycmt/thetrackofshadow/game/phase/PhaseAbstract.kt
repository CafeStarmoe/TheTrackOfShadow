package top.ycmt.thetrackofshadow.game.phase

import top.ycmt.thetrackofshadow.game.Game

// 阶段接口
abstract class PhaseAbstract : PhaseInterface {
    abstract override val game: Game // 游戏对象
    var isDone: Boolean = false
        private set

    abstract override fun onTick() // 处理当前阶段

    // 完成该阶段
    fun Done() {
        isDone = true
    }
}