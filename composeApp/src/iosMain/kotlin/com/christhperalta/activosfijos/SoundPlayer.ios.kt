package com.christhperalta.activosfijos

import platform.AVFAudio.AVAudioPlayer

actual class SoundPlayer actual constructor() {
    private var player: AVAudioPlayer? = null

    actual fun play(soundPath: String) { /* igual que antes */ }
    actual fun stop() { player?.stop() }
    actual fun release() { player?.stop(); player = null }
}