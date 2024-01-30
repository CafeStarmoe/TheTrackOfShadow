package top.ycmt.thetrackofshadow.game

import taboolib.common.platform.function.submit
import top.ycmt.thetrackofshadow.conf.GameSetting
import top.ycmt.thetrackofshadow.game.module.PlayerModule
import top.ycmt.thetrackofshadow.game.phase.InitPhase
import top.ycmt.thetrackofshadow.game.phase.LobbyPhase
import top.ycmt.thetrackofshadow.game.phase.PhaseAbstract
import top.ycmt.thetrackofshadow.game.phase.PhaseState
import top.ycmt.thetrackofshadow.game.phase.PhaseState.INIT_PHASE
import top.ycmt.thetrackofshadow.game.phase.PhaseState.LOBBY_PHASE
import top.ycmt.thetrackofshadow.pkg.logger.logger


// 游戏对象
class Game(
    // 游戏设置数据
    val gameSetting: GameSetting
) {
    // 游戏玩家模块
    val playerModule: PlayerModule = PlayerModule(this)

    // 游戏阶段状态
    private var phaseState: PhaseState = INIT_PHASE

    // 游戏阶段定时器
    private var gamePhase: PhaseAbstract = InitPhase(this)

    init {
        logger.info("游戏初始化, gameName: ${gameSetting.gameName}")

        // 运行游戏主调度器
        gameMainTask()
    }

    // 游戏主调度器
    private fun gameMainTask() {
        submit(period = 1 * 20L) {
            // 执行当前阶段任务
            gamePhase.handle()
            // 阶段处理完成跳转下一阶段
            if (gamePhase.isDone) {
                nextPhase()
            }
        }
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
        gamePhase = when (phaseState) {
            // 初始化游戏阶段
            INIT_PHASE -> InitPhase(this)
            // 大厅等待阶段
            LOBBY_PHASE -> LobbyPhase(this)
        }
        logger.info("游戏执行下一阶段, gameName: ${gameSetting.gameName}, phaseState: $phaseState")
    }

}
