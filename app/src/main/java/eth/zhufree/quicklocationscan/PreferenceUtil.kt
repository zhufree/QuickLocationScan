package eth.zhufree.quicklocationscan

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import eth.zhufree.quicklocationscan.ScanApplication.Companion.context

/**
 * Created by zhufree on 2018/8/30.
 * SharedPreference工具类
 */

object PreferenceUtil {

    private const val SP_NAME = "app"

    private fun getPrivateSharedPreference(): SharedPreferences? {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 工具方法
     */

    fun getStringValue(key: String): String {
        return getPrivateSharedPreference()?.getString(key, "") ?: ""
    }

    fun setStringValue(key: String, value: String) {
        getPrivateSharedPreference()?.edit {
            putString(key, value)
        }
    }
}