package dong.android.plod.login.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpPasswordViewModel: ViewModel() {

    var passWord = MutableLiveData<String>()
    var passWordRe = MutableLiveData<String>()

}