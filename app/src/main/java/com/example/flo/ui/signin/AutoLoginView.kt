package com.example.flo.ui.signin

import com.example.flo.data.remote.Auth

interface AutoLoginView {
    fun onAutoLoginLoading()
    fun onAutoLoginSuccess(auth: Auth)
    fun onAutoLoginFailure(code: Int, message: String)
}