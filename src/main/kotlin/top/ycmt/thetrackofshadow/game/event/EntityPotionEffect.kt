package top.ycmt.thetrackofshadow.game.event

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.Pair
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.SubscribeEvent
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.GameManager
import top.ycmt.thetrackofshadow.game.state.PhaseState


// 实体药水效果事件
object EntityPotionEffect {
    @SubscribeEvent
    fun onPotionEffect(e: EntityPotionEffectEvent) {
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

        invisibleEquipment(e, game, player) // 隐身时隐藏装备
    }

    // 隐身时隐藏装备
    private fun invisibleEquipment(e: EntityPotionEffectEvent, game: Game, player: Player) {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT) // 玩家装备发包
        packet.integers.write(0, player.entityId) // 设置实体id

        // 玩家获得隐身药水效果 隐藏装备显示
        if (e.newEffect?.type == PotionEffectType.INVISIBILITY) {
            packet.slotStackPairLists.write(
                0, listOf(
                    Pair(EnumWrappers.ItemSlot.MAINHAND, ItemStack(Material.AIR)),
                    Pair(EnumWrappers.ItemSlot.OFFHAND, ItemStack(Material.AIR)),
                    Pair(EnumWrappers.ItemSlot.FEET, ItemStack(Material.AIR)),
                    Pair(EnumWrappers.ItemSlot.LEGS, ItemStack(Material.AIR)),
                    Pair(EnumWrappers.ItemSlot.CHEST, ItemStack(Material.AIR)),
                    Pair(EnumWrappers.ItemSlot.HEAD, ItemStack(Material.AIR)),
                )
            )
        } else if (e.oldEffect?.type == PotionEffectType.INVISIBILITY) {
            // 玩家隐身药水效果过期 恢复装备显示
            packet.slotStackPairLists.write(
                0, listOf(
                    Pair(EnumWrappers.ItemSlot.MAINHAND, player.inventory.itemInMainHand),
                    Pair(EnumWrappers.ItemSlot.OFFHAND, player.inventory.itemInOffHand),
                    Pair(EnumWrappers.ItemSlot.FEET, player.inventory.boots),
                    Pair(EnumWrappers.ItemSlot.LEGS, player.inventory.leggings),
                    Pair(EnumWrappers.ItemSlot.CHEST, player.inventory.chestplate),
                    Pair(EnumWrappers.ItemSlot.HEAD, player.inventory.helmet),
                )
            )
        }
        // 发送给游戏里的玩家
        for (p in game.playerModule.getOnlinePlayers()) {
            // 玩家自己隐身可以看到自己的装备
            if (p.entityId == player.entityId) {
                continue
            }
            // 确保处于同一个世界
            if (p.world != player.world) {
                continue
            }
            // 发送数据包
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet, false)
        }
    }

}