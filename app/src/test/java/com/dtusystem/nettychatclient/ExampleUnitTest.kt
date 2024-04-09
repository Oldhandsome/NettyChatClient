package com.dtusystem.nettychatclient

import com.dtusystem.nettychatclient.network.message.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(Message.getMessageClass(1)?.simpleName)
    }

    class SuspendFunction {
        suspend fun testFunction(): String {
            withContext(Dispatchers.IO) {
                delay(1000)
            }
            return "Hello World"
        }

        suspend fun testFunction2(arg: String, arg2: Int): String {
            withContext(Dispatchers.IO) {
                delay(1000)
                println(arg + " : " + arg2)
                Unit
            }
            return "Hello World2"
        }
    }

    @Test
    fun TestShowFunction() {
        val methods = SuspendFunction::class.java.methods
        for (method in methods) {
            println(method)
        }
    }

}