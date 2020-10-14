package com.joseluisgs.rssnoticias.ui.noticias

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.joseluisgs.rssnoticias.MainActivity
import com.joseluisgs.rssnoticias.R
import com.joseluisgs.rssnoticias.rss.Noticia
import com.joseluisgs.rssnoticias.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_noticia_detalle.*


class NoticiaDetalleFragment(private val noticia: Noticia) : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Para decir que el menú tendrá cambios
		// setHasOptionsMenu(true)
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_noticia_detalle, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// Iniciamos la IU
		initUI()
	}

	/**
	 * Iniciamos la intrfaz
	 */
	private fun initUI() {
		// invalidamos el resto de eventos de fila e indicamos la noticia actual
		(activity as MainActivity?)!!.isClicEventoFila = false
		(activity as MainActivity?)!!.noticiaActual = noticia
		// Actualizamos el menú
		initMenuOpciones()
		// iniciamos los eventos Asociados
		initBotonesEventos()
		// Procesamos la noticia como interfaz
		procesarNoticia()
	}

	/**
	 * Muestra el menú con las opciones
	 */
	private fun initMenuOpciones() {
		(activity as MainActivity?)!!.menu.findItem(R.id.menu_atras).isVisible = true
		// (activity as MainActivity?)!!.menu.findItem(R.id.menu_compartir_noticia).isVisible = true
		(activity as MainActivity?)!!.menu.findItem(R.id.menu_mas).isVisible = false
	}

//	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//		super.onCreateOptionsMenu(menu, inflater)
//		// inflater.inflate(R.menu.menu_main,menu)
//		menu.findItem(R.id.menu_mas).isVisible = true
//		menu.findItem(R.id.menu_atras).isVisible = true
//	}




	/**
	 * Inicia los Eventos de la IU
	 */
	private fun initBotonesEventos() {
		// Abrimos la noticia
		noticiaDetalleFabIr.setOnClickListener { Utils.abrirURL(activity!!, noticia.link) }
		noticiaDetalleFabCompartir.setOnClickListener { Utils.compartirNoticia(activity!!, noticia) }
	}

	/**
	 * Procesamos una noticia
	 */
	private fun procesarNoticia() {
		noticiaDetalleTextTitular.text = noticia.titulo
		// A veces no tenemos contenido solo descripcion
		if (noticia.contenido.length > 1)
			noticiaDEtalleWebViewContenido.loadData(noticia.contenido, "text/html", null)
		else
			noticiaDEtalleWebViewContenido.loadData(noticia.descripcion, "text/html", null)
		Picasso.get().load(noticia.imagen).into(noticiaDetalleImageView)
	}
}
