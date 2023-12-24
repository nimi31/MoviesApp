@file:Suppress("DEPRECATION")

package com.example.movieapp.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.movieapp.Adapters.CategoryListAdapter
import com.example.movieapp.Adapters.FilmListAdapter
import com.example.movieapp.Adapters.SliderAdapters
import com.example.movieapp.Domain.GenreItem
import com.example.movieapp.Domain.ListFilm
import com.example.movieapp.Domain.SliderItems
import com.example.movieapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewBestMovies: RecyclerView
    private lateinit var recyclerViewUpComingMovies: RecyclerView
    private lateinit var recyclerViewCategory: RecyclerView

    private lateinit var mRequestQueue: RequestQueue
    private lateinit var mStringRequest: StringRequest
    private lateinit var mStringRequest2: StringRequest
    private lateinit var mStringRequest3: StringRequest
    private lateinit var loading1: ProgressBar
    private lateinit var loading2: ProgressBar
    private lateinit var loading3: ProgressBar

    private lateinit var viewPager2: ViewPager2
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        banners()
        sendRequestBestMovies()
        sendRequestUpComing()
        sendRequestCategory()
    }

    private fun sendRequestBestMovies() {
        mRequestQueue = Volley.newRequestQueue(this)
        loading1.visibility = View.VISIBLE
        mStringRequest = StringRequest(
            Request.Method.GET,
            "https://moviesapi.ir/api/v1/movies?page=1",
            { response ->
                val gson = Gson()
                loading1.visibility = View.GONE
                val items = gson.fromJson(response, ListFilm::class.java)
                val adapterBestMovies =  FilmListAdapter(items)
                recyclerViewBestMovies.adapter = adapterBestMovies
            },
            { error ->
                loading1.visibility = View.GONE
                Log.i("UiLover", "OnerrorRespoonse :$error")
            }
        )
        mRequestQueue.add(mStringRequest)
    }
    private fun sendRequestUpComing() {
        mRequestQueue = Volley.newRequestQueue(this)
        loading3.visibility = View.VISIBLE
        mStringRequest3 = StringRequest(
            Request.Method.GET,
            "https://moviesapi.ir/api/v1/movies?page=2",
            { response ->
                val gson = Gson()
                loading3.visibility = View.GONE
                val items = gson.fromJson(response, ListFilm::class.java)
                val adapterUpComing =  FilmListAdapter(items)
                recyclerViewUpComingMovies.adapter = adapterUpComing
            },
            { error ->
                loading3.visibility = View.GONE
                Log.i("UiLover", "OnerrorRespoonse :$error")
            }
        )
        mRequestQueue.add(mStringRequest3)
    }
    private fun sendRequestCategory() {
        mRequestQueue = Volley.newRequestQueue(this)
        loading2.visibility = View.VISIBLE
        mStringRequest2 = StringRequest(
            Request.Method.GET,
            "https://moviesapi.ir/api/v1/genres",
            { response ->
                val gson = Gson()
                loading2.visibility = View.GONE
                val catList: ArrayList<GenreItem> = gson.fromJson(response, object : TypeToken<ArrayList<GenreItem>>() {}.type)
                val adapterCategory = CategoryListAdapter(catList)

                recyclerViewCategory.adapter = adapterCategory
            },
            { error ->
                loading2.visibility = View.GONE
                Log.i("UiLover", "OnerrorRespoonse :$error")
            }
        )
        mRequestQueue.add(mStringRequest2)
    }

    private fun banners() {
        val sliderItems = mutableListOf<SliderItems>()
        sliderItems.add(SliderItems(R.drawable.wide))
        sliderItems.add(SliderItems(R.drawable.wide1))
        sliderItems.add(SliderItems(R.drawable.wide3))

        val sliderAdapter = SliderAdapters(sliderItems, viewPager2)
        viewPager2.adapter = sliderAdapter
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.offscreenPageLimit = 3
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))

        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPager2.setPageTransformer(compositePageTransformer)
        viewPager2.currentItem = 1
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(sliderRunnable)
            }
        })
    }

    private val sliderRunnable = Runnable {
        viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(sliderRunnable, 2000)
    }

    private fun initView() {
        viewPager2 = findViewById(R.id.viewPageSlider)
        recyclerViewBestMovies = findViewById(R.id.recyclerView1)
        recyclerViewBestMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategory = findViewById(R.id.recyclerView2)
        recyclerViewCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewUpComingMovies = findViewById(R.id.recyclerView3)
        recyclerViewUpComingMovies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        loading1 = findViewById(R.id.progressBar1)
        loading2 = findViewById(R.id.progressBar2)
        loading3 = findViewById(R.id.progressBar3)
    }
}
