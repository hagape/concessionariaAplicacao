package concessionaria.excelsior.com.br.concessionariaexcelsior.ui.lista

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import concessionaria.excelsior.com.br.concessionariaexcelsior.MainActivity
import concessionaria.excelsior.com.br.concessionariaexcelsior.R
import concessionaria.excelsior.com.br.concessionariaexcelsior.model.Carro
import concessionaria.excelsior.com.br.concessionariaexcelsior.ui.novo.NovoCarroFragment
import kotlinx.android.synthetic.main.item_carro.view.*

class ListaCarrosAdapter(private val carros: List<Carro>, private val context: Context) : RecyclerView.Adapter<ListaCarrosAdapter.CarrosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CarrosViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_carro, parent, false)
        return CarrosViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carros.size
    }

    override fun onBindViewHolder(holder: CarrosViewHolder?, position: Int) {
        val carro = carros[position]
        holder?.let {
            it.bindView(carro, context)
        }
    }

    class CarrosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(carro: Carro, context: Context) {

            val activity = context as MainActivity

            itemView.setOnClickListener{
                val edicao = NovoCarroFragment()
                edicao.carro = carro
                activity.changeFragment(edicao)
            }

            itemView.tvPreco.text = "R$ " + carro.preco.toString()
            itemView.tvModelo.text = carro.modelo
            if (carro.urlImagem.isNullOrEmpty()) {
                itemView.ivFotoCarro.setBackgroundResource(R.drawable.nofound_car)
            } else {
                Picasso.get()
                        .load(carro.urlImagem)
                        .placeholder(R.drawable.spinner_loading)
                        .error(R.drawable.nofound_car)
                        .into(itemView.ivFotoCarro)
            }
        }
    }
}