package br.ufrn.modelo;

import java.util.List;

public interface GrafoPonderadoNaoDirecionado {
    int n();
    List<Vizinho> adj(int u);
}