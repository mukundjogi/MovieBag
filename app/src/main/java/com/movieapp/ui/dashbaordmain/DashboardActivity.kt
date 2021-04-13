package com.movieapp.ui.dashbaordmain

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.movieapp.R
import com.movieapp.databinding.ActivityDashboardBinding
import com.movieapp.model.BaseModel
import com.movieapp.model.MovieModel
import com.movieapp.repo.MovieViewModel
import com.movieapp.ui.moviedetail.MovieDetailActivity
import com.movieapp.utiltyandconstant.APPCONSTANT.Companion.EXTRA_MOVIE_ID
import com.movieapp.utiltyandconstant.APPCONSTANT.Companion.EXTRA_MOVIE_TITLE
import com.movieapp.utiltyandconstant.isConnectedToNetwork

class DashboardActivity : AppCompatActivity(), BaseQuickAdapter.RequestLoadMoreListener {

    private var binding: ActivityDashboardBinding? = null
    private var movieViewModel: MovieViewModel? = null
    var nowPlayingMovieList: ArrayList<MovieModel>? = ArrayList()
    var popularMovieList: ArrayList<MovieModel>? = ArrayList()
    var popularMovieListAdapter: PopularMovieAdapter? = null
    var mPopularMovieResponse: BaseModel? = null
    var mNowPlayingMovieResponse: BaseModel? = null
    private lateinit var adapter: ViewPagerAdapter
    private var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(binding!!.root)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        title = getString(R.string.app_name)
        initUI()
    }

    private fun initUI() {
        if (isConnectedToNetwork(this)) {
            setInternetAvailable(true)
            movieViewModel = ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            ).get(MovieViewModel::class.java)
            movieViewModel?.getRepository()
            getNowPlayingMovieList(1)
            getPopularMovieList(1)
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
            binding?.layoutError?.btnTryAgain?.visibility = View.VISIBLE
            binding?.layoutError?.txtErrorDetail?.visibility = View.VISIBLE
            binding?.layoutError?.btnTryAgain?.setOnClickListener {
                initUI()
            }
        }
    }

    private fun getNowPlayingMovieList(page: Int?) {
        movieViewModel?.getNowPlayingMovies(page!!)?.observe(this) { nowPlayingMovieResponse ->
            mNowPlayingMovieResponse = nowPlayingMovieResponse
            if (nowPlayingMovieResponse?.list != null) {
                val mItems = nowPlayingMovieResponse?.list
                nowPlayingMovieList?.addAll(mItems!!)
                setNowPlayingMovieAdapter(nowPlayingMovieList)
            }
        }
    }

    private fun setNowPlayingMovieAdapter(nowPlayingMovieList: ArrayList<MovieModel>?) {
        this.adapter = ViewPagerAdapter(this, nowPlayingMovieList)
        setViewPager()
    }

    private fun setViewPager() {
        binding?.viewpager?.adapter = adapter!!
        // Disable clip to padding
        binding?.viewpager?.clipToPadding = false
        // set padding manually, the more you set the padding the more you see of prev & next page
        binding?.viewpager?.setPadding(60, 0, 60, 0)
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        binding?.viewpager?.pageMargin = 30
        /**
         * Start automatic scrolling with an
         * interval of 3 seconds.
         */
        binding?.viewpager?.autoScroll(3000)
    }

    private fun getPopularMovieList(page: Int?) {
        movieViewModel?.getPopularMovies(page)!!.observe(this) { popularMovieResponse ->
            mPopularMovieResponse = popularMovieResponse
            if (popularMovieResponse?.list != null) {
                val mItems = popularMovieResponse?.list
                popularMovieList?.addAll(mItems!!)
                setPopularMovieAdapter(popularMovieList!!)
            }
        }
    }

    private fun setPopularMovieAdapter(popularMovieList: ArrayList<MovieModel>?) {
        if (popularMovieListAdapter == null) {
            popularMovieListAdapter = PopularMovieAdapter(this, popularMovieList!!)
            binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
            binding?.recyclerview?.adapter = popularMovieListAdapter
            binding?.recyclerview?.isNestedScrollingEnabled = false
            binding?.recyclerview?.setPadding(4, 8, 4, 48)
            binding?.recyclerview?.addOnItemTouchListener(object : OnItemClickListener() {
                override fun onSimpleItemClick(
                    adapter: BaseQuickAdapter<*, *>?,
                    view: View?,
                    position: Int
                ) {
                    val intent = Intent(this@DashboardActivity, MovieDetailActivity::class.java)
                    intent.putExtra(EXTRA_MOVIE_ID, popularMovieList?.get(position).movieId)
                    intent.putExtra(EXTRA_MOVIE_TITLE, popularMovieList?.get(position).title)
                    startActivity(intent)
                }
            })
            popularMovieListAdapter?.setOnLoadMoreListener(
                this@DashboardActivity,
                binding?.recyclerview!!
            )
            popularMovieListAdapter?.setEnableLoadMore(true)
        } else {
            popularMovieListAdapter?.notifyDataSetChanged()
        }
        if (mPopularMovieResponse?.page == mPopularMovieResponse?.totalPages!!) {
            setLoadMoreEnd()
        } else {
            popularMovieListAdapter?.loadMoreComplete()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onLoadMoreRequested() {
        if (mPopularMovieResponse?.page!! < mPopularMovieResponse?.totalPages!! && popularMovieListAdapter?.isLoading!!) {
            mPopularMovieResponse?.page = mPopularMovieResponse?.page!! + 1
            getPopularMovieList(mPopularMovieResponse?.page)
        }
    }

    private fun setLoadMoreEnd() {
        popularMovieListAdapter?.loadMoreEnd()
        popularMovieListAdapter?.setEnableLoadMore(false)
    }

    //entension
    private fun ViewPager.autoScroll(interval: Long) {
        val handler = Handler()
        var scrollPosition = 0

        val runnable = object : Runnable {
            override fun run() {
                /**
                 * Calculate "scroll position" with
                 * adapter pages count and current
                 * value of scrollPosition.
                 */
                val count = adapter?.count ?: 0
                if (count > 0)
                    setCurrentItem(scrollPosition++ % count, true)

                handler.postDelayed(this, interval)
            }
        }
        handler.post(runnable)
    }
}