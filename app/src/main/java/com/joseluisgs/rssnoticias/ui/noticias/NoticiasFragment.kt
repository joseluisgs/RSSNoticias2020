package com.joseluisgs.rssnoticias.ui.noticias

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.joseluisgs.rssnoticias.R
import com.joseluisgs.rssnoticias.rss.Noticia
import com.joseluisgs.rssnoticias.rss.RSSController
import com.joseluisgs.rssnoticias.utils.Utils
import java.security.AccessController.getContext


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoticiasFragment : Fragment() {
    // Mis variables
    private val DIR_RSS = "http://ep00.epimg.net/rss/tags/ultimas_noticias.xml"
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var tarea: TareaCargarNoticias? = null
    private var noticias = mutableListOf<Noticia>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noticias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Comprobamos la red
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(view.context, "NO Conectado", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(view.context, "SI Conectado", Toast.LENGTH_LONG).show()
        }

        // iniciamos el swipe para recargar
        iniciarSwipeRecarga();

    }

    /**
     * Iniciamos el swipe de recarga
     */
    private fun iniciarSwipeRecarga() {
        swipeRefreshLayout = view!!.findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout?
        swipeRefreshLayout?.setOnRefreshListener{ //Se le ponen los colores que queramos
            swipeRefreshLayout!!.setColorSchemeResources(R.color.colorPrimary)
            swipeRefreshLayout!!.setProgressBackgroundColorSchemeResource(R.color.textColor)
            //Al refrescar llama a la tarea asíncrona
            tarea = TareaCargarNoticias()
            tarea?.execute(DIR_RSS)
        }
    }


    /**
     * Tarea asíncrona para la carga de noticias
     */
    inner class TareaCargarNoticias : AsyncTask<String?, Void?, Void?>() {
        // Primero comprobamos si hay internet, si no lo hay, pintamos en el thread de la
        //interfaz de usuario un snackbar que avise de que no hay conexión, y le decimos
        // al refreshlayout que deje de refrescar.  Tambíen pararemos la ejecución de la
        // tarea asíncrona (no se ejecutará el doInBackground)

        /**
         * Acciones antes de ejecutarse
         */
        override fun onPreExecute() {
            if (!Utils.isNetworkAvailable(this@NoticiasFragment)) {
                cancel(true)
                (context as Activity).runOnUiThread {
                    Toast.makeText(context, "NO Conectado a internet, imposible obtener noticias", Toast.LENGTH_LONG).show()
                }
                if (swipeRefreshLayout!!.isRefreshing) {
                    swipeRefreshLayout!!.isRefreshing = false
                }
            }
            Log.d("Noticias", "onPreExecute OK");
        }

        override fun doInBackground(vararg p0: String?): Void? {
            Log.d("Noticias", "Entrado en doInBackgroud con: " + p0[0].toString());
            try {
                noticias = RSSController.getNoticias(p0[0].toString())
                Log.d("Noticias", "Noticias pre tamaño: " + noticias.size.toString());
            } catch (e: Exception) {
                Log.e("T2Plano ", e.message.toString());
            }
            Log.d("Noticias", "onDoInBackgroud OK");
            return null
        }

        /**
         * Procedimiento a realizar al terminar
         * Cargamos la lista
         *
         * @param args
         */
        override fun onPostExecute(args: Void?) {
            Log.d("Noticias", "entrando en onPostExecute");
//            ad = NoticiasListAdapter(noticias, getFragmentManager())
//            rv.setAdapter(ad)
//            // Avismos que ha cambiado
//            ad.notifyDataSetChanged()
//            rv.setHasFixedSize(true)
//            swipeRefreshLayout.setRefreshing(false)
            Log.d("Noticias", "onPostExecute OK");
            Log.d("Noticias", "Noticias post tam: " + noticias.size.toString());
        }


    }

}