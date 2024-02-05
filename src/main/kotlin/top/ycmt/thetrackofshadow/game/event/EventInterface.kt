package top.ycmt.thetrackofshadow.game.event

// 事件接口
interface EventInterface {
    val finishTick: Long // 运行完毕的tick数
    val eventMsg: String // 事件消息
    fun exec(leftTick: Long) // 执行事件
}