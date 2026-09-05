package com.jojo.port.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.jojo.port.JojoGame

/** Android entry point sharing the same Kotlin/LibGDX game runtime as desktop. */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(JojoGame(verifyMode = false), AndroidApplicationConfiguration().apply {
            useImmersiveMode = true
            useWakelock = true
        })
    }
}
