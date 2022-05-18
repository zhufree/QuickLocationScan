package eth.zhufree.quicklocationscan
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navigateToAddCode: (location: String?) -> Unit, scanQrCode: (location: String)->Unit) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
    var allLocations = PreferenceUtil.getStringValue("locations").split(",").filter { i -> i.isNotBlank() }
    val openDialog = remember { mutableStateOf(false) }
    val currentLocation = remember {
        mutableStateOf("")
    }
    val listState = rememberLazyListState()

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,

        topBar = {
            TopAppBar(
                title = { Text("场所码捷径", color = MaterialTheme.colors.surface) },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    Button(
                        onClick = {
                            navigateToAddCode(null)
                        }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add New Location",
                            tint = Color.White,
                        )
                        Text("添加新场所码")
                    }
                }
            )
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                text="使用说明：点击按钮添加场所码，选择手机上拍好的场所码图片解析链接，保存后会出现在首页，之后直接点击即可启动支付宝自动扫。"
            )
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                state = listState,
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 16.dp,
                    end = 12.dp,
                    bottom = 16.dp
                ),
                content = {
                    items(allLocations.size) { index ->
                        val location = allLocations[index]
                        if (location.isNotBlank()) {
                            val cd6 = CornerSize(6.dp)
                            Card(
                                elevation = 2.dp,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth(),
                                backgroundColor = MaterialTheme.colors.primary,
                                shape = RoundedCornerShape(cd6, cd6, cd6, cd6),
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .combinedClickable(
                                            onLongClick = {
                                                currentLocation.value = location
                                                openDialog.value = true
                                            }
                                        ) {
                                            scanQrCode(location)
                                        },
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxHeight()
                                    ) {
                                        Text(
                                            text = location,
                                            color = MaterialTheme.colors.onPrimary,
                                            fontSize = 22.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                })
        }
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                text = {
                    Text(
                        "编辑or删除"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            navigateToAddCode(currentLocation.value)
                            openDialog.value = false
                        }
                    ) {
                        Text("编辑")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            allLocations = allLocations.minus(currentLocation.value)
                            Log.d("SCAN", "HomeScreen: " + allLocations.joinToString(","))
                            PreferenceUtil.setStringValue("locations", allLocations.joinToString(","))
                            openDialog.value = false
                        }
                    ) {
                        Text("删除")
                    }
                }
            )
        }
    }
}

