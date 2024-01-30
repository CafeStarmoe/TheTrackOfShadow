package top.ycmt.thetrackofshadow.game

import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.function.submit
import taboolib.platform.util.bukkitPlugin
import top.ycmt.thetrackofshadow.common.config.GameSetting
import top.ycmt.thetrackofshadow.common.constant.PrefixConst
import top.ycmt.thetrackofshadow.game.module.PlayerModule
import top.ycmt.thetrackofshadow.game.phase.PhaseAbstract
import top.ycmt.thetrackofshadow.game.phase.PhaseState
import top.ycmt.thetrackofshadow.game.phase.PhaseState.INIT_PHASE
import top.ycmt.thetrackofshadow.game.phase.PhaseState.LOBBY_PHASE
import top.ycmt.thetrackofshadow.game.phase.impl.InitPhase
import top.ycmt.thetrackofshadow.game.phase.impl.LobbyPhase
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
            gamePhase.onTick()
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

    // 设置玩家的属性 如生命值 饱和度 药水效果 模式...
    // 参数: 玩家对象 是否清空物品栏
    fun initPlayer(player: Player, cleanItem: Boolean = false) {
        player.gameMode = GameMode.ADVENTURE // 设置冒险模式
        clearEffect(player) // 清除玩家的效果

        // 设置玩家的基础信息
        player.health = 20.0
        player.foodLevel = 20
        player.level = 0
        player.exp = 0f

        // 禁止玩家不可见
        showPlayer(player)

        // 不允许玩家飞行
        player.allowFlight = false
        player.isFlying = false
        if (cleanItem) {
            player.inventory.clear() // 清空玩家的背包
        }
    }

    // 设置所有玩家的属性 如生命值 饱和度 药水效果 模式...
    // 参数: 玩家对象 是否清空物品栏
    fun initPlayer(playerList: List<Player>, cleanItem: Boolean = false) {
        playerList.forEach {
            initPlayer(it, cleanItem)
        }
    }

    // 设置玩家隐藏
    fun hidePlayer(player: Player) {
        playerModule.getOnlinePlayerList().forEach {
            it.hidePlayer(bukkitPlugin, player)
        }
    }

    // 设置玩家显示
    fun showPlayer(player: Player) {
        playerModule.getOnlinePlayerList().forEach {
            it.showPlayer(bukkitPlugin, player)
        }
    }

//    // 清除某个玩家的计分板
//    fun cleanScoreboard(player: Player) {
//        if (ScoreHelper.hasScore(player)) {
//            ScoreHelper.removeScore(player)
//        }
//    }
//
//    // 清除所有玩家的计分板
//    fun cleanScoreboard(playerList: List<Player>) {
//        playerList.forEach {
//            cleanScoreboard(it)
//        }
//    }

    // 给游戏中的某个玩家清空药水效果
    fun clearEffect(player: Player) {
        player.activePotionEffects.forEach {
            player.removePotionEffect(it.type)
        }
    }

    // 给游戏中的玩家删除药水效果
    fun removeEffect(playerList: List<Player>, effectType: PotionEffectType) {
        playerList.forEach {
            it.removePotionEffect(effectType)
        }
    }

    // 给游戏中的玩家添加药水效果
    fun addEffect(playerList: List<Player>, effectType: PotionEffectType, duration: Int, amplifier: Int) {
        playerList.forEach {
            it.addPotionEffect(PotionEffect(effectType, duration, amplifier))
        }
    }

    // 给游戏中的玩家发送声音
    fun playSound(playerList: List<Player>, sound: Sound) {
        playerList.forEach {
            it.playSound(it.location, sound, 1f, 1f)
        }
    }

    // 给游戏中的玩家发送Title
    fun sendTitle(playerList: List<Player>, title: String, subTitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        playerList.forEach {
            it.sendTitle(title, subTitle, fadeIn, stay, fadeOut)
        }
    }

    // 给游戏中的玩家发送消息
    fun sendMessage(playerList: List<Player>, vararg msg: String) {
        playerList.forEach { p ->
            msg.forEach {
                p.sendMessage(PrefixConst.PrefixMessage + it)
            }
        }
    }

}
