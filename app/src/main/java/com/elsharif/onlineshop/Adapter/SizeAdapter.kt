package com.elsharif.onlineshop.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elsharif.onlineshop.R
import com.elsharif.onlineshop.databinding.ViewholdeSizeBinding
import com.elsharif.onlineshop.databinding.ViewholderBrandBinding
import com.elsharif.onlineshop.databinding.ViewholderColorBinding
import com.elsharif.onlineshop.model.BrandModel

class SizeAdapter(val items:MutableList<String>) :
    RecyclerView.Adapter<SizeAdapter.Viewholder>() {

        private var  selectedPosition=-1
        private var  lastSelectedPosition=-1
        private lateinit var context: Context


    class Viewholder(val binding :ViewholdeSizeBinding)
        :RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeAdapter.Viewholder {
        context=parent.context
        val binding=ViewholdeSizeBinding
            .inflate(LayoutInflater.from(context),parent,false)

        return Viewholder(binding)


    }

    override fun onBindViewHolder(holder: SizeAdapter.Viewholder, position:Int) {
        val item =items[position]

        holder.binding.sizeText.text=item
        holder.binding.root.setOnClickListener {
            lastSelectedPosition=selectedPosition
            selectedPosition=position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

        }

        if(selectedPosition==position){
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg_selected)
            holder.binding.sizeText.setTextColor(context.resources.getColor(R.color.plure))
        }
        else {
            holder.binding.colorLayout.setBackgroundResource(R.drawable.grey_bg)
            holder.binding.sizeText.setTextColor(context.resources.getColor(R.color.black))


        }


    }

    override fun getItemCount(): Int = items.size
}