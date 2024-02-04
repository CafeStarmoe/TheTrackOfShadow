package top.ycmt.thetrackofshadow.game.event

import top.ycmt.thetrackofshadow.game.Game

// 事件抽象类
abstract class EventAbstract : EventInterface {
    abstract val game: Game // 游戏对象
    abstract val finishTick: Long // 运行完毕的tick数
    abstract val eventMsg: String // 事件消息
}