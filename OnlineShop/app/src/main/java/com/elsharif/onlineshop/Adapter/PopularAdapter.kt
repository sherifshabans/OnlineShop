package com.elsharif.onlineshop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.elsharif.onlineshop.R
import com.elsharif.onlineshop.activity.DetailActivity
import com.elsharif.onlineshop.databinding.ViewholdeRecommendationBinding
import com.elsharif.onlineshop.model.ItemModel

class PopularAdapter(val items :MutableList<ItemModel>):
    RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

        private var context:Context ?=null


    class ViewHolder (val binding :ViewholdeRecommendationBinding)
        :RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.ViewHolder {
   context =parent.context
        val binding=ViewholdeRecommendationBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularAdapter.ViewHolder, position: Int) {

        val item =items[position]
        holder.binding.titleText.text=item.title
        holder.binding.picText.text="$"+item.price.toString()
        holder.binding.ratingText.text=item.rating.toString()

        val requestOptions=RequestOptions().transform(CenterCrop())

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(requestOptions)
            .error(R.drawable.shoes) // Add a placeholder for error
            .into(holder.binding.picPopular)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,DetailActivity::class.java)
            intent.putExtra("object",item)
            holder.itemView.context.startActivity(intent)

        }



    }


    override fun getItemCount(): Int =items.size
}