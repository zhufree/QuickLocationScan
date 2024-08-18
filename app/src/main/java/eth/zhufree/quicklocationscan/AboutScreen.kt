package eth.zhufree.quicklocationscan

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AboutScreen(navigateBack: () -> Unit, goUrl: (url: String) -> Unit) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        topBar = {
            TopAppBar(
                title = { Text("关于", color = MaterialTheme.colors.surface) },
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
    ) { _ ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "开发者其他作品", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            val cd6 = CornerSize(6.dp)
            Card(
                elevation = 2.dp,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(cd6, cd6, cd6, cd6),
            ) {
                Button(onClick = {
                    goUrl("https://www.coolapk.com/apk/205561")
                }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.background
                )) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GlideImage(
                            imageModel = "https://s1.328888.xyz/2022/05/18/DlHmJ.jpg",
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 16.dp)
                                .size(120.dp)
                                .align(Alignment.CenterHorizontally),
                            failure = {
                                Text(text = "image request failed.")
                            }
                        )
                        Text(text = "SCP基金会中分阅读工具", fontSize = 18.sp)
                    }
                }
            }
            Card(
                elevation = 2.dp,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                backgroundColor = MaterialTheme.colors.background,
                shape = RoundedCornerShape(cd6, cd6, cd6, cd6),
            ) {
                Button(onClick = {
                    goUrl("https://www.coolapk.com/apk/231900")
                }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.background
                )) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GlideImage(
                            imageModel = "https://s1.328888.xyz/2022/05/18/Dl4H0.jpg",
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 16.dp)
                                .size(120.dp)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Fit,
                            failure = {
                                Text(text = "image request failed.")
                            }
                        )
                        Text(text = "批量图片处理工具", fontSize = 18.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    goUrl("https://web.okjike.com/originalPost/6273d39c45baa566b6c7d7df")
                }) {
                    Text(text = "创意来源", fontSize = 24.sp)
                }
                Button(onClick = {
                    goUrl("https://qun.qq.com/qqweb/qunpro/share?_wv=3&_wwv=128&inviteCode=CUfeA&from=246610&biz=ka")
                }) {
                    Text(text = "联系开发者", fontSize = 24.sp)
                }
            }

        }
    }
}
