package top.ycmt.thetrackofshadow.game.flow.impl

import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.flow.FlowAbstract

// 准备流程
class ReadyFlow(override val game: Game) : FlowAbstract() {
    private var waitTick = 5L // 准备的时间

    override fun onTick() {
        when (waitTick) {
            0L -> {
                // 完成该流程 进行下一流程
                this.Done()
            }

            else -> {
                game.sendMsg(game.playerModule.onlinePLayers, "剩余${waitTick}秒")
            }
        }
        waitTick--
    }

}