package com.movieapp.utiltyandconstant

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.movieapp.R


fun isConnectedToNetwork(context: Context?): Boolean {
    var hasInternet = false
    val cm: ConnectivityManager? =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        val networks = cm?.allNetworks
        if (networks?.isNotEmpty()!!) {
            for (network in networks) {
                val nc = cm.getNetworkCapabilities(network!!)
                if (nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet =
                    true
            }
        }
    } else {
        if (cm != null && cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting) {
            hasInternet = true
        }
    }
    return hasInternet
}

fun loadImage(context: Context, url: String?, imagePic: AppCompatImageView) {
    Glide.with(context)
        .load(if (url.isNullOrEmpty()) R.drawable.ic_not_available else url)
        .placeholder(R.drawable.ic_not_available)
        .error(R.drawable.ic_not_available)
        .into(imagePic)
}