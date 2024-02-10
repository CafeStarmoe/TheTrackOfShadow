package top.ycmt.thetrackofshadow.game.module

import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.platform.util.toBukkitLocation
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.PLAYER_RESPAWN_PREFIX_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.game.task.RespawnProtectCleanTask
import top.ycmt.thetrackofshadow.game.task.RespawnTask
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg
import top.ycmt.thetrackofshadow.pkg.hide.HidePlayer.hidePlayers
import top.ycmt.thetrackofshadow.pkg.hide.HidePlayer.showPlayers
import top.ycmt.thetrackofshadow.pkg.logger.Logger
import java.util.*

// 玩家重生管理模块
class RespawnModule(private val game: Game) {
    private val respawnPlayers: MutableMap<UUID, RespawnTask> = mutableMapOf() // 正在重生的玩家uuid列表
    private val playersInv: MutableMap<UUID, Array<ItemStack>> = mutableMapOf() // 正在重生的玩家物品栏
    private val protectPlayers: MutableMap<UUID, RespawnProtectCleanTask> = mutableMapOf() // 重生后保护状态的玩家uuid列表

    // 添加重生玩家
    fun respawnPlayer(player: Player) {
        // 判断玩家是否正在重生状态
        if (respawnPlayers.contains(player.uniqueId)) {
            // 取消正在运行的重生任务
            respawnPlayers[player.uniqueId]?.cancel()
            respawnPlayers.remove(player.uniqueId)
        }
        // 如果玩家的物品栏存在代表玩家上次的重生未完成 不覆盖
        if (!playersInv.contains(player.uniqueId)) {
            // 保存重生前的物品栏
            playersInv[player.uniqueId] = player.inventory.contents
        }
        // 初始化玩家
        game.playerModule.initPlayer(player)
        // 暂时允许飞行
        player.allowFlight = true
        player.isFlying = true
        // 给予玩家隐身效果
        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, -1, 0, true, false))
        // 设置玩家隐身
        player.hidePlayers(game.playerModule.getOnlineUsers())
        // 将玩家传送到重生点
        player.teleport(player.world.spawnLocation)
        // 重生任务
        val task = RespawnTask(game, player, 5L)
        // 添加正在重生玩家
        respawnPlayers[player.uniqueId] = task
        // 提交重生任务
        game.subTaskModule.submitTask(period = 1 * 20L, task = task)
    }

    // 重生倒计时结束
    fun respawnFinish(player: Player) {
        // 判断玩家是否正在重生状态
        if (!respawnPlayers.contains(player.uniqueId)) {
            return
        }
        // 判断玩家是否正在重生保护状态
        if (protectPlayers.contains(player.uniqueId)) {
            // 取消正在运行的重生保护清除任务
            protectPlayers[player.uniqueId]?.cancel()
            protectPlayers.remove(player.uniqueId)
        }
        // 删除正在重生玩家
        respawnPlayers.remove(player.uniqueId)
        // 初始化玩家 且清空物品栏
        game.playerModule.initPlayer(player)
        // 取消允许飞行
        player.allowFlight = false
        player.isFlying = false
        // 设置玩家显示
        player.showPlayers(game.playerModule.getOnlineUsers())
        // 清除摔落伤害
        player.fallDistance = 0F
        // 还原玩家物品栏
        val playerInv = playersInv[player.uniqueId]
        // 确保物品栏获取成功
        if (playerInv == null) {
            Logger.error("记录的玩家物品栏为空, uuid: ${player.uniqueId}, name: ${player.name}")
            return
        }
        player.inventory.contents = playerInv
        // 删除物品栏记录
        playersInv.remove(player.uniqueId)
        // 重生点坐标
        val respawnLoc = game.setting.gameMapRespawnVector.toLocation(game.setting.gameMapWorld).toBukkitLocation()
        // 将玩家传送至重生点
        player.teleport(respawnLoc)
        // 发射烟花示意有人重生了
        spawnFireworks(respawnLoc)
        // 复活后给其他玩家提示消息
        game.playerModule.getOnlineUsers().forEach {
            it.sendMessage(
                "",
                "$PLAYER_RESPAWN_PREFIX_LEGACY_TEXT<#deffd2,bee8ff>${player.name}</#>§f已经重生在出生点了!".toGradientColor(),
                ""
            )
            it.playSound(it, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f)
        }
        // 复活后给此玩家提示消息
        player.sendTitle(
            "<#deffd2,bee8ff>已重生!</#>".toGradientColor(),
            "<#ffefbb,e3ce82>重生后将获得</#><#ff9c9c,de4949>10秒</#><#ffefbb,e3ce82>保护!</#>".toGradientColor(),
            10,
            20,
            10
        )
        player.sendMsg("<#ffefbb,e3ce82>重生后将获得</#><#ff9c9c,de4949>10秒</#><#ffefbb,e3ce82>保护!</#>".toGradientColor())
        // 添加重生保护状态
        game.cancelModule.addPlayerCancelState(player, CancelState.CANCEL_PVP_RESPAWN_PROTECT)
        // 重生保护清除任务
        val task = RespawnProtectCleanTask(game, player, 10L)
        // 添加重生后玩家
        protectPlayers[player.uniqueId] = task
        // 提交重生后保护清除任务
        game.subTaskModule.submitTask(period = 1 * 20L, task = task)
    }

    // 取消重生 如玩家退出游戏
    fun cancelRespawn(player: Player) {
        respawnPlayers.remove(player.uniqueId)
    }

    // 取消重生后保护
    fun removeProtect(player: Player) {
        game.cancelModule.removePlayerCancelState(player, CancelState.CANCEL_PVP_RESPAWN_PROTECT)
        protectPlayers.remove(player.uniqueId)
    }

    // 玩家是否正在重生
    fun containsRespawnPlayer(player: Player): Boolean = respawnPlayers.contains(player.uniqueId)

    // 是否可以重生
    fun isRespawnable(): Boolean {
        return !game.cancelModule.containsGlobalCancelState(CancelState.CANCEL_RESPAWN)
    }

    // 生成重生烟花
    private fun spawnFireworks(loc: Location) {
        // 创建一个烟花
        val firework = loc.world?.spawn(loc, Firework::class.java) ?: return
        val meta = firework.fireworkMeta // 烟花的Meta
        // 烟花的效果
        meta.addEffects(
            FireworkEffect.builder()
                .withColor(Color.fromRGB(193, 255, 193))
                .withFade(Color.fromRGB(155, 205, 155))
                .with(FireworkEffect.Type.STAR).withFlicker().withTrail()
                .build()
        )
        // 设置烟花强度（可以调整强度以控制烟花的升空高度）
        meta.power = 0
        // 设置烟花实体不会发光，也不会对周围的实体造成伤害
        firework.isGlowing = false
        // 应用设置好的烟花元数据
        firework.fireworkMeta = meta
    }

}