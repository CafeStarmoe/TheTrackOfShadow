package top.ycmt.thetrackofshadow.game.flow

// 流程事件接口
interface FlowInterface {
    val finishTick: Long // 运行完毕的tick数
    val eventMsg: String // 事件消息
    fun exec(leftTick: Long) // 执行事件
}