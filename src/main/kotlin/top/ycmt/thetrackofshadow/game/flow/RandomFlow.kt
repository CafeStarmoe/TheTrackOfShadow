package top.ycmt.thetrackofshadow.game.flow

import org.bukkit.Sound
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.flow.random.AnvilRandom
import top.ycmt.thetrackofshadow.game.flow.random.BarbadosRandom
import top.ycmt.thetrackofshadow.game.flow.random.BloodyRandom
import top.ycmt.thetrackofshadow.pkg.chat.GradientColor.toGradientColor
import top.ycmt.thetrackofshadow.pkg.chat.SendMsg.sendMsg
import java.util.concurrent.TimeUnit

// 随机事件
class RandomFlow(private val game: Game) : FlowInterface {
    override var finishTick = 20L // 下一次随机事件
    override val eventMsg = "距离随机事件" // 提示信息

    private val addTick: Long = TimeUnit.MINUTES.toSeconds(1)  // 每次增加的时间

    private val randomEvents = listOf(
//        ThunderRandom(game), // 天雷圣裁
//        MeetRandom(game), // 相遇之缘
//        RockBlessRandom(game), // 岩之祝福
//        InsightRandom(game), // 洞察一切
//        HeavenRandom(game), // 上天去吧
//        CattailRandom(game), // 猫尾特调
        AnvilRandom(game), // 铁砧雨
//        ConfineRandom(game), // 禁锢器
//        ReverseRandom(game), // 反向重生
        BarbadosRandom(game), // 喝醉的巴巴托斯
        BloodyRandom(game), // 血色之夜
//        PotatoRandom(game), // 熟土豆
//        LeyLineRandom(game), // 地脉喷涌
//        RottenRandom(game), // 开摆
    ) // 随机事件列表

    override fun exec(leftTick: Long) {
        // 根据时间改变颜色
        val leftTickColor = when (leftTick) {
            in 4L..5L -> "f7c79c,ef987d"
            in 1L..3L -> "ff9c9c,de4949"
            else -> ""
        }

        // 输出提示通知玩家
        when (leftTick) {
            in 1L..5L -> {
                game.playerModule.getOnlineUsers().forEach {
                    it.sendMsg("<#b3ddfa,90bdff>随机事件将在</#><#$leftTickColor>${leftTick}秒</#><#b3ddfa,90bdff>后发生!</#>".toGradientColor())
                    it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
                }
            }

            0L -> {
                // 随机抽取一个事件
                randomEvents.shuffled().take(1).forEach {
                    it.exec() // 执行随机事件
                }
                finishTick += addTick // 计算出下一次执行时间
            }
        }
    }

}