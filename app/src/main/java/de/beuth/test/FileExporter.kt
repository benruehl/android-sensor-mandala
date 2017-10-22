package de.beuth.test

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/**
 * Created by Benjamin Rühl on 22.10.2017.
 */
class FileExporter(private val pathName: String, private val fileName: String) {

    private val exportFile: File by lazy { File(pathName, fileName) }

    val requiredPermission: String = Manifest.permission.WRITE_EXTERNAL_STORAGE

    fun checkStorageWritingPermission(context: Context) : Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    fun appendToFile(value: String) {
        val exportDirectory: File? = exportFile.parentFile

        if (exportDirectory != null && !exportDirectory.exists())
            exportDirectory.mkdirs()

        if (!exportFile.exists())
            exportFile.createNewFile()

        if (exportFile.exists()) {
            val fos = FileOutputStream(exportFile, true)
            val fow = OutputStreamWriter(fos)

            fow.append(value)
            fow.close()

            fos.flush()
            fos.close()
        }
    }
}