package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class AsteroidAdapter(private val clickListener: AsteroidAdapterListener) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(AsteroidItemCallback) {

    companion object AsteroidItemCallback :
        DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }


    class AsteroidViewHolder(private var binding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: AsteroidAdapterListener, item: Asteroid) {
            binding.apply {
                asteroid = item
                executePendingBindings()
                clickListener = listener
            }
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidItemBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder =
        AsteroidViewHolder.from(parent)

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) =
        holder.bind(clickListener, getItem(position))
}


class AsteroidAdapterListener(val clickListener: (item: Asteroid) -> Unit) {
    fun onClick(item: Asteroid) = clickListener(item)
}