package com.movieapp.ui.moviedetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.movieapp.R
import com.movieapp.databinding.ActivityMovieDetailBinding
import com.movieapp.model.BaseModel
import com.movieapp.model.MovieModel
import com.movieapp.model.ProductionCompaniesModel
import com.movieapp.network.ApiConstants
import com.movieapp.repo.MovieViewModel
import com.movieapp.ui.moviecast.MovieCastActivity
import com.movieapp.ui.moviereview.MovieReviewActivity
import com.movieapp.utiltyandconstant.APPCONSTANT.Companion.EXTRA_MOVIE_ID
import com.movieapp.utiltyandconstant.APPCONSTANT.Companion.EXTRA_MOVIE_TITLE
import com.movieapp.utiltyandconstant.isConnectedToNetwork
import com.movieapp.utiltyandconstant.loadImage

class MovieDetailActivity : AppCompatActivity(), BaseQuickAdapter.RequestLoadMoreListener {
    private var binding: ActivityMovieDetailBinding? = null
    private var movieViewModel: MovieViewModel? = null
    private var similarMoviesAdapter: SimilarMoviesAdapter? = null
    private var similarMovieList = ArrayList<MovieModel>()
    private var productionCompaniesAdapter: ProductionCompaniesAdapter? = null
    private var mSimilarMovieResponse: BaseModel? = null
    private var movieId = 0
    private var movieTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMovieDetailBinding.inflate(layoutInflater)
            setContentView(binding?.root)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initUI()
    }

    private fun initUI() {
        movieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0)
        if (isConnectedToNetwork(this)) {
            setInternetAvailable(true)
            binding?.layoutError?.txtErrorName?.text = "No details found"
            if (movieId > 0) {
                movieTitle = if (intent.getStringExtra(EXTRA_MOVIE_TITLE)
                        .isNullOrEmpty()
                ) "" else intent.getStringExtra(EXTRA_MOVIE_TITLE)!!
                if (movieTitle.isNotEmpty()) {
                    title = movieTitle
                }
                movieViewModel = ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                ).get(
                    MovieViewModel::class.java
                )
                movieViewModel?.getRepository()
                getMovieDetail(movieId)
                getSimilarMovieList(movieId, 1)
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

    private fun getMovieDetail(movieId: Int?) {
        movieViewModel?.getMovieDetail(movieId)!!.observe(this) { popularMovieResponse ->
            if (popularMovieResponse != null) {
                setMovieData(popularMovieResponse)
            }
            binding?.layoutError?.layout?.visibility =
                if (popularMovieResponse != null) GONE else View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setMovieData(movieModel: MovieModel) {
        loadImage(
            this,
            ApiConstants.IMAGE_BASE_URL + ApiConstants.NOW_PLAYING_MOVIES + movieModel.imagePath,
            binding?.imageView!!
        )
        binding?.txtMovieName!!.text =
            getString(R.string.movie_title) + " " + if (movieModel.title.isNullOrEmpty())
                getString(R.string.not_available) else movieModel.title
        binding?.txtVoteAvg!!.text =
            getString(R.string.rating_title) + " " + if (movieModel.voteAverage!! > 0) movieModel.voteAverage
            else getString(R.string.not_available)
        binding?.txtLanguage!!.text =
            getString(R.string.language_title) + " " + if (movieModel.originalLanguage.isNullOrEmpty())
                getString(R.string.not_available) else movieModel.originalLanguage
        binding?.txtPopularity!!.text =
            getString(R.string.popularity_title) + " " + if (movieModel.popularity!! > 0) movieModel.popularity
            else getString(R.string.not_available)
        binding?.txtReleaseDate!!.text =
            getString(R.string.released_on_title) + " " + if (movieModel.releaseDate.isNullOrEmpty())
                getString(R.string.not_available) else movieModel.releaseDate
        binding?.txtReleaseStatus!!.text =
            getString(R.string.release_status_title) + " " + if (movieModel.status.isNullOrEmpty())
                getString(R.string.not_available) else movieModel.status
        binding?.txtOverViewDescription!!.text =
            if (movieModel.overview.isNullOrEmpty())
                getString(R.string.not_available) else movieModel.overview

        binding?.btnCast?.setOnClickListener {
            val intent = Intent(this@MovieDetailActivity, MovieCastActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            startActivity(intent)
        }
        binding?.btnReviews?.setOnClickListener {
            val intent = Intent(this@MovieDetailActivity, MovieReviewActivity::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            startActivity(intent)
        }
        if (movieModel.productionCompanies.isNullOrEmpty()) {
            binding?.txtProduction?.text =
                getString(R.string.production_companies) + ": " + getString(
                    R.string.not_available
                )
            binding?.scrollViewProd!!.visibility = View.GONE
        } else {
            setProductionCompaniesData(movieModel.productionCompanies)
        }
    }

    private fun setProductionCompaniesData(productionCompanies: ArrayList<ProductionCompaniesModel>?) {
        if (productionCompaniesAdapter == null) {
            productionCompaniesAdapter = ProductionCompaniesAdapter(this, productionCompanies!!)
            binding?.rvProduction?.layoutManager = LinearLayoutManager(
                this,
                RecyclerView.HORIZONTAL,
                false
            )
            binding?.rvProduction?.isNestedScrollingEnabled = false
            binding?.rvProduction?.adapter = productionCompaniesAdapter
        } else {
            similarMoviesAdapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getSimilarMovieList(movieID: Int?, page: Int?) {
        movieViewModel?.getSimilarMovies(movieID, page)!!.observe(this) { popularMovieResponse ->
            mSimilarMovieResponse = popularMovieResponse
            if (!mSimilarMovieResponse?.list.isNullOrEmpty()) {
                val mItems = mSimilarMovieResponse?.list
                similarMovieList.addAll(mItems!!)
                Log.e("similarMovieList = ", similarMovieList.toString())
                setSimilarMoviesData(similarMovieList)
            } else {
                binding?.txtSimilarMovies?.text =
                    getString(R.string.similar_movies) + ": " + getString(
                        R.string.not_available
                    )
                binding?.scrollViewSimilar!!.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSimilarMoviesData(similarMovieList: ArrayList<MovieModel>?) {
        if (similarMoviesAdapter == null) {
            similarMoviesAdapter = SimilarMoviesAdapter(this, similarMovieList!!)
            binding?.rvSimilarMovies?.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            binding?.rvSimilarMovies?.adapter = similarMoviesAdapter
            binding?.rvSimilarMovies?.isNestedScrollingEnabled = false
            binding?.rvSimilarMovies?.setPadding(4, 8, 4, 48)
            similarMoviesAdapter?.setOnLoadMoreListener(
                this@MovieDetailActivity,
                binding?.rvSimilarMovies!!
            )
            similarMoviesAdapter?.setEnableLoadMore(true)
        } else {
            similarMoviesAdapter?.notifyDataSetChanged()
        }
        if (mSimilarMovieResponse?.page == mSimilarMovieResponse?.totalPages!!) {
            setLoadMoreEnd()
        } else {
            similarMoviesAdapter?.loadMoreComplete()
        }

        if (similarMoviesAdapter == null) {
            binding?.txtSimilarMovies?.text =
                getString(R.string.similar_movies) + ": " + getString(R.string.not_available)
            binding?.scrollViewSimilar?.visibility = View.GONE
        }
    }

    override fun onLoadMoreRequested() {
        if (mSimilarMovieResponse?.page!! < mSimilarMovieResponse?.totalPages!!) {
            mSimilarMovieResponse?.page = mSimilarMovieResponse?.page!! + 1
            getSimilarMovieList(movieId, mSimilarMovieResponse?.page)
        }
    }

    private fun setLoadMoreEnd() {
        similarMoviesAdapter?.loadMoreEnd()
        similarMoviesAdapter?.setEnableLoadMore(false)
    }
}