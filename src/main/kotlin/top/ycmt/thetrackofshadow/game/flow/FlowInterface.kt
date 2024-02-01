package top.ycmt.thetrackofshadow.game.flow

import top.ycmt.thetrackofshadow.game.Game

// 流程接口
interface FlowInterface {
    val game: Game // 游戏对象
    fun onTick() // 处理当前流程
}