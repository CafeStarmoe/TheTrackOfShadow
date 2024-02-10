package top.ycmt.thetrackofshadow.game.event

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.potion.PotionEffectType
import taboolib.platform.util.bukkitPlugin
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState

// 实体装备数据包事件
object EntityEquipmentPacket : PacketAdapter(bukkitPlugin, PacketType.Play.Server.ENTITY_EQUIPMENT) {
    override fun onPacketSending(e: PacketEvent) {
        val player = e.player
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
        invisibleCancelEquipment(e, game) // 玩家隐身时取消更新装备
    }

    // 玩家隐身时取消更新装备
    private fun invisibleCancelEquipment(e: PacketEvent, game: Game) {
        // 更新的目标实体id
        val entityId = e.packet.integers.values[0]
        // 获取目标实体的玩家
        val entityPlayer = game.playerModule.getOnlinePlayers().find { it.entityId == entityId } ?: return
        // 确保目标实体有隐身
        if (!entityPlayer.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            return
        }
        // 阻止更新目标玩家的装备
        e.isCancelled = true
    }
}