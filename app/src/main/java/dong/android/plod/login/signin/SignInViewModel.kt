package dong.android.plod.login.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel: ViewModel() {

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

}