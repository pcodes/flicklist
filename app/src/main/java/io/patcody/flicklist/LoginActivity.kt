package io.patcody.flicklist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var saveLogin: CheckBox
    private lateinit var savePassword: CheckBox
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password0)
        saveLogin = findViewById(R.id.saveUsername)
        savePassword = findViewById(R.id.savePassword)
        login = findViewById(R.id.loginBtn)
        signUp = findViewById(R.id.sign_up)
        firebaseAuth = FirebaseAuth.getInstance()

        login.isEnabled = false
        savePassword.isEnabled = false

        val preferences: SharedPreferences = getSharedPreferences("flickList", Context.MODE_PRIVATE)
        username.setText(preferences.getString("SAVED_USERNAME", ""))
        password.setText(preferences.getString("SAVED_PASSWORD", ""))

        if(username.text.toString().trim().isNotEmpty() && username.text.toString().trim().isNotEmpty()){
            login.isEnabled = true
        }

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        saveLogin.setOnClickListener {
            if(saveLogin.isChecked) {
                savePassword.isEnabled = true
            }else{
                savePassword.isChecked = false
                savePassword.isEnabled = false
            }
        }

        login.setOnClickListener {
            // Save the inputted username to file
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()

            firebaseAuth
                .signInWithEmailAndPassword(inputtedUsername, inputtedPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser: FirebaseUser? = firebaseAuth.currentUser
                        val email = currentUser?.email
                        Toast.makeText(this, "Logged in as $email", Toast.LENGTH_SHORT).show()

                        // Save the inputted username to file
                        if (saveLogin.isChecked) {
                            preferences
                                .edit()
                                .putString("SAVED_USERNAME", username.text.toString())
                                .putString("SAVED_PASSWORD", "")
                                .apply()
                            if (savePassword.isChecked) {
                                preferences
                                    .edit()
                                    .putString("SAVED_PASSWORD", password.text.toString())
                                    .apply()
                            }
                        }


                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                    } else {
                        val exception = task.exception
                        Toast.makeText(this, "Registration failed: $exception", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        //if the signup button is clicked launch the signup activity
        signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(newString: CharSequence, start: Int, before: Int, count: Int) {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            val enabled: Boolean = inputtedUsername.isNotEmpty() && inputtedPassword.isNotEmpty()

            // Kotlin shorthand for login.setEnabled(enabled)
            login.isEnabled = enabled
        }
    }
}
