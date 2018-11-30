package concessionaria.excelsior.com.br.concessionariaexcelsior

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.RetrofitClient
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.UsuarioAPI
import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Usuario
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    var s: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        s = applicationContext.getSharedPreferences("login", 0)
        val e = s?.edit()

         btn_login.setOnClickListener() {
            val api = RetrofitClient.getInstance("http://concessionariapistiven.herokuapp.com").create(UsuarioAPI::class.java)
            val login = Usuario("", usuario.editText?.text.toString(), senhaLogin.editText?.text.toString(), "")
            api.buscarUsuario(login).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>?, response: Response<Usuario>?) {
                    if(response?.isSuccessful == true) {
                        if (response.body()?.usuario != null && !response.body()?.usuario.equals("")) {
                            try {
                                if(checkLogin.isChecked){
                                    e?.putString("usuario", login.usuario)?.commit()
                                    e?.putString("senha", login.senha)?.commit()
                                    e?.putBoolean("validaLogin", true)?.commit()
                                }

                                val logado = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(logado)
                                finish()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Usuario ou Senha incorreto", Toast.LENGTH_LONG).show()
                        }
                    }else {
                        Toast.makeText(applicationContext, response?.body().toString(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Usuario>?, t: Throwable?) {
                    Log.e("LOGIN", t?.message)
                    Toast.makeText(applicationContext, t?.message, Toast.LENGTH_LONG).show()
                }
            })
        }

        btn_cadastrar.setOnClickListener(){
            try {
                val cadastrar = Intent(this@LoginActivity, CadastroActivity::class.java)
                startActivity(cadastrar)
                finish()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
