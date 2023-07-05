package com.example.instagramreelspoc

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import fi.finwe.orion360.sdk.pro.OrionContext
import fi.finwe.orion360.sdk.pro.licensing.LicenseManager
import fi.finwe.orion360.sdk.pro.licensing.LicenseStatus


/**
 * beneficial links  *
 *
 * portrait videos : https://pixabay.com/videos/search/vertical/
 * article : https://medium.com/devcnairobi/create-swipeable-videos-like-tiktok-57b6728082f8
 * orion github : https://github.com/FinweLtd/orion360-sdk-pro-hello-android
 * orion website : https://store.make360app.com
 * */

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private var mOrionContext: OrionContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mOrionContext = OrionContext()
        mOrionContext?.onCreate(this)
        verifyOrionLicense()

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = ReelsAdapter(ReelsFactory.getReels(), mOrionContext)
    }

    /**
     * Verify Orion360 license. This is the first thing to do after creating OrionContext.
     */
    private fun verifyOrionLicense() {
        val licenseManager = mOrionContext?.licenseManager
        val licenses = LicenseManager.findLicensesFromAssets(this)
        for (license in licenses) {
            val verifier = licenseManager?.verifyLicense(this, license)
            Log.i(
                TAG, "Orion360 license " + verifier?.licenseSource?.uri + " verified: "
                        + verifier?.licenseStatus
            )
            if (verifier?.licenseStatus == LicenseStatus.OK) break
        }
    }

    override fun onStart() {
        super.onStart()
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext?.onStart()
    }

    override fun onResume() {
        super.onResume()
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext?.onResume()
    }

    override fun onPause() {
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext?.onPause()
        super.onPause()
    }

    override fun onStop() {
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        // Propagate fragment lifecycle callbacks to the OrionContext object (singleton).
        mOrionContext?.onDestroy()
        super.onDestroy()
    }
}