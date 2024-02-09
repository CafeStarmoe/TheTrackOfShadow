package top.ycmt.thetrackofshadow.game

import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.game.module.*
import top.ycmt.thetrackofshadow.game.phase.*
import top.ycmt.thetrackofshadow.game.state.PhaseState.*
import top.ycmt.thetrackofshadow.pkg.logger.Logger

// 游戏对象
class Game(val setting: GameSetting) {
    val playerModule = PlayerModule(this) // 玩家管理模块
    val cancelModule = CancelModule(this) // 玩家状态管理模块
    val subTaskModule = SubTaskModule(this) // 子任务管理模块
    val damageModule = DamageModule(this) // 玩家伤害管理模块
    val respawnModule = RespawnModule(this) // 玩家重生管理模块
    val spawnModule = SpawnModule(this) // 重生点管理模块
    val reconnectModule = ReconnectModule(this) // 玩家重连管理模块
    val scoreModule = ScoreModule(this) // 玩家积分管理模块
    val statsModule = StatsModule(this) // 玩家操作统计管理模块
    val chestModule = ChestModule(this) // 宝箱管理模块
    val hologramModule = HologramModule(this) // 全息投影管理模块

    var phaseState = INIT_PHASE // 游戏阶段状态
        private set

    private var gamePhase: PhaseInterface = InitPhase(this) // 游戏当前阶段
    private lateinit var gameMainTask: PlatformExecutor.PlatformTask // 游戏主调度器

    init {
        // 运行游戏主调度器
        gameMainTask()
    }

    // 游戏主调度器
    private fun gameMainTask() {
        gameMainTask = submit(period = 1 * 20L) {
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
        val phases = entries.toTypedArray()
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
    }

    // 停止游戏
    fun stopGame() {
        // 取消游戏主调度器
        gameMainTask.cancel()
        // 取消所有子任务
        subTaskModule.cancelAllTask()
        // 清除所有全息投影
        hologramModule.deleteHolograms()
        // 踢出所有玩家
        playerModule.getOnlinePlayers().forEach {
            playerModule.removePlayer(it)
        }
        // 踢出所有观察者
        playerModule.getOnlineWatchers().forEach {
            playerModule.removeWatcher(it)
        }
    }

}
