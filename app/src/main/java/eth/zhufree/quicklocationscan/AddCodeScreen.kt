package eth.zhufree.quicklocationscan

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AddCodeScreen(
    intent: ActivityResultLauncher<Array<String>>,
    imageUri: MutableState<Uri?>,
    qrUrl: MutableState<String>,
    modifyLocation: String,
    navigateBack: () -> Unit) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))

    val location =  remember { mutableStateOf(if (modifyLocation== "null") "" else modifyLocation) }
    val allLocations = PreferenceUtil.getStringValue("locations").split(",").toMutableList()
    var modifyIndex = -1 // modify exist position

    if (modifyLocation != "null") {
        modifyIndex = allLocations.indexOf(modifyLocation)
        qrUrl.value = PreferenceUtil.getStringValue(modifyLocation)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { Text("添加场所码", color = MaterialTheme.colors.surface) },
                backgroundColor = MaterialTheme.colors.primary,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_white_24dp),
                            contentDescription = "back",
                            tint = MaterialTheme.colors.surface
                        )
                    }
                }
            )
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Button(onClick = {
                intent.launch(arrayOf("image/*"))
            }) {
                Text(text = "选择照片")
                Icon(Icons.Filled.Add, contentDescription = "choose Photo")
            }
            GlideImage(
                imageModel = imageUri.value,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .size(108.dp, 196.dp)
                    .align(Alignment.CenterHorizontally)
            )
            OutlinedTextField(
                value = location.value,
                onValueChange = { location.value = it },
                label = { Text("场所名称") }
            )
            OutlinedTextField(
                value = qrUrl.value,
                onValueChange = {  },
                readOnly = true,
                label = { Text("场所码链接") }
            )
            Spacer(modifier = Modifier.size(0.dp, 32.dp))
            Button(onClick = {
                // save to Pref
                // get all location
                if (modifyIndex == -1) {
                    // change
                    allLocations.add(location.value)
                } else {
                    allLocations[modifyIndex] = location.value
                }
                PreferenceUtil.setStringValue("locations", allLocations.joinToString(","))
                PreferenceUtil.setStringValue(location.value, qrUrl.value)
                navigateBack()
            }) {
                Text(text = "保存")
                Icon(Icons.Filled.Done, contentDescription = "choose Photo")
            }
        }
    }
}
