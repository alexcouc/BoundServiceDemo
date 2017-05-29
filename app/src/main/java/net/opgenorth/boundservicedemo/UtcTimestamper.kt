package net.opgenorth.boundservicedemo

import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder
import timber.log.Timber

class UtcTimestamper : IGetFormattedTimestamp {
    override fun getFormattedTimestamp(): String? {
        val now = DateTime();
        val period = Period(startTime, now)

        val timestamp = "Timestamp service was started ${periodFormatter.print(period)}."
        Timber.i(timestamp)
        return timestamp
    }

    var startTime = DateTime();

    private val periodFormatter: PeriodFormatter
        get() {
            val formatter = PeriodFormatterBuilder()
                    .appendSeconds().appendSuffix(" seconds ago\n")
                    .appendMinutes().appendSuffix(" minutes ago\n")
                    .appendHours().appendSuffix(" hours ago\n")
                    .printZeroAlways()
                    .toFormatter()
            return formatter
        }


}