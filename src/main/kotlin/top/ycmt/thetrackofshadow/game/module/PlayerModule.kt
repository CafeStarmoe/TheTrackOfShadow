package top.ycmt.thetrackofshadow.game.module

import top.ycmt.thetrackofshadow.game.Game
import java.util.*

// 游戏玩家模块
class PlayerModule(game: Game) {
    // 玩家列表 记录uuid
    val playerList: MutableList<UUID> = mutableListOf()
}