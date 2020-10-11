package com.joseluisgs.rssnoticias.ui.noticias

import android.app.Activity
import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.joseluisgs.rssnoticias.MainActivity
import com.joseluisgs.rssnoticias.R
import com.joseluisgs.rssnoticias.rss.Noticia
import com.joseluisgs.rssnoticias.rss.RSSController
import com.joseluisgs.rssnoticias.utils.Utils
import kotlinx.android.synthetic.main.fragment_noticias.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoticiasFragment : Fragment() {
    // Mis variables
    private val DIR_RSS = "http://ep00.epimg.net/rss/tags/ultimas_noticias.xml" // URL
    private var noticias = mutableListOf<Noticia>() // Lista
    // Interfaz gráfica
    private lateinit var adapter: NoticiasListAdapter //Adaptador de Noticias de Recycler
    private lateinit var tarea: TareaCargarNoticias // Tarea en segundo plano
    private var paintSweep = Paint()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noticias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Iniciamos la interfaz
        initUI()

    }

    private fun initUI() {

        // iniciamos el swipe para recargar
        iniciarSwipeRecarga();

        // Cargamos los datos pro primera vez
        cargarNoticias()

        // solo si hemos cargado hacemos sl swipeHorizontal
        iniciarSwipeHorizontal();

        // Mostramos las vistas de listas y adaptador asociado
        noticiasRecycler.layoutManager = LinearLayoutManager(context);
        Log.d("Noticias", "Asignado al RV");
    }


    /**
     * Iniciamos el swipe de recarga
     */
    private fun iniciarSwipeRecarga() {
        noticiasSwipe.setColorSchemeResources(R.color.colorPrimaryDark)
        noticiasSwipe.setProgressBackgroundColorSchemeResource(R.color.textColor)
        noticiasSwipe.setOnRefreshListener{
            cargarNoticias()
        }
    }

    /**
     * Realiza el swipe horizontal si es necesario
     */
    private fun iniciarSwipeHorizontal() {
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or
                    ItemTouchHelper.RIGHT
        ) {
            // Sobreescribimos los métodos
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Analizamos el evento según la dirección
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Si pulsamos a la de izquierda o a la derecha
                // Programamos la accion
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        // Log.d("Noticias", "Tocado izquierda");
                        borrarElemento(position)
                    }
                    else -> {
                        //  Log.d("Noticias", "Tocado derecha");
                        editarElemento(position)
                    }
                }
            }

            // Dibujamos los botones y eveneto. Nos lo creemos :):)
            // IMPORTANTE
            // Para que no te explote las imagenes deben ser PNG
            // Así que añade un IMAGE ASEET bjándtelos de internet
            // https://material.io/resources/icons/?style=baseline
            // como PNG y cargas el de mayor calidad
            // de otra forma Bitmap no funciona bien
            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    // Si es dirección a la derecha: izquierda->derecta
                    // Pintamos de azul y ponemos el icono
                    if (dX > 0) {
                        // Pintamos el botón izquierdo
                        botonIzquierdo(canvas, dX, itemView, width)
                    } else {
                        // Caso contrario
                        botonDerecho(canvas, dX, itemView, width)
                    }
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        // Añadimos los eventos al RV
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(noticiasRecycler)
    }

    /**
     * Mostramos el elemento inquierdo
     * @param canvas Canvas
     * @param dX Float
     * @param itemView View
     * @param width Float
     */
    private fun botonDerecho(canvas: Canvas, dX: Float, itemView: View, width: Float) {
        // Pintamos de rojo y ponemos el icono
        paintSweep.color = Color.RED
        val background = RectF(
            itemView.right.toFloat() + dX,
            itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat()
        )
        canvas.drawRect(background, paintSweep)
        val icon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_sweep_eliminar)
        val iconDest = RectF(
            itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right
                .toFloat() - width, itemView.bottom.toFloat() - width
        )
        canvas.drawBitmap(icon, null, iconDest, paintSweep)
    }

    /**
     * Mostramos el elemento izquierdo
     * @param canvas Canvas
     * @param dX Float
     * @param itemView View
     * @param width Float
     */
    private fun botonIzquierdo(canvas: Canvas, dX: Float, itemView: View, width: Float) {
        // Pintamos de azul y ponemos el icono
        paintSweep.setColor(Color.BLUE)
        val background = RectF(
            itemView.left.toFloat(), itemView.top.toFloat(), dX,
            itemView.bottom.toFloat()
        )
        canvas.drawRect(background, paintSweep)
        val icon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_sweep_detalles)
        val iconDest = RectF(
            itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left
                .toFloat() + 2 * width, itemView.bottom.toFloat() - width
        )
        canvas.drawBitmap(icon, null, iconDest, paintSweep)
    }

    /**
     * Carga las noticias
     */
    private fun cargarNoticias() {
        tarea = TareaCargarNoticias()
        tarea?.execute(DIR_RSS)
    }

    /**
     * Acción primaria: Borra un elemento
     * @param position Int
     */
    private fun borrarElemento(position: Int) {
        Log.d("Noticias", "Borrar Elemento: $position")
        // Acciones
        val deletedModel = noticias[position]
        adapter.removeItem(position)
        // Mostramos la barra. Se la da opción al usuario de recuperar lo borrado con el el snackbar
        val snackbar = Snackbar.make(view!!, "Noticia eliminada", Snackbar.LENGTH_LONG)
        snackbar.setAction("DESHACER") { // undo is selected, restore the deleted item
            adapter.restoreItem(deletedModel, position)
        }
        snackbar.setActionTextColor(resources.getColor(R.color.colorPrimary))
        snackbar.show()
    }

    /**
     * Acción secundaria: Ver/Editar
     * @param position Int
     */
    private fun editarElemento(position: Int){
        Log.d("Noticias", "Detalles Elemento: $position")
        val noticia = noticias[position]
        abrirNoticia(noticia)
        // Esto es para que no se quede el color. Quitamos y ponemos la noticia
        adapter.removeItem(position)
        adapter.restoreItem(noticia, position);
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
                    Toast.makeText(context, "Active la conexión a la red", Toast.LENGTH_LONG).show()
                }
                if (noticiasSwipe.isRefreshing) {
                    noticiasSwipe.isRefreshing = false
                }
            }
            Toast.makeText(context, "Obteniendo noticias", Toast.LENGTH_LONG).show()
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
            Log.d("Noticias", "entrando en onPostExecute")
            adapter = NoticiasListAdapter(noticias) {
                eventoClicFila(it)
            }

            noticiasRecycler.adapter = adapter
            // Avismos que ha cambiado
            adapter.notifyDataSetChanged()
            noticiasRecycler.setHasFixedSize(true)
            noticiasSwipe.isRefreshing = false
            Log.d("Noticias", "onPostExecute OK");
            Log.d("Noticias", "Noticias post tam: " + noticias.size.toString());
            Toast.makeText(context, "Noticias descargadas", Toast.LENGTH_LONG).show()
        }


    }

    private fun eventoClicFila(noticia: Noticia) {
        if((activity as MainActivity?)!!.isClicEventoFila) {
            Log.d("Noticias", "Has hecho clic en la noticia: $noticia")
            abrirNoticia(noticia)
        }
    }

    /**
     * Abre una noticia como Fragment
     * @param noticia Noticia
     */
    private fun abrirNoticia(noticia: Noticia) {
        val noticiaDetalle = NoticiaDetalleFragment(noticia)
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        // animaciones
        //        transaction.setCustomAnimations(R.anim.animacion_fragment1,
        //        	R.anim.animacion_fragment1, R.anim.animacion_fragment2,
        //        	R.anim.animacion_fragment1)
        //Llamamos al replace
        transaction.replace(R.id.fragment_noticias, noticiaDetalle)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}