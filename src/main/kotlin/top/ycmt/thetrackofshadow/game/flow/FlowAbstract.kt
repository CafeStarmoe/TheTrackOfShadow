package top.ycmt.thetrackofshadow.game.flow

import top.ycmt.thetrackofshadow.game.Game

// 流程接口
abstract class FlowAbstract : FlowInterface {
    abstract override val game: Game // 游戏对象
    var isDone: Boolean = false
        private set

    abstract override fun onTick() // 处理当前流程

    // 完成该流程
    fun Done() {
        isDone = true
    }
}