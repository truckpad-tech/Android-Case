package com.jonas.truckpadchallenge.core

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.plugins.RxAndroidPlugins.setInitMainThreadSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.reset
import io.reactivex.plugins.RxJavaPlugins.setComputationSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.setIoSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.setNewThreadSchedulerHandler
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxImmediateSchedulerRule : TestRule {

    override fun apply(base: Statement, d: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                setIoSchedulerHandler { Schedulers.trampoline() }
                setComputationSchedulerHandler { Schedulers.trampoline() }
                setNewThreadSchedulerHandler { Schedulers.trampoline() }
                setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

                try {
                    base.evaluate()
                } finally {
                    reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}