package com.chronoio.chrono.payments.mapping

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import com.chronoio.chrono.core_ui.R
import com.chronoio.chrono.payments.models.PeriodDescription
import com.chronoio.chrono.payments.models.PeriodUnit
import com.chronoio.chrono.payments.models.TierPeriod
import java.time.Period
import timber.log.Timber

fun String.parsePeriod(): PeriodDescription? {
    return try {
        val period = Period.parse(this)
        when {
            period.years > 0 -> PeriodDescription(period.years, PeriodUnit.YEARS)
            period.months > 0 -> PeriodDescription(period.months, PeriodUnit.MONTHS)
            period.days > 0 -> PeriodDescription(period.days, PeriodUnit.DAYS)
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

@Composable
fun LocalizedPeriodString(desc: PeriodDescription?): String {
    val context = LocalContext.current
    val localeList = context.resources.configuration.locales
    if (!localeList.isEmpty && desc != null) {
        val quantityStringId = when (desc.unit) {
            PeriodUnit.YEARS -> R.plurals.period_years
            PeriodUnit.MONTHS -> R.plurals.period_months
            PeriodUnit.DAYS -> R.plurals.period_days
            PeriodUnit.WEEKS -> R.plurals.period_weeks
        }
        return pluralStringResource(
            id = quantityStringId,
            count = desc.amount,
            formatArgs = arrayOf(desc.amount)
        )
    } else {
        Timber.e("Error getting the locale or desc is null")
        return ""
    }
}

fun TierPeriod.toPeriodDescription(): PeriodDescription {
    return when (this) {
        is TierPeriod.Unknown -> PeriodDescription(0, PeriodUnit.DAYS)
        is TierPeriod.Unlimited -> PeriodDescription(Int.MAX_VALUE, PeriodUnit.DAYS)
        is TierPeriod.Year -> PeriodDescription(count, PeriodUnit.YEARS)
        is TierPeriod.Month -> PeriodDescription(count, PeriodUnit.MONTHS)
        is TierPeriod.Week -> PeriodDescription(count, PeriodUnit.WEEKS)
        is TierPeriod.Day -> PeriodDescription(count, PeriodUnit.DAYS)
    }
}