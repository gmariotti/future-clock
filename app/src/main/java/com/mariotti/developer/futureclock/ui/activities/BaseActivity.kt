package com.mariotti.developer.futureclock.ui.activities

import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.mariotti.developer.futureclock.BuildConfig
import com.mariotti.developer.futureclock.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // StrictMode for debugging purposes
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build()
            )

            StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build()
            )
        }
    }
}
