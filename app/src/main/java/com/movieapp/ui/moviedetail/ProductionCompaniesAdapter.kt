package com.movieapp.ui.moviedetail

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.movieapp.R
import com.movieapp.utiltyandconstant.loadImage
import com.movieapp.model.ProductionCompaniesModel
import com.movieapp.network.ApiConstants.Companion.IMAGE_BASE_URL
import com.movieapp.network.ApiConstants.Companion.POPULAR_MOVIES

class ProductionCompaniesAdapter(context:Context?, mData: ArrayList<ProductionCompaniesModel>):
        BaseQuickAdapter<ProductionCompaniesModel,BaseViewHolder>(R.layout.raw_movie_list,mData) {
    val context : Context = context!!
    override fun convert(holder: BaseViewHolder?, movieModel: ProductionCompaniesModel?) {
        if(movieModel!=null) {
            holder?.setText(
                R.id.txtMovieName,
                 if (movieModel?.productionName!!.isNullOrEmpty()) context.getString(R.string.not_available) else movieModel?.productionName!!
            )
            loadImage(
                mContext,
                IMAGE_BASE_URL+ POPULAR_MOVIES + movieModel?.logoPath,
                holder?.getView<AppCompatImageView>(R.id.imgview)!!
            )
        }
    }
}