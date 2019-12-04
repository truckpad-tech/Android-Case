package com.diegobezerra.truckpadcase.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun debounce(scope: CoroutineScope, delay: Long = 400L, allowed: () -> Boolean, block: () -> Unit) {
    scope.launch {
        kotlinx.coroutines.delay(delay)
        if (!allowed()) {
            return@launch
        }
        block()
    }
}