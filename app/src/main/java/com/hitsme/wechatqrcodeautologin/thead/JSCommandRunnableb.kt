package com.hitsme.wechatqrcodeautologin.thead

import android.app.Activity
import android.content.Intent
import com.hitsme.wechatqrcodeautologin.Constant
import com.hitsme.wechatqrcodeautologin.bean.LoginUrlObj
import com.hitsme.wechatqrcodeautologin.dao.ConnectDataDao
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager


internal class JSCommandRunnableb(private val threadName: String, val webview: Any,var a:Activity) : Runnable {
    private var t: Thread? = null
    init {
        println("Creating $threadName")
        println("Creating $webview")
    }

    override fun run() {
        println("Running $threadName")
        try {

      var count=3
            while (count>0) {
             //   Toast.makeText(a, "hahahaha", Toast.LENGTH_SHORT).show()
                println("-------------------------------------------------------------------------------threadstart")
               /* webview.javaClass.getMethod("post", Runnable::class.java).invoke(
                        webview, Runnable {  webview.javaClass.getDeclaredMethod("loadUrl", String::class.java)
                        .invoke(webview, "javascript: alert(\"js注入线程启动...\");var x = document.getElementById(\"js_allow\");" +
                                " x.click();alert(\"js注入成功,销毁线程...\")")})*/
                webview.javaClass.getMethod("post", Runnable::class.java).invoke(
                        webview, Runnable {  webview.javaClass.getDeclaredMethod("loadUrl", String::class.java)
                        .invoke(webview, "javascript:var x = document.getElementById(\"js_allow\");" +
                                " x.click();")})
                Thread.sleep(8000)
                count--
                println("-------------------------------------------------------------------------------threadend")

            }
            val cntsrc= ConnectDataDao.ConnectMySql(Constant.CONNECTMYSQL_STR)
            val loginUrlDao: Dao<LoginUrlObj, String>
            loginUrlDao = DaoManager.createDao(cntsrc, LoginUrlObj::class.java)
            val loginUrlObj=loginUrlDao.queryBuilder().where().eq("islogin",false).queryForFirst()
            //update login status
            loginUrlObj.setIsLogin(true)
            loginUrlDao.update(loginUrlObj)
            cntsrc.close()
            //在完成注入后，再次进入LaunchUI候命
            val donateIntent = Intent()
            donateIntent.setClassName(a, "com.tencent.mm.ui.LauncherUI")
            a.startActivity(donateIntent)
            a.finish()

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