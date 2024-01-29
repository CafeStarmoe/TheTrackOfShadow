package top.ycmt.thetrackofshadow.game

import org.bukkit.scheduler.BukkitRunnable
import top.ycmt.thetrackofshadow.conf.GameSetting
import top.ycmt.thetrackofshadow.game.module.PlayerModule
import top.ycmt.thetrackofshadow.game.runnable.LobbyPhase
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.game.state.PhaseState.INIT_PHASE
import top.ycmt.thetrackofshadow.game.state.PhaseState.LOBBY_PHASE
import top.ycmt.thetrackofshadow.pkg.logger.logger


// 游戏对象
class Game(
    // 游戏id
    val gameId: UInt,
    // 游戏设置数据
    val gameSetting: GameSetting
) {
    // 游戏玩家模块
    val playerModule: PlayerModule = PlayerModule(this)

    // 游戏阶段状态
    private var phaseState: PhaseState = INIT_PHASE

    // 游戏阶段定时器
    private lateinit var gamePhase: BukkitRunnable

    init {
        initGame()
    }

    // 初始化游戏
    private fun initGame() {
        logger.info("游戏初始化, gameId: $gameId")
        // 随机宝箱放置
        // 宝箱物品设置

        // 初始化完毕进入下一阶段
        nextPhase()
    }

    // 设置游戏为下阶段
    private fun nextPhase() {
        val phases = PhaseState.values()
        val nextPhaseIndex = phaseState.ordinal + 1
        // 校验阶段索引是否正常
        if (nextPhaseIndex >= phases.size) {
            logger.error("索引越界, nextPhaseIndex: $nextPhaseIndex, phasesSize: ${phases.size}")
            return
        }
        // 设置阶段
        phaseState = phases[nextPhaseIndex]
        // 根据阶段运行阶段定时器
        when (phaseState) {
            // 大厅等待阶段
            LOBBY_PHASE -> gamePhase = LobbyPhase(this)
            else -> {
                logger.error("阶段无效, phaseState: $phaseState")
                return
            }
        }
        logger.info("游戏执行下一阶段, gameId: $gameId, phaseState: $phaseState")
    }

}
