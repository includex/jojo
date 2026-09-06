package com.jojo.game.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.jojo.game.application.runtime.GameLaunchConfiguration
import com.jojo.game.JojoGame

/** 데스크톱과 동일한 게임 런타임을 시작하는 안드로이드 진입점입니다. */
class AndroidLauncher : AndroidApplication() {
    /** 안드로이드 화면과 게임 런타임을 연결합니다. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(JojoGame(GameLaunchConfiguration()), AndroidApplicationConfiguration().apply {
            useImmersiveMode = true
            useWakelock = true
        })
    }
}
