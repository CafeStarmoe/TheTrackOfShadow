package top.ycmt.thetrackofshadow.game.module

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.module.chat.impl.DefaultComponent
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import java.util.*

// 宝箱管理模块
class ChestModule(private val game: Game) {
    private val findChests = mutableMapOf<Location, UUID>() // 被找到的宝箱

    // 玩家找到宝箱
    fun playerFindChest(player: Player, chest: Chest) {
        // 增加已被找到的宝箱
        findChests[chest.location] = player.uniqueId
        // 设置宝箱随机物品
        setChestRandomItems(chest)
        // 创建全息投影
        val loc = Location(chest.location.world, chest.location.x + 0.5, chest.location.y + 1.5, chest.location.z + 0.5)
        val hologram = game.hologramModule.createHologram(loc)
        hologram.lines.appendText("<#ff9c9c,de4949>已被打开!</#>".toGradientColor())
        hologram.lines.appendText("§f属于: <#f7c79c,ef987d>${player.name}</#>".toGradientColor())
        // 奖励玩家积分 随机5-30的积分
        val score = (5..30).random()
        game.scoreModule.addPlayerScore(player, score)
        // 高亮玩家5s
        player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 5 * 20, 0))
        // 增加找到的宝箱次数
        val playerStatsInfo = game.statsModule.getPlayerStats(player) ?: return
        playerStatsInfo.findChestCount++
        // 宝箱位置文本
        val locationText = "§f位于<#b4f1ff,8ab7e1>${loc.blockX}, ${loc.blockY}, ${loc.blockZ}</#>".toGradientColor()
        // 宝箱被发现前缀
        val prefixText = DefaultComponent()
            .append("<#ffe0a2,f5c66a>藏宝点被发现 > </#>".toGradientColor()).bold()
            .toLegacyText()
        // 提示宝箱已被找到
        game.playerModule.getOnlineUsers().forEach {
            it.sendMessage(
                "",
                "$prefixText$locationText§f的宝箱被<#deffd2,bee8ff>${player.name}</#>§f打开了!".toGradientColor(),
                ""
            )
            it.playSound(it, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
        }
    }

    // 设置宝箱随机物品
    private fun setChestRandomItems(chest: Chest) {
        chest.customName = "测试"
        chest.inventory.clear()
        chest.open()
        chest.update()
    }

    // 重置所有宝箱
    fun resetChests() {
        for (entry in findChests) {
            // 获取宝箱坐标的方块
            val block = entry.key.block
            // 确保是宝箱
            if (block.type != Material.CHEST) {
                continue
            }
            val chest = block.state as Chest
            chest.customName = null
            chest.inventory.clear()
            chest.close()
            chest.update()
        }
    }

    // 获取剩余宝箱数量
    fun getRemainChestCount(): Int {
        return game.setting.chestCount - findChests.size
    }

    // 获取该位置宝箱找到它的玩家uuid
    fun getChestFoundUUID(loc: Location): UUID? {
        return findChests[loc]
    }

}