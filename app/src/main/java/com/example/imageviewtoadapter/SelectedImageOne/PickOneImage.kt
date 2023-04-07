package com.example.imageviewtoadapter.SelectedImageOne

import android.content.Intent
import com.base.view.BaseActivity
import com.example.getimagetoadapter.viewmodel.MainViewModel
import com.example.imageviewtoadapter.databinding.PickoneimageBinding

class PickOneImage:BaseActivity<PickoneimageBinding,MainViewModel>() {
    companion object
    {
        val IMAGE_REQUEST_CODE = 100
    }
    override fun binding(): PickoneimageBinding {
        return PickoneimageBinding.inflate(layoutInflater)
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        binding.apply {
            POIImage.setOnClickListener {
                PickImage()
            }
        }
    }
    fun PickImage()
    {
        var intent_ :Intent = Intent(Intent.ACTION_PICK)
        intent_.type = "image/*"
        startActivityForResult(intent_, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK)
        {
            binding.POIImage.setImageURI(data?.data)
        }
    }
}