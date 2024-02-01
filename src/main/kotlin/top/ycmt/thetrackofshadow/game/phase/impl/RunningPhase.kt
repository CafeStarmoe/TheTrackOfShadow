package top.ycmt.thetrackofshadow.game.phase.impl

import org.bukkit.Location
import org.bukkit.entity.Player
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.flow.FlowAbstract
import top.ycmt.thetrackofshadow.game.flow.FlowState
import top.ycmt.thetrackofshadow.game.flow.FlowState.COLLECT_FLOW
import top.ycmt.thetrackofshadow.game.flow.FlowState.READY_FLOW
import top.ycmt.thetrackofshadow.game.flow.impl.CollectFlow
import top.ycmt.thetrackofshadow.game.flow.impl.ReadyFlow
import top.ycmt.thetrackofshadow.game.phase.PhaseAbstract
import top.ycmt.thetrackofshadow.pkg.logger.logger
import java.util.concurrent.TimeUnit

// 游戏运行阶段
class RunningPhase(override val game: Game) : PhaseAbstract() {
    private var gameTick: Long = 0 // 游戏tick
    private var flowState: FlowState = READY_FLOW // 游戏流程状态
    private var gameFlow: FlowAbstract = ReadyFlow(game) // 游戏当前流程

    init {
        // 初始化玩家
        // 随机传送玩家至地图
        game.playerModule.getOnlinePlayers().forEach {
            randomTeleport(it)
        }
    }

    override fun onTick() {
        val onlinePlayers = game.playerModule.getOnlinePlayers()

        // 胜利者是最后一名玩家 或 时间超出设置的时间 结算游戏
        if (onlinePlayers.size <= 1 ||
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
            // 准备流程
            READY_FLOW -> ReadyFlow(game)
            // 收集流程
            COLLECT_FLOW -> CollectFlow(game)
        }
        logger.info("游戏执行下一流程, gameName: ${game.gameSetting.gameName}, flowState: $flowState")
    }

    // 随机传送到游戏地图上的某个位置
    private fun randomTeleport(p: Player) {
        val loc1 = game.gameSetting.gameMapLoc1
        val loc2 = game.gameSetting.gameMapLoc2

        // 随机取地图内的坐标
        val x = (loc1.x.coerceAtMost(loc2.x).toInt() until loc1.x.coerceAtLeast(loc2.x).toInt()).random()
        val z = (loc1.z.coerceAtMost(loc2.z).toInt() until loc1.z.coerceAtLeast(loc2.z).toInt()).random()

        // 设置游戏开始玩家的高度
        val y = 152.0
        val loc = Location(loc1.world, x.toDouble(), y, z.toDouble())

        // 加载这个区块
        val chunk = loc.chunk
        if (!chunk.isLoaded) {
            chunk.load(true)
        }
        // 将玩家传送至这个坐标
        p.teleport(loc)
    }

}