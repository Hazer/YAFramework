package io.vithor.yamvpframework.core

import io.vithor.yamvpframework.core.extensions.debugLog
import java.io.File

/**
 * Created by Vithorio Polten on 1/17/16.
 */
object FileUtils {
    fun renameImage(imageFile: File, newName: File): File {
        val success = imageFile.renameTo(newName)

        success.debugLog { "Renamed: " + success }
        success.debugLog { "Image Picked: " + newName.name }

        return newName
    }
}