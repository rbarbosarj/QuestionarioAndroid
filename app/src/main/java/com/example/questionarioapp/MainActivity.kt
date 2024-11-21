package com.example.questionarioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val label: TextView = findViewById(R.id.bem_vindo)
        val texto: EditText = findViewById(R.id.EditText_nome_usuario)
        val botao: Button   = findViewById(R.id.botao_iniciar_questionario)

        label.text = getString(R.string.tela1_label)
        texto.hint = getString(R.string.tela1_campo)
        botao.text = getString(R.string.tela1_botao)

        botao.setOnClickListener {
            val nome_usuario = texto.text.toString()
            val intent = Intent(this, QuestionarioActivity::class.java)
            intent.putExtra("NOME_DE_USUARIO", nome_usuario)
            startActivity(intent)
        }

    }
}
