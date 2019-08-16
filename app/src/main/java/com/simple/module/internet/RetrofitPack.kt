package com.simple.module.internet

import android.annotation.SuppressLint
import com.google.gson.Gson
import okhttp3.*
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitPack {
    val client=OkHttpClient.Builder()
        .sslSocketFactory(getSSLFactory())
        .cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {

            }

            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                val list = ArrayList<Cookie>()
                list.add(
                    Cookie.Builder()
                        .name("kg_mid")
                        .value("b204e40c143122ac61bd7c53796e01570")
                        .domain("wwwapi.kugou.com")
                        .build()
                )
                return list
            }
        })
        .build()
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://www.baidu.com")
            .client(client)
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(object : Converter.Factory() {
                override fun responseBodyConverter(
                    type: Type,
                    annotations: Array<Annotation>,
                    retrofit: Retrofit
                ): Converter<ResponseBody, *>? {
                    return Converter<ResponseBody, Any> {
                        val str = it.string()
                        if (type.toString().contains("class java.lang.String")) {
                            return@Converter str
                        } else {
                            return@Converter Gson().fromJson(str, type)
                        }
                    }
                }
            })
            .build()
    }
    fun request(url:String):Call{
        return client.newCall(Request.Builder().url(url).build())
    }

    private fun getSSLFactory(): SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        return sslContext.socketFactory
    }
}