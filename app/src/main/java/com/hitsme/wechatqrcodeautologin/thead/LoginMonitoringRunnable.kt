package com.hitsme.wechatqrcodeautologin.thead

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.hitsme.wechatqrcodeautologin.Constant
import com.hitsme.wechatqrcodeautologin.bean.LoginUrlObj
import com.hitsme.wechatqrcodeautologin.dao.ConnectDataDao
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource


internal class LoginMonitoringRunnable(private val threadName: String,var a: Activity) : Runnable {
    private var t: Thread? = null
    init {
        println("Creating $threadName")
    }

    @SuppressLint("WrongConstant")
    override fun run() {
        println("Running $threadName")
        val cntsrc=ConnectDataDao.ConnectMySql(Constant.CONNECTMYSQL_STR)
        val loginUrlDao: Dao<LoginUrlObj, String>
        try {

            loginUrlDao = DaoManager.createDao(cntsrc, LoginUrlObj::class.java)
            while (true) {
                //   Toast.makeText(a, "hahahaha", Toast.LENGTH_SHORT).show()
                println("-------------------------------------------------------------------------------threadstart")
                val loginUrlObj=loginUrlDao.queryBuilder().where().eq("islogin",false).queryForFirst()
                if(loginUrlObj!=null)
                {
                    val intentWbv = Intent()
                    intentWbv.putExtra("rawUrl", loginUrlObj.loginurl)
                    intentWbv.putExtra("useJs", true)
                    intentWbv.putExtra("srcUsername", "wxid_zftaoa1bsgjd21")
                    intentWbv.putExtra("bizofstartfrom", "enterpriseHomeSubBrand")
                    intentWbv.addFlags(67108864)
                    intentWbv.setClassName(a,"com.tencent.mm.plugin.webview.ui.tools.WebViewUI")
                    a.startActivity(intentWbv)
                    a.finish()

                    //update login status
                    //loginUrlObj.setIsLogin(true)
                   // loginUrlDao.update(loginUrlObj)

                 // break
                }

            }
            println("-------------------------------------------------------------------------------threadend")
        }catch (e:Exception){
            println(e.printStackTrace())
        }
        catch (e:java.lang.Exception)
        {
            println(e.printStackTrace())
        }finally {
            cntsrc.close()
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