package com.example.questionarioapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class QuestionarioActivity : AppCompatActivity() {

    private lateinit var nomeUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_questionario)

        nomeUsuario = intent.getStringExtra("NOME_DE_USUARIO") ?: ""
        if (nomeUsuario.isBlank()) nomeUsuario = "USUÁRIO"
        val saudacaoUsuario = getString(R.string.tela2_label, nomeUsuario)

        findViewById<TextView>(R.id.nome_de_usuario).text = saudacaoUsuario
        findViewById<Button>(R.id.botao_enviar).setOnClickListener { avaliarPerfil() }
    }

    private fun avaliarPerfil() {
        if (validarRespostas()) {
            val respostas = resgatarRespostas()
            val perfil    = calcularPerfil(respostas)
            exibirResultado(perfil)
        }
    }

    private fun exibirResultado(perfil: String) {
        val intent = Intent(this, ResultadoActivity::class.java)
        intent.putExtra("PERFIL_DE_USUARIO", perfil)
        intent.putExtra("NOME_DE_USUARIO", nomeUsuario)
        startActivity(intent)
    }

    private fun validarRespostas(): Boolean {

        val editTextQ1Campo1 = findViewById<EditText>(R.id.q1_campo1)
        val editTextQ1Campo2 = findViewById<EditText>(R.id.q1_campo2)
        val editTextQ2Campo  = findViewById<EditText>(R.id.q2_campo)
        val checkBoxQ2       = findViewById<CheckBox>(R.id.retiro_unico)
        val radioGroupQ3     = findViewById<RadioGroup>(R.id.q3_grupo_opcoes)
        val radioGroupQ4     = findViewById<RadioGroup>(R.id.q4_grupo_opcoes)
        val radioGroupQ5     = findViewById<RadioGroup>(R.id.q5_grupo_opcoes)
        val radioGroupQ6     = findViewById<RadioGroup>(R.id.q6_grupo_opcoes)

        return when {
            editTextQ1Campo1.text.isNullOrBlank() || editTextQ1Campo2.text.isNullOrBlank() || editTextQ2Campo.text.isNullOrBlank() ||
                    radioGroupQ3.checkedRadioButtonId == -1 || radioGroupQ4.checkedRadioButtonId == -1 || radioGroupQ5.checkedRadioButtonId == -1 || radioGroupQ6.checkedRadioButtonId == -1 ||
                    (checkBoxQ2.isChecked && editTextQ2Campo.text.isNullOrBlank()) -> {
                exibirAlertaCamposVazios()
                false
            }
            else -> true
        }
    }

    private fun exibirAlertaCamposVazios() {
        AlertDialog.Builder(this)
            .setTitle("Campos obrigatórios não preenchidos")
            .setMessage("Por favor, responda a todas as questões obrigatórias.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun resgatarRespostas(): List<Any> {
        return listOf(
            findViewById<EditText>(R.id.q1_campo1).text.toString().toIntOrNull() ?: 0,
            findViewById<EditText>(R.id.q1_campo2).text.toString().toIntOrNull() ?: 0,
            findViewById<CheckBox>(R.id.retiro_unico).isChecked,
            findViewById<EditText>(R.id.q2_campo).text.toString().toIntOrNull() ?: 0,
            getSelectedRadioButtonText(R.id.q3_grupo_opcoes),
            getSelectedRadioButtonText(R.id.q4_grupo_opcoes),
            getSelectedRadioButtonText(R.id.q5_grupo_opcoes),
            getSelectedRadioButtonText(R.id.q6_grupo_opcoes),
            getSelectedCheckBoxes(R.id.q7_opcao1, R.id.q7_opcao2, R.id.q7_opcao3, R.id.q7_opcao4, R.id.q7_opcao5, R.id.q7_opcao6, R.id.q7_opcao7),
            getSelectedCheckBoxes(R.id.q8_opcao1, R.id.q8_opcao2, R.id.q8_opcao3, R.id.q8_opcao4),
            findViewById<EditText>(R.id.q9_campo).text.toString(),
            getSelectedRadioButtonText(R.id.q10_grupo_opcoes),
            getSelectedRadioButtonText(R.id.q11_grupo_opcoes)
        )
    }

    private fun getSelectedRadioButtonText(radioGroupId: Int): String {
        val radioGroup = findViewById<RadioGroup>(radioGroupId)
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        return if (selectedRadioButtonId != -1) {
            (findViewById<RadioButton>(selectedRadioButtonId)).text.toString()
        } else ""
    }

    private fun getSelectedCheckBoxes(vararg checkBoxIds: Int): List<Boolean> {
        return checkBoxIds.map { findViewById<CheckBox>(it).isChecked }
    }

    private fun calcularPerfil(respostas: List<Any>): String {

        var pontuacao = 0

        pontuacao += when (respostas[0] as Int) {
            in 100000..Int.MAX_VALUE -> 3
            in 50000..99999          -> 2
            in 1..49999              -> 1
            else                           -> 0
        }

        pontuacao += when (respostas[1] as Int) {
            in 5000..Int.MAX_VALUE -> 3
            in 1000..4999          -> 2
            in 1..999              -> 1
            else                         -> 0
        }

        pontuacao += if (respostas[2] as Boolean) 2 else 0
        pontuacao += if (respostas[3] as Int > 0) 1 else 0

        pontuacao += when (respostas[4] as String) {
            getString(R.string.questao3_opcao1) -> 3
            getString(R.string.questao3_opcao2) -> 2
            else -> 1
        }

        pontuacao += when (respostas[5] as String) {
            getString(R.string.questao4_opcao1) -> 5
            getString(R.string.questao4_opcao2) -> 4
            getString(R.string.questao4_opcao3) -> 3
            getString(R.string.questao4_opcao4) -> 2
            getString(R.string.questao4_opcao5) -> 1
            else -> 0
        }

        pontuacao += when (respostas[6] as String) {
            getString(R.string.questao5_opcao1) -> 3
            getString(R.string.questao5_opcao2) -> 2
            getString(R.string.questao5_opcao3) -> 1
            else -> 0
        }

        pontuacao += when (respostas[7] as String) {
            getString(R.string.questao6_opcao1) -> 3
            getString(R.string.questao6_opcao2) -> 2
            else -> 1
        }

        pontuacao += contarCheckboxesSelecionados(respostas[8] as List<Boolean>)

        pontuacao += contarCheckboxesSelecionados(respostas[9] as List<Boolean>)

        pontuacao += if ((respostas[10] as String).isNotBlank()) 1 else 0

        pontuacao += when (respostas[11] as String) {
            getString(R.string.questao10_opcao1) -> 4
            getString(R.string.questao10_opcao2) -> 3
            getString(R.string.questao10_opcao3) -> 2
            getString(R.string.questao10_opcao3) -> 1
            else -> 0
        }

        pontuacao += when (respostas[12] as String) {
            getString(R.string.questao11_opcao1) -> 3
            getString(R.string.questao11_opcao2) -> 2
            getString(R.string.questao11_opcao3) -> 1
            else -> 0
        }

        return when {
            pontuacao >= 15          -> "Agressivo"
            pontuacao in 7..14 -> "Moderado"
            else                     -> "Conservador"
        }
    }

    private fun contarCheckboxesSelecionados(checkboxes: List<Boolean>): Int {
        return checkboxes.count { it }
    }

}
