package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.common.Constants.KEY_CITY
import com.example.weatherapp.common.Constants.SHARED_PREFERENCE_KEY
import com.example.weatherapp.common.MyApplication
import com.example.weatherapp.model.WeatherResponseState
import com.example.weatherapp.model.WeatherViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import javax.inject.Inject

class WeatherActivity : ComponentActivity() {
    @Inject
    lateinit var factory: WeatherViewModel.WeatherViewModelFactory
    private val viewModel: WeatherViewModel by viewModels {
        factory
    }
    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WeatherReport()
                }
            }
        }
        //Initialize shared preferences and read the last search info
        sharedPref = getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
        val city = sharedPref?.getString(KEY_CITY, "")
        if (city?.isNotEmpty() == true) {
            viewModel.city.value = city
            viewModel.getWeatherReport()
        }
    }

    @Composable
    private fun WeatherReport() {
        val keyboardController = LocalSoftwareKeyboardController.current
        Box(modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.sky_bg),
                contentScale = ContentScale.FillBounds,
                alpha = 0.5f
            )) {
            Column {
                Text(
                    text = stringResource(R.string.title),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.purple_700)
                    ),
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = viewModel.city.value, onValueChange = {
                        viewModel.city.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, Color.DarkGray)),
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.getWeatherReport()
                        sharedPref?.edit()?.putString(KEY_CITY, viewModel.city.value)?.apply()
                        keyboardController?.hide()
                    }),
                    label = {
                        Text(stringResource(R.string.hint))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    ),
                    singleLine = true
                )
                Box(modifier = Modifier.weight(1f)) {
                    when (viewModel.uiState.value) {
                        is WeatherResponseState.Loading -> {
                            if ((viewModel.uiState.value as WeatherResponseState.Loading).isLoading)
                                ShowLoader()
                        }
                        is WeatherResponseState.OnFailed -> {
                            ShowError(msg = (viewModel.uiState.value as WeatherResponseState.OnFailed).errorMsg)
                        }
                        else -> {
                            ShowReport()
                        }
                    }
                }
            }
        }
    }

    /**
     * Show weather report based on city
     */
    @Composable
    private fun ShowReport() {
        val uiData = viewModel.uiState.value as WeatherResponseState.OnSuccess
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = uiData.uiModel.icon),
                    contentDescription = "icon",
                    modifier = Modifier.size(128.dp)
                )
                Column(modifier = Modifier.wrapContentWidth()) {
                    Text(
                        text = uiData.uiModel.temp,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiData.uiModel.title,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            val info = buildString {
                appendLine(stringResource(id = R.string.temp, uiData.uiModel.temp))
                appendLine()
                appendLine(stringResource(id = R.string.desc, uiData.uiModel.description))
                appendLine()
                appendLine(stringResource(id = R.string.humidity, uiData.uiModel.humidity))
                appendLine()
                appendLine(stringResource(id = R.string.pressure, uiData.uiModel.pressure))
                appendLine()
                appendLine(stringResource(id = R.string.wind, uiData.uiModel.windSpeed))
                appendLine()
                appendLine(stringResource(id = R.string.location, uiData.uiModel.location))
            }
            Text(
                text = info,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }

    /**
     * Show content loader
     */
    @Composable
    private fun ShowLoader() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    /**
     * Show error message
     */
    @Composable
    private fun ShowError(msg: String) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = msg, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp))
        }
    }
}