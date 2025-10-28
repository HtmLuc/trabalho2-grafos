package br.ufrn.algoritmo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloydWarshall {

    public static class Resultado {
        public final double[][] distancias;
        public final int[][] proximo;
        public final boolean temCicloNegativo;

        private Resultado(double[][] distancias, int[][] proximo, boolean temCicloNegativo) {
            this.distancias = distancias;
            this.proximo = proximo;
            this.temCicloNegativo = temCicloNegativo;
        }

        public List<Integer> reconstruirCaminho(int origem, int destino) {
            if (origem < 0 || destino < 0 ||
                    origem >= distancias.length || destino >= distancias.length) {
                return List.of();
            }

            if (proximo[origem][destino] == -1){
                return List.of();
            }

            for (int v = 0; v < distancias.length; v++) {
                if (distancias[v][v] < 0) return List.of();
            }

            List<Integer> caminho = new ArrayList<>();
            int atual = origem;
            caminho.add(atual);
            while (atual != destino) {
                atual = proximo[atual][destino];
                if (atual == -1)
                {
                    return List.of();
                }
                caminho.add(atual);
            }
            return caminho;
        }
    }

    public static Resultado apsp(double[][] pesos) {
        final int n = pesos.length;
        final double INF = Double.POSITIVE_INFINITY;

        double[][] distancia = new double[n][n];
        int[][] proximo = new int[n][n];

        for (int i = 0; i < n; i++) {
            if (pesos[i].length != n) {
                throw new IllegalArgumentException("Matriz de pesos deve ser n x n.");
            }
            for (int j = 0; j < n; j++) {
                distancia[i][j] = pesos[i][j];
                if (i != j && pesos[i][j] < INF) {
                    proximo[i][j] = j;
                } else {
                    proximo[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (distancia[i][k] == INF) continue;
                for (int j = 0; j < n; j++) {
                    if (distancia[k][j] == INF) continue;

                    double viaK = distancia[i][k] + distancia[k][j];
                    if (viaK < distancia[i][j]) {
                        distancia[i][j] = viaK;
                        proximo[i][j] = proximo[i][k];
                    }
                }
            }
        }

        boolean cicloNegativo = false;
        for (int v = 0; v < n; v++) {
            if (distancia[v][v] < 0.0) {
                cicloNegativo = true;
                break;
            }
        }

        return new Resultado(distancia, proximo, cicloNegativo);
    }

    public static double[][] matrizVazia(int n) {
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(m[i], Double.POSITIVE_INFINITY);
            m[i][i] = 0.0;
        }
        return m;
    }

    public static void adicionarArestaNaoDirecionada(double[][] m, int u, int v, double peso) {
        m[u][v] = Math.min(m[u][v], peso);
        m[v][u] = Math.min(m[v][u], peso);
    }

    public static void adicionarArestaDirecionada(double[][] m, int u, int v, double peso) {
        m[u][v] = Math.min(m[u][v], peso);
    }
}