package com.simple

import com.simple.module.internet.ConcurrentRequest
import kotlinx.coroutines.*
import org.junit.Test
import java.lang.Runnable

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

    @Test
    fun coroutine() {
        GlobalScope.launch {
            val c=System.currentTimeMillis()
            val j1=async{
                delay(1000)
                print(123)
            }

            val j2=async {
                delay(1000)
                print(333)
            }
            j1.await()
            j2.await()
            println("\n"+ "  "+(System.currentTimeMillis()-c))
        }
        Thread.sleep(1500)
    }
}
