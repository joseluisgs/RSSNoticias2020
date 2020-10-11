package com.joseluisgs.rssnoticias

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    private val TIME: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ocultamos los elementos que no queremos que salgan
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        //crea un intent para ir al activity main
        val main = Intent(this, MainActivity::class.java)

        //un hilo con duración 1'5 s que empieza el intent cuando transcurra el tiempo y  se cierra.
        Handler().postDelayed(Runnable {
            startActivity(main)
            finish()
        }, this.TIME)

    }
}