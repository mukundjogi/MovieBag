package com.movieapp.ui.moviedetail

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.movieapp.R
import com.movieapp.utiltyandconstant.loadImage
import com.movieapp.model.MovieModel
import com.movieapp.network.ApiConstants.Companion.IMAGE_BASE_URL
import com.movieapp.network.ApiConstants.Companion.POPULAR_MOVIES

class SimilarMoviesAdapter(context:Context?, mData: ArrayList<MovieModel>):
        BaseQuickAdapter<MovieModel,BaseViewHolder>(R.layout.raw_movie_list,mData) {
    val context : Context = context!!
    override fun convert(holder: BaseViewHolder?, movieModel: MovieModel?) {
        if(movieModel!=null) {
            holder?.setText(
                R.id.txtMovieName,
                 if (movieModel?.title!!.isNullOrEmpty()) context.getString(R.string.not_available) else movieModel?.title!!
            )
            loadImage(
                context,
                IMAGE_BASE_URL+ POPULAR_MOVIES + movieModel?.imagePath,
                holder?.getView<AppCompatImageView>(R.id.imgview)!!
            )
        }
    }
}