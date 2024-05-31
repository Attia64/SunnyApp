package com.attia.sunny.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attia.sunny.databinding.ForecastviewholderBinding
import com.attia.sunny.network.ForcastWeatherResponse
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Calendar

class ForecastAdapter() :
    ListAdapter<ForcastWeatherResponse.data, ForecastAdapter.ForecastViewHolder>(ForecastDiffer) {

    class ForecastViewHolder(val binding: ForecastviewholderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            ForecastviewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val data =
            SimpleDateFormat("yyyy-MM-dd HH:MM:SS").parse(currentList[position].dtTxt.toString())
        val calender = Calendar.getInstance()
        calender.time = data

        val dayOfWeekName = when (calender.get(Calendar.DAY_OF_WEEK)) {
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> "-"
        }
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val ampm = if (hour < 12) "am" else "pm"
        val hour12 = calender.get(Calendar.HOUR)

        val icon = when (currentList[position].weather?.get(0)?.icon.toString()) {
            "01d", "0n" -> "sunny"
            "02d", "02n" -> "cloudy_sunny"
            "03d", "03n" -> "cloudy_sunny"
            "04d", "04n" -> "cloudy"
            "09d", "09n" -> "rainy"
            "10d", "10n" -> "rainy"
            "11d", "11n" -> "storm"
            "13d", "13n" -> "snowy"
            "50d", "50n" -> "windy"
            else -> "sunny"
        }

        holder.binding.apply {
            tvDay.text = dayOfWeekName
            tvHour.text = hour12.toString() + ampm
            tvTemp.text = currentList[position].main?.temp?.let { Math.round(it) }.toString() + "°"
            val drawableResourceId: Int = root.resources.getIdentifier(
                icon,
                "drawable", root.context.packageName
            )
            Glide.with(root.context)
                .load(drawableResourceId)
                .into(imicon)
        }
    }

    private object ForecastDiffer : DiffUtil.ItemCallback<ForcastWeatherResponse.data>() {
        override fun areItemsTheSame(
            oldItem: ForcastWeatherResponse.data,
            newItem: ForcastWeatherResponse.data
        ): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(
            oldItem: ForcastWeatherResponse.data,
            newItem: ForcastWeatherResponse.data
        ): Boolean {
            return newItem == oldItem
        }
    }
}