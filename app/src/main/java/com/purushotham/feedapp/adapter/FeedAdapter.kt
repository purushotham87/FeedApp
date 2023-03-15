package com.purushotham.feedapp.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.purushotham.feedapp.R
import com.purushotham.feedapp.databinding.FeedItemBinding
import com.purushotham.feedapp.models.Feeds
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


class FeedAdapter(private val list: MutableList<Feeds>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        with(holder){
            with(list[position]){

                binding.tvTitle.text = this.post_title
                binding.tvDescription.text = this.post_description


                val imageBytes = Base64.decode(this.img, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.ivPostImage.setImageBitmap(decodedImage)

                val date = this.created_time?.let { Date(it.toLong()) }

                //val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

                sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

                val formattedDate = sdf.format(date)


                binding.tvCreated.text = formattedDate.toString()

            }
        }
    }

    class FeedViewHolder(val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root)
}