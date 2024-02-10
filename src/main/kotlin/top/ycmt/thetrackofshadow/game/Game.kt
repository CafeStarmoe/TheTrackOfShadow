package top.ycmt.thetrackofshadow.game

import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import top.ycmt.thetrackofshadow.config.GameSetting
import top.ycmt.thetrackofshadow.game.module.*
import top.ycmt.thetrackofshadow.game.phase.*
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.game.state.PhaseState.*

// 游戏对象
class Game(val setting: GameSetting) {
    lateinit var playerModule: PlayerModule // 玩家管理模块
    lateinit var cancelModule: CancelModule // 玩家状态管理模块
    lateinit var subTaskModule: SubTaskModule // 子任务管理模块
    lateinit var damageModule: DamageModule // 玩家伤害管理模块
    lateinit var respawnModule: RespawnModule // 玩家重生管理模块
    lateinit var spawnModule: SpawnModule // 重生点管理模块
    lateinit var reconnectModule: ReconnectModule // 玩家重连管理模块
    lateinit var scoreModule: ScoreModule // 玩家积分管理模块
    lateinit var statsModule: StatsModule // 玩家操作统计管理模块
    lateinit var chestModule: ChestModule // 宝箱管理模块
    lateinit var hologramModule: HologramModule // 全息投影管理模块

    // 游戏阶段状态
    lateinit var phaseState: PhaseState
        private set

    private lateinit var gamePhase: PhaseInterface // 游戏当前阶段
    private lateinit var gameMainTask: PlatformExecutor.PlatformTask // 游戏主调度器

    init {
        // 初始化游戏
        initGame()
    }

    // 初始化游戏
    private fun initGame() {
        // 初始化模块
        playerModule = PlayerModule(this) // 玩家管理模块
        cancelModule = CancelModule(this) // 玩家状态管理模块
        subTaskModule = SubTaskModule(this) // 子任务管理模块
        damageModule = DamageModule(this) // 玩家伤害管理模块
        respawnModule = RespawnModule(this) // 玩家重生管理模块
        spawnModule = SpawnModule(this) // 重生点管理模块
        reconnectModule = ReconnectModule(this) // 玩家重连管理模块
        scoreModule = ScoreModule(this) // 玩家积分管理模块
        statsModule = StatsModule(this) // 玩家操作统计管理模块
        chestModule = ChestModule(this) // 宝箱管理模块
        hologramModule = HologramModule(this) // 全息投影管理模块
        // 初始化阶段
        phaseState = INIT_PHASE
        gamePhase = InitPhase(this)
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
            // 阶段都执行完毕了重启游戏
            restartGame()
            return
        }
        // 设置阶段状态
        phaseState = phases[nextPhaseIndex]
        // 根据阶段状态设置阶段
        gamePhase = when (phaseState) {
            // 初始化阶段
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

    // 重启游戏
    private fun restartGame() {
        // 关闭游戏后再初始化游戏
        stopGame()
        initGame()
    }

    // 还原地图
    private fun resetGame() {
        // 重置宝箱
        chestModule.resetChests()
    }

    // 停止游戏
    fun stopGame() {
        // 取消游戏主调度器
        gameMainTask.cancel()
        // 取消所有子任务
        subTaskModule.cancelTasks()
        // 清除所有全息投影
        hologramModule.deleteHolograms()
        // 还原地图
        resetGame()
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
