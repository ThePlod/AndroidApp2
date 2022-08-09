package dong.android.plod.di

import android.app.Application
import dong.android.plod.pref.SharedPreferencesHelper

class App : Application() {

    companion object {
        lateinit var pref: SharedPreferencesHelper
    }

    override fun onCreate() {
        super.onCreate()
        pref = SharedPreferencesHelper(applicationContext)
    }
}