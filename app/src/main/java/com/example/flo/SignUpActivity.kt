package com.example.flo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignupBinding
import com.example.flo.db.SongDatabase
import com.example.flo.db.User

class SignUpActivity : AppCompatActivity() {

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
        val nickname: String = binding.signupNicknameEt.text.toString()
        val email: String = binding.signupIdEt.text.toString() + "@" + binding.signupEmailEt.text.toString()
        val password: String = binding.signupPasswordEt.text.toString()

        return User(nickname, email, password)
    }

    //회원가입 진행 함수
    private fun signUp() {

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

        //db 저장
        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val users = userDB.userDao().getUsers()
        Log.d("signUp", users.toString())

        finish()

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