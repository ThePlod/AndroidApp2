package dong.android.plod.login.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpEmailViewModel : ViewModel() {

    var verificationCodeInput = MutableLiveData<String>()

}