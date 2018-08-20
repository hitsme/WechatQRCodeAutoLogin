package com.hitsme.wechatqrcodeautologin.hook

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.hitsme.wechatqrcodeautologin.Constant
import com.hitsme.wechatqrcodeautologin.getPreferenceBoolean
import com.hitsme.wechatqrcodeautologin.thead.JSCommandRunnableb
import com.hitsme.wechatqrcodeautologin.thead.LoginMonitoringRunnable
import com.hitsme.wechatqrcodeautologin.tryHook
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.Exception
import kotlin.Exception as Ex


class HookWeChat {
    companion object {
        private const val WECHAT_HOOK_CLASS_NAME = "com.tencent.mm.ui.LauncherUI"
        private const val WECHAT_HOOK_CLASS_NAME2 = "com.tencent.mm.plugin.webwx.ui.ExtDeviceWXLoginUI"
        private const val WECHAT_HOOK_CLASS_NAME3 = "com.tencent.mm.plugin.webview.ui.tools.WebViewUI"
        private val TAG = HookWeChat::class.java.simpleName
    }

    /**
     * 自动确认微信电脑端登录

     * @param lpParam LoadPackageParam
     */
    fun autoConfirmWeChatLogin(lpParam: XC_LoadPackage.LoadPackageParam) {
        // 获取Class
       val loginClass = XposedHelpers.findClassIfExists(WECHAT_HOOK_CLASS_NAME, lpParam.classLoader) ?: return
        // 获取Class里面的Field
       // val loginClass2 = XposedHelpers.findClassIfExists(WECHAT_HOOK_CLASS_NAME2, lpParam.classLoader) ?: return
        tryHook(TAG, Constant.HOOK_ERROR) {
           XposedHelpers.findAndHookMethod(loginClass, "onCreate", Bundle::class.java, object : XC_MethodHook() {
            @SuppressLint("WrongConstant")
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {

                val activity = param.thisObject as Activity
                //Toast.makeText(activity, "链接"+param.args[0], Toast.LENGTH_SHORT).show()
                /*     loginClass.declaredMethods?.filter {
                           true
                       }?.forEach {
                           Toast.makeText(activity, "參數方法"+it.name+"类型"+it.parameterTypes[0], Toast.LENGTH_SHORT).show()
                       }*/

                val budl=activity.intent.getExtras()
                // val s=budl.size() as Int
                  for(i in budl.keySet())
                 {
                //  if(i.equals("geta8key_result_req_url"))
                //    budl.putString(i,"http://mp.weixin.qq.com/wap/loginauthqrcode?action=scan&qrticket=ae2fb15cd89fb346dd155bd586403c2a#wechat_redirect")
                //  if(i.equals("geta8key_result_full_url"))
                //       budl.putString(i,budl.getString(i)+"javascript:setTimeout( function() { var x = document.getElementsById(\"js_allow\"); for (var i = 0; i < x.length; i++) { if (x[i].innerText.indexOf(\"确定\") > -1) { x[i].click();} } } , 100);")
              //  Toast.makeText(activity, "budle的键"+i+"键值为"+budl.getString(i), Toast.LENGTH_SHORT).show()
                 }
                Toast.makeText(activity, "开始监听", Toast.LENGTH_SHORT).show()

                 val  thread = Thread(LoginMonitoringRunnable("test",activity))
                 thread.start()
               // intent3.addFlags()
                //Toast.makeText(activity, "參數個數"+param.args.get(0), Toast.LENGTH_SHORT).show()
                //    val intent2=activity.intent as Intent
                // Toast.makeText(activity, "參數個數"+intent2, Toast.LENGTH_SHORT).show()
                val enable = getPreferenceBoolean(activity, key = Constant.WECHAT_ENABLE)
                if (!enable) return
                val declaredFields = loginClass.declaredFields ?: return
                declaredFields.filter {
                    true
                    //  it.type.canonicalName.toString() == Constant.ANDROID_WIDGET_BUTTON
                }.forEach {
                    it.isAccessible = true
                    // Toast.makeText(activity,"type--"+it.type+"==="+""+(""+it.type).indexOf("MMWebView"), Toast.LENGTH_SHORT).show()
                    //  Toast.makeText(activity,"name:  "+it.name+"type--"+it.type, Toast.LENGTH_SHORT).show()
                    if((""+it.type).indexOf("MMWebView")>0) {
                        //  Toast.makeText(activity,((it.name).equals("mhH")).toString()+"開始111"+param, Toast.LENGTH_SHORT).show()
                     //   Toast.makeText(activity,it.name+"---"+it.type, Toast.LENGTH_SHORT).show()
                        try {
                            it.isAccessible = true
                            val webview = it.get(activity)
                            //webview.javaClass.getDeclaredMethod("loadUrl",String::class.java).invoke(webview,"https://www.baidu.com")
                            //   webview.javaClass.getDeclaredMethod("loadUrl",String::class.java).invoke(webview,param.args[0])
                         //  val  thread = Thread(MyRunnableb("test",webview,activity))
                         //  thread.start()
                         //   webview.javaClass.getDeclaredMethod("loadUrl",String::class.java).invoke(webview,"javascript:var i=2;alert(i);var x=document.getElementById(\"js_allow\");alert(x)")
                            //Toast.makeText(activity,webview.javaClass.getDeclaredMethod("getTitle").invoke(webview).toString(), Toast.LENGTH_SHORT).show()
                            //  webview.loadUrl("https://www.baidu.com")
                        }catch (e:Exception){
                          //  Toast.makeText(activity,e.printStackTrace().toString(),Toast.LENGTH_LONG).show()
                        }catch (e:Ex)
                        {
                            println(e.printStackTrace())
                            //Toast.makeText(activity,e.printStackTrace().toString(),Toast.LENGTH_LONG).show()
                        }
                        //webview.loadUrl("www.baidu.com")
                       // Toast.makeText(activity,"結束", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        })
            val loginClass2 = XposedHelpers.findClassIfExists(WECHAT_HOOK_CLASS_NAME2, lpParam.classLoader) ?: return
            val declaredFields = loginClass2.declaredFields ?: return
            XposedHelpers.findAndHookMethod(loginClass2, Constant.ON_CREATE, Bundle::class.java, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as Activity
                    val enable = getPreferenceBoolean(activity, key = Constant.WECHAT_ENABLE)
                    if (!enable) return
                    Toast.makeText(activity, "开始自动登陆", Toast.LENGTH_SHORT).show()
                    declaredFields.filter {
                        it.type.canonicalName.toString() == Constant.ANDROID_WIDGET_BUTTON
                    }.forEach {
                        it.isAccessible = true
                        val loginButton = it.get(param.thisObject) as Button
                        val loginButtonText = loginButton.text.toString()
                        if (Constant.WECHAT_LOGIN_TEXT == loginButtonText
                                || Constant.WECHAT_LOGIN_TEXT_CF == loginButtonText
                                || Constant.WECHAT_LOGIN_TEXT_EN == loginButtonText
                                || Constant.WECHAT_LOGIN_TEXT_JP == loginButtonText||Constant.WECHAT_LOGIN_TEXT_GZH==loginButtonText) {
                            loginButton.performClick()
                            Toast.makeText(activity, Constant.AUTO_LOGIN, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            val loginClass3 = XposedHelpers.findClassIfExists(WECHAT_HOOK_CLASS_NAME3, lpParam.classLoader) ?: return
            XposedHelpers.findAndHookMethod(loginClass3, "p", String::class.java,Map::class.java, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam) {

                    val activity = param.thisObject as Activity
                    val enable = getPreferenceBoolean(activity, key = Constant.WECHAT_ENABLE)
                    if (!enable) return
                    Toast.makeText(activity, "开始自动登陆", Toast.LENGTH_SHORT).show()

                    val declaredFields = loginClass3.declaredFields ?: return
                    declaredFields.filter {
                        true
                    }.forEach {
                        it.isAccessible = true
                        if((""+it.type).indexOf("MMWebView")>0) {
                            //  Toast.makeText(activity,((it.name).equals("mhH")).toString()+"開始111"+param, Toast.LENGTH_SHORT).show()
                            Toast.makeText(activity,it.name+"---"+it.type, Toast.LENGTH_SHORT).show()
                            try {
                                it.isAccessible = true
                                val webview = it.get(activity)
                                val  thread = Thread(JSCommandRunnableb("test",webview,activity))
                                thread.start()
                                //   webview.javaClass.getDeclaredMethod("loadUrl",String::class.java).invoke(webview,"javascript:var i=2;alert(i);var x=document.getElementById(\"js_allow\");alert(x)")
                                //Toast.makeText(activity,webview.javaClass.getDeclaredMethod("getTitle").invoke(webview).toString(), Toast.LENGTH_SHORT).show()
                                //  webview.loadUrl("https://www.baidu.com")
                            }catch (e:Exception){
                                //  Toast.makeText(activity,e.printStackTrace().toString(),Toast.LENGTH_LONG).show()
                                println(e.printStackTrace())
                            }catch (e:Ex)
                            {
                                println(e.printStackTrace())
                                //Toast.makeText(activity,e.printStackTrace().toString(),Toast.LENGTH_LONG).show()
                            }
                            Toast.makeText(activity,"結束", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            })
        }

    }

}