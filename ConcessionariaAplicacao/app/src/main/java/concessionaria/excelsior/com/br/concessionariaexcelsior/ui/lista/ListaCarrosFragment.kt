package concessionaria.excelsior.com.br.concessionariaexcelsior.ui.lista

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import concessionaria.excelsior.com.br.concessionariaexcelsior.R
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.CarroAPI
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.RetrofitClient
import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Carro
import kotlinx.android.synthetic.main.erro.*
import kotlinx.android.synthetic.main.fragment_lista_carros.*
import kotlinx.android.synthetic.main.fragment_novo_carro.*
import kotlinx.android.synthetic.main.loading.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaCarrosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_lista_carros, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPesquisar.setOnClickListener {
            val api = RetrofitClient
                    .getInstance("http://concessionariapistiven.herokuapp.com")
                    .create(CarroAPI::class.java)

            api.buscarPorMarca(buscaMarca.editText?.text.toString().toLowerCase()).enqueue(object : Callback<List<Carro>> {
                override fun onResponse(call: Call<List<Carro>>?, response: Response<List<Carro>>?) {
                    if(response?.isSuccessful == true) {
                        if(buscaMarca.editText?.text.toString().isNullOrEmpty()){
                            Toast.makeText(context,
                                    "Preencha uma marca para buscar",
                                    Toast.LENGTH_SHORT).show()
                        }
                        buscaMarca.editText?.setText("")
                        setupLista(response?.body())
                    }
                }

                override fun onFailure(call: Call<List<Carro>>?, t: Throwable?) {
                    Toast.makeText(context,
                            "Não foi possivel carregar os Carros buscados",
                            Toast.LENGTH_SHORT).show()
                }
            })
        }

        carregarCarros()
    }

    fun carregarCarros() {
        val api = RetrofitClient
                .getInstance("http://concessionariapistiven.herokuapp.com")
                .create(CarroAPI::class.java)

        loading.visibility = View.VISIBLE

        api.buscarCarros().enqueue(object: Callback<List<Carro>> {

            override fun onResponse(call: Call<List<Carro>>?, response: Response<List<Carro>>?) {

                containerErro.visibility = View.GONE
                tvMsgErro.text = ""

                if(response?.isSuccessful == true) {
                    setupLista(response?.body())
                } else {
                    containerErro.visibility = View.VISIBLE
                    tvMsgErro.text = response?.errorBody()?.string()
                }

                loading.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Carro>>?, t: Throwable?) {
                containerErro.visibility = View.VISIBLE
                tvMsgErro.text = t?.message
                loading.visibility = View.GONE

            }
        })
    }

    fun setupLista(carros: List<Carro>?) {
        carros.let {
            rvCarros.adapter = ListaCarrosAdapter(carros!!, context)
            val layoutManager = LinearLayoutManager(context)
            rvCarros.layoutManager = layoutManager
        }
    }
}
