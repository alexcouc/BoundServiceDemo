package net.opgenorth.boundservicedemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class TimestampService : Service(), IGetFormattedTimestamp {

    private var timestamper: UtcTimestamper? = null

    var binder : TimestampBinder? = null
    override fun onCreate() {
        super.onCreate()
        Timber.d("onCreate")
        timestamper = UtcTimestamper()
    }

    override fun onBind(intent: Intent): IBinder? {
        Timber.d("onBind")

        this.binder = TimestampBinder(this)
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.d("onUnBind")

        binder = null

        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Timber.d("onDestroy")

        binder = null
        timestamper = null
        super.onDestroy()
    }

    override fun getFormattedTimestamp() : String? {
        return timestamper?.getFormattedTimestamp()
    }
}
