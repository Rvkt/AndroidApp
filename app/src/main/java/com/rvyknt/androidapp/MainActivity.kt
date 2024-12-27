package com.rvyknt.androidapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.*
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rvyknt.androidapp.network.ApiHeaders
import com.rvyknt.androidapp.network.models.ResponseModel
import com.rvyknt.androidapp.network.RetrofitClient
import com.rvyknt.androidapp.ui.theme.AndroidAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            AndroidAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    fun makePostApiCall() {
        // Request body map
        val requestBody = mapOf(
            "userName" to "9999726418",
            "password" to "",
            "source" to "APP",
            "mode" to "VIA_MOBILE",
            "otp" to "111111"
        )


        RetrofitClient.apiService.postLogin(ApiHeaders.deviceIdHeader(this@MainActivity).toString(), requestBody = requestBody)
            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            Log.d("Response", "Success: $it")
                            Log.d("Complete Response", response.toString())
                        }
                    } else {
                        Log.e("Error", "Response unsuccessful: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Log.e("Error", "Network call failed: ${t.message}")
                }
            })
    }


    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
    }

}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val activity = LocalContext.current as MainActivity

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Press the button to call API",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { activity.makePostApiCall() }) {
            Text(text = "Call API")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    AndroidAppTheme {
        MainContent()
    }
}
