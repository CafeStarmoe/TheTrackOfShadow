package top.ycmt.thetrackofshadow.game.module

import top.ycmt.thetrackofshadow.game.Game

// 重生点管理模块
class SpawnModule(private val game: Game) {
    var enableRegain = true // 是否启用重生点回复血量
    var reverse = false // 是否反向回复
}