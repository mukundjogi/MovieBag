package com.movieapp.ui.moviereview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import com.movieapp.databinding.ActivityMovieReviewBinding
import com.movieapp.model.MovieReviewBaseModel
import com.movieapp.model.MovieReviewModel
import com.movieapp.repo.MovieViewModel
import com.movieapp.utiltyandconstant.APPCONSTANT
import com.movieapp.utiltyandconstant.isConnectedToNetwork

class MovieReviewActivity : AppCompatActivity(), BaseQuickAdapter.RequestLoadMoreListener {
    private var binding: ActivityMovieReviewBinding? = null
    private var movieReviewAdapter: MovieReviewAdapter? = null
    private var movieId = 0
    private var movieViewModel: MovieViewModel? = null
    private var mMovieReviewResponse: MovieReviewBaseModel? = null
    private var movieReviewList: ArrayList<MovieReviewModel>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieReviewBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initUI()
    }

    private fun initUI() {
        movieId = intent.getIntExtra(APPCONSTANT.EXTRA_MOVIE_ID, 0)
        title = "Reviews"
        if (isConnectedToNetwork(this)) {
            setInternetAvailable(true)
            binding?.layoutError?.txtErrorName?.text = "No reviews found"
            if (movieId > 0) {
                movieViewModel = ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                ).get(
                    MovieViewModel::class.java
                )
                movieViewModel?.getRepository()
                getMovieReviewList(movieId, 1)
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

    private fun getMovieReviewList(movieID: Int?, page: Int?) {
        movieViewModel?.getMovieReview(movieID, page)!!.observe(this) { movieReviewResponse ->
            val movieReviewBaseModel =
                Gson().fromJson(movieReviewResponse.toString(), MovieReviewBaseModel::class.java)
            mMovieReviewResponse = movieReviewBaseModel
            if (mMovieReviewResponse?.list != null) {
                val mItems = mMovieReviewResponse?.list
                Log.e("totalPages = ", mMovieReviewResponse?.totalPages.toString())
                movieReviewList?.addAll(mItems!!)
                setMovieReviewData(movieReviewList)
            } else {
                binding?.layoutError?.layout?.visibility =
                    if (movieReviewList.isNullOrEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setMovieReviewData(movieReviewList: ArrayList<MovieReviewModel>?) {
        if (movieReviewAdapter == null) {
            movieReviewAdapter = MovieReviewAdapter(this, movieReviewList!!)
            binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
            binding?.recyclerview?.adapter = movieReviewAdapter
            binding?.recyclerview?.isNestedScrollingEnabled = false
            binding?.recyclerview?.setPadding(4, 8, 4, 48)
            movieReviewAdapter?.setOnLoadMoreListener(
                this@MovieReviewActivity,
                binding?.recyclerview!!
            )
            movieReviewAdapter?.setEnableLoadMore(true)
        } else {
            movieReviewAdapter?.notifyDataSetChanged()
        }
        if (mMovieReviewResponse?.page == mMovieReviewResponse?.totalPages!!) {
            setLoadMoreEnd()
        } else {
            movieReviewAdapter?.loadMoreComplete()
        }
        binding?.layoutError?.layout?.visibility =
            if (movieReviewList.isNullOrEmpty()) View.VISIBLE else View.GONE
    }

    override fun onLoadMoreRequested() {
        if (mMovieReviewResponse?.page!! < mMovieReviewResponse?.totalPages!!) {
            mMovieReviewResponse?.page = mMovieReviewResponse?.page!! + 1
            getMovieReviewList(movieId, mMovieReviewResponse?.page)
        }
    }

    private fun setLoadMoreEnd() {
        movieReviewAdapter?.loadMoreEnd()
        movieReviewAdapter?.setEnableLoadMore(false)
    }
}