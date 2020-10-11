package com.joseluisgs.rssnoticias.rss

import android.util.Log
import org.w3c.dom.DOMException
import org.w3c.dom.Element
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

/**
 * Controlador de noticias. Singleton
 */
object RSSController {
	/**
	 * Devuleve la lista de noticias de un RSS
	 * @param uri String Dirección del RSS
	 * @return MutableList<Noticia> Lista de noticias
	 */
	fun getNoticias(uri: String): MutableList<Noticia> {
		// Parser
		val factory = DocumentBuilderFactory.newInstance()
		// Lista de noticias
		val noticias = mutableListOf<Noticia>()
		// Log.d("Noticias", "Noticias Controller uri: " + uri)

		try {
			// Filtramos por elementos del RSS
			val builder = factory.newDocumentBuilder()
			val document = builder.parse(uri)
			val items = document.getElementsByTagName("item")

			for (i in 0 until items.length) {
				val nodo = items.item(i)
				val noticia = Noticia()
				// Vamos a contar las imagenes que hay
				var contadorImagenes = 0
				var n = nodo.firstChild
				while (n != null) {
					when (n.nodeName) {
						"title" -> {
							noticia.titulo = n.textContent
							// println("Título: $noticia.titulo");
						}
						"link" -> {
							noticia.link = n.textContent
							// println("Enlace: $noticia.enlace");
						}
						"description" -> {
							noticia.descripcion = n.textContent
							// println("Descripción: $noticia.descripcion");
						}
						"pubDate" -> {
							noticia.fecha = n.textContent
							// println("Fecha: $noticia.fecha");
						}
						"content:encoded" -> {
							noticia.contenido = n.textContent
							// println("Contenido: $noticia.contenido");
						}
						"enclosure" -> {
							val imagen: String = (n as Element).getAttribute("url")
							//Controlamos que solo rescate una imagen
							if (contadorImagenes == 0) {
								noticia.imagen = imagen
							}
							contadorImagenes++
						}
					}
					n = n.nextSibling
				}
				noticias.add(noticia)
			}
			// Log.d("Noticias", "Noticias Controller tam: " + noticias.size.toString())
			return noticias
		} catch (e: ParserConfigurationException) {
			Log.d("Noticias","Error: " + e.message)
		} catch (e: IOException) {
			Log.d("Noticias", "Error: " + e.message)
		} catch (e: DOMException) {
			Log.d("Noticias", "Error: " + e.message)
		} catch (e: SAXException) {
			Log.d("Noticias","Error: " + e.message)
		}
		return noticias
	}
}