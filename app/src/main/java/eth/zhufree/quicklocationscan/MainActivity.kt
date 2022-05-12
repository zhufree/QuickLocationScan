package eth.zhufree.quicklocationscan

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.zxing.BinaryBitmap
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import eth.zhufree.quicklocationscan.ui.theme.QuickLocationScanTheme


class MainActivity : ComponentActivity() {

    private val imageUri =  mutableStateOf<Uri?>(null)
    private val qrUrl = mutableStateOf("")
    private val choosePhotoIntent: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            it?.let {
                imageUri.value = it
                val resolver = contentResolver
                val inputStream = resolver.openInputStream(it)

                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = false //读取图片内容
                options.inPreferredConfig = Bitmap.Config.RGB_565 //根据情况进行修改

                val srcBitmap = BitmapFactory.decodeStream(inputStream, null, options)
                val width: Int = srcBitmap?.width ?: 0
                val height: Int = srcBitmap?.height ?:0
                val pixels = IntArray(width * height)
                srcBitmap?.getPixels(pixels, 0, width, 0, 0, width, height)
                val source = RGBLuminanceSource(width, height, pixels)
                val binaryBitmap = BinaryBitmap(GlobalHistogramBinarizer(source))
                val reader = QRCodeReader() // 初始化解析对象
                val result = reader.decode(binaryBitmap)
                qrUrl.value = result.text
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickLocationScanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ScanApp(choosePhotoIntent, imageUri, qrUrl) {
                        alipayScan(it)
                    }
                }
            }
        }
    }

    private fun alipayScan(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}

@Composable
fun ScanApp(intent: ActivityResultLauncher<Array<String>>,
            imageUri: MutableState<Uri?>,
            qrUrl: MutableState<String>,
            scanQr: (url: String)->Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen({
                navController.navigate("addNewCode/${it}")
            }, {
                scanQr(PreferenceUtil.getStringValue(it))
            })
        }
        composable("addNewCode/{location}") { backStackEntry ->
            AddCodeScreen(intent, imageUri, qrUrl, backStackEntry.arguments?.getString("location")?:"null"){
                navController.popBackStack()
            }
        }
    }
}

