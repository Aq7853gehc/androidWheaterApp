package com.example.wheatherapp

import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.wheatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//32fd60e07562509ebb718d32f07aa2d5

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchWheatherData("Delhi")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWheatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWheatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response =
            retrofit.getWeatherData(cityName, "32fd60e07562509ebb718d32f07aa2d5", "metric")
        response.enqueue(object : Callback<WheatherApp> {
            override fun onResponse(call: Call<WheatherApp>, response: Response<WheatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {

                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val seaLevel = responseBody.main.sea_level
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    val windSpeed = responseBody.wind.speed
                    val sunset = responseBody.sys.sunset
                    val sunrise = responseBody.sys.sunrise
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"

                    binding.temp.text = "$temperature C"
                    binding.humidity.text = " $humidity %"
                    binding.condition.text = condition
                    binding.sea.text = "$seaLevel"
                    binding.max.text = "$maxTemp C"
                    binding.min.text = "$minTemp C"
                    binding.wheather.text = condition
                    binding.city.text = "$cityName"
                    binding.windspeed.text= "$windSpeed m/s"
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.sunset.text = "${sunset}"
                    binding.sunrise.text = "$sunrise"

                    changeAccordingBG(condition)


                }

            }

            override fun onFailure(call: Call<WheatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun changeAccordingBG(condition: String) {
        when (condition){
            "Mist","Partly Clouds","Clouds","Overcast","Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Clear Sky","Sunny","Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain","Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow","Moderate Snow","Heavy Snow","Blezzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

        }
//        binding.lottieAnimationView.playAnimation()

    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))

    }


}