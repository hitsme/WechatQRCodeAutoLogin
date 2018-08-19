package com.hitsme.wechatqrcodeautologin.hook

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import com.hitsme.wechatqrcodeautologin.Constant
import com.hitsme.wechatqrcodeautologin.getPreferenceBoolean
import com.hitsme.wechatqrcodeautologin.tryHook
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookTIMQQ {
    companion object {
        private val TAG = HookTIMQQ::class.java.simpleName
        private const val QR_CODE_HOOK_CLASS_NAME = "com.tencent.biz.qrcode.activity.QRLoginActivity"
        private const val QR_CODE_WEB_HOOK_CLASS_NAME = "com.tencent.mobileqq.activity.DevlockQuickLoginActivity"

        /**
         * 判断doOnCreate
         */
        private var count = 0
    }

    /**
     * 网页登录弹出
     */
    fun autoWebConfirmQQLogin(lpParam: XC_LoadPackage.LoadPackageParam) {
        // 获取Class
        val aClass = XposedHelpers.findClassIfExists(QR_CODE_WEB_HOOK_CLASS_NAME, lpParam.classLoader) ?: return
        // 获取Class里面的Field
        val declaredFields = aClass.declaredFields
        tryHook(TAG, Constant.HOOK_ERROR) {
            XposedHelpers.findAndHookMethod(aClass, Constant.ON_CREATE, Bundle::class.java, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as Activity
                    val enable = getPreferenceBoolean(activity, key = Constant.WEB_QQ_ENABLE)
                    if (!enable) return
                    declaredFields.filter {
                        it.type.canonicalName.toString() == Constant.ANDROID_WIDGET_BUTTON
                    }.forEach {
                        // 设置true
                        it.isAccessible = true
                        // 获取值
                        (it.get(param.thisObject) as Button).apply {
                            if (text.toString().contains(Constant.CONTAIN_TEXT)) {
                                performClick()
                            }
                        }
                    }
                }
            })
        }
    }

    /**
     * 扫一扫电脑端二维码后，自动点击允许登录TIM/QQ按钮
     * @param lpParam LoadPackageParam
     * @param methodName hook method name
     */
    fun autoConfirmQQLogin(lpParam: XC_LoadPackage.LoadPackageParam, methodName: String) {
        // 获取Class
        val aClass = XposedHelpers.findClassIfExists(QR_CODE_HOOK_CLASS_NAME, lpParam.classLoader) ?: return
        // 获取Class里面的Field
        val declaredFields = aClass.declaredFields
        tryHook(TAG, Constant.HOOK_ERROR) {
            // Hook指定方法
            XposedHelpers.findAndHookMethod(aClass, methodName, Bundle::class.java, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam) {
                    // 每次打开都会调用两次doOnCreate
                    if (count == 1) {
                        return
                    }
                    // 调用doOnCreate超过两次后，也就是第二次打开扫一扫后就设置为0
                    if (count > 1) {
                        count = 0
                    }
                    val activity = param.thisObject as Activity
                    val enable = getPreferenceBoolean(activity, key = Constant.TIM_QQ_ENABLE)
                    if (!enable) return

                    // 获取Handler全名
                    val hookHandlerClassName = declaredFields.first {
                        it.type.canonicalName.toString() == Constant.ANDROID_OS_HANDLER
                    }.run { get(param.thisObject).javaClass.name } ?: return

                    declaredFields.filter {
                        it.type.canonicalName.toString() == Constant.ANDROID_WIDGET_BUTTON
                    }.forEach {
                        val handlerClass = XposedHelpers.findClassIfExists(hookHandlerClassName, lpParam.classLoader) ?: return
                        // 设置true
                        it.isAccessible = true
                        // 获取值
                        val loginButton = it.get(param.thisObject) as Button
                        tryHook(TAG, Constant.HOOK_ERROR) {
                            // Hook方法，对handleMessage方法调用后，进行判断Button的Text进行判断，并且自动调用点击方法
                            XposedHelpers.findAndHookMethod(handlerClass, Constant.HANDLE_MESSAGE, Message::class.java, object : XC_MethodHook() {
                                @Throws(Throwable::class)
                                override fun afterHookedMethod(param: MethodHookParam) {
                                    // 当Button的Text为允许登录TIM/允许登录QQ的时候才实现点击
                                    val text = loginButton.text.toString()
                                    if (text.contains(Constant.CONTAIN_TEXT)
                                            || text.contains(Constant.QQ_INTERNATIONAL_TEXT)
                                            || text.contains(Constant.QQ_INTERNATIONAL_TEXT_CF)
                                            || text.contains(Constant.QQ_INTERNATIONAL_TEXT_EN)
                                            || text.contains(Constant.QQ_INTERNATIONAL_TEXT_DE)
                                            || text.contains(Constant.QQ_INTERNATIONAL_TEXT_ES)
                                            || text.contains(Constant.QQ_INTERNATIONAL_TEXT_FR)
                                            || text.contains(Constant.QQ_INTERNATIONAL_TEXT_JP)
                                            || text.contains(Constant.QQ_INTERNATIONAL_TEXT_KO)) {
                                        if (count == 0) {
                                            loginButton.performClick()
                                        }
                                        // 每次都增加
                                        count++
                                    }
                                }
                            })
                        }
                    }
                }
            })
        }
    }

}