package com.heid.games.model.undercover

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SDCardUtils
import com.heid.games.R
import com.heid.games.base.BaseGameActivity
import com.heid.games.config.GameConfig
import com.heid.games.model.undercover.bean.PersonInfoBean
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_setup.*
import kotlinx.android.synthetic.main.view_under_setup_right.*
import java.util.*
import kotlin.concurrent.thread

/**
 * @package     com.heid.games.model.undercover
 * @author      lucas
 * @date        2018/8/30
 * @version     V1.0
 * @describe    确认身份
 */
class SetupActivity : BaseGameActivity() {
    override fun getContentLayoutId(): Int = R.layout.activity_setup
    override fun getRightView(): Int = R.layout.view_under_setup_right
    var index = 0
    //成员身份
    val persons = ArrayList<PersonInfoBean>()
    var mCamera: Camera? = null
    var personCount: Int = 0
    var isSaveing = false
    lateinit var identitys: Array<String>
    val startTexts = arrayOf(
//            R.mipmap.ic_under_start1,
            R.mipmap.ic_under_start2, R.mipmap.ic_under_start3,
            R.mipmap.ic_under_start4, R.mipmap.ic_under_start5, R.mipmap.ic_under_start6,
            R.mipmap.ic_under_start7, R.mipmap.ic_under_start8, R.mipmap.ic_under_start9,
            R.mipmap.ic_under_start10, R.mipmap.ic_under_start11, R.mipmap.ic_under_start12)

    companion object {
        fun launch(activity: BaseGameActivity,person_count:Int){
            val intent = Intent(activity, SetupActivity::class.java)
            intent.putExtra("person_count",person_count)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBg(R.mipmap.ic_under_setup_bg)
        personCount = intent.getIntExtra("person_count", 0)
        val underCount = GameConfig.underPersonTable.getValue(personCount)
        v_join_count.text = personCount.toString()
        v_under_count.text = underCount.toString()
        identitys = initIdentity(personCount, underCount)
        v_start_bt.setOnClickListener {
            if (isSaveing) {
                "正在保存图片".toast(this)
                return@setOnClickListener
            }
            if (index++ % 2 == 0) {
                v_start_text.setImageResource(R.mipmap.ic_under_remanber)
                v_show.visibility = View.GONE
                v_identity.visibility = View.VISIBLE
                v_words.text = identitys[index / 2]
                mCamera?.startPreview()
            } else {
                v_start_text.setImageResource(startTexts[index / 2 - 1])
                //保存照片
                saveImg()
                v_show.visibility = View.VISIBLE
                v_identity.visibility = View.GONE
            }
        }
        //相机
        with(v_camera.holder)
        {
            //surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
            addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, w: Int, h: Int) {
                }

                override fun surfaceDestroyed(p0: SurfaceHolder?) {
                    with(mCamera!!) {
                        stopPreview()
                        release()
                    }
                    v_camera.holder.removeCallback(this)
                }

                override fun surfaceCreated(p0: SurfaceHolder?) {
                    //获取权限
                    initCamera()
                }
            })
        }
    }

    lateinit var underKey: String

    //创建身份
    private fun initIdentity(personCount: Int, underCount: Int): Array<String> {
        val array = resources.getStringArray(R.array.undercoverword)
        val random = Random()
        val split = array[random.nextInt(array.size - 1)].split("_")
        //更具卧底数随机排布
        underKey = split[random.nextInt(1) + 1]
        val commonKey = if (split[1] == underKey) split[2] else split[1]
        val identity = Array(personCount, { commonKey })
        var addUnder = 0
        while (underCount != addUnder) {
            addUnder = 0
            identity[random.nextInt(identity.size - 1)] = underKey
            identity.forEach {
                if (it == underKey)
                    addUnder++
            }
        }
        return identity
    }

    private fun saveImg() {
        mCamera?.parameters.let {
            //设置保存路径
            isSaveing = true
            v_progress.visibility = View.VISIBLE
            v_start_text.visibility = View.GONE
            mCamera?.takePicture(null, null, { data, c ->
                mCamera?.stopPreview()
                try {
                    val bitmap = ImageUtils.bytes2Bitmap(data)
                    val path = SDCardUtils.getSDCardPaths()[0] + "/" + System.currentTimeMillis() + ".jpg"
                    thread {
                        val save = ImageUtils.save(bitmap, path, Bitmap.CompressFormat.JPEG, true)
                        val identity = identitys[index / 2 - 1]
                        persons.add(PersonInfoBean(path, identity, identity == underKey,persons.size+1))
                        if (save) Log.d("lucas", "头像保存成功 $path")
                        if (personCount == persons.size) {
                            //身份确认完毕
                            finish()
                            val intent = Intent(this, ConfirmPersonActivity::class.java)
                            intent.putExtra("persons", persons)
                            startActivity(intent)
                        } else {
                            runOnUiThread {
                                v_progress.visibility = View.GONE
                                v_start_text.visibility = View.VISIBLE
                            }
                        }
                        isSaveing = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    isSaveing = false
                }
            })
        }
    }

    private fun initCamera() {
        RxPermissions(this@SetupActivity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe {
                    if (it) {
                        //打开前置摄像头
                        for (i in 0 until Camera.getNumberOfCameras()) {
                            val cameraInfo = Camera.CameraInfo()
                            Camera.getCameraInfo(i, cameraInfo)
                            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                try {
                                    mCamera = Camera.open(i)
                                } catch (e: Exception) {
                                    "相机打开失败".toast(this)
                                }
                                mCamera?.startFaceDetection()
                                mCamera?.setDisplayOrientation(90)
                                break
                            }
                        }
                        try {
                            with(mCamera!!) {
                                setPreviewDisplay(v_camera.holder)
                                startPreview()
                                mCamera!!.parameters.let {
                                    it.pictureFormat = PixelFormat.RGBA_8888
                                    it.setPreviewSize(400, 400)
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

}
