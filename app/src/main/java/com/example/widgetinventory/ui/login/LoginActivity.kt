package com.example.widgetinventory.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.widgetinventory.MainActivity
import com.example.widgetinventory.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var errorPassword: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inicializar vistas
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        emailLayout = findViewById(R.id.emailLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        errorPassword = findViewById(R.id.errorPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        // Inactivar botones al inicio
        btnLogin.isEnabled = false
        btnRegister.isEnabled = false

        // Validar Email y Password
        inputEmail.addTextChangedListener(formWatcher)
        inputPassword.addTextChangedListener(formWatcher)

        // Acción de Login
        btnLogin.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            loginUser(email, password)
        }

        // Acción de Registrarse
        btnRegister.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            registerUser(email, password)
        }
    }

    override fun onStart() {
        super.onStart()
        // Criterio 1: Verificar si el usuario ya ha iniciado sesión
        if (auth.currentUser != null) {
            goToMainActivity()
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login exitoso
                    goToMainActivity()
                } else {
                    // Si el login falla, mostrar un mensaje.
                    Toast.makeText(
                        baseContext, "Error de autenticación: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso
                    goToMainActivity()
                } else {
                    // Si el registro falla, mostrar un mensaje.
                    Toast.makeText(
                        baseContext, "Error de registro: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        // Limpiar la pila de actividades para que no pueda volver a LoginActivity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Watcher para validar email y password en tiempo real
    private val formWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validateForm()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validateForm() {
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()

        // Validación Password 6-10 dígitos solo números
        if (password.length in 1..5) {
            errorPassword.text = "Mínimo 6 dígitos"
            errorPassword.visibility = TextView.VISIBLE
            passwordLayout.boxStrokeColor = getColor(android.R.color.holo_red_dark)
        } else {
            errorPassword.visibility = TextView.GONE
            passwordLayout.boxStrokeColor = getColor(android.R.color.white)
        }

        // Activar Login solo si ambas entradas son válidas
        val validForm = email.isNotEmpty() && password.length >= 6
        btnLogin.isEnabled = validForm

        // Activar Registro con ambos campos llenos
        btnRegister.isEnabled = email.isNotEmpty() && password.isNotEmpty()
    }
}