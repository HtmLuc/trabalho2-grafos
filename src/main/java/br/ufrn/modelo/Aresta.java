package br.ufrn.modelo;

public class Aresta {
    public final int u;
    public final int v;
    public final double peso;

    public Aresta(int u, int v, double peso) {
        this.u = u;
        this.v = v;
        this.peso = peso;
    }
}
