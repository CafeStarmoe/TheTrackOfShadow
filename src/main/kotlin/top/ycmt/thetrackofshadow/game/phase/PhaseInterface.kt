package top.ycmt.thetrackofshadow.game.phase

// 阶段接口
interface PhaseInterface {
    fun onTick() // 处理当前阶段
    var isDone: Boolean  // 该阶段是否完成

    // 完成该阶段
    fun done() {
        isDone = true
    }
}