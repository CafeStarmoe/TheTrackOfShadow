package top.ycmt.thetrackofshadow.game

import taboolib.common.platform.function.submit
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.game.module.CancelModule
import top.ycmt.thetrackofshadow.game.module.PlayerModule
import top.ycmt.thetrackofshadow.game.module.SubTaskModule
import top.ycmt.thetrackofshadow.game.phase.InitPhase
import top.ycmt.thetrackofshadow.game.phase.LobbyPhase
import top.ycmt.thetrackofshadow.game.phase.PhaseInterface
import top.ycmt.thetrackofshadow.game.phase.RunningPhase
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.game.state.PhaseState.*
import top.ycmt.thetrackofshadow.pkg.logger.logger

// 游戏对象
class Game(val setting: GameSetting) {
    val playerModule = PlayerModule(this) // 玩家管理模块
    val cancelModule = CancelModule(this) // 玩家状态管理模块
    val subTaskModule = SubTaskModule(this) // 子任务管理模块

    private var phaseState = INIT_PHASE // 游戏阶段状态
    private var gamePhase: PhaseInterface = InitPhase(this) // 游戏当前阶段

    init {
        logger.info("游戏初始化, gameName: ${setting.gameName}")

        // 运行游戏主调度器
        gameMainTask()
    }

    // 游戏主调度器
    private fun gameMainTask() {
        submit(period = 1 * 20L) {
            // 刷新在线玩家对象列表
            playerModule.refreshPlayers()

            // 执行当前阶段
            gamePhase.onTick()
            // 阶段处理完成跳转下一阶段
            if (gamePhase.isDone) {
                nextPhase()
            }
        }
    }

    // 进行下一阶段
    private fun nextPhase() {
        val phases = PhaseState.values()
        val nextPhaseIndex = phaseState.ordinal + 1
        // 校验阶段索引是否正常
        if (nextPhaseIndex >= phases.size) {
            logger.error("索引越界, nextPhaseIndex: $nextPhaseIndex, phasesSize: ${phases.size}")
            return
        }
        // 设置阶段状态
        phaseState = phases[nextPhaseIndex]
        // 根据阶段状态设置阶段
        gamePhase = when (phaseState) {
            // 初始化游戏阶段
            INIT_PHASE -> InitPhase(this)
            // 大厅等待阶段
            LOBBY_PHASE -> LobbyPhase(this)
            // 游戏运行阶段
            RUNNING_PHASE -> RunningPhase(this)
        }
        // 切换阶段完执行一次
        gamePhase.onTick()
        logger.info("游戏执行下一阶段, gameName: ${setting.gameName}, phaseState: $phaseState")
    }

    // 停止游戏
    fun stopGame() {
        // TODO 踢出玩家
        // 取消所有任务
        subTaskModule.cancelAllTask()
    }

}
