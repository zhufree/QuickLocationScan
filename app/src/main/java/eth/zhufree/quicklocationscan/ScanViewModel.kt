package eth.zhufree.quicklocationscan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {
    private val _scanQrUrl = MutableLiveData("")
    var scanQrUrl: LiveData<String> = _scanQrUrl
        private set

//    private val _localQrUrl = MutableLiveData("")
//    var localQrUrl: LiveData<String> = _localQrUrl
//        private set

    fun setScanQrUrl(url: String) {
        _scanQrUrl.value = url
    }

//    fun setLocalQrUrl(url: String) {
//        _localQrUrl.value = url
//    }
}