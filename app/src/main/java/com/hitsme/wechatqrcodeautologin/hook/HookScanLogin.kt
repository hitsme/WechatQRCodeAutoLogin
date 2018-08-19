package com.hitsme.wechatqrcodeautologin.hook

import com.hitsme.wechatqrcodeautologin.Constant
import com.hitsme.wechatqrcodeautologin.tryHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookScanLogin {
    companion object {
        private const val TOP_JOWANXU_SCANLOGIN_ACTIVITY = "com.hitsme.wechatqrcodeautologin.MainActivity"
        private const val HOOK_SCANLOGIN_METHOD_NAME = "isModuleLoaded"
        private val TAG = HookScanLogin::class.java.simpleName
    }

    /**
     * 判断模块是否加载成功
     */
    fun checkModuleLoaded(lpParam: XC_LoadPackage.LoadPackageParam) {
        // 获取Class
        val activityClass = XposedHelpers.findClassIfExists(TOP_JOWANXU_SCANLOGIN_ACTIVITY, lpParam.classLoader) ?: return
        tryHook(TAG, Constant.HOOK_ERROR) {
            // 将方法返回值返回为true
            XposedHelpers.findAndHookMethod(activityClass, HOOK_SCANLOGIN_METHOD_NAME, object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any = true
            })
        }
    }
}