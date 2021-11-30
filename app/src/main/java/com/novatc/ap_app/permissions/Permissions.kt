package com.novatc.ap_app.permissions

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class Permissions: AppCompatActivity() {

    private fun hasReadExternalStoragePermission(permissions: Fragment) =
        ActivityCompat.checkSelfPermission(permissions.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    public fun checkPermissions(fragment: Fragment){
        var permissionsToRequest = mutableListOf<String>()
        if (!hasReadExternalStoragePermission(fragment)){
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(fragment.requireActivity(), permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty()){
            for (i in grantResults.indices){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("PermissionRequest", "${permissions[i]} granted")
                }
            }
        }
    }
}