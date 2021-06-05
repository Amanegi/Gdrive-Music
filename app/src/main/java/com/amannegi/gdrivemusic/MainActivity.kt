package com.amannegi.gdrivemusic

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.amannegi.gdrivemusic.helper.DriveHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.api.services.drive.model.FileList
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drive = DriveHelper.getDriveService(this)


        thread {
            // Print the names and IDs for up to 10 files.
            val result: FileList = drive.files().list()
                .setQ("mimeType = 'application/vnd.google-apps.folder'")
                .setFields("files(name)")
                .execute()
            val files = result.files
            if (files == null || files.isEmpty()) {
                println("Logg Result : No files found.")
            } else {
                val res = StringBuffer("Logg Result Files:\n")
                for (file in files) {
                    res.append(file.name + "  " + file.id + "\n")
                }
                println(res.toString())
            }

        }


    }

    private fun signOutFromApp() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(DriveHelper.APP_DRIVE_SCOPE))
                .requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // sign out user
        mGoogleSignInClient.signOut()
        // disconnect user's drive from app
        mGoogleSignInClient.revokeAccess()
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
                .setMessage("Are you sure you want to exit?")
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