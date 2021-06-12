package com.amannegi.gdrivemusic.helper

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import java.util.*


object DriveHelper {
    private lateinit var driveService: Drive
    private const val APPLICATION_NAME = "Gdrive Music"
    const val APP_DRIVE_SCOPE = DriveScopes.DRIVE_READONLY

    fun getDriveService(context: Context): Drive {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context)
        val credentials = GoogleAccountCredential.usingOAuth2(
            context,
            Collections.singleton(APP_DRIVE_SCOPE)
        )
        credentials.selectedAccount = googleSignInAccount?.account
        driveService = Drive.Builder(
            NetHttpTransport(),
            GsonFactory(),
            credentials
        )
            .setApplicationName(APPLICATION_NAME).build()

        return driveService
    }

    fun getMusicFolderId(drive: Drive): String {
        val result: FileList = drive.files().list()
            .setQ("mimeType = 'application/vnd.google-apps.folder'")
            .setQ("name = 'Music'")
            .setFields("files(id, name)")
            .execute()
        val files = result.files
        if (files == null || files.isEmpty()) {
            println("getMusicFolderId : No music folder found.")
        } else {
            val file = files[0]
            println("getMusicFolderId : " + file.id)
            return file.id
        }
        return ""
    }

    fun getSongList(drive: Drive): MutableList<File>? {
        val musicFolderId = getMusicFolderId(drive)
        val result: FileList = drive.files().list()
            .setQ("'${musicFolderId}' in parents")
            .setFields("files(id, name)")
            .execute()
        return result.files
    }


}