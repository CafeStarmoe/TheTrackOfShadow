package top.ycmt.thetrackofshadow.game.phase.impl

import org.bukkit.Sound
import org.bukkit.entity.Player
import taboolib.module.chat.toGradientColor
import top.ycmt.thetrackofshadow.constant.MessageConst
import top.ycmt.thetrackofshadow.game.Game
import top.ycmt.thetrackofshadow.game.phase.PhaseAbstract
import top.ycmt.thetrackofshadow.pkg.scoreboard.ScoreBoard
import java.text.SimpleDateFormat
import java.util.*

// 大厅等待阶段
class LobbyPhase(override val game: Game) : PhaseAbstract() {
    // 剩余等待的时间
    private var waitTick: Long = game.gameSetting.lobbyWaitTick

    override fun onTick() {
        val onlinePlayers = game.playerModule.getOnlinePlayers()

        // 刷新记分板
        refreshBoard(onlinePlayers)

        // 校验玩家人数是否符合开始游戏所需最小玩家数
        if (onlinePlayers.size < game.gameSetting.minPlayerCount) {
            // 重置剩余等待时间并提示玩家
            if (waitTick != game.gameSetting.lobbyWaitTick) {
                waitTick = game.gameSetting.lobbyWaitTick
                // 提示玩家
                notifyWaitMorePlayer(onlinePlayers)
            }
            return
        }

        // 根据剩余时间执行
        when (waitTick) {
            in 1..5, 10L -> notifyCountdown(onlinePlayers)
            0L -> {
                // 剩余等待时间结束 此阶段完成
                this.Done()
                return
            }

            else -> {
                // 不是指定的时间每10秒提示一次
                if (waitTick % 10 == 0L) {
                    notifyCountdown(onlinePlayers)
                }
            }
        }
        waitTick--
    }

    // 提示玩家等待更多玩家
    private fun notifyWaitMorePlayer(onlinePlayers: List<Player>) {
        game.playSound(onlinePlayers, Sound.BLOCK_NOTE_BLOCK_HAT)
        game.sendTitle(
            onlinePlayers,
            "等待更多玩家加入...".toGradientColor(listOf(0xffcbcb, 0xff7093)),
            "",
            5,
            30,
            5
        )
        game.sendMessage(
            onlinePlayers,
            "玩家数量不足, 等待更多玩家...".toGradientColor(
                listOf(0xffcbcb, 0xff7093)
            )
        )
    }

    // 提示倒计时
    private fun notifyCountdown(onlinePlayers: List<Player>) {
        // 倒计时颜色
        val countdownColor = when (waitTick) {
            10L -> listOf(0xf7c79c, 0xef987d)
            in 1..5 -> listOf(0xff9c9c, 0xde4949)
            else -> listOf(0xc1c1ff, 0x7373ff)
        }

        game.playSound(onlinePlayers, Sound.BLOCK_NOTE_BLOCK_HAT)
        game.sendTitle(
            onlinePlayers, "$waitTick".toGradientColor(countdownColor), "", 5, 20, 5
        )
        game.sendMessage(
            onlinePlayers,
            "游戏将在".toGradientColor(
                listOf(
                    0xf5ead0,
                    0xeee6a1
                )
            ) + "${waitTick}秒".toGradientColor(countdownColor) + "后开始哦~".toGradientColor(
                listOf(0xf5ead0, 0xeee6a1)
            )
        )
    }

    // 刷新或创建计分板
    private fun refreshBoard(onlinePlayers: List<Player>) {
        // 设置日期格式
        val df = SimpleDateFormat("MM/dd/yy")

        for (p in game.playerModule.getOnlinePlayers()) {
            val board: ScoreBoard =
                if (ScoreBoard.hasScore(p)) ScoreBoard.getByPlayer(p)!! else ScoreBoard.createScore(
                    p,
                    MessageConst.CNLogoMessage
                )
            board.setSlot(11, "§7${df.format(Date())}  §8${game.gameSetting.gameName}")
            board.setSlot(10, "")
            board.setSlot(9, "§f地图: ${game.gameSetting.mapName.toGradientColor(listOf(0xdcffcc, 0x9adbb1))}")
            board.setSlot(
                8,
                "§f玩家: " + "${onlinePlayers.size}/${game.gameSetting.maxPlayerCount}".toGradientColor(
                    listOf(
                        0xdcffcc,
                        0x9adbb1
                    )
                )
            )
            board.setSlot(7, "")
            board.setSlot(
                6,
                if (onlinePlayers.size >= game.gameSetting.minPlayerCount) "§f即将开始: " + "${waitTick}秒".toGradientColor(
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