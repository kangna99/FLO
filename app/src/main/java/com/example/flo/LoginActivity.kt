package com.example.flo

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.db.SongDatabase
import com.example.flo.db.User

class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginLoginTv.setOnClickListener {
            login()
        }

        binding.loginCloseIv.setOnClickListener {
            finish()
        }
    }

    private fun login() {
        //이메일 validation
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginEmailEt.text.toString().isEmpty()) {
            showToast("이메일을 입력해주세요.")
            return
        }

        //비밀번호 validation
        if(binding.loginPasswordEt.text.toString().isEmpty()) {
            showToast("비밀번호를 입력해주세요.")
            return
        }

        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginEmailEt.text.toString()
        val password: String = binding.loginPasswordEt.text.toString()
        val user = User(email, password, "")

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(user)


//        //db에 정보가 있는지 확인
//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.userDao().getUser(email, password)
//
//        user?.let { //유저 정보 존재
//            Log.d("login", "userId : ${user.id}, $user")
//            //발급받은 jwt를 저장해주는 함수
//            saveJwt(user.id)
//            startMainActivity()
//        } ?: run { //유저 정보 없다면
//            showToast("회원정보가 존재하지 않습니다.")
//        }
    }

//    private fun login() {
//        //이메일 validation
//        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginEmailEt.text.toString().isEmpty()) {
//            showToast("이메일을 입력해주세요.")
//            return
//        }
//
//        //비밀번호 validation
//        if(binding.loginPasswordEt.text.toString().isEmpty()) {
//            showToast("비밀번호를 입력해주세요.")
//            return
//        }
//
//        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginEmailEt.text.toString()
//        val password: String = binding.loginPasswordEt.text.toString()
//
//        //db에 정보가 있는지 확인
//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.userDao().getUser(email, password)
//
//        user?.let { //유저 정보 존재
//            Log.d("login", "userId : ${user.id}, $user")
//            //발급받은 jwt를 저장해주는 함수
//            saveJwt(user.id)
//            startMainActivity()
//        } ?: run { //유저 정보 없다면
//            showToast("회원정보가 존재하지 않습니다.")
//        }
//    }

    override fun onLoginLoading() {
        binding.loginLoadingPb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(auth: Auth) {
        binding.loginLoadingPb.visibility = View.GONE

        //유저 정보 저장
        saveJwt(this, auth.jwt)
        saveUserIdx(this, auth.userIdx)

        startMainActivity()
    }

    override fun onLoginFailure(code: Int, message: String) {
        binding.loginLoadingPb.visibility = View.GONE

        when(code) {
            2015, 2019, 3014 -> showToast(message)
        }
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(string: String) {
        val context: Context = this
        val message: CharSequence = string
        val duration = Toast.LENGTH_SHORT

        val layout: View = layoutInflater.inflate(
            R.layout.custom_toast,
            findViewById(R.id.toast_container)
        )

        val text = layout.findViewById<TextView>(R.id.toast_text)
        text.text = message
        val toast = Toast(context)
        toast.view = layout
        toast.duration = duration

        toast.show()

        if(Build.VERSION_CODES.R >= 30) {

        } else {

        }
    }
}