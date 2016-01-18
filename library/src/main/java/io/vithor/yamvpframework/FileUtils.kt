package io.vithor.yamvpframework

import com.orhanobut.logger.Logger

import java.io.File

/**
 * Created by Hazer on 1/17/16.
 */
public object FileUtils {
    fun renameImage(imageFile: File, newName: File): File {
        val success = imageFile.renameTo(newName)

        Logger.d("Renamed: " + success)
        Logger.d("Image Picked: " + newName.name)
        return newName
    }
}