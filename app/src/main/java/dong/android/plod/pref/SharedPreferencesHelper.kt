package dong.android.plod.pref

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    companion object {
        const val PREF_NAME = "sharedPref"
        const val PRIVATE_MODE = Context.MODE_PRIVATE

        const val SIGNED_IN_AS = "signed_in_as"

        const val REFRESH_TOKEN = "refresh_token"
        const val ACCESS_TOKEN = "access_token"
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun signInAs(name: String) {
        editor.putString(SIGNED_IN_AS, name)
        editor.apply()
    }

    fun getUserName(): String {
        return pref.getString(SIGNED_IN_AS, "") ?: ""
    }

    fun setRefreshToken(refreshToken: String) {
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.apply()
    }

    fun getRefreshToken(): String {
        return pref.getString(REFRESH_TOKEN, "") ?: ""
    }

    fun setAccessToken(accessToken: String) {
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun getAccessToken(): String {
        return pref.getString(ACCESS_TOKEN, "") ?: ""
    }
}