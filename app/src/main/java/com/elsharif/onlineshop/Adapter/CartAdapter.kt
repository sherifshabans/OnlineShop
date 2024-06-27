package com.elsharif.onlineshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.elsharif.onlineshop.Helper.ChangeNumberItemsListener
import com.elsharif.onlineshop.Helper.ManagmentCart
import com.elsharif.onlineshop.databinding.ViewholderCartBinding
import com.elsharif.onlineshop.model.ItemModel

class CartAdapter(private val listIteamSelected:ArrayList<ItemModel>,
    context: Context,
    var  changeNumberItemsListener: ChangeNumberItemsListener?=null
):RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val binding :ViewholderCartBinding):RecyclerView.ViewHolder(binding.root){

    }

    private val managementCart=ManagmentCart(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val binding=ViewholderCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val item =listIteamSelected[position]

        holder.binding.titleTax.text=item.title
        holder.binding.feeEach.text="$${item.price}"
        holder.binding.totalEach.text="$${Math.round(item.numberInCart*item.price)}"
        holder.binding.numberItemTax.text=item.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.pic)

        holder.binding.plus.setOnClickListener {
            managementCart.plusItem(listIteamSelected,position,object :ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }

            })
        }

        holder.binding.minus.setOnClickListener {
            managementCart.minusItem(listIteamSelected,position,object :ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }

            })
        }
    }

    override fun getItemCount(): Int =listIteamSelected.size
}