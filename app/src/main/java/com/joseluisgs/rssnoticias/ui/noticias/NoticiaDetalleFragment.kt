package com.joseluisgs.rssnoticias.ui.noticias

import com.joseluisgs.rssnoticias.R
import kotlinx.android.synthetic.main.fragment_noticia_detalle.*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joseluisgs.rssnoticias.MainActivity
import com.joseluisgs.rssnoticias.rss.Noticia
import com.joseluisgs.rssnoticias.utils.Utils
import com.squareup.picasso.Picasso


class NoticiaDetalleFragment : Fragment {
	// El Objeto
	private lateinit var noticia: Noticia

	// Elementos de la interfaz
	private var tvDetalleTitulo: TextView? = null
	private var wvDetalleContenido: WebView? = null
	private var ivDetalleImagen: ImageView? = null
	private var fabDetallesIr: FloatingActionButton? = null

	// Constructores
	constructor(noticia: Noticia) {
		this.noticia = noticia
	}

	constructor() {}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
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
		// Actualizamos el menú
		initMenuOpciones()
		// Obtenemos los elementos de la interfaz
		iniciarComponentesIU()
		// iniciamos los eventos Asociados
		initBotonesEventos()
		// Procesamos la noticia como interfaz
		procesarNoticia()
	}

	/**
	 * Muestra el menú con las opciones
	 */
	private fun initMenuOpciones() {
		(activity as MainActivity?)!!.menu!!.findItem(R.id.menu_atras).isVisible = true
		(activity as MainActivity?)!!.menu!!.findItem(R.id.menu_compartir_noticia).isVisible = true
	}


	/**
	 * Inicia la componentes de la IU
	 */
	private fun iniciarComponentesIU() {
		tvDetalleTitulo = view?.findViewById(R.id.tvNoticiaDetalleTitular)
		wvDetalleContenido = view?.findViewById(R.id.wvNoticiaDetalleContenido)
		ivDetalleImagen = view?.findViewById(R.id.ivNoticiaDetalleImagen)
		fabDetallesIr = view?.findViewById(R.id.fabNoticiaDetalleIr)
	}

	/**
	 * Inicia los Eventos de la IU
	 */
	private fun initBotonesEventos() {
		// Abrimos la noticia
		fabDetallesIr!!.setOnClickListener { Utils.abrirURL(activity, noticia!!.link) }
	}

	/**
	 * Procesamos una noticia
	 */
	private fun procesarNoticia() {
		tvDetalleTitulo?.text = noticia?.titulo
		wvDetalleContenido?.loadData(noticia!!.contenido, "text/html", null)
		Picasso.get().load(noticia?.imagen).into(ivDetalleImagen)
	}
}
