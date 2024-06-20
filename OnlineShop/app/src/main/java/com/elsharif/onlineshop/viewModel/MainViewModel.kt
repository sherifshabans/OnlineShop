package com.elsharif.onlineshop.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elsharif.onlineshop.model.BrandModel
import com.elsharif.onlineshop.model.ItemModel
import com.elsharif.onlineshop.model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel():ViewModel() {

    private val firebaseDatabase=FirebaseDatabase.getInstance()

    private val _banners=MutableLiveData<List<SliderModel>>()
    private val _brand =MutableLiveData<MutableList<BrandModel>>()
    private val _popular =MutableLiveData<MutableList<ItemModel>>()


    val banners :LiveData<List<SliderModel>> = _banners
    val brands : LiveData<MutableList<BrandModel>> = _brand
    val popular : LiveData<MutableList<ItemModel>> = _popular



    fun loadBanners(){
        val  Ref=firebaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for(childSnapshot in snapshot.children){
                    val list =childSnapshot.getValue(SliderModel::class.java)
                    if(list!=null){
                        lists.add(list)
                    }
                }

                _banners.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun loadBrand(){
        val  Ref=firebaseDatabase.getReference("Category")
        Ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<BrandModel>()
                for(childSnapshot in snapshot.children){
                    val list =childSnapshot.getValue(BrandModel::class.java)

                    if(list !=null){
                        lists.add(list)
                    }
                }
                _brand.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun loadRecommendation(){
        val  Ref=firebaseDatabase.getReference("Items")
        Ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemModel>()
                for(childSnapshot in snapshot.children){
                    val list =childSnapshot.getValue(ItemModel::class.java)

                    if(list !=null){
                        lists.add(list)
                    }
                }
                _popular.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}