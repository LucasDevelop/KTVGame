package com.heid.games.widget

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.Camera
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SDCardUtils
import com.heid.games.model.undercover.ConfirmPersonActivity
import com.heid.games.model.undercover.bean.PersonInfoBean
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_setup.*
import kotlin.concurrent.thread

/**
 * @package     com.heid.games.widget
 * @author      lucas
 * @date        2018/9/3
 * @des
 */
class CameraView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
//        holder.removeCallback(this)
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        RxPermissions(context as FragmentActivity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe {
                    if (it) {
                        //打开前置摄像头
                        for (i in 0 until android.hardware.Camera.getNumberOfCameras()) {
                            val cameraInfo = android.hardware.Camera.CameraInfo()
                            android.hardware.Camera.getCameraInfo(i, cameraInfo)
                            if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                try {
                                    mCamera = android.hardware.Camera.open(i)
                                } catch (e: Exception) {
                                    "相机打开失败".showToast()
                                }
                                mCamera?.startFaceDetection()
                                mCamera?.setDisplayOrientation(90)
                                break
                            }
                        }
                        try {
                            with(mCamera!!) {
                                setPreviewDisplay(holder)
                                startPreview()
                                mCamera!!.parameters.let {
                                    it.pictureFormat = PixelFormat.RGBA_8888
                                    it.setPreviewSize(measuredWidth, measuredHeight)
                                    mCamera!!.parameters = it
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        "拒绝相机权限会导致头像无法显示".showToast()
                    }
                }
    }

    var mCamera: Camera? = null

    init {
        holder.addCallback(this)
    }

    fun saveImg(onSaveBefore: () -> Unit, onSave: (path: String, isSuccess: Boolean) -> Unit) {
        onSaveBefore()
        mCamera?.parameters.let {
            //设置保存路径
            mCamera?.takePicture(null, null, { data, c ->
                mCamera?.stopPreview()
                try {
                    val bitmap = ImageUtils.bytes2Bitmap(data)
                    val path = SDCardUtils.getSDCardPaths()[0] + "/" + System.currentTimeMillis() + ".jpg"
                    thread {
                        val isSave = ImageUtils.save(bitmap, path, Bitmap.CompressFormat.JPEG, true)
                        handler.post { onSave(path, isSave) }
                        if (isSave) Log.d("lucas", "头像保存成功 $path")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    onSave("", false)
                }
            })
        }
    }

    fun String.showToast() {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}