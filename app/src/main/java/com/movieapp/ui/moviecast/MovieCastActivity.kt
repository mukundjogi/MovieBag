package com.movieapp.ui.moviecast

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.movieapp.databinding.ActivityMovieReviewBinding
import com.movieapp.model.CastBaseModel
import com.movieapp.model.MovieCastModel
import com.movieapp.repo.MovieViewModel
import com.movieapp.utiltyandconstant.APPCONSTANT
import com.movieapp.utiltyandconstant.isConnectedToNetwork

class MovieCastActivity : AppCompatActivity() {
    private var binding: ActivityMovieReviewBinding? = null
    private var movieReviewAdapter: MovieCastAdapter? = null
    private var movieId = 0
    private var movieViewModel: MovieViewModel? = null
    private var movieCastList: ArrayList<MovieCastModel>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieReviewBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initUI()
    }

    private fun initUI() {
        movieId = intent.getIntExtra(APPCONSTANT.EXTRA_MOVIE_ID, 0)

        if (isConnectedToNetwork(this)) {
            setInternetAvailable(true)
            binding?.layoutError?.txtErrorName?.text = "No cast data found"
            if (movieId > 0) {
                title = "Cast"
                movieViewModel = ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                ).get(
                    MovieViewModel::class.java
                )
                movieViewModel?.getRepository()
                getMovieCastList(movieId)
            } else {
                binding?.layoutError?.layout?.visibility = View.VISIBLE
            }
        } else {
            setInternetAvailable(false)
        }
    }

    private fun setInternetAvailable(isAvailable: Boolean) {
        if (isAvailable) {
            binding?.layoutError?.layout?.visibility = View.GONE
            binding?.layoutError?.btnTryAgain?.visibility = View.GONE
            binding?.layoutError?.txtErrorDetail?.visibility = View.GONE
        } else {
            binding?.layoutError?.txtErrorName?.text = "You are not connected to network!!"
            binding?.layoutError?.txtErrorDetail?.text =
                "If your are connected to network please click below."
            binding?.layoutError?.layout?.visibility = View.VISIBLE
            binding?.layoutError?.txtErrorDetail?.visibility = View.VISIBLE
            binding?.layoutError?.btnTryAgain?.visibility = View.VISIBLE
            binding?.layoutError?.btnTryAgain?.setOnClickListener {
                initUI()
            }
        }
    }

    private fun getMovieCastList(movieID: Int?) {
        movieViewModel?.getMovieCast(movieID)!!.observe(this) { movieCastResponse ->
            val castBaseModel =
                Gson().fromJson(movieCastResponse.toString(), CastBaseModel::class.java)
            if (castBaseModel?.cast != null) {
                val mItems = castBaseModel?.cast
                movieCastList?.addAll(mItems!!)
                setMovieReviewData(movieCastList!!)
            } else {
                binding?.layoutError?.layout?.visibility =
                    if (movieCastList.isNullOrEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setMovieReviewData(movieReviewList: ArrayList<MovieCastModel>) {
        if (movieReviewAdapter == null) {
            movieReviewAdapter = MovieCastAdapter(this, movieReviewList!!)
            binding?.recyclerview?.layoutManager = GridLayoutManager(this, 2)
            binding?.recyclerview?.adapter = movieReviewAdapter
            binding?.recyclerview?.isNestedScrollingEnabled = false
            binding?.recyclerview?.setPadding(4, 8, 4, 48)
            movieReviewAdapter?.setEnableLoadMore(true)
        } else {
            movieReviewAdapter?.notifyDataSetChanged()
        }
        binding?.layoutError?.layout?.visibility =
            if (movieReviewList.isNullOrEmpty()) View.VISIBLE else View.GONE
    }
}