package com.amannegi.gdrivemusic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.amannegi.gdrivemusic.databinding.ActivityLoginBinding
import com.amannegi.gdrivemusic.helper.DriveHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(DriveHelper.APP_DRIVE_SCOPE))
                .requestEmail().build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (googleSignInAccount != null) {
            openMainActivity()
        }


        binding.signInButton.setOnClickListener {
            val intent = mGoogleSignInClient.signInIntent
            onSignInResult.launch(intent)
        }

    }

    private val onSignInResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                openMainActivity()
            }
        }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}