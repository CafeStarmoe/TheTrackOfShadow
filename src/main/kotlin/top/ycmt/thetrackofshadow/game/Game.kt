package top.ycmt.thetrackofshadow.game

// 游戏对象
class Game {
    // 游戏id
    var gameId: UInt = 0u
        private set

    // 游戏名
    private var gameName: String = ""

    constructor(gameId: UInt, gameName: String) {
        this.gameId = gameId
        this.gameName = gameName
    }
}