package com.christhperalta.activosfijos


import android.media.MediaPlayer
import android.util.Log

actual class SoundPlayer actual constructor() {
    private var mediaPlayer: MediaPlayer? = null

    actual fun play(soundPath: String) {
        release()
        val context = SoundContextProvider.get()
        
        // Usar directamente el ID de recurso si es "beep"
        val resId = if (soundPath == "beep") {
            R.raw.beep
        } else {
            context.resources.getIdentifier(
                soundPath, "raw", context.packageName
            )
        }

        if (resId != 0) {
            try {
                mediaPlayer = MediaPlayer.create(context, resId)?.apply {
                    start()
                    setOnCompletionListener { it.release() }
                }
            } catch (e: Exception) {
                Log.e("SoundPlayer", "Error playing sound: ${e.message}")
            }
        } else {
            Log.e("SoundPlayer", "Resource not found: $soundPath in package ${context.packageName}")
        }
    }

    actual fun stop() {
        mediaPlayer?.stop()
    }

    actual fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}