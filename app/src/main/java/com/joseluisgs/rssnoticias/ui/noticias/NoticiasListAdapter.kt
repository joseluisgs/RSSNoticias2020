package com.joseluisgs.rssnoticias.ui.noticias

import com.joseluisgs.rssnoticias.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.joseluisgs.rssnoticias.rss.Noticia
import com.joseluisgs.rssnoticias.utils.CirculoTransformacion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_noticia.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Adaptador de la Lista de Noticias
 */
class NoticiasListAdapter(// Objeto con el modelo de datos (lista)
	private val listaNoticias: MutableList<Noticia>, // Fragment Manager para trabajar con el
	private val fm: FragmentManager
) :
	RecyclerView.Adapter<NoticiasListAdapter.NoticiaViewHolder>() {

	/**
	 * Asociamos la vista
	 *
	 * @param parent
	 * @param viewType
	 * @return
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
		return NoticiaViewHolder(LayoutInflater.from(parent.context)
			.inflate(R.layout.item_noticia, parent, false))
	}

	/**
	 * Procesamos las noticias y las metemos en un Holder
	 *
	 * @param holder
	 * @param position
	 */
	override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
		val noticia = listaNoticias[position]
		var titular: String = listaNoticias[position].titulo

		//Controlamos la longitud para que si llega a una cantidad de caracteres, recortarlo
		if (titular.length >= 30) {
			titular = titular.substring(0, 30)
			holder.tvTitular.text = "$titular..."
		} else {
			holder.tvTitular.text = titular
		}

		//Formateamos la fecha
		val date = Date(listaNoticias[position].fecha)
		val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
		val fechaFormato: String = formatoFecha.format(date)
		holder.tvFecha.text = fechaFormato
		//Sacamos la hora
		holder.tvHora.setText(listaNoticias[position].fecha.substring(16, 25))
		//Usando Picasso para poder obtener las fotos y redondearlas
		Picasso.get()
			.load(listaNoticias[position].imagen) //Instanciamos un objeto de la clase (creada más abajo) para redondear la imagen
			.transform(CirculoTransformacion())
			.resize(375, 200)
			.into(holder.ivNoticia)


		// Aquí programamos el evento clik que hacemos en un objeto de la lista
		/*holder.relativeLayout.setOnClickListener{
			fun onClick(v: View?) {
				*//**
				 * Transacción entre fragments. Lo primero es llamar al fragment manager
				 * A continuación instanciamos un objeto fragment correspondiente al
				 * fragment que va a entrar y le pasamos el objeto noticia en la posición de
				 * la lista correspondiente. Iniciamos la transacción, dándole si queremos
				 * animaciones. Lo más importante es el replace, en el que remplazamos el
				 * host por el objeto fragment detalle
				 * De esta manera lo hacemos nosotros de manera manual
				 *//*
				val detalle = NoticiaDetalleFragment(noticia)
				val transaction: FragmentTransaction = fm.beginTransaction()
				// La animación es opcional
				transaction.setCustomAnimations(
					R.anim.animacion_fragment1,
					R.anim.animacion_fragment1, R.anim.animacion_fragment2, R.anim.animacion_fragment1
				)
				//Llamamos al replace
				transaction.replace(R.id.nav_host_fragment, detalle)
				transaction.addToBackStack(null)
				transaction.commit()
			}
		}*/
	}

	/**
	 * Elimina un item de la lista
	 *
	 * @param position
	 */
	fun removeItem(position: Int) {
		listaNoticias.removeAt(position)
		notifyItemRemoved(position)
		notifyItemRangeChanged(position, listaNoticias.size)
	}

	/**
	 * Recupera un Item de la lista
	 *
	 * @param item
	 * @param position
	 */
	fun restoreItem(item: Noticia, position: Int) {
		listaNoticias.add(position, item)
		notifyItemInserted(position)
		notifyItemRangeChanged(position, listaNoticias.size)
	}

	/**
	 * Devuelve el número de items de la lista
	 *
	 * @return
	 */
	override fun getItemCount(): Int {
		return listaNoticias.size
	}

	/**
	 * Holder que encapsula los objetos a mostrar en la lista
	 */
	class NoticiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		var ivNoticia = itemView.ivItemImagenNoticia
		var tvTitular = itemView.tvItemTitularNoticia
		var tvFecha = itemView.tvItemFechaNoticia
		var tvHora = itemView.tvItemHoraNoticia
	}
}
