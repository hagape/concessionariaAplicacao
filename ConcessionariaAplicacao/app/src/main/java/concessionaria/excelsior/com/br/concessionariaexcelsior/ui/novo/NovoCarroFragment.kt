package concessionaria.excelsior.com.br.concessionariaexcelsior.ui.novo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import concessionaria.excelsior.com.br.concessionariaexcelsior.MainActivity

import concessionaria.excelsior.com.br.concessionariaexcelsior.R
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.CarroAPI
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.RetrofitClient
import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Carro
import concessionaria.excelsior.com.br.concessionariaexcelsior.ui.lista.ListaCarrosFragment
import kotlinx.android.synthetic.main.fragment_novo_carro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NovoCarroFragment : Fragment() {

    var carro: Carro = Carro("", "", "", 0, "", "", 0, "")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_novo_carro, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!carro.id.isNullOrEmpty()){
            inputMarca.editText?.setText(carro.marca)
            inputModelo.editText?.setText(carro.modelo)
            inputAno.editText?.setText(Integer.toString(carro.ano))
            inputPlaca.editText?.setText(carro.placa)
            inputUrlImagem.editText?.setText(carro.urlImagem)
            inputPreco.editText?.setText(Integer.toString(carro.preco))
            inputDescricao.editText?.setText(carro.descricao)
            btnDeletar.visibility = View.VISIBLE
        } else {
            btnDeletar.visibility = View.GONE
        }

        btSalvar.setOnClickListener {
            val api = RetrofitClient
                    .getInstance("http://concessionariapistiven.herokuapp.com")
                    .create(CarroAPI::class.java)

            if (!validarCadastro()) {
                Toast.makeText(context, "Preencher todos os campos", Toast.LENGTH_LONG).show()
            } else if(inputAno.editText?.length()!! == 4 && inputPreco.editText?.length()!! > 3 && inputPreco.editText?.length()!! < 7){

                if(carro.id.isNullOrEmpty()) {
                    val carro = Carro(null,
                            inputMarca.editText?.text.toString().toLowerCase(),
                            inputModelo.editText?.text.toString(),
                            inputAno.editText?.text.toString().toInt(),
                            inputPlaca.editText?.text.toString(),
                            inputUrlImagem.editText?.text.toString(),
                            inputPreco.editText?.text.toString().toInt(),
                            inputDescricao.editText?.text.toString())

                    api.salvar(carro).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                            if (response?.isSuccessful == true) {
                                Toast.makeText(context,
                                        "Carro cadastrado com Sucesso",
                                        Toast.LENGTH_SHORT).show()
                                limparCampos()
                                val activity = context as MainActivity
                                activity.changeFragment(ListaCarrosFragment())

                            } else {
                                Toast.makeText(context,
                                        "Não foi possivel cadastrar um carro",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            Log.e("CARRO", t?.message)
                        }
                    })
                } else {
                    val carro = Carro(carro.id,
                            inputMarca.editText?.text.toString().toLowerCase(),
                            inputModelo.editText?.text.toString(),
                            inputAno.editText?.text.toString().toInt(),
                            inputPlaca.editText?.text.toString(),
                            inputUrlImagem.editText?.text.toString(),
                            inputPreco.editText?.text.toString().toInt(),
                            inputDescricao.editText?.text.toString())

                    api.update(carro).enqueue(object : Callback<Void>{
                        override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                            if (response?.isSuccessful == true) {
                                Toast.makeText(context, "Carro alterado com sucesso!", Toast.LENGTH_SHORT).show()
                                limparCampos()
                                val activity = context as MainActivity
                                activity.changeFragment(ListaCarrosFragment())
                            } else {
                                Toast.makeText(context, "Não foi possivel alterar o carro!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            Log.e("CARRO", t?.message)                        }
                    })
                }
            }else{
                Toast.makeText(context,
                        "Ano deve ter 4 caracteres e Preço deve ter entre 4 e 6 caracteres",
                        Toast.LENGTH_LONG).show()
            }
        }


        btnDeletar.setOnClickListener {
            val api = RetrofitClient.getInstance("http://concessionariapistiven.herokuapp.com").create(CarroAPI::class.java)

            if(!carro.id.isNullOrEmpty()) {
                api.delete(carro.id!!).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response?.isSuccessful == true) {
                            Toast.makeText(context, "Carro deletado com Sucesso!", Toast.LENGTH_SHORT).show()
                            limparCampos()
                            val activity = context as MainActivity
                            activity.changeFragment(ListaCarrosFragment())
                        } else {
                            Toast.makeText(context, "Carro não pode ser deletado!", Toast.LENGTH_SHORT).show()
                        }                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e("CARRO", t?.message)                    }
                })
            }
        }
    }

    fun validarCadastro(): Boolean {
        var valor = true

        if(inputMarca.editText?.text.toString() == null || inputMarca.editText?.text.toString().equals("")) {
            valor = false
        }

        if(inputModelo.editText?.text.toString() == null || inputModelo.editText?.text.toString().equals("")){
            valor = false
        }

        if(inputAno.editText?.text.toString() == null || inputAno.editText?.text.toString().equals("")){
            valor = false
        }

        if(inputPreco.editText?.text.toString() == null || inputPreco.editText?.text.toString().equals("")){
            valor = false
        }

        return valor
    }

    private fun limparCampos() {
        carro.id = null
        inputMarca.editText?.setText("")
        inputModelo.editText?.setText("")
        inputAno.editText?.setText("")
        inputPlaca.editText?.setText("")
        inputUrlImagem.editText?.setText("")
        inputPreco.editText?.setText("")
        inputDescricao.editText?.setText("")
    }
}
