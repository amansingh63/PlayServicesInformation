package com.amansingh.playservicesinformation

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG: String = this.javaClass.simpleName
    val PLAY_SERVICES_PACKAGE_NAME: String = "com.google.android.gms"
    val PLAY_STORE_PACKAGE_NAME: String = "com.android.vending"
    var packageInfo: PackageInfo? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            packageInfo = packageManager.getPackageInfo(PLAY_SERVICES_PACKAGE_NAME, 0)
        } catch (exeption: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Package $PLAY_SERVICES_PACKAGE_NAME not found")
        }

        setDataOnView(packageInfo)

        tvOpenApkMirror.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apkmirror.com/apk/google-inc/google-play-services/")))
        }

        tvOpenPlayStore.setOnClickListener {
            var url: String?
            try {
                packageManager.getPackageInfo(PLAY_STORE_PACKAGE_NAME, 0)
                url = "market://details?id=$PLAY_SERVICES_PACKAGE_NAME"
            } catch (execption: PackageManager.NameNotFoundException) {
                Log.e(TAG, "Package $PLAY_STORE_PACKAGE_NAME not found")
                url = "https://play.google.com/store/apps/details?id=$PLAY_SERVICES_PACKAGE_NAME"
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        tvOpenAppInfo.setOnClickListener {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", PLAY_SERVICES_PACKAGE_NAME, null)))
        }

        tvAbout.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("Play Services Info (${BuildConfig.VERSION_NAME})\n\nby Amanjot Singh\namansingh63@xda.com")
                    .setPositiveButton("Close", DialogInterface.OnClickListener { dialogInterface, i -> })
                    .create()
                    .show()
        }

    }

    private fun setDataOnView(packageInfo: PackageInfo?) {

        if (applicationInfo != null && packageInfo != null) {
            ivIcon.setImageResource(R.mipmap.ic_play_services)
            tvInstallStatus.text = getString(R.string.text_play_services_installed)
            tvPlayServicesVersion.text = "Version Installed : ${packageInfo?.versionName}"
            tvPlayServicesVarient.text = "Variant : ${packageInfo?.versionName.substring(9, 12)}"
        } else {
            ivIcon.setImageResource(R.mipmap.ic_play_services_greysclae)
            tvInstallStatus.text = getString(R.string.text_play_services_installed)
            tvPlayServicesVersion.visibility = View.GONE
            tvPlayServicesVarient.visibility = View.GONE
            divider1.visibility = View.GONE
            tvOpenAppInfo.visibility = View.GONE

        }
    }

}
