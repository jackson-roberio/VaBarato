package br.com.jackson.vabarrto;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import br.com.jackson.vabarrto.entidade.Rota;
import br.com.jackson.vabarrto.regra.DirectionFinder;
import br.com.jackson.vabarrto.util.Alerta;
import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_PADRAO;
import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_KM;
import static br.com.jackson.vabarrto.util.Constante.CUSTO_TAXA_TEMPO;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, IDirectionFinderListener {

    private GoogleMap mMap;

    private ProgressDialog progressDialog;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinesPaths = new ArrayList<>();

    private Double custoTaxaPadrao, custoTaxaKm, custoTaxaDuracao;

    private TextView txtDistancia, txtTempo, txtValor;

    private EditText edtOrigem, edtDestino;

    private Button btnCalcular;

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

        edtOrigem       = findViewById(R.id.edt_selecionar_origem);
        edtDestino      = findViewById(R.id.edt_selecionar_destino);

        btnCalcular     = findViewById(R.id.btn_recalcular);
        btnCalcular.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        tracarRota(view);
    }

    private void tracarRota(View view) {
        try{
            if(validar(view)) {
                String origem = edtOrigem.getText().toString();
                String destino = edtDestino.getText().toString();
                new DirectionFinder(this, origem, destino).execute();
            }
        } catch (UnsupportedEncodingException e){
            Alerta.mostrar(view, "Erro na execução ou busca de endereço.");
            System.out.println(e.getMessage());
        }

    }

    private boolean validar(View view){
        if(edtOrigem.getText().toString().isEmpty() || edtDestino.getText().toString().isEmpty()){
            Alerta.mostrar(view, "Endereço de Origem/Destino necessitam ter um valor válido");
            return false;
        }

        return true;
    }

    private void setarValoresCalculados(Rota rota) {
        txtDistancia.setText(rota.distance.description);
        txtTempo.setText(rota.duration.description);
        String valorTotal = "0";

//        txtDistancia.getText().toString().replace(" km", "")
        String distancia = rota.getDistancia().replace(",", "");

        Double distanciaPecorrida = Double.parseDouble(distancia.replace(" km", ""));


        /*
        *    Objetivo é pegar todos os dados e transformar em minutos
        *   Para a regra, levando em consideração que o primeiro a ser exibido
        *   é o dia, logo em seguida as horas e por último os minutos, então
        *   os valores são sendo retirados das strings de acordo com a sequência de exibição.
        * */
        Double duracaoTransladoMinutos = 0.0;

        String stringDuracao = rota.getDuracao();

        //Ver dias
        int posicaoRetirarDuracaoDias = stringDuracao.indexOf("days");

        if(posicaoRetirarDuracaoDias != -1){
            String valorDiasString = stringDuracao.replace(stringDuracao.substring(posicaoRetirarDuracaoDias), "");
            duracaoTransladoMinutos = duracaoTransladoMinutos + (Double.parseDouble(valorDiasString) * 1440); // 24h x 60min = 1440min
            stringDuracao = stringDuracao.replace(stringDuracao.substring(0, posicaoRetirarDuracaoDias + 4), "");
        } else if (stringDuracao.indexOf("day") != -1){
            duracaoTransladoMinutos = duracaoTransladoMinutos + 1440; //1440 é o valor de minutos em um dia.
            stringDuracao = stringDuracao.replace(stringDuracao.substring(0, posicaoRetirarDuracaoDias + 3), "");
        }

        //Ver horas
        int posicaoRetirarDuracaoHoras = stringDuracao.indexOf("hours");

        if (posicaoRetirarDuracaoHoras != -1) {
            String valorHorasString = stringDuracao.replace(stringDuracao.substring(posicaoRetirarDuracaoHoras), "");
            duracaoTransladoMinutos = duracaoTransladoMinutos + (Double.parseDouble(valorHorasString) * 60);
            stringDuracao = stringDuracao.replace(stringDuracao.substring(0, posicaoRetirarDuracaoHoras + 5), "");
        } else if(rota.getDuracao().indexOf("hour") != -1) { //se tiver só uma hora
            duracaoTransladoMinutos = duracaoTransladoMinutos + 60;
            stringDuracao = stringDuracao.replace(stringDuracao.substring(0, posicaoRetirarDuracaoHoras + 4), "");
        }

        //Ver minutos
        int posicaoRetirarDuracaoMinutos = stringDuracao.indexOf("min");

        if(posicaoRetirarDuracaoMinutos != -1){
            String valorMinutos = stringDuracao.replace(stringDuracao.substring(posicaoRetirarDuracaoMinutos), "");
            duracaoTransladoMinutos = duracaoTransladoMinutos + Double.parseDouble(valorMinutos);
        }

        System.out.println(duracaoTransladoMinutos);

        Double calculoValorTotal = custoTaxaPadrao + (distanciaPecorrida * custoTaxaKm) + (duracaoTransladoMinutos * custoTaxaDuracao);

        valorTotal = calculoValorTotal.toString();

        txtValor.setText(valorTotal);
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


    @Override
    public void onDirectionFinderStart() {
        progressDialog = progressDialog.show(this, "Aguarde!", "Traçando rotas ...", true);
        if(originMarkers != null){
            for(Marker marker : originMarkers){
                marker.remove();
            }
        }
        if(destinationMarkers != null){
            for(Marker marker : destinationMarkers){
                marker.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Rota> routes) {
        progressDialog.dismiss();
        polylinesPaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for(Rota route : routes){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 15));
            setarValoresCalculados(route);

//            txtTempo.setText(route.duration.description);
//            txtDistancia.setText(route.distance.description);

//            Marcadores
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(route.startAddress)
                    .position(route.startLocation)
            ));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(route.endAddress)
                    .position(route.endLocation)
            ));

//            Polyline
            PolylineOptions polylineOptions = new PolylineOptions().geodesic(true).color(Color.RED).width(10);
            for(int i= 0; i < route.points.size(); i++){
                polylineOptions.add(route.points.get(i));
            }
            polylinesPaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
