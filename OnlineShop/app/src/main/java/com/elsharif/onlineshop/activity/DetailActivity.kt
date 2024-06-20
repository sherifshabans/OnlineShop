package com.elsharif.onlineshop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elsharif.onlineshop.Adapter.ColorAdapter
import com.elsharif.onlineshop.Adapter.SliderAdapter
import com.elsharif.onlineshop.Adapter.SizeAdapter
import com.elsharif.onlineshop.Helper.ManagmentCart
import com.elsharif.onlineshop.databinding.ActivityDetailBinding
import com.elsharif.onlineshop.model.ItemModel
import com.elsharif.onlineshop.model.SliderModel

class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item:ItemModel
    private var numberOrder=1
    private lateinit var mangmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mangmentCart=ManagmentCart(this)

        getBundle()
        banners()
        initLists()


    }

    private fun initLists() {
        val sizeList=ArrayList<String>()
        for(size in item.size){
            sizeList.add(size.toString())
        }

        binding.sizeList.adapter=SizeAdapter(sizeList)
        binding.sizeList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        val colorList =ArrayList<String>()

        for(imageUrl in item.picUrl){
            colorList.add(imageUrl.toString())
        }

        binding.colorList.adapter=ColorAdapter(colorList)
        binding.colorList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

    }

    private fun banners() {
        val sliderItems=ArrayList<SliderModel>()
        for(imageUrl in item.picUrl){
            sliderItems.add(SliderModel(imageUrl))

        }
        binding.slider.adapter=SliderAdapter(sliderItems,binding.slider)
        binding.slider.clipToPadding=true
        binding.slider.clipChildren=true
        binding.slider.offscreenPageLimit=1
        if(sliderItems.size>1){
            binding.dotsindicator.visibility= View.VISIBLE
            binding.dotsindicator.attachTo(binding.slider)
        }


    }

    private fun getBundle() {

        item=intent.getParcelableExtra("object")!!

        binding.titleText.text=item.title
        binding.descriptiontext.text=item.description
        binding.pricetext.text="$"+item.price
        binding.ratingText.text= "${item.rating} Rating"
        binding.addToCartBtn.setOnClickListener{
            item.numberInCart=numberOrder
            mangmentCart.insertFood(item)

        }
        binding.backbtn.setOnClickListener { finish() }
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@DetailActivity,CartActivity::class.java))
        }

    }
}