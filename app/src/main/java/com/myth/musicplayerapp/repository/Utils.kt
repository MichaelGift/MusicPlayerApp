package com.myth.musicplayerapp.repository

enum class PlayType { OneLoop, Shuffle, Linear }
enum class AppTheme { PINK, SHERPA_BLUE, BLUE, RED }

fun String.toAppTheme(): AppTheme {
    var theme = AppTheme.SHERPA_BLUE
    if (this == AppTheme.BLUE.toString()) theme = AppTheme.BLUE
    else if (this == AppTheme.PINK.toString()) theme = AppTheme.PINK
    else if (this == AppTheme.RED.toString()) theme = AppTheme.RED
    return theme
}