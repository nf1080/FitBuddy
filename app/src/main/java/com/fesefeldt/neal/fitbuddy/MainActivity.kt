package com.fesefeldt.neal.fitbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {
    lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        const val RC_SIGN_IN = 1234
        const val TAG = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Google sign in objects
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, signInOptions)

        val signOnButton = findViewById<SignInButton>(R.id.sign_in_button)
        signOnButton.setSize(SignInButton.SIZE_STANDARD)
        signOnButton.setOnClickListener {
            when(it.id) {
                R.id.sign_in_button -> signIn()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // check for existing sign in
        val userAccount = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(userAccount)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // get user data from sign in
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun updateUI(userAccount: GoogleSignInAccount?) {
        if (userAccount == null) {
            // TODO show sign in button and prompt
        } else {
            Toast.makeText(this, "Users name is: " + userAccount.displayName, Toast.LENGTH_LONG).show()
            println("Users name is: " + userAccount.displayName)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            updateUI(account)

        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code = " + e.statusCode)
            updateUI(null)
        }
    }
}
