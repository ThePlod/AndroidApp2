package dong.android.plod.main.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddDialogViewModel: ViewModel() {

    val song = MutableLiveData<String>()
    val singer = MutableLiveData<String>()
    val genre = MutableLiveData<String>()
    val inDate = MutableLiveData<String>()

}