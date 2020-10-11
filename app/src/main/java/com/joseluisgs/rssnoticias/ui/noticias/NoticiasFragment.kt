package com.joseluisgs.rssnoticias.ui.noticias

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.joseluisgs.rssnoticias.R


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoticiasFragment : NoticiasFragmentOtro() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noticias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            // findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

}