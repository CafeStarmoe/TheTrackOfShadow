package top.ycmt.thetrackofshadow.game.event

// 事件接口
interface EventInterface {
    fun exec(leftTick: Long) // 执行事件
}