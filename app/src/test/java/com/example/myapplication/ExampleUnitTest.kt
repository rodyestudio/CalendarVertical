package com.example.myapplication

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val t1 = Thread(Racer("1"))
        val t2 = Thread(Racer("2"))
        val t3 = Thread(Racer("3"))
        val t4 = Thread(Racer("4"))
        val t5 = Thread(Racer("5"))
        t5.start()
        t3.start()
        t1.start()
        t3.join()
        t2.start()
        t1.join()
        t4.start()
        t2.join()
        t4.join()
        t5
            .join()
        Thread.sleep(10000)
    }

    class Racer(val name:String):Runnable{
        init {
            print("t${name}")
        }
        override fun run() {
            try{
                Thread.sleep(100)
                print(name)
            }catch (e:Exception){

            }
        }

    }
}