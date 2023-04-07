package com.example.imageviewtoadapter

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.base.view.BaseActivity
import com.example.getimagetoadapter.viewmodel.MainViewModel
import com.example.imageviewtoadapter.SelectedImageOne.PickOneImage
import com.example.imageviewtoadapter.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    var PICK_IMAGE : Int = 1
    lateinit var Images:ArrayList<Uri>
    lateinit var ImageUri: Uri
    override fun binding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)

    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        startActivity(Intent(this@MainActivity,PickOneImage::class.java))
        binding.apply {
            imageMain.setOnClickListener {
                Images = ArrayList<Uri>()
                var intent_: Intent = Intent(Intent.ACTION_GET_CONTENT)
                intent_.type = "image/*"
                intent_.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
                startActivityForResult(intent_,PICK_IMAGE)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE)
        {
            if (resultCode == RESULT_OK)
            {
                if (data?.clipData != null)
                {
                    var countClipData:Int = data.clipData!!.itemCount
                    var currentImageSelect = 0
                    while (currentImageSelect < countClipData)
                    {
                        ImageUri = data.clipData!!.getItemAt(currentImageSelect).uri
                        Images.add(ImageUri)
                        currentImageSelect = currentImageSelect+1

                    }
                    binding.apply {
                        imageMain.setImageURI(Images.get(0))
                    }

                }
            }else
            {
                Toast.makeText(this@MainActivity,"Chon ....", Toast.LENGTH_LONG).show()
            }

        }
    }


}