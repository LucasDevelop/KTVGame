package com.heid.games.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.FileProvider
import android.widget.Toast
import com.blankj.utilcode.util.SDCardUtils
import com.heid.games.BuildConfig
import com.heid.games.base.BaseGameActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.File

/**
 * @package     com.heid.games.utils
 * @author      lucas
 * @date        2018/8/30
 * @des  功能型工具
 */
interface FuncUtil {

    fun Int.repeat(func:(i:Int)->Unit){
        for ( i in 0 until this)
            func(i)
    }

    fun FragmentActivity.openCamera(callback: (bitmap: Bitmap, path: String) -> Unit, imageName: String = "camera_${System.currentTimeMillis()}") {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA).subscribe {
                    if (it) {//打开相机
                        val imageUri: Uri
                        val file = File(SDCardUtils.getSDCardPaths()[0], imageName+".jpg")
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION) //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            imageUri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)//通过FileProvider创建一个content类型的Uri
                        } else {
                            imageUri = Uri.fromFile(file)
                        }
                        (this as? BaseGameActivity)?.onAcResultList?.add({requestCode, resultCode, data ->
                            if (resultCode != Activity.RESULT_CANCELED) {
                                when (requestCode) {
                                    0x1011// 拍照返回
                                    -> callback(BitmapFactory.decodeFile(file.absolutePath),file.absolutePath)
                                }
                            }
                        })
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)//将拍取的照片保存到指定URI
                        startActivityForResult(intent, 0x1011)
                    } else {
                        "拒绝权限可能会导致功能无法使用！".toast(this)
                    }
                }
    }

    fun Any.toast(context: Context) {
        Toast.makeText(context, this.toString(), Toast.LENGTH_SHORT).show()
    }
}