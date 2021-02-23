package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.MainViewAdapter
import com.udacity.asteroidradar.main.MainViewModel


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as MainViewAdapter
    adapter.submitList(data)
}


//new adapter declares an extension function on TextView, and passes in a Asteroid
//this is a TextView so we can directly access the text property
//new adapter
@BindingAdapter("asteroidItemName")
//new binding item for the MainFragment textviews
fun TextView.setAsteroidCodeName(item: Asteroid?) {
    item?.let {
        text = item.codeName
    }
}

@BindingAdapter("asteroidApproachDate")
fun TextView.setAsteroidDate(item: Asteroid?) {
    item?.let {
        text = item.closeApproachDate
    }
}

//new adapter
@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

//for the image of the day, using Picasso
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Picasso.get()
                .load(imgUri)
                .placeholder(R.drawable.placeholder_picture_of_day)
                //.error(R.drawable.)
                .into(imgView)
    }
}

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

//this adapter is used for the loading screen
@BindingAdapter("asteroidApiStatus")
fun bindStatus(statusImageView: ImageView, status: MainViewModel.AsteroidStatus?) {
    when (status) {      //when the status is true
        MainViewModel.AsteroidStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MainViewModel.AsteroidStatus.FAILED -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }

        MainViewModel.AsteroidStatus.SUCCESS -> {
            statusImageView.visibility = View.GONE
        }
    }
}
