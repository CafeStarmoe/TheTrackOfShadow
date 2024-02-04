package top.ycmt.thetrackofshadow.game.phase.impl

import org.bukkit.Sound
import taboolib.module.chat.toGradientColor
import top.ycmt.thetrackofshadow.constant.LegacyTextConst.CN_LOGO_LEGACY_TEXT
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.phase.PhaseAbstract
import top.ycmt.thetrackofshadow.pkg.scoreboard.ScoreBoard
import top.ycmt.thetrackofshadow.pkg.sendmsg.sendMsg
import java.text.SimpleDateFormat
import java.util.*

// 大厅等待阶段
class LobbyPhase(override val game: Game) : PhaseAbstract() {
    // 剩余等待的时间
    private var waitTick = game.setting.lobbyWaitTick

    override fun onTick() {
        // 刷新记分板
        refreshBoard()

        // 校验玩家人数是否符合开始游戏所需最小玩家数
        if (game.playerModule.getOnlinePlayers().size < game.setting.minPlayerCount) {
            // 重置剩余等待时间并提示玩家
            if (waitTick != game.setting.lobbyWaitTick) {
                waitTick = game.setting.lobbyWaitTick
                // 提示玩家
                notifyWaitMorePlayer()
            }
            return
        }

        // 根据剩余时间执行
        when (waitTick) {
            in 1..5, 10L -> notifyCountdown()

            0L -> {
                // 剩余等待时间结束 此阶段完成
                this.done()
                return
            }

            else -> {
                // 不是指定的时间每10秒提示一次
                if (waitTick % 10 == 0L) {
                    notifyCountdown()
                }
            }
        }
        waitTick--
    }

    // 提示玩家等待更多玩家
    private fun notifyWaitMorePlayer() {
        game.playerModule.getOnlinePlayers().forEach {
            it.sendMsg(
                "玩家数量不足, 等待更多玩家...".toGradientColor(
                    listOf(0xffcbcb, 0xff7093)
                )
            )
            it.sendTitle(
                "等待更多玩家加入...".toGradientColor(listOf(0xffcbcb, 0xff7093)),
                "",
                5,
                30,
                5
            )
            it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
        }
    }

    // 提示倒计时
    private fun notifyCountdown() {
        // 倒计时颜色
        val countdownColor = when (waitTick) {
            10L -> listOf(0xf7c79c, 0xef987d)
            in 1..5 -> listOf(0xff9c9c, 0xde4949)
            else -> listOf(0xc1c1ff, 0x7373ff)
        }

        game.playerModule.getOnlinePlayers().forEach {
            it.sendMsg(
                "游戏将在".toGradientColor(
                    listOf(
                        0xf5ead0,
                        0xeee6a1
                    )
                ) + "${waitTick}秒".toGradientColor(countdownColor) + "后开始哦~".toGradientColor(
                    listOf(0xf5ead0, 0xeee6a1)
                )
            )
            it.sendTitle("$waitTick".toGradientColor(countdownColor), "", 5, 20, 5)
            it.playSound(it, Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f)
        }
    }

    // 刷新或创建计分板
    private fun refreshBoard() {
        // 设置日期格式
        val df = SimpleDateFormat("MM/dd/yy")

        for (p in game.playerModule.getOnlinePlayers()) {
            val board: ScoreBoard =
                if (ScoreBoard.hasScore(p)) ScoreBoard.getByPlayer(p)!! else ScoreBoard.createScore(
                    p,
                    CN_LOGO_LEGACY_TEXT
                )
            board.setSlot(11, "§7${df.format(Date())}  §8${game.setting.gameName}")
            board.setSlot(10, "")
            board.setSlot(9, "§f地图: ${game.setting.mapName.toGradientColor(listOf(0xdcffcc, 0x9adbb1))}")
            board.setSlot(
                8,
                "§f玩家: " + "${game.playerModule.getOnlinePlayers().size}/${game.setting.maxPlayerCount}".toGradientColor(
                    listOf(
                        0xdcffcc,
                        0x9adbb1
                    )
                )
            )
            board.setSlot(7, "")
            board.setSlot(
                6,
                if (game.playerModule.getOnlinePlayers().size >= game.setting.minPlayerCount) "§f即将开始: " + "${waitTick}秒".toGradientColor(
                    listOf(0xdcffcc, 0x9adbb1)
                ) else "§f等待中..."
            )
            board.setSlot(5, "")
            board.setSlot(4, "§f模式: " + "单挑".toGradientColor(listOf(0xdcffcc, 0x9adbb1)))
            board.setSlot(3, "§f版本: " + "2.0 Beta".toGradientColor(listOf(0xffd6d6, 0xe14d4d)))
            board.setSlot(2, "")
            board.setSlot(1, "mc.ycmt.top".toGradientColor(listOf(0xfff4ba, 0xf4f687)))
        }
    }

}