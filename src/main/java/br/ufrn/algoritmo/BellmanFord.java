package br.ufrn.algoritmo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BellmanFord {
    public static class Arco {
        public final int u;
        public final int v;
        public final double peso;

        public Arco(int u, int v, double peso) {
            this.u = u;
            this.v = v;
            this.peso = peso;
        }

        @Override public String toString() { return u + " -> " + v + " (w=" + peso + ")"; }
    }

    public static class Resultado {
        public final int n;
        public final int origem;
        public final double[] distancias;
        public final int[] pai;
        public final boolean temCicloNegativo;

        private Resultado(int n, int origem, double[] dist, int[] pai, boolean neg) {
            this.n = n;
            this.origem = origem;
            this.distancias = dist;
            this.pai = pai;
            this.temCicloNegativo = neg;
        }

        public List<Integer> reconstruirCaminho(int destino) {
            if (destino < 0 || destino >= n) {
                return List.of();
            }

            if (temCicloNegativo) {
                return List.of();
            }

            if (Double.isInfinite(distancias[destino])) {
                return List.of();
            }

            List<Integer> caminho = new ArrayList<>();
            int atual = destino;
            while (atual != -1) {
                caminho.add(atual);
                if (atual == origem) break;
                atual = pai[atual];
            }

            if (caminho.get(caminho.size() - 1) != origem) {
                return List.of();
            }

            Collections.reverse(caminho);
            return caminho;
        }
    }

    public static Resultado caminhosMinimosAPartirDe(int n, List<Arco> arestas, int origem) {
        if (origem < 0 || origem >= n) {
            origem = 0;
        }

        final double INF = Double.POSITIVE_INFINITY;

        double[] dist = new double[n];
        int[] pai = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(pai, -1);
        dist[origem] = 0.0;

        for (int i = 1; i <= n - 1; i++) {
            boolean atualizou = false;
            for (Arco a : arestas) {
                if (!Double.isInfinite(dist[a.u]) && dist[a.u] + a.peso < dist[a.v]) {
                    dist[a.v] = dist[a.u] + a.peso;
                    pai[a.v] = a.u;
                    atualizou = true;
                }
            }
            if (!atualizou) break;
        }

        boolean neg = false;
        for (Arco a : arestas) {
            if (!Double.isInfinite(dist[a.u]) && dist[a.u] + a.peso < dist[a.v]) {
                neg = true;
                break;
            }
        }

        return new Resultado(n, origem, dist, pai, neg);
    }

    public static void adicionarArestaDirecionada(List<Arco> E, int u, int v, double peso) {
        for (int i = 0; i < E.size(); i++) {
            Arco a = E.get(i);
            if (a.u == u && a.v == v) {
                if (peso < a.peso) E.set(i, new Arco(u, v, peso));
                return;
            }
        }
        E.add(new Arco(u, v, peso));
    }

    public static void adicionarArestaNaoDirecionada(List<Arco> E, int u, int v, double peso) {
        adicionarArestaDirecionada(E, u, v, peso);
        adicionarArestaDirecionada(E, v, u, peso);
    }
}