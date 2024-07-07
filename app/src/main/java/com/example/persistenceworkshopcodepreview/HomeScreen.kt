package com.example.persistenceworkshopcodepreview

import android.app.Application
import android.os.CountDownTimer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

class TimerViewModel(application: Application) : ViewModel() {
    private var timer: CountDownTimer? = null
    var remainingTime by mutableStateOf(25 * 60 * 1000L)
        private set

    fun startTimer(initialTime: Long, onTick: (Long) -> Unit, onFinish: () -> Unit) {
        timer?.cancel()
        timer = object : CountDownTimer(initialTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                onTick(millisUntilFinished)
            }

            override fun onFinish() {
                remainingTime = 0L
                onFinish()
            }
        }.start()
    }

    fun stopTimer(defaultTime: Long) {
        timer?.cancel()
        remainingTime = defaultTime
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    pomodoroViewModel: PomodoroViewModel = viewModel(),
    timerSettingViewModel: TimerSettingViewModel = viewModel(factory = TimerSettingViewModelFactory(LocalContext.current.applicationContext as Application)),
    timerViewModel: TimerViewModel = viewModel(factory = TimerViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val defaultTimerTime by timerSettingViewModel.defaultTimerTime.observeAsState(initial = 25)
    var remainingTime by remember { mutableStateOf(defaultTimerTime * 60 * 1000L) }

    LaunchedEffect(defaultTimerTime) {
        remainingTime = defaultTimerTime * 60 * 1000L
        timerViewModel.stopTimer(remainingTime)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = String.format("%02d:%02d", timerViewModel.remainingTime / 1000 / 60, (timerViewModel.remainingTime / 1000 % 60)),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row {
            Button(onClick = {
                timerViewModel.startTimer(remainingTime, { millisUntilFinished ->
                    remainingTime = millisUntilFinished
                }) {
                    remainingTime = 0L
                }
            }) {
                Text("Start Timer")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                timerViewModel.stopTimer(defaultTimerTime * 60 * 1000L)
                remainingTime = defaultTimerTime * 60 * 1000L
            }) {
                Text("Stop Timer")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        var startTime by remember { mutableStateOf("") }
        var endTime by remember { mutableStateOf("") }

        OutlinedTextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Start Time") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("End Time") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            val session = PomodoroSession(startTime = startTime, endTime = endTime, isCompleted = true)
            pomodoroViewModel.insert(session)
        }) {
            Text("Add Session")
        }

        // Observe the sessions
        val sessions by pomodoroViewModel.allSessions.observeAsState(emptyList())
        sessions.forEach { session ->
            Text(text = "ID: ${session.id}, Start: ${session.startTime}, End: ${session.endTime}")
        }
    }
}

class TimerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
