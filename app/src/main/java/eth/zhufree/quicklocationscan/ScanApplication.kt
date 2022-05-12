package eth.zhufree.quicklocationscan

import android.app.DownloadManager
import android.content.Context
import androidx.multidex.MultiDexApplication

class ScanApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }

    companion object {
        lateinit var context: Context
    }
}