package top.ycmt.thetrackofshadow.event

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg

// 玩家死亡事件
object PlayerDeath {
    @SubscribeEvent
    fun onPlayerDeath(e: PlayerDeathEvent) {
        respawn(e) // 玩家死亡重生
    }

    // 玩家死亡重生
    private fun respawn(e: PlayerDeathEvent) {
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

        // 给玩家播放死亡音效
        player.playSound(player.location, Sound.ENTITY_BAT_DEATH, 1f, 1f)
        // 给所有玩家提示死亡信息
        if (player.killer != null && player.killer != player) {
            val killer = player.killer ?: return // 获取杀手的对象
//            // 死亡者积分对象
//            val deathScore: GameScore = game.modScore.playerScores[player.uniqueId] ?: return
//            // 击杀者积分对象
//            val killerScore: GameScore = game.modScore.playerScores[killer.uniqueId] ?: return

            // 被击杀者死亡坐标
            val loc: Location = player.location
            loc.world?.strikeLightningEffect(loc) // 劈个雷提示玩家
            // 先向所有玩家公开死亡信息
            game.playerModule.getOnlineUsers().forEach {
                it.sendMsg("§6${player.name}§7被§6${killer.name}${if (killer.inventory.itemInOffHand.type == Material.BOW) "§7使用${killer.inventory.itemInOffHand.itemMeta?.displayName}" else if (killer.inventory.itemInMainHand.type != Material.AIR) "§7使用${killer.inventory.itemInMainHand.itemMeta?.displayName}" else ""}§7位于§b§lX:§b${loc.blockX} §b§lY:§b${loc.blockY} §b§lZ:§b${loc.blockZ}§7击败${if (!game.respawnModule.isRespawnable()) " §b§l最终击杀!" else ""}")
            }

//            // 判断是否扣除积分 如是则惩罚玩家
//            if (game.modScore.deductScore) {
//                val reduceScore = (deathScore.score / 2).coerceAtLeast(10)
//                // 给予被击杀者惩罚
//                deathScore.score -= reduceScore
//                player.sendMessage("§b-${reduceScore}积分✫！§e你的积分已被掠夺！")
//                // 给予击杀者奖励
//                killerScore.score += reduceScore * game.modScore.scoreMultiple // 根据倍率给予玩家积分
//                killer.sendMessage("§a+${reduceScore * game.modScore.scoreMultiple}积分✫！§e你成功掠夺积分！")
//            }
//            killerScore.killCount++ // 击杀数+1
            // 全图高亮击杀者5s 并且 不能覆盖已有的效果
            if (!killer.hasPotionEffect(PotionEffectType.GLOWING)) {
                killer.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 5 * 20, 0))
            }
        } else {
            game.playerModule.getOnlineUsers().forEach {
                it.sendMsg("§6${player.name}§7死了。${if (!game.respawnModule.isRespawnable()) "§b§l最终击杀！" else ""}")
            }
        }
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