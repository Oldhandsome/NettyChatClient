package com.dtusystem.nettychatclient

import com.dtusystem.nettychatclient.network.utils.Utils
import com.dtusystem.nettychatclient.network.message.Message
import com.dtusystem.nettychatclient.network.message.Response
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.netty.util.concurrent.DefaultPromise
import io.netty.util.concurrent.Promise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.junit.Test
import java.lang.reflect.Constructor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
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

        fun testFunction4(arg: String, arg2: Int): Promise<List<String>> {
            return DefaultPromise<List<String>>(null)
        }

        suspend fun testFunction5(arg: String, arg2: Int): List<Response<String>> {
            withContext(Dispatchers.IO) {
                delay(1000)
                println(arg + " : " + arg2)
                Unit
            }
            return emptyList()
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
            }else{
                val returnType = method.returnType
                val genericType = method.genericReturnType
                println(returnType)
            }
        }
    }



    @Test
    fun testGetSuspendFunction(){
        val methods = SuspendFunction::class.java.methods;
        for (method in methods) {
            if(method.name.equals("testFunction3")){
                val length = method.parameterCount
                val parameterTypes = method.parameterTypes
                // 判断最后一个参数的类型是
                if(length > 0){
                    val classOfLastParameter = parameterTypes.get(length - 1)
                    println(classOfLastParameter == Continuation::class.java)
                    println(classOfLastParameter)

                }
            }
        }
    }

    @Test
    fun testGetReturnType(){
        val methods = SuspendFunction::class.java.methods
        for (method in methods) {
            if(method.name.equals("testFunction5")){
                val length = method.parameterCount
                val parameter = method.parameters[length - 1]
                val parameterizedType = parameter.parameterizedType as ParameterizedType
                val wildcardTypeImpl = parameterizedType.actualTypeArguments[0] as WildcardType
                val returnType = wildcardTypeImpl.lowerBounds[0]
                val typeName = returnType.typeName
                println("1234")
            }
        }
    }






}
