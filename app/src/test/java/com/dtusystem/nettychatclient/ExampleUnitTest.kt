package com.dtusystem.nettychatclient

import com.dtusystem.nettychatclient.network.utils.Utils
import com.dtusystem.nettychatclient.network.message.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.junit.Test
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.coroutines.Continuation

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
//        suspend fun testFunction(): String {
//            withContext(Dispatchers.IO) {
//                delay(1000)
//            }
//            return "Hello World"
//        }
//
//        suspend fun testFunction2(arg: String, arg2: Int): String {
//            withContext(Dispatchers.IO) {
//                delay(1000)
//                println(arg + " : " + arg2)
//                Unit
//            }
//            return "Hello World2"
//        }

        suspend fun testFunction3(arg: String, arg2: Int): List<String> {
            withContext(Dispatchers.IO) {
                delay(1000)
                println(arg + " : " + arg2)
                Unit
            }
            return listOf("a", "b", "c")
        }
    }

    @Test
    fun TestShowFunction() {
        val methods = SuspendFunction::class.java.methods
        for (method in methods) {
            val parameterTypes = method.parameterTypes
            val length = method.parameterCount
//            if(method.name.equals("testFunction3")){
//                val continuation = method.parameters[length - 1]
//                val type = continuation.parameterizedType
//                println(type)
//                val innerType = (type as ParameterizedType).actualTypeArguments[0]
//                println(innerType)
//                val trueType = Utils.getRawType(innerType)
//                println(trueType)
//            }
            if(Utils.getRawType(parameterTypes[length - 1]) == Continuation::class.java){
                val parameterOfContinuation = method.parameters[length - 1]
                val parameterizedType = parameterOfContinuation.parameterizedType as ParameterizedType
                val innerType = parameterizedType.actualTypeArguments[0]

                println(innerType)
                println(innerType)
                deserialize(innerType)

            }
        }
    }

    @Test
    fun testShowFunction(){
        val type = object : TypeToken<List<String>>() {}.type
        println(type)
        deserialize(type)
    }

    fun deserialize(type: Type){
        val gson = Gson()
        val obj = gson.fromJson<Any>("[\"string1\", \"string2\", \"string3\"]", type)
        println(obj)
    }
}
