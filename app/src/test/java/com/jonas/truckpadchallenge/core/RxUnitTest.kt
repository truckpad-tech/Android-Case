package com.jonas.truckpadchallenge.core

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule

abstract class RxUnitTest {

    init {
        RxJavaPlugins.setComputationSchedulerHandler { TestScheduler() }
    }

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()
}