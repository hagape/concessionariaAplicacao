package concessionaria.excelsior.com.br.concessionariaexcelsior

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.RetrofitClient
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.UsuarioAPI
import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Usuario
import kotlinx.android.synthetic.main.activity_cadastro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        btnCadastro.setOnClickListener() {
            val api = RetrofitClient.getInstance("http://concessionariapistiven.herokuapp.com").create(UsuarioAPI::class.java)

            if(!validarCadastro()){
                Toast.makeText(applicationContext, "Preencher todos os campos", Toast.LENGTH_LONG).show()
            }else if(novaSenha?.editText?.text.toString().equals(confirmaSenha?.editText?.text.toString())) {

                val cadastrar = Usuario(null,
                        novoUsuario?.editText?.text.toString(),
                        novaSenha?.editText?.text.toString(),
                        novoEmail?.editText?.text.toString())
                api.salvarUsuario(cadastrar).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response?.isSuccessful == true) {
                            Toast.makeText(applicationContext, "Usuario cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                            try {
                                val cadastro = Intent(this@CadastroActivity, LoginActivity::class.java)
                                startActivity(cadastro)
                                finish()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Erro, não foi possivel salvar :(", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.e("CADASTRO", t?.message)
                        Toast.makeText(applicationContext, t?.message, Toast.LENGTH_LONG).show()
                    }
                })
            }else{
                Toast.makeText(applicationContext, "Senhas são diferentes", Toast.LENGTH_LONG).show()
            }
        }

        btnVoltar.setOnClickListener {
            val voltar = Intent(this@CadastroActivity, LoginActivity::class.java)
            startActivity(voltar)
            finish()
        }
    }

    fun validarCadastro(): Boolean {
        var valor = true

        if(novoUsuario?.editText?.text.toString() == null || novoUsuario?.editText?.text.toString().equals("")) {
            valor = false
        }

        if(novoEmail?.editText?.text.toString() == null || novoEmail?.editText?.text.toString().equals("")){
            valor = false
        }

        if(novaSenha?.editText?.text.toString() == null || novaSenha?.editText?.text.toString().equals("")){
            valor = false
        }

        if(confirmaSenha?.editText?.text.toString() == null || confirmaSenha?.editText?.text.toString().equals("")){
            valor = false
        }

        return valor
    }
}
