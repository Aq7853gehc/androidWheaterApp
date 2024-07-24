package com.example.wheatherapp

import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wheatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//32fd60e07562509ebb718d32f07aa2d5

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchWheatherData()
    }

    private fun fetchWheatherData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData("delhi","32fd60e07562509ebb718d32f07aa2d5","metric")
        response.enqueue(object :Callback<WheatherApp>{
            override fun onResponse(call: Call<WheatherApp>, response: Response<WheatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    val temperature= responseBody.main.temp.toString()

                }

            }

            override fun onFailure(call: Call<WheatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


}