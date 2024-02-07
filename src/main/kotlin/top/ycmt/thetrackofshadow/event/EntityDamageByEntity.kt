package top.ycmt.thetrackofshadow.event

import org.bukkit.Material
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.getMeta
import top.ycmt.thetrackofshadow.game.Game


// 实体受到伤害了来自其他实体事件
object EntityDamageByEntity {
    @SubscribeEvent
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        setAnvilDamage(e) // 设置铁砧砸到实体的伤害
    }

    // 设置铁砧砸到实体的伤害
    private fun setAnvilDamage(e: EntityDamageByEntityEvent) {
        // 判断是否为下落方块
        if (e.damager !is FallingBlock) {
            return
        }
        // 没有游戏对象的元属性就跳出
        val game = e.damager.getMeta("game")[0].value() as Game? ?: return
        // 判断实体是否为玩家
        if (e.entity !is Player) {
            return
        }
        val player = e.entity as Player
        // 确保受到伤害的玩家是同个游戏的玩家
        if (!game.playerModule.containsPlayer(player)) {
            return
        }
        val fallingBlock = e.damager as FallingBlock
        // 判断下落的方块是否为铁砧
        if (fallingBlock.blockData.material == Material.ANVIL) {
            e.damage = 10.0 // 设置伤害
        }
    }
}