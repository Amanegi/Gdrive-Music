package com.amannegi.gdrivemusic.helper

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope

class GoogleSignInHelper {

    fun getGoogleSignInClient(context: Context): GoogleSignInClient{
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(DriveHelper.APP_DRIVE_SCOPE))
                .requestEmail().build()
        return GoogleSignIn.getClient(context, gso)
    }

}