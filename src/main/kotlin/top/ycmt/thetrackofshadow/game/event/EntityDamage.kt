package top.ycmt.thetrackofshadow.game.event

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.CancelState
import top.ycmt.thetrackofshadow.game.state.PhaseState

// 实体受伤事件
object EntityDamage {
    @SubscribeEvent
    fun onEntityDamage(e: EntityDamageEvent) {
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
        // 事件流程
        lobbyCancelDamage(e, game) // 大厅取消受伤
        settleCancelDamage(e, game) // 结算阶段取消受伤
        cancelDamageState(e, game, player) // 游戏取消伤害状态
        protectNoPVPExplosion(e, game, player) // 游戏禁止PVP时爆炸保护
    }

    // 大厅取消受伤
    private fun lobbyCancelDamage(e: EntityDamageEvent, game: Game) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 确保游戏处于大厅等待阶段
        if (game.phaseState != PhaseState.LOBBY_PHASE) {
            return
        }
        // 取消受伤事件
        e.isCancelled = true
    }

    // 结算阶段取消受伤
    private fun settleCancelDamage(e: EntityDamageEvent, game: Game) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 确保游戏处于结算阶段
        if (game.phaseState != PhaseState.SETTLE_PHASE) {
            return
        }
        // 取消受伤事件
        e.isCancelled = true
    }

    // 游戏取消伤害状态
    private fun cancelDamageState(e: EntityDamageEvent, game: Game, player: Player) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 确保玩家存在取消伤害状态
        if (!game.cancelModule.containsCancelState(player, CancelState.CANCEL_DAMAGE)) {
            return
        }
        // 取消受伤事件
        e.isCancelled = true
    }

    // 游戏禁止PVP时爆炸保护
    private fun protectNoPVPExplosion(e: EntityDamageEvent, game: Game, player: Player) {
        // 事件已取消则跳出
        if (e.isCancelled) {
            return
        }
        // 确保玩家存在禁止PVP状态
        if (!game.cancelModule.containsCancelState(player, CancelState.CANCEL_PVP) &&
            !game.cancelModule.containsCancelState(player, CancelState.CANCEL_PVP_RESPAWN_PROTECT)
        ) {
            return
        }
        // 检查事件是否是爆炸造成的伤害
        if (e.cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION &&
            e.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
        ) {
            return
        }
        // 取消爆炸伤害
        e.isCancelled = true
    }

}