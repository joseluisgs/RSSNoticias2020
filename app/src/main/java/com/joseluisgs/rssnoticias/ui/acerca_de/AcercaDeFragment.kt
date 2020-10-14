package com.joseluisgs.rssnoticias.ui.acerca_de

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joseluisgs.rssnoticias.MainActivity
import com.joseluisgs.rssnoticias.R
import com.joseluisgs.rssnoticias.utils.Utils
import kotlinx.android.synthetic.main.fragment_acerca_de.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AcercaDeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AcercaDeFragment : Fragment() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_acerca_de, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// Inicialiamos la IU
		initUI()
	}

	/**
	 * Inicializamos los elementos de la IU
	 */
	private fun initUI() {
		// Desactivamos los eventos de fila
		(activity as MainActivity?)!!.isClicEventoFila = false
		// inicializamos el menu
		initMenuOpciones()
		// Eventos de boyones
		initBotonesEventos()
	}

	/**
	 * Inicia los eventos de botones y su funcionalidad
	 */
	private fun initBotonesEventos() {
		acercaDeBtnCorreo.setOnClickListener {
			Utils.mandarEMail(activity, para = "dam@moviles.com", asunto ="contacto")
		}
		acercaDeViewGitHub.setOnClickListener { Utils.abrirURL(activity!!, "https://github.com/joseluisgs") }
		acercaDeViewTwitter.setOnClickListener { Utils.abrirURL(activity!!, "https://twitter.com/joseluisgonsan") }
	}

	/**
	 * Muestra el menú con las opciones
	 */
	private fun initMenuOpciones() {
		(activity as MainActivity?)!!.menu.findItem(R.id.menu_atras).isVisible = true
		// (activity as MainActivity?)!!.menu.findItem(R.id.menu_acerca_de).isVisible = false
		(activity as MainActivity?)!!.menu.findItem(R.id.menu_mas).isVisible = false

	}


}