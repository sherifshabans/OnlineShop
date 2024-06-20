package com.elsharif.onlineshop.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.elsharif.onlineshop.Adapter.CartAdapter
import com.elsharif.onlineshop.Helper.ChangeNumberItemsListener
import com.elsharif.onlineshop.Helper.ManagmentCart

import com.elsharif.onlineshop.R
import com.elsharif.onlineshop.databinding.ActivityCartBinding

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax :Double =0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart=ManagmentCart(this )

        setVariable()
        initCartList()
        calculateCart()

    }

    private fun initCartList() {
        binding.viewCart.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.viewCart.adapter=CartAdapter(managmentCart.getListCart(),this,object :ChangeNumberItemsListener{
            override fun onChanged() {
                calculateCart()
            }

        })

        with(binding){
            emptytext.visibility =if(managmentCart.getListCart().isEmpty())View.VISIBLE else View.GONE
            scrollview2.visibility =if(managmentCart.getListCart().isEmpty())View.GONE else View.VISIBLE
        }
    }
    private fun calculateCart(){
        val percentTax=0.02
        val delivery=10.0
        tax =Math.round((managmentCart.getTotalFee()*percentTax)*100)/100.0
        val total =Math.round((managmentCart.getTotalFee()+tax+delivery)*100)/100
        val itemTotal= Math.round(managmentCart.getTotalFee()*100)/100

        with(binding){
            TotalFeeTax.text="$${itemTotal}"
            taxsum.text="$${tax}"
            deliveryTxt.text="$${delivery}"
            totalTax.text="$${total}"
        }
    }

    private fun setVariable() {
        binding.button2.setOnClickListener{ finish() }
    }
}