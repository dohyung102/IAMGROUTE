package com.ssafy.smartstore.src.main.intercepter

import android.util.Log
import com.ssafy.groute.config.ApplicationClass
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val TAG = "ReceivedCooki_Groute"

class ReceivedCookiesInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain):Response{
        val originalResponse: Response = chain.proceed(chain.request())

        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {

            val cookies = HashSet<String>()
            for (header in originalResponse.headers("Set-Cookie")) {
                cookies.add(header)
                Log.d("_ssafy", "intercept: $header")
            }
            
            // cookie 내부 데이터에 저장
            ApplicationClass.sharedPreferencesUtil.addUserCookie(cookies)
        }
        return originalResponse
    }
}