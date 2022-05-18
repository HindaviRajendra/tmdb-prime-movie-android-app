package com.app.primemovie.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.primemovie.R
import com.squareup.picasso.Picasso
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {
    @BindView(R.id.toolbar_back)
    lateinit var toolbar_back: ImageView

    @BindView(R.id.movie_image)
    lateinit var movie_image: ImageView

    @BindView(R.id.title)
    lateinit var title: TextView

    @BindView(R.id.release_date)
    lateinit var release_date: TextView

    @BindView(R.id.overview)
    lateinit var overview: TextView

    @BindView(R.id.sort_spinner)
    lateinit var sort_spinner: Spinner

    @BindView(R.id.vote_avg)
    lateinit var vote_avg: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        ButterKnife.bind(this)

        toolbar_back.visibility = View.VISIBLE
        sort_spinner.visibility = View.GONE

        val bundle: Bundle? = intent.extras
        val titleValue = bundle?.get("TITLE")
        val imagePathValue = bundle?.get("IMAGE")
        val releaseDateValue = bundle?.get("RELEASE_DATE")
        val overviewValue = bundle?.get("OVERVIEW")
        val vote_avgValue = bundle?.get("VOTE_AVG")

        title?.text = titleValue as CharSequence?

        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val inputDateStr = releaseDateValue as CharSequence?
        val date: Date = inputFormat.parse(inputDateStr.toString())
        val outputDateStr: String = outputFormat.format(date)

        release_date?.text =outputDateStr
        overview?.text = overviewValue as CharSequence?
        vote_avg?.text = (vote_avgValue as Float?).toString()

        Picasso.get().load(Uri.parse(imagePathValue as String?)).into(movie_image)

        toolbar_back.setOnClickListener { finish() }
    }
}