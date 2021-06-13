package com.amannegi.gdrivemusic

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.amannegi.gdrivemusic.adapter.MainRecyclerViewAdapter
import com.amannegi.gdrivemusic.databinding.ActivityMainBinding
import com.amannegi.gdrivemusic.helper.DriveHelper
import com.amannegi.gdrivemusic.helper.GoogleSignInHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.api.services.drive.Drive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drive: Drive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drive = DriveHelper.getDriveService(this)

        GlobalScope.launch {
            val files = DriveHelper.getSongList(drive)
            withContext(Dispatchers.Main) {
                // run this on main thread
                binding.progressBar.visibility = View.GONE
                if (files == null || files.isEmpty()) {
                    binding.textView.visibility = View.VISIBLE
                } else {
                    val itemClickListener =
                        object : MainRecyclerViewAdapter.ItemClickListener {
                            override fun onClick(view: View, position: Int) {
                                Toast.makeText(
                                    this@MainActivity,
                                    files[position].name,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    val songsAdapter = MainRecyclerViewAdapter(files, itemClickListener)
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.recyclerView.adapter = songsAdapter
                    binding.recyclerView.visibility = View.VISIBLE
                }

            }
        }

    }

    private fun signOutFromApp() {
        val mGoogleSignInClient = GoogleSignInHelper().getGoogleSignInClient(this)
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