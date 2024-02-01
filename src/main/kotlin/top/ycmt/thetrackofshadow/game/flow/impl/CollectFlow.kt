package top.ycmt.thetrackofshadow.game.flow.impl

import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.flow.FlowAbstract
import java.util.concurrent.TimeUnit

// 搜集流程
class CollectFlow(override val game: Game) : FlowAbstract() {
    private var waitTick = TimeUnit.MINUTES.toSeconds(10) // 搜集流程时间

    override fun onTick() {
        val onlinePLayers = game.playerModule.getOnlinePlayers()

        when (waitTick) {
            0L -> {
                // 完成该流程 进行下一流程
                this.Done()
            }

            else -> {
                game.sendMessage(onlinePLayers, "搜集流程剩余${waitTick}秒")
            }
        }
        waitTick--
    }

}