package top.ycmt.thetrackofshadow.game.phase

// 阶段抽象类
abstract class PhaseAbstract : PhaseInterface {
    override var isDone: Boolean = false // 该阶段是否完成

    // 完成该阶段
    override fun done() {
        isDone = true
    }
}