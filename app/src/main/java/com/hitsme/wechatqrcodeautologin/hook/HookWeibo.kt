package com.hitsme.wechatqrcodeautologin.hook

import android.app.Activity
import android.webkit.WebView
import com.hitsme.wechatqrcodeautologin.Constant
import com.hitsme.wechatqrcodeautologin.getPreferenceBoolean
import com.hitsme.wechatqrcodeautologin.tryHook
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookWeibo {
    companion object {
        private const val HOOK_LOAD_CLASS = "loadClass"
        private const val COM_SINA_WEIBO_BROWSER = "com.sina.weibo.browser.WeiboBrowser"
        private const val HOOK_WEIBO_METHOD_NAME = "onWebViewPageFinished"
        private const val COMFIRM_URL = "passport.weibo.cn/signin/qrcode/scan"
        private const val JS_CODE =
            "javascript:setTimeout( function() { var x = document.getElementsByTagName(\"a\"); for (var i = 0; i < x.length; i++) { if (x[i].innerText.indexOf(\"确认登录\") > -1) { x[i].click();} } } , 100);"
        private val TAG = HookWeibo::class.java.simpleName
    }

    fun autoConfirmWeiboLogin(lpParam: XC_LoadPackage.LoadPackageParam) {
        tryHook(TAG, Constant.HOOK_ERROR) {
            // 获取loadClass
            XposedHelpers.findAndHookMethod(ClassLoader::class.java,
                HOOK_LOAD_CLASS,
                String::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val browserClass = param.result as Class<*>? ?: return
                        if (COM_SINA_WEIBO_BROWSER != browserClass.name) {
                            return
                        }
                        XposedHelpers.findAndHookMethod(
                            browserClass,
                            HOOK_WEIBO_METHOD_NAME,
                            WebView::class.java,
                            String::class.java,
                            object : XC_MethodHook() {
                                override fun afterHookedMethod(param: MethodHookParam) {
                                    val activity = param.thisObject as Activity
                                    val enable =
                                        getPreferenceBoolean(activity, key = Constant.WEIBO_ENABLE)
                                    if (!enable) return
                                    val url = param.args[1] as String? ?: return
                                    if (!url.contains(COMFIRM_URL)) {
                                        return
                                    }
                                    val webView = param.args[0] as WebView? ?: return
                                    webView.loadUrl(JS_CODE)
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}