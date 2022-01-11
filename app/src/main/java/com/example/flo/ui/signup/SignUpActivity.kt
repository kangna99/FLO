package com.example.flo.ui.signup

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.R
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySignupBinding
import com.example.flo.utils.saveUserName

class SignUpActivity : AppCompatActivity(), SignUpView {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupSignupBtnTv.setOnClickListener {
            signUp()
        }

    }

    private fun getUser(): User {
        val email: String = binding.signupIdEt.text.toString() + "@" + binding.signupEmailEt.text.toString()
        val password: String = binding.signupPasswordEt.text.toString()
        val name: String = binding.signupNicknameEt.text.toString()

        return User(email, password, name)
    }

    //회원가입 진행 함수
    private fun signUp() {

        //닉네임 validation
        if(binding.signupNicknameEt.text.toString().isEmpty()) {
            showToast("이름 형식이 잘못되었습니다.")
            return
        }
        //이메일 validation
        if(binding.signupIdEt.text.toString().isEmpty() || binding.signupEmailEt.text.toString().isEmpty()) {
            showToast("이메일 형식이 잘못되었습니다.")
            return
        }

        //비밀번호 validation
        if(binding.signupPasswordEt.text.toString() != binding.signupPasswordCheckEt.text.toString()) {
            showToast("비밀번호가 일치하지 않습니다.")
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
        saveUserName(this, getUser().name)

        Log.d("SIGNUPACT/ASYNC", "Hello, ")

    }

    override fun onSignUpLoading() {
        binding.signupLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        binding.signupLoadingPb.visibility = View.GONE

        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signupLoadingPb.visibility = View.GONE

        when(code) {
//            2000 -> {}
            2016, 2017 -> {
                showToast(message)
            }
        }
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