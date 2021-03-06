package com.amannegi.gdrivemusic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.amannegi.gdrivemusic.databinding.ActivityLoginBinding
import com.amannegi.gdrivemusic.helper.DriveHelper
import com.amannegi.gdrivemusic.helper.GoogleSignInHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (googleSignInAccount != null) {
            openMainActivity()
        }

        binding.signInButton.setOnClickListener {
            val mGoogleSignInClient = GoogleSignInHelper().getGoogleSignInClient(this)
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