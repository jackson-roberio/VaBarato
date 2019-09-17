package br.com.jackson.vabarrto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import static br.com.jackson.vabarrto.util.Constante.CARREGAR_EM_MILIISEGUNDOS;

public class TelaDeAberturaActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_de_abertura);
        Handler thread = new Handler();
        thread.postDelayed(this, CARREGAR_EM_MILIISEGUNDOS);
    }

    @Override
    public void run() {
        irTelaInicial();
    }

    private void irTelaInicial(){
        Intent i = new Intent(TelaDeAberturaActivity.this, DefinirTaxaActivity.class);
        startActivity(i);
        finish();
    }
}
