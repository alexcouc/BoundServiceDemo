package net.opgenorth.boundservicedemo

import android.os.Binder

class TimestampBinder(var service: TimestampService) : Binder() {
    fun getFormattedTimestamp(): String? {
        return this.service.getFormattedTimestamp()
    }
}