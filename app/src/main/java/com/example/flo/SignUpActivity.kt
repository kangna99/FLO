package com.example.flo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignupBinding
import com.example.flo.db.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        Log.d("SIGNUPACT/ASYNC", "Hello, ")
        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").addConverterFactory(GsonConverterFactory.create()).build()

//        val signUpService = retrofit.create(AuthRetrofitInterface::class.java)
//        Log.d("SIGNUPACT", getUser().toString())
//
//        signUpService.signUp(getUser()).enqueue(object : Callback<AuthResponse>{
//            //응답 처리
//            @SuppressLint("LongLogTag")
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                Log.d("SIGNUPACT/API_RESPONSE", response.toString())
//
//                val resp = response.body()!!
//
//                Log.d("SIGNUPACT/API_RESPONSE-FLO", resp.toString())
//
//                when(resp.code) {
//                    1000 -> finish()
//                    2016, 2017-> {
//                        showToast(resp.message)
//                    }
//
//                }
//            }
//
//            //네트워크 실패
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.d("SIGNUPACT/API_ERROR", t.message.toString())
//            }
//        })
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