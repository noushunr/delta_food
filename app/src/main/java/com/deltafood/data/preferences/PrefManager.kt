package com.deltafood.data.preferences

import android.content.Context
import com.deltafood.utils.KEY_IS_USER_LOGGED_IN
import com.deltafood.utils.KEY_SET_SITE
import com.deltafood.utils.KEY_USER_NAME
import com.deltafood.utils.KEY_USER_TOKEN


/**
 * Created by Ajith V M on 29/05/2020.
 **/

class PrefManager(var context: Context?) {


    private val sharedPreferences =
        context?.getSharedPreferences("eMooks_private_data", Context.MODE_PRIVATE)!!

    val editor = sharedPreferences.edit()

    var userName: String?
        get() {
            return sharedPreferences.getString(KEY_USER_NAME, "")
        }
        set(userName) {
            editor.putString(KEY_USER_NAME, userName)
            editor.commit()
        }

    var token: String?
        get() {
            return sharedPreferences.getString(KEY_USER_TOKEN, "")
        }
        set(token) {
            editor.putString(KEY_USER_TOKEN, token)
            editor.commit()
        }

    var isUserLoggedIn: Boolean?
        get() {
            return sharedPreferences.getBoolean(KEY_IS_USER_LOGGED_IN, false)
        }
        set(isUserLoggedIn) {
            editor.putBoolean(KEY_IS_USER_LOGGED_IN, isUserLoggedIn!!)
            editor.commit()
        }

    var setSite: String?
        get() {
            return sharedPreferences.getString(KEY_SET_SITE, "")
        }
        set(site) {
            editor.putString(KEY_SET_SITE, site)
            editor.commit()
        }
}