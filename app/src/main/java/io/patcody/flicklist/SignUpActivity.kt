package io.patcody.flicklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password1: EditText
    private lateinit var password2: EditText
    private lateinit var signUp: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        username = findViewById(R.id.username)
        password1 = findViewById(R.id.password1)
        password2 = findViewById(R.id.password2)
        signUp = findViewById(R.id.signUpBtn)
        firebaseAuth = FirebaseAuth.getInstance()

        signUp.isEnabled = false

        username.addTextChangedListener(textWatcher)
        password1.addTextChangedListener(textWatcher)
        password2.addTextChangedListener(textWatcher)

        signUp.setOnClickListener {
            val inputtedUsername: String = username.text.toString().trim()
            if (password1.text.toString() == password2.text.toString()){
                val inputtedPassword: String = password1.text.toString().trim()
                firebaseAuth
                    .createUserWithEmailAndPassword(inputtedUsername, inputtedPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val currentUser: FirebaseUser? = firebaseAuth.currentUser
                            val email = currentUser?.email
                            Toast.makeText(this, "Registered as $email", Toast.LENGTH_SHORT).show()
                        } else {
                            val exception = task.exception
                            Toast.makeText(this, "Registration failed: $exception", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(newString: CharSequence, start: Int, before: Int, count: Int) {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword1: String = password1.text.toString().trim()
            val inputtedPassword2: String = password2.text.toString().trim()
            val enabled: Boolean = inputtedUsername.isNotEmpty() && inputtedPassword1.isNotEmpty() && inputtedPassword2.isNotEmpty()

            // Kotlin shorthand for login.setEnabled(enabled)
            signUp.isEnabled = enabled
        }
    }
}
