package com.smit.ppsa

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val repository = ApiRepositoryImpl()
    fun submitPassword(
        userId: Int,
        password: String,
        oldpassword: String ="",
        context: LogIn? = null,
        passwordcontext: PasswordActivity? = null,
        text: String = "normal"
    ) {
        viewModelScope.launch {
            val cont  = if (passwordcontext != null) passwordcontext else context

            Log.d("koio", "submitPassword: " +userId)
           /* Log.d("dda", "submitPassword: "+ oldpassword)
            Log.d("dda", "submitPassword: "+ password)*/
            Log.d("dda", "submitPassword: "+ BaseUtils.getUserInfo(cont).getcPassword())

                if (passwordcontext != null){
                    if (text.equals("newUser")){
                        repository.submitPassword(userId, password, context, passwordcontext, text)
                    }else{
                        if (oldpassword.equals(BaseUtils.getUserInfo(cont).getcPassword())) {
                            repository.submitPassword(userId, password, context, passwordcontext, text)
                        }else{
                            BaseUtils.showToast(cont, "Old password is incorrect")
                        }
                    }
                }else{
                    if (password.equals(BaseUtils.getUserInfo(cont).getcPassword())) {
                        repository.submitPassword(userId, password, context, passwordcontext, text)
                    }else{
                        BaseUtils.showToast(cont, "password is incorrect")
                    }
                }


        }
    }
}