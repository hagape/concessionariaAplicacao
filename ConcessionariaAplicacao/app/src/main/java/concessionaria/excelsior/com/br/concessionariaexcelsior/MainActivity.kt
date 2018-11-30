package concessionaria.excelsior.com.br.concessionariaexcelsior

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import concessionaria.excelsior.com.br.concessionariaexcelsior.ui.lista.ListaCarrosFragment
import concessionaria.excelsior.com.br.concessionariaexcelsior.ui.novo.NovoCarroFragment
import concessionaria.excelsior.com.br.concessionariaexcelsior.ui.sobre.SobreFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var s: SharedPreferences? = null
    var e = s?.edit()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(ListaCarrosFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment(NovoCarroFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment(SobreFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_logout -> {
                s = applicationContext.getSharedPreferences("login", 0)
                val e = s?.edit()

                e?.putBoolean("validaLogin", false)?.commit()
                val logado = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(logado)
                finish()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containerFragment, fragment)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        changeFragment(ListaCarrosFragment())
    }
}
