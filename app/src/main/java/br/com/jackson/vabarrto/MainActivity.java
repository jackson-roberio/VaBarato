package br.com.jackson.vabarrto;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_PADRAO;
import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_KM;
import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_TEMPO;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Double custoTaxaPadrao, custoTaxaKm, custoTaxaDuracao;

    private TextView txtDistancia, txtTempo, txtValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popularVariaveis();
    }

    private void popularVariaveis() {
        resgastarValoresDasTaxas();
        vincularViewsDoLayout();
    }

    private void vincularViewsDoLayout() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        txtDistancia    = findViewById(R.id.txt_distancia_pecorrida);
        txtTempo        = findViewById(R.id.txt_duracao_translado);
        txtValor        = findViewById(R.id.txt_valor_para_pagar);

        setarValoresCalculados();
    }

    private void setarValoresCalculados() {
        txtDistancia.setText(custoTaxaKm.toString());
        txtTempo.setText(custoTaxaDuracao.toString());
        txtValor.setText(custoTaxaPadrao.toString());
    }

    private void resgastarValoresDasTaxas() {
        Bundle extras       = getIntent().getExtras();
        custoTaxaPadrao     = extras.getDouble(CUSTO_TAXA_PADRAO);
        custoTaxaKm         = extras.getDouble(CUSTO_TAXA_KM);
        custoTaxaDuracao    = extras.getDouble(CUSTO_TAXA_TEMPO);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
