package top.ycmt.thetrackofshadow.game.event

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.chat.impl.DefaultComponent
import taboolib.platform.util.hoverItem
import taboolib.platform.util.isNotAir
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendSpigotMsg


// 玩家死亡事件
object PlayerDeath {
    @SubscribeEvent
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val player = e.entity
        // 获取玩家所在的游戏
        val playerGamePair = GameManager.getPlayerGame(player) ?: return
        val game = playerGamePair.first
        val playerType = playerGamePair.second
        // 如果不是玩家则跳出
        if (playerType != GameManager.PlayerType.PLAYER) {
            return
        }
        // 确保游戏处于运行阶段
        if (game.phaseState != PhaseState.RUNNING_PHASE) {
            return
        }
        // 击杀者
        val killer = player.killer
        // 事件流程
        deathMessage(game, player, killer) // 玩家死亡信息
        scoreIncentive(game, player, killer) // 积分奖惩
        highLightKiller(player, killer) // 高亮击杀者
        statsDeath(game, player, killer) // 死亡统计信息
        deathRespawn(game, player) // 玩家死亡重生
    }

    // 玩家死亡消息
    private fun deathMessage(game: Game, player: Player, killer: Player?) {
        // 玩家死亡坐标
        val loc: Location = player.location
        // 给玩家播放死亡音效
        player.playSound(loc, Sound.ENTITY_BAT_DEATH, 1f, 1f)
        // 劈个雷提示玩家
        loc.world?.strikeLightningEffect(loc)
        // 最终击杀文本 不可以重生就是最终击杀
        val finalKillText = if (!game.respawnModule.isRespawnable()) " §b§l最终击杀!" else ""
        // 击杀者不存在或者是玩家自身
        if (killer == null || killer == player) {
            game.playerModule.getOnlineUsers().forEach {
                it.sendMsg("<#dcffcc,9adbb1>${player.name}</#>§f死了!$finalKillText".toGradientColor())
            }
            return
        }
        val itemInMainHand = killer.inventory.itemInMainHand
        val itemInOffHand = killer.inventory.itemInOffHand
        // 获取击杀者使用的物品
        val killerItem: ItemStack? = when {
            itemInOffHand.type == Material.BOW -> itemInOffHand
            itemInMainHand.isNotAir() -> itemInMainHand
            itemInOffHand.isNotAir() -> itemInOffHand
            else -> null
        }
        // 使用的物品文本
        val killerUseItemText = DefaultComponent()
        if (killerItem != null) {
            // 获取击杀者使用的物品名称
            var killerItemName = killerItem.type.name
            // 判断物品是否设置了自定义名称
            if (killerItem.hasItemMeta() && killerItem.itemMeta != null) {
                killerItemName = killerItem.itemMeta!!.displayName
            }
            killerUseItemText
                .append("§f使用")
                .append(
                    DefaultComponent()
                        .append("<#f7c79c,ef987d>$killerItemName</#>".toGradientColor())
                        .hoverItem(killerItem)
                )
        } else {
            // 没有使用物品
            killerUseItemText
                .append("§f赤手空拳")
        }
        // 死亡位置文本
        val deathLocationText =
            "§f位于<#b4f1ff,8ab7e1>${loc.blockX}, ${loc.blockY}, ${loc.blockZ}</#>".toGradientColor()
        // 提示死亡信息
        val deathMessage = DefaultComponent()
            .append("<#dcffcc,9adbb1>${player.name}</#>§f被<#ff9c9c,de4949>${killer.name}</#>".toGradientColor())
            .append(killerUseItemText)
            .append(deathLocationText)
            .append("§f击败!")
            .append(finalKillText)
        game.playerModule.getOnlineUsers().forEach {
            it.sendSpigotMsg(deathMessage)
        }
    }

    // 积分奖惩
    private fun scoreIncentive(game: Game, player: Player, killer: Player?) {
        // 击杀者不存在或者是玩家自身
        if (killer == null || killer == player) {
            return
        }
        // 判断是否扣除积分 如是则惩罚玩家
        if (game.scoreModule.deductScore) {
            return
        }
        // 被击杀者的积分
        val playerScore = game.scoreModule.getPlayerScore(player)
        // 扣除的积分
        val reduceScore = (playerScore / 2).coerceAtLeast(10)
        // 根据倍率奖励的积分
        val rewardScore = (reduceScore * game.scoreModule.scoreMultiple).toInt()
        // 给予被击杀者惩罚
        game.scoreModule.cutPlayerScore(player, reduceScore)
        player.sendMessage("<#deffd2,bee8ff>扣除${reduceScore}积分✫, 你的积分已被掠夺!</#>".toGradientColor())
        // 给予击杀者奖励
        game.scoreModule.addPlayerScore(killer, rewardScore)
        killer.sendMessage("<#ffcbcb,ff7093>增加${rewardScore}积分✫, §e你成功掠夺积分!</#>".toGradientColor())
    }


    // 高亮击杀者
    private fun highLightKiller(player: Player, killer: Player?) {
        // 击杀者不存在或者是玩家自身
        if (killer == null || killer == player) {
            return
        }
        // 全图高亮击杀者5s 并且 不能覆盖已有的效果
        if (!killer.hasPotionEffect(PotionEffectType.GLOWING)) {
            killer.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 5 * 20, 0))
        }
    }

    // 死亡统计信息
    private fun statsDeath(game: Game, player: Player, killer: Player?) {
        val playerStatsInfo = game.statsModule.getPlayerStats(player) ?: return
        // 增加死亡次数
        playerStatsInfo.deathCount += 1

        // 击杀者为空就跳出
        if (killer == null) {
            return
        }
        // 获取击杀者的统计信息
        val killerStatsInfo = game.statsModule.getPlayerStats(killer) ?: return
        // 增加击杀玩家次数
        killerStatsInfo.killPlayerCount += 1
    }

    // 玩家死亡重生
    private fun deathRespawn(game: Game, player: Player) {
        // 判断是否可以重生
        if (game.respawnModule.isRespawnable()) {
            // 重生玩家
            game.respawnModule.respawnPlayer(player)
        } else {
            // 最终死亡那就是变成观察者
            game.playerModule.playerToWatcher(player)
        }
        player.health = 20.0 // 确保玩家不死亡
    }
}