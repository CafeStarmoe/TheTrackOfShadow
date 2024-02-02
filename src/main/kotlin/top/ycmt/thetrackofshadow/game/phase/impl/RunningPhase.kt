package top.ycmt.thetrackofshadow.game.phase.impl

import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.flow.FlowAbstract
import top.ycmt.thetrackofshadow.game.flow.FlowState
import top.ycmt.thetrackofshadow.game.flow.FlowState.*
import top.ycmt.thetrackofshadow.game.flow.impl.CollectFlow
import top.ycmt.thetrackofshadow.game.flow.impl.ReadyFlow
import top.ycmt.thetrackofshadow.game.flow.impl.TeleportFlow
import top.ycmt.thetrackofshadow.game.phase.PhaseAbstract
import top.ycmt.thetrackofshadow.pkg.logger.logger
import java.util.concurrent.TimeUnit

// 游戏运行阶段
class RunningPhase(override val game: Game) : PhaseAbstract() {
    private var gameTick = 0L // 游戏tick
    private var flowState = TELEPORT_FLOW // 游戏流程状态
    private var gameFlow: FlowAbstract = TeleportFlow(game) // 游戏当前流程

    init {
        // 初始化玩家
    }

    override fun onTick() {
        // 胜利者是最后一名玩家 或 时间超出设置的时间 结算游戏
        if (game.playerModule.onlinePLayers.size <= 1 ||
            gameTick >= TimeUnit.MINUTES.toSeconds(30)
        ) {
            // 此阶段结束 进入结算阶段
            this.Done()
            return
        }

        // 执行当前流程
        gameFlow.onTick()
        // 阶段处理完成跳转下一阶段
        if (gameFlow.isDone) {
            nextFlow()
        }

        gameTick++
    }

    // 进行下一流程
    private fun nextFlow() {
        val flows = FlowState.values()
        val nextFlowIndex = flowState.ordinal + 1
        // 校验流程索引是否正常
        if (nextFlowIndex >= flows.size) {
            logger.error("索引越界, nextFlowIndex: $nextFlowIndex, flowsSize: ${flows.size}")
            return
        }
        // 设置流程状态
        flowState = flows[nextFlowIndex]
        // 根据流程状态设置流程
        gameFlow = when (flowState) {
            // 随机传送流程
            TELEPORT_FLOW -> TeleportFlow(game)
            // 准备流程
            READY_FLOW -> ReadyFlow(game)
            // 收集流程
            COLLECT_FLOW -> CollectFlow(game)
        }
        logger.info("游戏执行下一流程, gameName: ${game.gameSetting.gameName}, flowState: $flowState")
    }

}