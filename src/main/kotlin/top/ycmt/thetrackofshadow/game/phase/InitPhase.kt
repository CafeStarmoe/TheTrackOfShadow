package top.ycmt.thetrackofshadow.game.phase

import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.logger.Logger

// 初始化阶段
class InitPhase(private val game: Game) : PhaseAbstract() {
    override fun onTick() {
        // 随机宝箱放置
        // 宝箱物品设置
        Logger.info("初始化阶段测试")
        this.done()
    }
}