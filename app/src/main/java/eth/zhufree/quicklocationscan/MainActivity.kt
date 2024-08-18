package eth.zhufree.quicklocationscan

import android.content.Intent
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
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.qrcode.QRCodeReader
import eth.zhufree.quicklocationscan.ui.theme.QuickLocationScanTheme


class MainActivity : ComponentActivity() {

    private val imageUri = mutableStateOf<Uri?>(null)
    private val scanViewModel = ScanViewModel()
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
                val height: Int = srcBitmap?.height ?: 0
                val pixels = IntArray(width * height)
                srcBitmap?.getPixels(pixels, 0, width, 0, 0, width, height)
                val source = RGBLuminanceSource(width, height, pixels)
                val binaryBitmap = BinaryBitmap(GlobalHistogramBinarizer(source))
                try {
                    val reader = QRCodeReader() // 初始化解析对象
                    val result = reader.decode(binaryBitmap)
                    // %2F : /
                    // %3D : =
                    // %3F: ?
                    if (result.text.startsWith("alipays://")) {
                        scanViewModel.setScanQrUrl(result.text)
                        //alipays://platformapi/startapp?appld=
                        //2021002175684865&page=pages%2Findex%2Findex&nbupdate=syncforce&query=communityCode
                        //%3D3301040080061522038249882320897
                    } else if ("qrcode.sh.gov.cn" in result.text) {
                        // 随申码
                        // http://qrcode.sh.gov.cn/enterprise/
                        // scene?f=1&m=ZMMAXd0HRb1k%2B%2B29UpPE0hGo5MGf9zWTWPNlw4
                        // %2BfD0PWEBkeAgSc3g
                        // %2F03JGPhLxueO6hSwcceZml0NYWGk9S5
                        // %2FtvoCLNg1vthbDHgCpX1fc
                        // %3D&qrcodeType=80
                        scanViewModel.setScanQrUrl(
                            "alipays://platformapi/startapp?appId=20000067&url=" +
                                    result.text
//                                    .replace("%2B", "+")
                                        .replace("=", "%3D")
                                        .replace("+", "%2B")
                                        .replace("?", "%3F")
                                        .replace("&", "%26")
                                        .replace("/", "%2F")
                                        .replace(":", "%3A")
                        )
//                        qrUrl.value = result.text
                    } else if ("jiaxing.gov.cn" in result.text) {
                        scanViewModel.setScanQrUrl(
                            "alipays://platformapi/startapp?appId=20000067&url=" +
                                    result.text.replace("=", "%3D")
                                        .replace("/", "%2F")
                                        .replace(":", "%3A")
                        )
                    } else if (result.text.startsWith("http")) {
                        scanViewModel.setScanQrUrl(
                            "alipays://platformapi/startapp?appId=20000067&url=" +
                                    result.text.replace("=", "%3D")
                                        .replace("/", "%2F")
                                        .replace(":", "%3A")
                                        .replace("&", "%26")
                                        .replace("#", "%23")
                        )
                    } else {
                        scanViewModel.setScanQrUrl(result.text)
                    }
                } catch (e: NotFoundException) {
                    Toast.makeText(this, "未找到场所码", Toast.LENGTH_SHORT).show()
                }

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
                    ScanApp(scanViewModel, choosePhotoIntent, imageUri, {
                        startIntent(it)
                    }) {
                        startIntent(it)
                    }
                }
            }
        }
    }

    private fun startIntent(url: String) {
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show()
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}

@Composable
fun ScanApp(
    scanViewModel: ScanViewModel,
    intent: ActivityResultLauncher<Array<String>>,
    imageUri: MutableState<Uri?>,
    scanQr: (url: String) -> Unit,
    goUrl: (url: String) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen({
                imageUri.value = null
                navController.navigate("addNewCode/${it}")
            }, {
                navController.navigate("about")
            }, {
                scanQr(PreferenceUtil.getStringValue(it))
            })
        }
        composable("addNewCode/{location}") { backStackEntry ->
            val location = backStackEntry.arguments?.getString("location") ?: "null"
            if (location != "null") {
                scanViewModel.setScanQrUrl(PreferenceUtil.getStringValue(location))
            }
            AddCodeScreen(
                scanViewModel,
                intent,
                imageUri,
                location
            ) {
                scanViewModel.setScanQrUrl("")
                navController.popBackStack()
            }
        }
        composable("about") {
            AboutScreen({
                navController.popBackStack()
            }, {
                goUrl(it)
            })
        }
    }
}

