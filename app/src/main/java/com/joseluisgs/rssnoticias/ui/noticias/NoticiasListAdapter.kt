package com.joseluisgs.rssnoticias.ui.noticias

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.joseluisgs.rssnoticias.R
import com.joseluisgs.rssnoticias.rss.Noticia
import com.joseluisgs.rssnoticias.utils.CirculoTransformacion
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_noticia.view.*
import java.text.SimpleDateFormat
import java.util.*



/**
 * Adaptador de la Lista de Noticias
 */

class NoticiasListAdapter(
	private val listaNoticias: MutableList<Noticia>,
	private val listener: (Noticia) -> Unit
) :
	RecyclerView.Adapter<NoticiasListAdapter.NoticiaViewHolder>() {

//class NoticiasListAdapter(// Objeto con el modelo de datos (lista)
//	private val listaNoticias: MutableList<Noticia>, // Fragment Manager para trabajar con el
//	private val fm: FragmentManager
//) :
//	RecyclerView.Adapter<NoticiasListAdapter.NoticiaViewHolder>() {

	/**
	 * Asociamos la vista
	 *
	 * @param parent
	 * @param viewType
	 * @return
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaViewHolder {
		return NoticiaViewHolder(
			LayoutInflater.from(parent.context)
				.inflate(R.layout.item_noticia, parent, false)
		)
	}

	/**
	 * Procesamos las noticias y las metemos en un Holder
	 *
	 * @param holder
	 * @param position
	 */
	override fun onBindViewHolder(holder: NoticiaViewHolder, position: Int) {
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
		holder.tvHora.text = listaNoticias[position].fecha.substring(16, 25)
		//Usando Picasso para poder obtener las fotos y redondearlas
		Picasso.get()
			.load(listaNoticias[position].imagen) //Instanciamos un objeto de la clase (creada más abajo) para redondear la imagen
			.transform(CirculoTransformacion())
			.resize(375, 200)
			.into(holder.ivNoticia)

		// Programamos el clic de cada fila (itemView)
		holder.tvTitular
			.setOnClickListener {
			// Devolvemos la noticia
			listener(listaNoticias[position])
		}
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
		// Elementos graficos con los que nos asociamos
		var ivNoticia = itemView.ivItemImagenNoticia
		var tvTitular = itemView.tvItemTitularNoticia
		var tvFecha = itemView.tvItemFechaNoticia
		var tvHora = itemView.tvItemHoraNoticia

		// Indicamos el Layout para el click
		// var relativeLayout = itemView.itemNoticiaLayout
	}
}
