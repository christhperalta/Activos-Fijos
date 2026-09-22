package com.christhperalta.activosfijos.feature.scanner.presentation

import androidx.lifecycle.ViewModel
import com.christhperalta.activosfijos.SoundPlayer

class ScannerViewModel (
    private val soundPlayer: SoundPlayer
) : ViewModel() {

    fun onButtonClick() {
        soundPlayer.play("beep")
    }

    fun onDispose() {
        soundPlayer.release()
    }
}