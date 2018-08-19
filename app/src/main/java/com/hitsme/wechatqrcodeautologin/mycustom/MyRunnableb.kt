package com.hitsme.wechatqrcodeautologin.mycustom

import android.app.Activity


internal class MyRunnableb(private val threadName: String, val o: Any,var a:Activity) : Runnable {
    private var t: Thread? = null
    init {
        println("Creating $threadName")
        println("Creating $o")
    }

    override fun run() {
        println("Running $threadName")
        try {


            while (true) {
             //   Toast.makeText(a, "hahahaha", Toast.LENGTH_SHORT).show()
                println("-------------------------------------------------------------------------------threadstart")

                o.javaClass.getMethod("post", Runnable::class.java).invoke(o, Runnable {  o.javaClass.getDeclaredMethod("loadUrl", String::class.java).invoke(o, "javascript: alert(\"js注入线程启动...\");var x = document.getElementById(\"js_allow\"); x.click();alert(\"js注入成功,销毁线程...\")")})
                Thread.sleep(8000)
                println("-------------------------------------------------------------------------------threadend")

            }

         //   webView.post(Runnable { webView.loadUrl("javascript: alert($data)") })
        }catch (e:Exception){
            println(e.printStackTrace())
        }
        catch (e:java.lang.Exception)
        {
            println(e.printStackTrace())
        }

    }

    fun start() {
        println("Starting $threadName")
        if (t == null) {
            t = Thread(this, threadName)
            t!!.start()
        }
    }
}