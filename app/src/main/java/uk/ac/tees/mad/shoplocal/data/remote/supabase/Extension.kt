package uk.ac.tees.mad.careerconnect.data.remote

import android.content.Context
import android.net.Uri
import okio.IOException

@Throws(IOException::class)
fun Uri.uriToByteArray (context: Context) = context .contentResolver.openInputStream(this)?.use {
    it.buffered().readBytes()
}