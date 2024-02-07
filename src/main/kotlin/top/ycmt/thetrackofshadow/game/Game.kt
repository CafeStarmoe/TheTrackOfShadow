package top.ycmt.thetrackofshadow.game

import taboolib.common.platform.function.submit
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.game.module.*
import top.ycmt.thetrackofshadow.game.phase.*
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.game.state.PhaseState.*
import top.ycmt.thetrackofshadow.pkg.logger.Logger

// 游戏对象
class Game(val setting: GameSetting) {
    val playerModule = PlayerModule(this) // 玩家管理模块
    val cancelModule = CancelModule(this) // 玩家状态管理模块
    val subTaskModule = SubTaskModule(this) // 子任务管理模块
    val damageModule = DamageModule(this) // 玩家伤害管理模块
    val respawnModule = RespawnModule(this) // 玩家重生管理模块
    val spawnModule = SpawnModule(this) // 出生点管理模块
    val reconnectModule = ReconnectModule(this) // 玩家重连管理模块

    var phaseState = INIT_PHASE // 游戏阶段状态
        private set

    private var gamePhase: PhaseInterface = InitPhase(this) // 游戏当前阶段

    init {
        Logger.info("游戏初始化, gameName: ${setting.gameName}")

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
            Logger.error("索引越界, nextPhaseIndex: $nextPhaseIndex, phasesSize: ${phases.size}")
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
            // 游戏结算阶段
            SETTLE_PHASE -> SettlePhase(this)
        }
        // 切换阶段完执行一次
        gamePhase.onTick()
        Logger.info("游戏执行下一阶段, gameName: ${setting.gameName}, phaseState: $phaseState")
    }

    // 停止游戏
    fun stopGame() {
        // TODO 踢出玩家
        // 取消所有子任务
        subTaskModule.cancelAllTask()
    }

}
