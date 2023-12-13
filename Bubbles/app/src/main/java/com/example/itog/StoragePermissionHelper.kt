package com.example.itog

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class StoragePermissionHelper(private val activity: Activity) {

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 101
    }

    fun requestStoragePermission() {
        val readPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        val isReadPermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            readPermission
        ) == PackageManager.PERMISSION_GRANTED

        val isWritePermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            writePermission
        ) == PackageManager.PERMISSION_GRANTED

        val permissionsToRequest = mutableListOf<String>()

        if (!isReadPermissionGranted) {
            permissionsToRequest.add(readPermission)
        }

        if (!isWritePermissionGranted) {
            permissionsToRequest.add(writePermission)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                permissionsToRequest.toTypedArray(),
                REQUEST_CODE_STORAGE_PERMISSION
            )
        } else {
            // Разрешения уже предоставлены
            // Ваш код для обработки файла или загрузки изображения
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Разрешения предоставлены
                    // Ваш код для обработки файла или загрузки изображения
                } else {
                    // Разрешения не предоставлены
                    // Ваш код для обработки отсутствия разрешения
                }
            }
        }
    }
}
