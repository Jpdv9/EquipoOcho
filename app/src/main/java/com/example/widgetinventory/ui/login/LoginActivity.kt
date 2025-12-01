package com.example.widgetinventory.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.widgetinventory.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargar ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarEventos()
    }

    private fun configurarEventos() {

        // Validación en tiempo real del password
        binding.inputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validarPassword()
                validarFormulario()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Validación del email mientras escribe
        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validarFormulario()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Botón LOGIN
        binding.btnLogin.setOnClickListener {
            Toast.makeText(this, "Login presionado", Toast.LENGTH_SHORT).show()
        }

        // Botón REGISTRO
        binding.btnRegister.setOnClickListener {
            Toast.makeText(this, "Registrar presionado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validarPassword() {
        val password = binding.inputPassword.text.toString()

        if (password.length < 6) {
            binding.errorPassword.text = "Mínimo 6 dígitos"
            binding.errorPassword.visibility = android.view.View.VISIBLE
            binding.passwordLayout.boxStrokeColor = 0xFFFF4444.toInt() // rojo
        } else {
            binding.errorPassword.visibility = android.view.View.GONE
            binding.passwordLayout.boxStrokeColor = 0xFFFFFFFF.toInt() // blanco
        }
    }

    private fun validarFormulario() {
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()

        // Campos llenos y contraseña válida
        val esValido = email.isNotEmpty() && password.length >= 6

        binding.btnLogin.isEnabled = esValido
        binding.btnRegister.isEnabled = esValido
    }
}
