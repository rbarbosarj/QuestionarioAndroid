package com.example.questionarioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        val perfil_usuario     = intent.getStringExtra("PERFIL_DE_USUARIO")
        val nome_usuario       = intent.getStringExtra("NOME_DE_USUARIO")
        val resultadoTextView  = findViewById<TextView>(R.id.TextView_resultado)
        resultadoTextView.text = getString(R.string.tela3_label, nome_usuario, perfil_usuario)

        val btnCompartilhar = findViewById<Button>(R.id.botao_compartilhar)
        btnCompartilhar.setOnClickListener {
            compartilharResultado(nome_usuario, perfil_usuario)
        }

    }

    private fun compartilharResultado(nomeUsuario: String?, perfilUsuario: String?) {

        val texto  = getString(R.string.tela3_label, nomeUsuario, perfilUsuario)
        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "text/plain"
        intent.setPackage("com.whatsapp")
        intent.putExtra(Intent.EXTRA_TEXT, texto)

        try {
            startActivity(intent)
        } catch (ex: android.content.ActivityNotFoundException) {
            val intentDefault = Intent(Intent.ACTION_SEND)
            intentDefault.type = "text/plain"
            intentDefault.putExtra(Intent.EXTRA_TEXT, texto)
            startActivity(Intent.createChooser(intentDefault, getString(R.string.tela3_botao)))
        }

    }

}
