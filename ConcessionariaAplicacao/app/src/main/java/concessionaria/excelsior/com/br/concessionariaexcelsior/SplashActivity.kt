package concessionaria.excelsior.com.br.concessionariaexcelsior

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.RetrofitClient
import concessionaria.excelsior.com.br.concessionariaexcelsior.api.UsuarioAPI
import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Usuario
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_splashscreen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    var s: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        s = applicationContext.getSharedPreferences("login", 0)

       if(s!!.getBoolean("validaLogin", false)){
           val usuario = s!!.getString("usuario", null)
           val senha = s!!.getString("senha", null)

           val api = RetrofitClient.getInstance("http://concessionariapistiven.herokuapp.com").create(UsuarioAPI::class.java)
           val login = Usuario("", usuario, senha, "")
           api.buscarUsuario(login).enqueue(object : Callback<Usuario> {
               override fun onResponse(call: Call<Usuario>?, response: Response<Usuario>?) {
                   if(response?.isSuccessful == true) {
                       if (response.body()?.usuario != null && !response.body()?.usuario.equals("")) {
                           try {
                               val logado = Intent(this@SplashActivity, MainActivity::class.java)
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

        carregarSplash()
    }

    fun carregarSplash(){
        val animacao = AnimationUtils.loadAnimation(this, R.anim.animacao_splash)
        ivSplashLogo.startAnimation(animacao)

        Handler().postDelayed({
            if(!s!!.getBoolean("validaLogin", false)) {
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }
        }, 6000)
    }
}