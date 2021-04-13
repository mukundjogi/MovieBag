package com.movieapp.ui.moviecast

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.movieapp.R
import com.movieapp.utiltyandconstant.loadImage
import com.movieapp.model.MovieCastModel
import com.movieapp.network.ApiConstants.Companion.IMAGE_BASE_URL
import com.movieapp.network.ApiConstants.Companion.POPULAR_MOVIES

class MovieCastAdapter(context:Context?, mData: ArrayList<MovieCastModel>):
        BaseQuickAdapter<MovieCastModel,BaseViewHolder>(R.layout.raw_movie_cast_list,mData) {
    val context : Context = context!!
    override fun convert(holder: BaseViewHolder?, castModel: MovieCastModel?) {
        if(castModel!=null) {
            holder?.setText(
                R.id.txtCastName,
                 if (castModel?.name!!.isNullOrEmpty()) context.getString(R.string.not_available) else castModel?.name!!
            )
            holder?.setText(
                R.id.txtDepartment,
                 "Department: "+ if (castModel?.knownForDepartment!!.isNullOrEmpty()) context.getString(R.string.not_available) else castModel?.knownForDepartment!!
            )
            holder?.setText(
                R.id.txtCharacter,
                 "Character: "+ if (castModel?.character!!.isNullOrEmpty()) context.getString(R.string.not_available) else castModel?.character!!
            )
            loadImage(
                context,
                IMAGE_BASE_URL+ POPULAR_MOVIES + castModel?.profilePath,
                holder?.getView<AppCompatImageView>(R.id.imgview)!!
            )
        }
    }
}