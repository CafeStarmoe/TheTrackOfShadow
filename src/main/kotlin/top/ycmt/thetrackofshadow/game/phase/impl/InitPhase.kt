package top.ycmt.thetrackofshadow.game.phase.impl

import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.phase.PhaseAbstract
import top.ycmt.thetrackofshadow.pkg.logger.logger

// 初始化阶段
class InitPhase(override val game: Game) : PhaseAbstract() {

    override fun onTick() {
        // 随机宝箱放置
        // 宝箱物品设置
        logger.info("初始化阶段测试")
        this.done()
    }

}