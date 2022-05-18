package com.app.primemovie.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.primemovie.R
import com.app.primemovie.ViewMovieListActivity
import com.app.primemovie.activity.MovieDetailsActivity
import com.app.primemovie.model.ResultList
import com.squareup.picasso.Picasso

class MovieListAdapter(
    private val context: ViewMovieListActivity,
    private val movieList: List<ResultList>,
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title?.text = movieList[position].title

        var str = movieList[position].release_date
        var delimiter = "-"
        val parts = str?.split(delimiter)
        holder.release_date?.text = parts?.get(0)


        val url = movieList[position].poster_path
        Picasso.get().load(IMAGE_URL + url).into(holder.movie_image)

        holder.containerView.setOnClickListener {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("TITLE", movieList[position].title)
            intent.putExtra("IMAGE", IMAGE_URL + movieList[position].backdrop_path)
            intent.putExtra("RELEASE_DATE", movieList[position].release_date)
            intent.putExtra("OVERVIEW", movieList[position].overview)
            intent.putExtra("VOTE_AVG", movieList[position].vote_average)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    class ViewHolder(val containerView: View) : RecyclerView.ViewHolder(containerView) {
        var title: TextView? = null
        var release_date: TextView? = null
        var movie_image: ImageView? = null

        // var link: TextView? = null
        init {
            title = containerView.findViewById<TextView>(R.id.title)
            release_date = containerView.findViewById<TextView>(R.id.release_date)
            movie_image = containerView.findViewById<ImageView>(R.id.movie_image)
            // link = containerView.findViewById<TextView>(R.id.link)

        }

    }


    /* override fun retryPageLoad() {
         context.loadNextPage()
     }*/


}
