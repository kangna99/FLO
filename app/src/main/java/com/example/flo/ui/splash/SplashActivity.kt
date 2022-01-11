package com.example.flo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.remote.Auth
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySplashBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.signin.AutoLoginView
import com.example.flo.ui.signin.LoginActivity
import com.example.flo.utils.getJwt

class SplashActivity : AppCompatActivity(), AutoLoginView {
    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autoLogin()
    }

    private fun autoLogin() {
        val authService = AuthService()
        authService.setAutoLoginView(this)
        Log.d("auto-login", getJwt(this))

        authService.autoLogin(getJwt(this))

        Log.d("AUTOLOGINACT/ASYNC", "Hello, ")
    }

    override fun onAutoLoginLoading() {
        binding.splashLoadingPb.visibility = View.VISIBLE
        Log.d("auto-login status", "로딩중")
    }

    override fun onAutoLoginSuccess(auth: Auth) {
        binding.splashLoadingPb.visibility = View.GONE
        Log.d("auto-login status", "로그인 성공")

        startMainActivity()
    }

    override fun onAutoLoginFailure(code: Int, message: String) {
        binding.splashLoadingPb.visibility = View.GONE
        Log.d("auto-login status", "로그인 실패")

        startLoginActivity()
    }

    private fun startMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)},2000) //2초 delay
    }

    private fun startLoginActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)},2000) //2초 delay
    }
}