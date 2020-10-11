package com.joseluisgs.rssnoticias.ui.noticias

import com.joseluisgs.rssnoticias.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joseluisgs.rssnoticias.MainActivity
import com.joseluisgs.rssnoticias.rss.Noticia
import com.squareup.picasso.Picasso


class NoticiaDetalleFragment : Fragment {
	// El Objeto
	private var noticia: Noticia? = null

	// Elementos de la interfaz
	private var tvDetalleTitulo: TextView? = null
	private var wvDetalleContenido: WebView? = null
	private var ivDetalleImagen: ImageView? = null
	private var fabDetallesIr: FloatingActionButton? = null

	// Constructores
	constructor(noticia: Noticia?) {
		this.noticia = noticia
		noticiaActual = noticia
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
		// Obtenemos los elementos de la interfaz
		iniciarComponentesIU()
		// iniciamos los eventos Asociados
		iniciarEventosIU()
		// Procesamos la noticia
		procesarNoticia()
		//Para mostrar el item de compartir
		// Elementos de la interfaz
		actualizarInterfaz()
	}


	/**
	 * Inicia la componentes de la IU
	 */
	private fun iniciarComponentesIU() {
		tvDetalleTitulo = getView().findViewById(R.id.tvNoticiaDetalleTitular)
		wvDetalleContenido = getView().findViewById(R.id.wvNoticiaDetalleContenido)
		ivDetalleImagen = getView().findViewById(R.id.ivNoticiaDetalleImagen)
		fabDetallesIr = getView().findViewById(R.id.fabNoticiaDetalleIr)
	}

	/**
	 * Inicia los Eventos de la IU
	 */
	private fun iniciarEventosIU() {
		// Enviamos el email
		fabDetallesIr!!.setOnClickListener(object : OnClickListener() {
			fun onClick(v: View?) {
				abrirEnlaceNavegador(noticia!!.link)
			}
		})
	}

	/**
	 * Procesamos una noticia
	 */
	private fun procesarNoticia() {
		tvDetalleTitulo!!.text = noticia!!.titulo
		wvDetalleContenido!!.loadData(noticia!!.contenido, "text/html", null)
		Picasso.get().load(noticia!!.imagen).into(ivDetalleImagen)
	}

	/**
	 * Llamamos al navegador con un enlace
	 *
	 * @param enlance
	 */
	private fun abrirEnlaceNavegador(enlance: String) {
		val uri: Uri = Uri.parse(enlance)
		val intent = Intent(Intent.ACTION_VIEW, uri)
		startActivity(intent)
	}

	/**
	 * ACtualizamos la interfaz de usuario
	 */
	private fun actualizarInterfaz() {
		// Oculto lo que no me interesa
		(getActivity() as MainActivity).ocultarElementosIU()

		// Muestro los elementos de menú que quiero en este fragment
		// Menú
		(getActivity() as MainActivity).menu!!.findItem(R.id.menu_compartir_noticia).isVisible = true
		(getActivity() as MainActivity).menu!!.findItem(R.id.menu_atras).isVisible = true

		//Para ocultar el acceso al menú lateral
		//Se hace un getActivity haciendole un casting a MainActivity
		(getActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
		(getActivity() as MainActivity).supportActionBar!!.setDisplayShowHomeEnabled(false)
	}

	companion object {
		// Esto debemos hacerlo porque la opción de copratir está en el menú y debe ser accesible
		// Como variable de clase
		var noticiaActual: Noticia? = null
	}
}
