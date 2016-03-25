package com.mariotti.developer.futureclock.activities

import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.mariotti.developer.futureclock.BuildConfig
import com.mariotti.developer.futureclock.R

abstract class SingleFragmentActivity : AppCompatActivity() {
    protected abstract fun createFragment(): Fragment

    protected val layoutResId: Int = R.layout.activity_fragment

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

        setContentView(layoutResId)

        val fragmentManager = supportFragmentManager
        var fragment: Fragment? = fragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = createFragment()
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
}
