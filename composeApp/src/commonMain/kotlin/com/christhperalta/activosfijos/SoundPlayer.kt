package com.christhperalta.activosfijos

expect class SoundPlayer() {
    fun play(soundPath: String)
    fun stop()
    fun release()
}