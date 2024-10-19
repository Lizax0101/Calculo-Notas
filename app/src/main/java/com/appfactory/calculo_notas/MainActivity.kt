package com.appfactory.calculo_notas

import android.os.Bundle
import android.text.InputFilter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.appfactory.calculo_notas.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var notasEditTexts: Array<TextInputEditText>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_dark)

        // Botão para captura do layout
        binding.capturar.setOnClickListener {
            criarCampos() // Chama a função ao clicar
        }

        binding.Calcular.setOnClickListener {
            calcular()
        }

        binding.reset.setOnClickListener {
            it.animate().rotationBy(360f).setDuration(500).start()
            reset()


        }
    }

    private fun criarCampos() {
        val textInputLayout1 = binding.textInputLayout
        val textInputEdit2 = binding.textInputEdit.text.toString()

        if (textInputEdit2.isBlank()) {
            textInputLayout1.error = "O campo não pode estar vazio"
            return
        } else {
            textInputLayout1.error = null
            binding.textInputEdit.text?.clear()
        }

        val convertCampoInt = textInputEdit2.toIntOrNull()
        if (convertCampoInt == null || convertCampoInt <= 0) {
            textInputLayout1.error = "Os valores devem ser positivos"
            return
        }

        if (convertCampoInt > 10) {
            textInputLayout1.error = "Número máximo de notas excedido"
            return
        }

        binding.LinearLayout.removeAllViews()
        notasEditTexts = Array(convertCampoInt) { index ->
            val recebeNotas = TextInputLayout(this).apply {
                //TextInputLayout é uma classe.
                //O this nesse contexto se refere a instancia atual da classe onde o codigo esta sendo executado, no MainActivity.
                layoutParams = LinearLayout.LayoutParams(
                    // LayoutParams propriedade que refere aos parametros do layout, define como a wiew deve ser exibida no seu container pai.
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 8, 16, 8)
                }
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
                //define o modo do layout, que é a linha ao redor do campo de texto com o hint(dica) flutuante, outline o contorno.
            }
            //campos criados dinamicamente
            val editTextLayout = TextInputEditText(recebeNotas.context).apply {
                inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

                setPadding(16, 30, 16, 30)
                //definindo preenchimento

                typeface = binding.textInputEdit.typeface
                backgroundTintList = binding.textInputEdit.backgroundTintList
                filters = arrayOf(InputFilter.LengthFilter(3))
                hint = "Digite a nota ${index + 1}"
            }
            recebeNotas.addView(editTextLayout)
            // tudo que foi feito no editTextLayout esta sendo passado agora como paramentro para val recebeNotas

            binding.LinearLayout.addView(recebeNotas)
            //aqui estamos mandando o layout pronto la para o linear do XML no arquivo activityMain

            editTextLayout // esse retorno faz referencia ao TextInputEditLayout

            //retorno ( expression body- corpo de expressao )
            // O valor definido nessa posicao é o valor retornado pela lambda.
        }
    }

    private fun calcular() {

        var soma = 0.0
        //monitora se houve algum erro durante a verificação

        var houveError = false
        // instancia de TextInputEditText que foi armazenado no array la encima.

        notasEditTexts?.forEach { campoNota ->
            //forEachIndexed itera pelos itens do array.
            //O index determina o numero de iterações, campoNotas é o valor em si.

            val textInput = campoNota.parent.parent as TextInputLayout
            val notaText = campoNota.text.toString()

            if (notaText.isBlank()) {
                textInput.error = "Digite todas notas primeiro"
                //Conseguimos acessar o metodo de tratamento de erros por conta da linha do val textInput

                houveError = true

            } else {
                textInput.error = null
                val nota = notaText.toDoubleOrNull()

                if (nota == null || nota < 0 || nota > 10) {
                    textInput.error = " Nota Maxima(0-10)"
                    houveError = true

                } else {
                    textInput.error = null
                    soma += nota
                    binding.textInputEdit.text?.clear()
                }

            }
        }
        if (!houveError && soma > 0.0) {
            binding.Resultado.text = getString(R.string.resultado, soma)
            binding.LinearLayout.removeAllViews()
        } else {
            binding.Resultado.text = getString(R.string.resultado2)
        }
    }


    private fun reset() {
        val quantNotas = binding.textInputEdit.text

        if (!quantNotas.isNullOrEmpty()) {
            quantNotas.clear()
            binding.textInputLayout.error = null
        }
        if (binding.LinearLayout.childCount > 0) {
            binding.LinearLayout.children.forEach { limpar ->
                if (limpar is TextInputLayout) {
                    limpar.error = null
                    limpar.editText?.text?.clear()
                }
            }
        } else {
            binding.Resultado.text = getString(R.string.resultado2)
            notasEditTexts?.let {
                it.forEach { campoNota -> campoNota.text?.clear() }
            }
        }
    }
}




