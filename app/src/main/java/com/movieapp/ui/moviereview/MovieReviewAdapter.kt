package com.movieapp.ui.moviereview

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.movieapp.R
import com.movieapp.utiltyandconstant.loadImage
import com.movieapp.model.MovieReviewModel

class MovieReviewAdapter(context:Context?, mData: ArrayList<MovieReviewModel>):
        BaseQuickAdapter<MovieReviewModel,BaseViewHolder>(R.layout.raw_movie_review_list,mData) {
    val context : Context = context!!
    override fun convert(holder: BaseViewHolder?, movieModel: MovieReviewModel?) {
        if(movieModel!=null) {
            holder?.setText(
                R.id.txAuthorName,
                 if (movieModel?.author!!.isNullOrEmpty()) context.getString(R.string.not_available) else movieModel?.author!!
            )
            holder?.setText(
                R.id.txtCreatedDate,
                 if (movieModel?.createdAt!!.isNullOrEmpty()) context.getString(R.string.not_available) else movieModel?.createdAt!!
            )
            holder?.setText(
                R.id.txtContent,
                 if (movieModel?.content!!.isNullOrEmpty()) context.getString(R.string.not_available) else movieModel?.content!!
            )
            loadImage(
                mContext,
                 movieModel?.avatarPath,
                holder?.getView<AppCompatImageView>(R.id.imgview)!!
            )
        }
    }
}