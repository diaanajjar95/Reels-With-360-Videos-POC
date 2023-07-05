package com.example.instagramreelspoc

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.finwe.math.Vec2f
import fi.finwe.orion360.sdk.pro.OrionContext
import fi.finwe.orion360.sdk.pro.OrionScene
import fi.finwe.orion360.sdk.pro.OrionViewport
import fi.finwe.orion360.sdk.pro.controllable.DisplayClickable
import fi.finwe.orion360.sdk.pro.controller.TouchDisplayClickListener
import fi.finwe.orion360.sdk.pro.item.OrionCamera
import fi.finwe.orion360.sdk.pro.item.OrionPanorama
import fi.finwe.orion360.sdk.pro.source.OrionTexture
import fi.finwe.orion360.sdk.pro.view.OrionView
import fi.finwe.orion360.sdk.pro.widget.SimpleTouchController

private const val TAG = "ReelViewHolder"

class ReelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mOrionView: OrionView? = itemView.findViewById(R.id.orion_view)

    private var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
    private var txtDesc: TextView = itemView.findViewById(R.id.txtDesc)

    /** The 3D scene where our panorama sphere will be added to.  */
    private var mScene: OrionScene? = OrionScene()

    /** The panorama sphere where our video texture will be mapped to.  */
    private var mPanorama: OrionPanorama? = OrionPanorama()

    /** The video texture where our decoded video frames will be updated to.  */
    private var mPanoramaTexture: OrionTexture? = null

    /** The camera which will project our 3D scene to a 2D (view) surface.  */
    private var mCamera: OrionCamera? = OrionCamera()

    private var mTouchController: SimpleTouchController? = null

    private var mOrionContext: OrionContext? = null

    fun bind(reel: ReelItem, orionContext: OrionContext?) {
        mOrionContext = orionContext
        txtTitle.text = reel.title
        txtDesc.text = reel.desc
        play("https://s3.amazonaws.com/orion360-us/Orion360_test_video_2d_equi_360x180deg_1920x960pix_30fps_30sec_x264.mp4")
    }

    private fun play(contentUri: String?) {
//        val orionContext = OrionContext.getDefaultOrionContext()
        // For compatibility with Google Cardboard 1.0 with magnetic switch, disable magnetometer
        // from sensor fusion. Also recommended for devices with poorly calibrated magnetometer.
        mOrionContext?.sensorFusion?.setMagnetometerEnabled(false)

        // Create a new scene. This represents a 3D world where various objects can be placed.
        mScene = OrionScene()

        // Bind sensor fusion as a controller. This will make it available for scene objects.
        mScene?.bindController(mOrionContext?.sensorFusion)

        // Create a new panorama. This is a 3D object that will represent a spherical video/image.
        mPanorama = OrionPanorama()

        mScene?.bindSceneItem(mPanorama)

        // Create a new video (or image) texture from a video (or image) source URI.
        mPanoramaTexture = OrionTexture.createTextureFromURI(
            itemView.context,
            contentUri
        )

        // Bind the panorama texture to the panorama object. Here we assume full spherical
        // equirectangular monoscopic source, and wrap the complete texture around the sphere.
        // If you have stereoscopic content or doughnut shape video, use other method variants.
        mPanorama?.bindTextureFull(0, mPanoramaTexture)

        // Bind the panorama to the scene. This will make it part of our 3D world.
        mScene?.bindSceneItem(mPanorama)

        // Create a new camera. This will become the end-user's eyes into the 3D world.
        mCamera = OrionCamera()

        // Define maximum limit for zooming. As an example, at value 1.0 (100%) zooming in is
        // disabled. At 3.0 (300%) camera will never reduce the FOV below 1/3 of the base FOV.
        mCamera?.zoomMax = 3.0f

        // Set yaw angle to 0. Now the camera will always point to the same yaw angle
        // (to the horizontal center point of the equirectangular video/image source)
        // when starting the app, regardless of the orientation of the device.
        mCamera?.setRotationYaw(0f)

        // Bind camera as a controllable to sensor fusion. This will let sensors rotate the camera.
        mOrionContext?.sensorFusion?.bindControllable(mCamera)

        // Bind the scene to the view. This is the 3D world that we will be rendering to this view.
        mOrionView?.bindDefaultScene(mScene)

        // Bind the camera to the view. We will look into the 3D world through this camera.
        mOrionView?.bindDefaultCamera(mCamera)

        mTouchController = SimpleTouchController(mOrionContext, mCamera)
        mScene?.bindWidget(mTouchController)

        // The view can be divided into one or more viewports. For example, in VR mode we have one
        // viewport per eye. Here we fill the complete view with one (landscape) viewport.
        mOrionView?.bindViewports(
            OrionViewport.VIEWPORT_CONFIG_FULL,
            OrionViewport.CoordinateType.FIXED_LANDSCAPE
        )

        val listener = TouchDisplayClickListener()
        listener.bindClickable(null, object : TouchDisplayClickListener.Listener {

            override fun onDisplayClick(clickable: DisplayClickable?, displayCoords: Vec2f) {
                Log.d(TAG, "onDisplayClick: ")
            }

            override fun onDisplayDoubleClick(clickable: DisplayClickable?, displayCoords: Vec2f) {
                Log.d(TAG, "onDisplayDoubleClick: ")
            }

            override fun onDisplayLongClick(clickable: DisplayClickable?, displayCoords: Vec2f) {
                Log.d(TAG, "onDisplayLongClick: ")
            }
        })
        mScene?.bindController(listener)
    }

    fun pause() {
        mPanoramaTexture?.pause()
    }

}