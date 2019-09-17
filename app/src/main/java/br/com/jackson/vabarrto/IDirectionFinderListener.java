package br.com.jackson.vabarrto;

import java.util.List;

import br.com.jackson.vabarrto.entidade.Rota;

public interface IDirectionFinderListener {

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Rota> routes);
}
