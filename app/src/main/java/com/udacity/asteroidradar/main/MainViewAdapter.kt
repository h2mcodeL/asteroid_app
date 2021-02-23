package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding
import com.udacity.asteroidradar.domain.Asteroid


class MainViewAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Asteroid, MainViewAdapter.AsteroidViewHolder>(AsteroidDiffCallback) {

    class AsteroidViewHolder(private var binding: ListItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {

            binding.asteroidList = asteroid
            binding.executePendingBindings()
        }
    }

    //class
    companion object AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        //use ctrl I to invoke
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder((ListItemAsteroidBinding.inflate(LayoutInflater.from(parent.context))))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        //add the info for the detail page
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }

    //this class may need to be outside of the scope. this creates a named lambda
    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}
