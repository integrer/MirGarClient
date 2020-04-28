package org.mirgar.android.common.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

data class ActivityResult(val resultCode: Int, val data: Intent?)

abstract class AsyncActivity : AppCompatActivity() {

    private var currentCode = 0
    private val resultByCode = mutableMapOf<Int, CompletableDeferred<ActivityResult?>>()

    /**
     * Launches the intent allowing to process the result using await()
     *
     * @param intent the intent to be launched.
     *
     * @return Deferred<ActivityResult>
     */
    fun launchIntent(intent: Intent): Deferred<ActivityResult?> {
        val activityResult = CompletableDeferred<ActivityResult?>()

        if (intent.resolveActivity(packageManager) != null) {
            val requestCode = currentCode++
            resultByCode[requestCode] = activityResult
            startActivityForResult(intent, requestCode)
        } else {
            activityResult.complete(null)
        }
        return activityResult
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        resultByCode.remove(requestCode)?.complete(ActivityResult(resultCode, data))
            ?: super.onActivityResult(requestCode, resultCode, data)
    }
}