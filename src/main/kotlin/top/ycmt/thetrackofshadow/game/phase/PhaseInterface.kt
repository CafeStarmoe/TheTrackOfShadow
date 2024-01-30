package top.ycmt.thetrackofshadow.game.phase

import top.ycmt.thetrackofshadow.game.Game

// 阶段接口
interface PhaseInterface {
    val game: Game // 游戏对象
    fun handle() // 处理当前阶段
}