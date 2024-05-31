package com.attia.sunny.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.attia.sunny.activity.MainActivity
import com.attia.sunny.databinding.CityViewholderBinding
import com.attia.sunny.network.CityResponse

class CityAdapter() :
    ListAdapter<CityResponse.CityResponseItem, CityAdapter.CityViewHolder>(CitytDiffer) {

    class CityViewHolder(val binding: CityViewholderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        return CityViewHolder(
            CityViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.binding.apply {
            tvCityName.text = currentList[position].name
            tvCityName.setOnClickListener {
                val intent = Intent(root.context, MainActivity::class.java)
                intent.putExtra("lat", currentList[position].lat)
                intent.putExtra("lon", currentList[position].lon)
                intent.putExtra("name", currentList[position].name)
                root.context.startActivity(intent)
            }
        }
    }

    private object CitytDiffer : DiffUtil.ItemCallback<CityResponse.CityResponseItem>() {
        override fun areItemsTheSame(
            oldItem: CityResponse.CityResponseItem,
            newItem: CityResponse.CityResponseItem
        ): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(
            oldItem: CityResponse.CityResponseItem,
            newItem: CityResponse.CityResponseItem
        ): Boolean {
            return newItem == oldItem
        }
    }
}