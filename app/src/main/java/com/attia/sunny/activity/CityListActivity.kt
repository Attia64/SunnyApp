package com.attia.sunny.activity
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.attia.sunny.adapter.CityAdapter
import com.attia.sunny.databinding.ActivityCityListBinding
import com.attia.sunny.network.CityResponse
import com.attia.sunny.viewModel.CityViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@AndroidEntryPoint
class CityListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCityListBinding
    private lateinit var cityViewModel: CityViewModel
    private val cityAdapter by lazy { CityAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cityViewModel = ViewModelProvider(this)[CityViewModel::class.java]

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            etCity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    pbloader.visibility = View.VISIBLE
                    cityViewModel.loadCity(s.toString(), 10)
                        .enqueue(object : Callback<CityResponse> {
                            override fun onResponse(
                                p0: Call<CityResponse>,
                                p1: Response<CityResponse>
                            ) {
                                if (p1.isSuccessful) {
                                    val data = p1.body()
                                    pbloader.visibility = View.GONE
                                    data?.let {
                                        cityAdapter.submitList(it)
                                        rvCity.apply {
                                            layoutManager = LinearLayoutManager(
                                                this@CityListActivity,
                                                LinearLayoutManager.HORIZONTAL,
                                                false
                                            )
                                            adapter = cityAdapter
                                        }
                                    }
                                }
                            }

                            override fun onFailure(p0: Call<CityResponse>, p1: Throwable) {
                                Toast.makeText(this@CityListActivity, "$p1", Toast.LENGTH_SHORT).show()
                                Log.i("MainActivity", "$p1")
                            }

                        })
                }

            })
        }

    }
}