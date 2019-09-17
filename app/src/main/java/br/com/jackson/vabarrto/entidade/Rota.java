package br.com.jackson.vabarrto.entidade;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Rota {

    public Duracao duration;

    public Distancia distance;

    public String endAddress;
    public String startAddress;
    public LatLng endLocation;
    public LatLng startLocation;

    public List<LatLng> points;

    public String getDuracao(){
        return duration.description;
    }

    public String getDistancia(){
        return distance.description;
    }
}
