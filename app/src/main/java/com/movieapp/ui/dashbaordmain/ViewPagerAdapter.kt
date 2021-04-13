package com.movieapp.ui.dashbaordmain

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.viewpager.widget.PagerAdapter
import com.movieapp.utiltyandconstant.APPCONSTANT
import com.movieapp.R
import com.movieapp.utiltyandconstant.loadImage
import com.movieapp.model.MovieModel
import com.movieapp.network.ApiConstants.Companion.IMAGE_BASE_URL
import com.movieapp.network.ApiConstants.Companion.NOW_PLAYING_MOVIES
import com.movieapp.ui.moviedetail.MovieDetailActivity

class ViewPagerAdapter(context: Context?, private val movieList: List<MovieModel?>?) :
    PagerAdapter() {
    val mContext: Context = context!!

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val movieModel = movieList!![position]!!
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.raw_sliding_movie, container, false)
        view.setOnClickListener {
            val intent = Intent(mContext, MovieDetailActivity::class.java)
            intent.putExtra(APPCONSTANT.EXTRA_MOVIE_ID,movieList?.get(position)?.movieId)
            intent.putExtra(APPCONSTANT.EXTRA_MOVIE_TITLE,movieList?.get(position)?.title)
            mContext.startActivity(intent)
        }
        val imageView = view.findViewById<AppCompatImageView>(R.id.imageView)
        val txtMovieName = view.findViewById<TextView>(R.id.txtMovieName)
        val txtReleaseDate = view.findViewById<TextView>(R.id.txtReleaseDate)
        loadImage(
            mContext,
            IMAGE_BASE_URL + NOW_PLAYING_MOVIES + movieModel.imagePath,
            imageView
        )
        txtMovieName.text =
            mContext.getString(R.string.movie_title) + " " + if (movieModel.title.isNullOrEmpty()) mContext.getString(R.string.not_available) else movieModel.title
        txtReleaseDate.text =
            mContext.getString(R.string.released_on_title) + " " + if (movieModel.releaseDate.isNullOrEmpty()) mContext.getString(R.string.not_available) else movieModel.releaseDate
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int = movieList?.size!!

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}