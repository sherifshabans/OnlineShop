package com.elsharif.onlineshop.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.elsharif.onlineshop.Adapter.BrandAdapter
import com.elsharif.onlineshop.Adapter.PopularAdapter
import com.elsharif.onlineshop.Adapter.SliderAdapter
import com.elsharif.onlineshop.databinding.ActivityMainBinding
import com.elsharif.onlineshop.model.SliderModel
import com.elsharif.onlineshop.viewModel.MainViewModel

class MainActivity : BaseActivity() {

    private val viewModel=MainViewModel()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initBanners()
        initBrand()
        initPopular()
        initMenu()

    }

    private fun initMenu() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity,CartActivity::class.java))
        }
    }

    private fun initBanners() {
        binding.progressBar.visibility= View.VISIBLE
        viewModel.banners.observe(this, Observer { items->
            banners(items)
            binding.progressBar.visibility=View.GONE

        })

        viewModel.loadBanners()

    }

    private fun banners(images:List<SliderModel>){
        binding.viewPagerSlider.adapter= SliderAdapter(images,binding.viewPagerSlider)
        binding.viewPagerSlider.clipToPadding=false
        binding.viewPagerSlider.clipChildren=false
        binding.viewPagerSlider.offscreenPageLimit=3
        binding.viewPagerSlider.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositePagerTransformer=CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))

        }
        binding.viewPagerSlider.setPageTransformer(compositePagerTransformer)

        if(images.size>1){

            binding.dotsindicator.visibility=View.VISIBLE
            binding.dotsindicator.attachTo(binding.viewPagerSlider)
        }


    }


    private fun initBrand(){
        binding.progressBarBrand.visibility= View.VISIBLE
        viewModel.brands.observe(this, Observer {

            binding.viewBrand.layoutManager=
                LinearLayoutManager(this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,false)
            binding.viewBrand.adapter=BrandAdapter(it)
            binding.progressBarBrand.visibility=View.GONE

        })

        viewModel.loadBrand()

    }
    private fun initPopular(){
        binding.progressBarPopular.visibility= View.VISIBLE
        viewModel.popular.observe(this, Observer {

            binding.viewPopular.layoutManager=GridLayoutManager(this@MainActivity,2)
            binding.viewPopular.adapter=PopularAdapter(it)
            binding.progressBarPopular.visibility=View.GONE



        })

        viewModel.loadRecommendation()

    }

}