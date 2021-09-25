package edu.bupt.smart_refresh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.bupt.smart_refresh.ui.theme.SmartRefreshTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartRefreshTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SmartSwipeRefreshDemo()
                }
            }
        }
    }
}


@Preview
@Composable
fun SmartSwipeRefreshDemo() {
    val sentences = remember {
        mutableStateListOf(
            "铁轨总得创死一个人，要不就创死你吧",
            "嘉然，我真的好喜欢你啊，为了你我要听猫中毒",
            "你们平时都不看的吗",
            "我真的怀疑有些人闲的程度",
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        SmartSwipeRefresh(onRefresh = {
            delay(2000)
            if (sentences.size == 4) {
                sentences.add(0, "骂谁罕见，骂谁罕见")
                sentences.add(0, "真的绝绝子，好喝到翘jiojio")
                sentences.add(0, "乃琳你带我走吧")
            }
        }) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(sentences.size) {
                    val currentSentence = sentences[it]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        elevation = 4.dp,
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp), contentAlignment = Alignment.Center) {
                            Text(text = currentSentence, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}