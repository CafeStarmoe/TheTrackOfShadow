package top.ycmt.thetrackofshadow.game.module

import top.ycmt.thetrackofshadow.game.Game

// 出生点管理模块
class SpawnModule(private val game: Game) {
    var enableProtect = true // 是否启用出生点保护以及回复
    var reverse = false // 是否反向回复
}