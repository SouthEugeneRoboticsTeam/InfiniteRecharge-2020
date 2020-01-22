package org.sert2521.infiniterecharge2020.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.periodic
import kotlin.coroutines.coroutineContext

suspend fun timer(period: Long, runTime: Long, task: () -> Unit) {
    var elapsedTime: Long = 0
    CoroutineScope(coroutineContext).launch {
        periodic(period) {
            task()
            elapsedTime += period
            if (elapsedTime > runTime) {
                cancel()
            }
        }
    }.join()
}