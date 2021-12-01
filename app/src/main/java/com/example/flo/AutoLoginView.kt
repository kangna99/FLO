package com.example.flo

interface AutoLoginView {
    fun onAutoLoginLoading()
    fun onAutoLoginSuccess(auth: Auth)
    fun onAutoLoginFailure(code: Int, message: String)
}