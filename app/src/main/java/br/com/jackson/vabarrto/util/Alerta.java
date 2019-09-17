package br.com.jackson.vabarrto.util;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Alerta {

    public static void mostrar(View view, String texto) {
        Snackbar.make(view, texto, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
