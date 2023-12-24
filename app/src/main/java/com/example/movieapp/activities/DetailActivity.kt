package com.example.movieapp.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.movieapp.Adapters.ActorsListAdapters
import com.example.movieapp.Adapters.CategoryEachFilmListAdapter
import com.example.movieapp.Domain.FilmItem
import com.example.movieapp.R
import com.google.gson.Gson

class DetailActivity : AppCompatActivity() {
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var mStringRequest: StringRequest
    private lateinit var progressBar: ProgressBar
    private lateinit var titleTxt: TextView
    private lateinit var movieStarRate: TextView
    private lateinit var movieTimetxt: TextView
    private lateinit var movieSummaryInfo: TextView
    private lateinit var movieActorInfo: TextView
    private var idFilm: Int = 0
    private lateinit var Backimage: ImageView
    private lateinit var Fav: ImageView
    private lateinit var pic2: ImageView
    private lateinit var recyclerViewActors: RecyclerView
    private lateinit var  recyclerViewCategory: RecyclerView
    private lateinit var scrollView: NestedScrollView
    private lateinit var adapterActorList: ActorsListAdapters
    private lateinit var adapterCategory: CategoryEachFilmListAdapter

    private fun initView() {
        titleTxt = findViewById(R.id.movieNameTxt)
        progressBar = findViewById(R.id.progressBarDetail)
        scrollView = findViewById(R.id.scrollViewDetail)
        pic2 = findViewById(R.id.picDetail)
        movieStarRate = findViewById(R.id.movieStar)
        movieTimetxt = findViewById(R.id.movieTime)
        movieSummaryInfo = findViewById(R.id.movieSummary)
        movieActorInfo = findViewById(R.id.movieActorInfo)
        Backimage = findViewById(R.id.Backimg)
        Fav = findViewById(R.id.Favimage)
        recyclerViewCategory = findViewById(R.id.genreView)
        recyclerViewActors = findViewById(R.id.imageRecycler)
         recyclerViewCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewActors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        Backimage.setOnClickListener { finish() }
    }

    private fun sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this)
        progressBar.visibility = View.VISIBLE
        scrollView.visibility = View.GONE
        mStringRequest = StringRequest(
            Request.Method.GET,
            "https://moviesapi.ir/api/v1/movies/$idFilm",
            { response ->
                val gson = Gson()
                progressBar.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                val item: FilmItem = gson.fromJson(response, FilmItem::class.java)

                    Glide.with(this)
                        .load(item.poster)
                        .into(pic2)

                    titleTxt.text = item.title
                    movieStarRate.text = item.imdbRating
                    movieTimetxt.text = item.year
                    movieActorInfo.text = item.actors
                movieSummaryInfo.text=item.plot
                if (item.images != null) {
                        adapterActorList = ActorsListAdapters(item.images)
                        recyclerViewActors.adapter = adapterActorList
                    }

                    if (item.genres != null) {
                        adapterCategory = CategoryEachFilmListAdapter(item.genres)
                        recyclerViewCategory.adapter = adapterCategory
                    }
                    // Always set an adapter, even if the data is null
                    adapterActorList = ActorsListAdapters(item.images ?: emptyList())
                    recyclerViewActors.adapter = adapterActorList

                    adapterCategory = CategoryEachFilmListAdapter(item.genres ?: emptyList())
                    recyclerViewCategory.adapter = adapterCategory



            },
            { error ->
                progressBar.visibility = View.GONE
            })

        mRequestQueue.add(mStringRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        idFilm = intent.getIntExtra("id", 0)
        initView()
        sendRequest()
    }
}
