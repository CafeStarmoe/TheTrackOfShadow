package top.ycmt.thetrackofshadow.game.event

import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.getMeta
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.game.state.PhaseState
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendFailMsg


// 实体受到伤害了来自其他实体事件
object EntityDamageByEntity {
    @SubscribeEvent
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        // 判断实体是否为玩家
        if (e.entity !is Player) {
            return
        }
        val player = e.entity as Player
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
        // 事件流程
        setAnvilDamage(e, game) // 设置铁砧砸到实体的伤害
        cancelPVPState(e, game, player) // 游戏禁止PVP状态
        respawnProtect(e, game, player) // 重生后PVP保护
        playerDamageMultiple(e, game) // 受到其他玩家攻击伤害翻倍
        playerDamageSpeed(e, player) // 受到其他玩家攻击给予速度效果
    }

    // 设置铁砧砸到实体的伤害
    private fun setAnvilDamage(e: EntityDamageByEntityEvent, game: Game) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 没有游戏对象的元属性就不清除
        val metadataList = e.damager.getMeta("game")
        if (metadataList.isEmpty()) {
            return
        }
        val anvilGame = metadataList[0].value() as Game? ?: return
        // 确保受到伤害的玩家是同个游戏
        if (anvilGame.setting.gameName != game.setting.gameName) {
            return
        }
        // 判断是否为下落方块
        if (e.damager !is FallingBlock) {
            return
        }
        val fallingBlock = e.damager as FallingBlock
        // 判断下落的方块是否为铁砧
        if (fallingBlock.blockData.material == Material.ANVIL) {
            e.damage = 10.0 // 设置伤害
        }
    }

    // 游戏禁止PVP状态
    private fun cancelPVPState(e: EntityDamageByEntityEvent, game: Game, player: Player) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 确保玩家存在禁止PVP状态
        if (!game.cancelModule.containsCancelState(player, CancelState.CANCEL_PVP)) {
            return
        }
        // 获取攻击者
        val damagerPlayer: Player = getShooterPlayer(e.damager) ?: return
        // 如果攻击者是玩家自己就不阻止
        if (damagerPlayer.uniqueId != player.uniqueId) {
            // 提示攻击者
            damagerPlayer.sendFailMsg("游戏暂时还没开启PVP!")
            e.isCancelled = true
        }
    }

    // 重生后PVP保护
    private fun respawnProtect(e: EntityDamageByEntityEvent, game: Game, player: Player) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 确保玩家存在禁止PVP状态
        if (!game.cancelModule.containsCancelState(player, CancelState.CANCEL_PVP_RESPAWN_PROTECT)) {
            return
        }
        // 阻止一切实体造成伤害
        e.isCancelled = true
        // 获取攻击者
        val damagerPlayer: Player = getShooterPlayer(e.damager) ?: return
        // 如果攻击者是玩家自己就不阻止
        if (damagerPlayer.uniqueId != player.uniqueId) {
            // 提示攻击者
            damagerPlayer.sendFailMsg("对方还在重生保护状态!")
        }
    }

    // 获取射出实体的攻击者
    private fun getShooterPlayer(damager: Entity): Player? {
        when (damager) {
            // 玩家
            is Player -> {
                return damager
            }
            // 箭矢
            is Arrow -> {
                val shooter = damager.shooter
                if (shooter !is Player) {
                    return null
                }
                return shooter
            }
            // 三叉戟
            is Trident -> {
                val shooter = damager.shooter
                if (shooter !is Player) {
                    return null
                }
                return shooter
            }
        }
        return null
    }

    // 受到其他玩家攻击伤害翻倍
    private fun playerDamageMultiple(e: EntityDamageByEntityEvent, game: Game) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 玩家受到的伤害*=伤害倍数
        e.damage *= game.damageModule.damageMultiple
    }

    // 受到其他玩家攻击给予速度效果
    private fun playerDamageSpeed(e: EntityDamageByEntityEvent, player: Player) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 确保攻击者是玩家
        if (e.damager !is Player) {
            return
        }
        // 给予玩家基于扣的血量的速度II效果
        val duration = (e.damage * 10).toInt().coerceAtMost(5 * 20)
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, duration, 1))
    }

}