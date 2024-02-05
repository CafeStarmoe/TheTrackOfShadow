package top.ycmt.thetrackofshadow.event

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.FallingBlock
import org.bukkit.event.entity.EntityChangeBlockEvent
import taboolib.common.platform.event.SubscribeEvent


// 实体改变方块事件
object EntityChangeBlock {
    @SubscribeEvent
    fun onEntityChangeBlock(e: EntityChangeBlockEvent) {
        cleanAnvil(e) // 清理铁砧
    }

    // 清理下落到地上的铁砧
    private fun cleanAnvil(e: EntityChangeBlockEvent) {
        val entity = e.entity

        // 判断实体是否为正在掉落的方块
        if (entity !is FallingBlock) {
            return
        }
        // 判断实体是铁砧
        if (entity.blockData.material != Material.ANVIL && entity.blockData.material != Material.CHIPPED_ANVIL && entity.blockData.material != Material.DAMAGED_ANVIL) {
            return
        }
        // 确保实体已经掉到地上了
        if (!entity.isOnGround()) {
            return
        }

        // 取消事件
        e.isCancelled = true
        // 从世界中移除实体
        entity.remove()
        // 播放铁砧落地的声音
        entity.getWorld().playSound(entity.location, Sound.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1.0f, 1.0f);
        // 生成烟雾效果
        entity.getWorld().spawnParticle(Particle.SMOKE_NORMAL, entity.location, 50, 0.5, 0.5, 0.5, 0.1);
    }
}