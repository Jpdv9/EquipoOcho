package com.example.widgetinventory.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.widget.ImageView
import android.widget.Toast
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar el ejecutor
        executor = ContextCompat.getMainExecutor(this)

        // Configurar el diálogo de autenticación biométrica
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    // Aquí podrías abrir tu MainActivity:
                    // startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    // finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            })

        // Configurar el contenido del cuadro emergente
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación con Biometría")
            .setSubtitle("Ingrese su huella digital")
            .setNegativeButtonText("Cancelar")
            .build()

        // Vincular la imagen de huella y activar la autenticación al presionarla
        val fingerprintImage = findViewById<ImageView>(R.id.fingerprintAnimation)
        fingerprintImage.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}
