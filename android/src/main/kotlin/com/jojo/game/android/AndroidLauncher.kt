package com.jojo.game.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.jojo.game.GameLaunchConfiguration
import com.jojo.game.JojoGame

/** Android entry point sharing the same Kotlin/LibGDX game runtime as desktop. */
class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(JojoGame(GameLaunchConfiguration()), AndroidApplicationConfiguration().apply {
            useImmersiveMode = true
            useWakelock = true
        })
    }
}
