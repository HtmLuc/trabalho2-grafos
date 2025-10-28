package br.ufrn.modelo;

import java.util.ArrayList;
import java.util.List;

public class GrafoListaPonderado implements GrafoPonderadoNaoDirecionado {
    private final List<List<Vizinho>> adj;

    public GrafoListaPonderado(int n) {
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void adicionarAresta(int u, int v, double peso) {
        adj.get(u).add(new Vizinho(v, peso));
        adj.get(v).add(new Vizinho(u, peso));
    }

    @Override public int n() {
        return adj.size();
    }
    @Override public List<Vizinho> adj(int u) {
        return adj.get(u);
    }
}
