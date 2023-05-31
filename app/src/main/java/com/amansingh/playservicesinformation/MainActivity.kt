package com.amansingh.playservicesinformation

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amansingh.playservicesinformation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val _TAG: String = this.javaClass.simpleName
    private val PLAY_SERVICES_PACKAGE_NAME: String = "com.google.android.gms"
    private val PLAY_STORE_PACKAGE_NAME: String = "com.android.vending"
    private var packageInfo: PackageInfo? = null
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        try {
            packageInfo = packageManager.getPackageInfo(PLAY_SERVICES_PACKAGE_NAME, 0)
        } catch (exception: PackageManager.NameNotFoundException) {
            Log.e(_TAG, "Package $PLAY_SERVICES_PACKAGE_NAME not found")
        }

        setDataOnView(packageInfo)

        binding.tvOpenApkMirror.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.apkmirror.com/apk/google-inc/google-play-services/")
                )
            )
        }

        binding.tvOpenPlayStore.setOnClickListener {
            var url: String?
            try {
                packageManager.getPackageInfo(PLAY_STORE_PACKAGE_NAME, 0)
                url = "market://details?id=$PLAY_SERVICES_PACKAGE_NAME"
            } catch (exception: PackageManager.NameNotFoundException) {
                Log.e(_TAG, "Package $PLAY_STORE_PACKAGE_NAME not found")
                url = "https://play.google.com/store/apps/details?id=$PLAY_SERVICES_PACKAGE_NAME"
            }
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        binding.tvOpenAppInfo.setOnClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", PLAY_SERVICES_PACKAGE_NAME, null)
                )
            )
        }

        binding.tvAbout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("Play Services Info (${BuildConfig.VERSION_NAME})\n\nby Amanjot Singh\namansingh63@xda.com")
                .setPositiveButton(
                    "Close"
                ) { _, _ -> }
                .create()
                .show()
        }

    }

    private fun setDataOnView(packageInfo: PackageInfo?) {

        if (applicationInfo != null && packageInfo != null) {
            binding.ivIcon.setImageResource(R.mipmap.ic_play_services)
            binding.tvInstallStatus.text = getString(R.string.text_play_services_installed)
            binding.tvPlayServicesVersion.text = "Version Installed : ${packageInfo.versionName}"
            binding.tvPlayServicesVarient.text =
                "Variant : ${packageInfo.versionName?.substring(10, 16)}"
        } else {
            binding.ivIcon.setImageResource(R.mipmap.ic_play_services_greysclae)
            binding.tvInstallStatus.text = getString(R.string.text_play_services_installed)
            binding.tvPlayServicesVersion.visibility = View.GONE
            binding.tvPlayServicesVarient.visibility = View.GONE
            binding.divider1.visibility = View.GONE
            binding.tvOpenAppInfo.visibility = View.GONE

        }
    }

}
