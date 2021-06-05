package com.amannegi.gdrivemusic

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amannegi.gdrivemusic.helper.DriveHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var drive: Drive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drive = DriveHelper.getDriveService(this)


        GlobalScope.launch {
            val res = getMusicFolderId()
            withContext(Dispatchers.Main) {
                // run this on main thread
                Toast.makeText(this@MainActivity, res, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun getMusicFolderId(): String {
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
        return "";
    }

    private fun signOutFromApp() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(DriveHelper.APP_DRIVE_SCOPE))
                .requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // disconnect user's drive from app
        mGoogleSignInClient.revokeAccess().addOnSuccessListener {
            Toast.makeText(this, "Drive access revoked.", Toast.LENGTH_LONG).show()
        }
        // sign out user
        mGoogleSignInClient.signOut()
        // go to login activity
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout_menu_action) {
            // show alert dialog
            MaterialAlertDialogBuilder(this)
                .setTitle("Logout")
                .setMessage("Are sure you want to logout and revoke the drive access from Gdrive Music?")
                .setNegativeButton("No") { dialog, _ ->
                    run {
                        dialog.dismiss()
                    }
                }
                .setPositiveButton("Yes") { _, _ ->
                    run {
                        signOutFromApp()
                    }
                }
                .show()
        }
        return super.onOptionsItemSelected(item)
    }
}