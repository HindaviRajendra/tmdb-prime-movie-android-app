package com.app.primemovie

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.primemovie.adapter.MovieListAdapter
import com.app.primemovie.model.MovieResponse
import com.app.primemovie.model.ResultList
import com.app.primemovie.network.RestClient
import com.app.primemovie.network.RetroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ViewMovieListActivity : AppCompatActivity() {

    @BindView(R.id.toolbar_back)
    lateinit var toolbar_back: ImageView

    @BindView(R.id.sort_spinner)
    lateinit var sort_spinner: Spinner

    @BindView(R.id.movieListRecyclerView)
    lateinit var movieListRecyclerView: RecyclerView

    @BindView(R.id.progressBar)
    lateinit var progressBar: ProgressBar

    @BindView(R.id.idNestedSV)
    lateinit var idNestedSV: NestedScrollView

    private var mApiService: RetroService? = null

    lateinit var movieAdapter: MovieListAdapter

    private lateinit var layoutManager: LinearLayoutManager

    private var movieList: MutableList<ResultList> = ArrayList()

    val API_KEY = "db5ee635ddd1ef2e0fdcda232d608c62"

    var page = 0

    val limit = 9

    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_movie_list)
        ButterKnife.bind(this)
        layoutManager = LinearLayoutManager(this)
        toolbar_back.visibility = View.GONE

        val sort_category = resources.getStringArray(R.array.sort_Categories)


        if (sort_spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sort_category)
            sort_spinner.adapter = adapter

            sort_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (sort_category[position].equals("Top Rated")) {
                        getTopRated()
                    } else {
                        getPopularMovie();
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }

        mApiService = RestClient.client.create(RetroService::class.java)
        movieListRecyclerView!!.layoutManager = GridLayoutManager(applicationContext, 3)
        movieAdapter = MovieListAdapter(this, movieList)
        movieListRecyclerView!!.adapter = movieAdapter

        getPage()


        movieListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == movieList.size - 1) {
                        // loadData()
                        isLoading = true
                    }
                }

                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = movieAdapter?.itemCount

                if (!isLoading) {
                    if ((visibleItemCount + pastVisibleItem) >= total!!) {
                        page++
                        getPage()
                    }
                }
            }
        })
    }

    private fun getPage() {
        progressBar.visibility = View.VISIBLE
        val start = (page - 1) * limit
        val end = (page) * limit

        for (i in start..end) {
            mApiService = RestClient.client.create(RetroService::class.java)
            movieListRecyclerView!!.layoutManager = GridLayoutManager(applicationContext, 3)
            movieAdapter = MovieListAdapter(this, movieList)
            movieListRecyclerView!!.adapter = movieAdapter

        }

        Handler().postDelayed({
            if (::movieAdapter.isInitialized) {
                movieAdapter?.notifyDataSetChanged()
            } else {
                movieAdapter = MovieListAdapter(this, movieList)

                isLoading = false
            }
            progressBar.visibility = View.GONE

        }, 4 * 1000)
    }

    private fun getPopularMovie() {
        if (isNetworkConnectionAvailable(this)) {


            if (page > limit) {
                Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();
                // hiding our progress bar.
                progressBar.setVisibility(View.GONE);
                return;
            }

            val call = mApiService!!.getPopular(API_KEY);
            call.enqueue(object : Callback<MovieResponse> {

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    Log.e(TAG, "Total Results:: " + response.body()!!.results!!.size)
                    val popularMovieResponse = response.body()
                    if (popularMovieResponse != null) {

                        // var movieList: MutableList<ResultList> = ArrayList()
                        movieList.clear()
                        movieList.addAll(popularMovieResponse.results!!)
                        movieAdapter!!.notifyDataSetChanged()


                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG, "Got error : " + t.localizedMessage)
                }
            })
        } else {
            Toast.makeText(
                this@ViewMovieListActivity,
                "No Internet!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getTopRated() {
        if (isNetworkConnectionAvailable(this)) {

            if (page > limit) {
                Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();
                // hiding our progress bar.
                progressBar.setVisibility(View.GONE);
                return;
            }


            val call = mApiService!!.getTopRatedMovie(API_KEY);
            call.enqueue(object : Callback<MovieResponse> {

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {

                    Log.e(TAG, "Total Results:: " + response.body()!!.results!!.size)
                    val topRatedMovieResponse = response.body()
                    if (topRatedMovieResponse != null) {

                        // var movieList: MutableList<ResultList> = ArrayList()
                        movieList.clear()
                        movieList.addAll(topRatedMovieResponse.results!!)
                        movieAdapter!!.notifyDataSetChanged()


                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG, "Got error : " + t.localizedMessage)
                }
            })
        } else {
            Toast.makeText(
                this@ViewMovieListActivity,
                "No Internet!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.dialogMessage)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            super.onBackPressed()
        }
        builder.setNegativeButton("No") { dialogInterface, which ->

        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun isNetworkConnectionAvailable(context: Context?): Boolean {
        var isNetworkConnectionAvailable = false
        if (context != null) {
            val connectivityManager =
                context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null) {
                isNetworkConnectionAvailable =
                    activeNetworkInfo.state == NetworkInfo.State.CONNECTED
            }
        }
        return isNetworkConnectionAvailable
    }

    companion object {
        private val TAG = ViewMovieListActivity::class.java.simpleName
    }
}