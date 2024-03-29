package br.com.jackson.vabarrto;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import br.com.jackson.vabarrto.util.Alerta;
import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_PADRAO;
import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_KM;
import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_TEMPO;

public class DefinirTaxaActivity extends AppCompatActivity {

    private EditText edtTaxaPadrao, edtTaxaTempo, edtValorKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir_taxa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        popularVariaveis();
    }

    private void popularVariaveis() {
        edtTaxaPadrao   = findViewById(R.id.edt_taxa_padrao);
        edtTaxaTempo    = findViewById(R.id.edt_taxa_tempo);
        edtValorKm      = findViewById(R.id.edt_valor_km);
    }


    public void irTelaPrincipal(View view) {
        Intent i = new Intent(DefinirTaxaActivity.this, MainActivity.class);
        if(validar(view)) {
            i.putExtra(CUSTO_TAXA_PADRAO, emDouble(edtTaxaPadrao));
            i.putExtra(CUSTO_TAXA_KM, emDouble(edtValorKm));
            i.putExtra(CUSTO_TAXA_TEMPO, emDouble(edtTaxaTempo));
            startActivity(i);
        }
    }

    /**
     * Confere se os campos de entrada (Taxa padrão, Valor KM e Taxa por tempo de serviço)
     * estão com algum valor. Envia mensagem ao usuário, caso exista alguns desses campos de
     * entrada como vazio.
     *
     * @return boolean, se o valor do campo de entrada estiver vazio, ele retorna falso.
     **/
    private boolean validar(View view) {
        boolean retorno = false;

        if(edtTaxaPadrao.getText().toString().isEmpty())
            Alerta.mostrar(view, "O campo \"Taxa padrão\" não pode ser vazio.");
        else if (edtValorKm.getText().toString().isEmpty())
            Alerta.mostrar(view,"O campo \"Valor por KM rodado\" não pode ser vazio.");
        else if (edtTaxaTempo.getText().toString().isEmpty())
            Alerta.mostrar(view,"O campo \"Taxa por minuto de serviço\" não pode ser vazio.");
         else
            retorno = true;

        return retorno;
    }

    private Double emDouble(EditText edt){
        return Double.parseDouble(edt.getText().toString());
    }
}
