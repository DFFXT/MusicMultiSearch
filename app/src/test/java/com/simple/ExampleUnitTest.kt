package com.simple

import com.simple.module.internet.ConcurrentRequest
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        ConcurrentRequest.Builder()
            .addRequest(Runnable {
                Thread.sleep(2000)
            })
            .addRequest(Runnable {
                Thread.sleep(1000)
            })
            .addRequest(Runnable {
                Thread.sleep(3000)
            }).complete {
                println("sdfsf")
            }
            .request()
    }
}
