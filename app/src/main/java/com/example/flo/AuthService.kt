package com.example.flo

import android.annotation.SuppressLint
import android.util.Log
import com.example.flo.db.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var autoLoginView: AutoLoginView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun setAutoLoginView(autoLoginView: AutoLoginView) {
        this.autoLoginView = autoLoginView
    }

    fun signUp(user: User) {

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        //호출 전 loading
        signUpView.onSignUpLoading()

        //api 호출 후 응답받으면 callback
        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            //응답 처리
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUPACT/API_RESPONSE", response.toString())

                if(response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("SIGNUPACT/API_RESPONSE-FLO", resp.toString())

                    when (resp.code) {
                        1000 -> signUpView.onSignUpSuccess()
                        else -> signUpView.onSignUpFailure(resp.code, resp.message)
                    }
                }
            }

            //네트워크 실패
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUPACT/API_ERROR", t.message.toString())
                signUpView.onSignUpFailure(400, t.message.toString())
            }
        })
    }

    fun login(user: User) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        //호출 전 loading
        loginView.onLoginLoading()

        //api 호출 후 응답받으면 callback
        authService.login(user).enqueue(object : Callback<AuthResponse> {
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGINACT/API_RESPONSE", response.toString())

                if(response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("LOGINACT/API_RESPONSE-FLO", resp.toString())

                    when (resp.code) {
                        1000 -> loginView.onLoginSuccess(resp.result!!)
                        else -> loginView.onLoginFailure(resp.code, resp.message)
                    }
                }
            }

            //네트워크 실패
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGINACT/API_ERROR", t.message.toString())
                loginView.onLoginFailure(400, t.message.toString())
            }
        })
    }

    fun autoLogin(jwt: String) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        //호출 전 loading
        autoLoginView.onAutoLoginLoading()

        //api 호출 후 응답받으면 callback
        authService.autoLogin(jwt).enqueue(object : Callback<AuthResponse> {
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<AuthResponse?>, response: Response<AuthResponse?>) {
                Log.d("AUTOLOGINACT/API_RESPONSE", response.toString())

                if(response.isSuccessful && response.code() == 200) {
                    val resp = response.body()!!
                    Log.d("AUTOLOGINACT/API_RESPONSE-FLO", resp.toString())

                    when (resp.code) {
                        1000 -> autoLoginView.onAutoLoginSuccess(resp.result!!)
                        else -> autoLoginView.onAutoLoginFailure(resp.code, resp.message)
                    }
                }
            }

            //네트워크 실패
            override fun onFailure(call: Call<AuthResponse?>, t: Throwable) {
                Log.d("AUTOLOGINACT/API_ERROR", t.message.toString())
                autoLoginView.onAutoLoginFailure(400, t.message.toString())
            }
        })
    }
}