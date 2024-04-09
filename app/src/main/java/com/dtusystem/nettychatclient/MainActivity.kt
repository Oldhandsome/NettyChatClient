package com.dtusystem.nettychatclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.dtusystem.nettychatclient.network.ClientBinder
import com.dtusystem.nettychatclient.network.message.Formula
import com.dtusystem.nettychatclient.network.message.Technology
import com.dtusystem.nettychatclient.repository.ExampleRepository
import com.dtusystem.nettychatclient.ui.theme.NettyChatClientTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var client: ClientBinder

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            client.startConnect()
        }
        setContent {
            NettyChatClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val formulaStr by viewModel.formula.collectAsState()

                    val technologyStr by viewModel.technology.collectAsState()

                    Column {
                        Text(text = "Formula str" + formulaStr)
                        Button(onClick = {
                            viewModel.issueFormula(Formula(1, 2, 3, 1, 2, 3))
                        }) {
                            Text(text = "On Click Get Formula")
                        }

                        Button(onClick = {
                            viewModel.issueFormula2(Formula(1, 2, 3, 1, 2, 3))
                        }) {
                            Text(text = "On Click Get Formula 2")
                        }

                        Text(text = "Technology str" + technologyStr)
                        Button(onClick = {
                            viewModel.issueTechnology(Technology(10, 20, 30))
                        }) {
                            Text(text = "On Click Get Technology")
                        }


                    }
                }
            }
        }
    }


    override fun onDestroy() {
        lifecycleScope.launch(Dispatchers.IO) {
            client.stopConnect()
        }
        super.onDestroy()
    }
}


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: ExampleRepository,
) : ViewModel() {

    private val _formula = MutableStateFlow("")

    val formula = _formula.asStateFlow()

    fun issueFormula(formula: Formula) = viewModelScope.launch(Dispatchers.IO) {
        val promise = repository.issueFormula(formula)
        promise.addListener { future ->
            if (future.isSuccess) {
                Log.d("TAG", "issueFormula: ")
                Log.d("TAG", "this is what i get ${future.get()}")
            } else {
                Log.e("TAG", future.cause().message ?: "暂无")
            }
        }
    }

    private val _technology = MutableStateFlow("")

    val technology = _technology.asStateFlow()

    fun issueTechnology(technology: Technology) = viewModelScope.launch(Dispatchers.IO) {
        repository.issueTechnology(technology)
            .addListener { future ->
                if (future.isSuccess) {
                    val message = future.get()
                    println("on Click Get Technology Callback")
                    println(message)

                    _technology.value = message.toString()
                } else {
                    Log.e("TAG", future.cause().stackTraceToString())
                }
            }
    }

    fun issueFormula2(formula: Formula) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = repository.issueFormula2(formula)
            println("***************************")
            println(response.success)
            println(response.reason)
        } catch (e: Exception) {
            println("---------------------1234---------------------")
            e.printStackTrace()
        }
    }
}
