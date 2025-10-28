package br.ufrn.algoritmo;

import br.ufrn.modelo.Aresta;
import br.ufrn.modelo.Vizinho;
import br.ufrn.modelo.GrafoPonderadoNaoDirecionado;

import java.util.*;

public class Prim {

    public static class Resultado {
        public final List<Aresta> arestas;
        public final int[] pai;
        public final double pesoTotal;

        public Resultado(List<Aresta> arestas, int[] pai, double pesoTotal) {
            this.arestas = arestas;
            this.pai = pai;
            this.pesoTotal = pesoTotal;
        }
    }

    private static class No {
        int v;
        double chave;
        No(int v, double chave) { this.v = v; this.chave = chave; }
    }

    public static Resultado agm(GrafoPonderadoNaoDirecionado grafo, int inicio) {
        int n = grafo.n();
        if (n == 0) {
            return new Resultado(List.of(), new int[0], 0.0);
        }
        if (inicio < 0 || inicio >= n) {
            inicio = 0;
        }

        double[] chave = new double[n];
        int[] pai = new int[n];
        boolean[] naArvore = new boolean[n];

        Arrays.fill(pai, -1);
        Arrays.fill(chave, Double.POSITIVE_INFINITY);

        PriorityQueue<No> fila = new PriorityQueue<>(Comparator.comparingDouble(a -> a.chave));

        chave[inicio] = 0.0;
        fila.add(new No(inicio, 0.0));

        List<Aresta> arestasAGM = new ArrayList<>();
        double somaPesos = 0.0;

        while (!fila.isEmpty()) {
            No atual = fila.poll();
            int u = atual.v;

            if (naArvore[u]) {
                continue;
            }
            naArvore[u] = true;

            if (pai[u] != -1) {
                arestasAGM.add(new Aresta(pai[u], u, chave[u]));
                somaPesos += chave[u];
            }

            for (Vizinho viz : grafo.adj(u)) {
                int v = viz.v;
                double w = viz.peso;
                if (!naArvore[v] && w < chave[v]) {
                    chave[v] = w;
                    pai[v] = u;
                    fila.add(new No(v, chave[v]));
                }
            }
        }

        return new Resultado(arestasAGM, pai, somaPesos);
    }
}
