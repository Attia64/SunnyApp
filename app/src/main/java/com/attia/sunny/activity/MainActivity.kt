package com.attia.sunny.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.attia.sunny.adapter.ForecastAdapter
import com.attia.sunny.R
import com.attia.sunny.databinding.ActivityMainBinding
import com.attia.sunny.network.CurrentWeatherResponse
import com.attia.sunny.network.ForcastWeatherResponse
import com.attia.sunny.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForecastAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]


        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            var lat = intent.getDoubleExtra("lat", 0.0)
            var lon = intent.getDoubleExtra("lon", 0.0)
            var name = intent.getStringExtra("name")

            if (lat == 0.0) {
                lat = 30.03
                lon = 31.23
                name = "Cairo"
            }

            imAddCity.setOnClickListener {
                startActivity(Intent(this@MainActivity, CityListActivity::class.java))
            }

            pbloader.visibility = View.VISIBLE
            tvCity.text = name

            weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object :
                retrofit2.Callback<CurrentWeatherResponse> {
                override fun onResponse(
                    p0: Call<CurrentWeatherResponse>,
                    p1: Response<CurrentWeatherResponse>
                ) {
                    if (p1.isSuccessful) {
                        val data = p1.body()
                        pbloader.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        data?.let {
                            tvStatus.text = it.weather?.get(0)?.main ?: "-"
                            tvWind.text = it.wind?.speed.let { Math.round(it!!).toString() } + "KM"
                            tvCurrentTemp.text =
                                it.main?.temp.let { Math.round(it!!).toString() } + "°"
                            tvHumidity.text = it.main!!.humidity.toString() + "%"
                            tvMaxTemp.text =
                                it.main?.tempMax.let { Math.round(it!!).toString() } + "°"
                            tvMinTemp.text =
                                it.main?.tempMin.let { Math.round(it!!).toString() } + "°"

                            val drawable = if (isNighNow()) R.drawable.night_bg
                            else {
                                setDynamicallyWallpaper(it.weather!![0]?.icon ?: "-")
                            }
                            ivBackground.setImageResource(drawable)
                        }
                    }
                }

                override fun onFailure(p0: Call<CurrentWeatherResponse>, p1: Throwable) {
                    Toast.makeText(this@MainActivity, "$p1", Toast.LENGTH_SHORT).show()
                    Log.i("MainActivity", "$p1")
                }

            })

            weatherViewModel.loadForecastWeather(lat, lon, "metric")
                .enqueue(object : retrofit2.Callback<ForcastWeatherResponse> {
                    override fun onResponse(
                        p0: Call<ForcastWeatherResponse>,
                        p1: Response<ForcastWeatherResponse>
                    ) {
                        if (p1.isSuccessful) {
                            val data = p1.body()
                            clForecast.visibility = View.VISIBLE
                            data?.let {
                                forecastAdapter.submitList(it.list?.toMutableList())
                                rvforecast.apply {
                                    layoutManager = LinearLayoutManager(
                                        this@MainActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                    adapter = forecastAdapter
                                }
                            }
                        }
                    }

                    override fun onFailure(p0: Call<ForcastWeatherResponse>, p1: Throwable) {
                        Toast.makeText(this@MainActivity, "$p1", Toast.LENGTH_SHORT).show()
                        Log.i("MainActivity", "$p1")
                    }
                })
        }

    }

    private fun isNighNow(): Boolean {
        if(calendar.get(Calendar.HOUR_OF_DAY) <= 5 && calendar.get(Calendar.HOUR_OF_DAY) >= 19)
            return true
        return false
    }

    private fun setDynamicallyWallpaper(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> R.drawable.sunny_bg
            "02", "03", "04" -> R.drawable.cloudy_bg
            "09", "10", "11" -> R.drawable.rainy_bg
            "013" -> R.drawable.snow_bg
            "50" -> R.drawable.haze_bg
            else -> 0
        }
    }

}