package com.example.persistenceworkshopcodepreview.ui

import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.persistenceworkshopcodepreview.PomodoroSession
import com.example.persistenceworkshopcodepreview.PomodoroViewModel

@Composable
fun HomeScreen(pomodoroViewModel: PomodoroViewModel) {
    var defaultTimerTime by remember { mutableStateOf(25) }
    var currentTime by remember { mutableStateOf("25:00") }
    var timer: CountDownTimer? by remember { mutableStateOf(null) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = currentTime,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    timer?.cancel()
                    timer = object : CountDownTimer((defaultTimerTime * 60 * 1000).toLong(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val minutes = millisUntilFinished / 1000 / 60
                            val seconds = millisUntilFinished / 1000 % 60
                            currentTime = String.format("%02d:%02d", minutes, seconds)
                        }

                        override fun onFinish() {
                            currentTime = "00:00"
                        }
                    }.start()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Timer")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    timer?.cancel()
                    currentTime = String.format("%02d:%02d", defaultTimerTime, 0)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Stop Timer")
            }
        }

        Column {
            Text("Sessions")
            Spacer(modifier = Modifier.height(16.dp))
            EditText(placeholder = "Start Time", text = startTime, onTextChange = { startTime = it }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            EditText(placeholder = "End Time", text = endTime, onTextChange = { endTime = it }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val session = PomodoroSession(startTime = startTime, endTime = endTime, isCompleted = true)
                    pomodoroViewModel.insert(session)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Session")
            }
        }
    }
}

@Composable
fun EditText(placeholder: String, text: String, onTextChange: (String) -> Unit, modifier: Modifier) {
    // Implement EditText logic here, e.g., TextField
}
