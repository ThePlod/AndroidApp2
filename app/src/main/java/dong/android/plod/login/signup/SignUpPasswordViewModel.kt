package dong.android.plod.login.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpPasswordViewModel: ViewModel() {

    var passWord = MutableLiveData<String>()
    var passWordRe = MutableLiveData<String>()

    fun updatePasswordText(input: String) {
        passWord.value = input
    }

    fun updatePasswordReText(input: String) {
        passWordRe.value = input
    }
}