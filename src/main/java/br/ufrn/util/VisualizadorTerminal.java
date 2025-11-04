package br.ufrn.util;

import br.ufrn.algoritmo.BellmanFord;
import br.ufrn.algoritmo.FloydWarshall;
import br.ufrn.algoritmo.Prim;
import br.ufrn.modelo.Aresta;

import java.util.List;

public class VisualizadorTerminal {

    public static void visualizarResultadoBellmanFord(BellmanFord.Resultado resultado, List<String> nomesVertices) {
        System.out.println("\nBELLMAN-FORD");
        System.out.println("Origem: " + nomesVertices.get(resultado.origem));
        System.out.println("Ciclo negativo: " + (resultado.temCicloNegativo ? "SIM" : "NAO"));
        
        System.out.println("\nDistancias minimas:");
        for (int i = 0; i < resultado.n; i++) {
            String dist = Double.isInfinite(resultado.distancias[i]) ? "INF" : String.format("%.2f", resultado.distancias[i]);
            System.out.println(nomesVertices.get(resultado.origem) + " -> " + nomesVertices.get(i) + ": " + dist);
        }
    }

    public static void visualizarResultadoFloydWarshall(FloydWarshall.Resultado resultado, List<String> nomesVertices) {
        System.out.println("\nFLOYD-WARSHALL");
        System.out.println("Ciclo negativo: " + (resultado.temCicloNegativo ? "SIM" : "NAO"));
        
        System.out.println("\nMatriz de distancias:");
        int n = resultado.distancias.length;
        
        System.out.printf("%6s", "");
        for (int j = 0; j < n; j++) {
            System.out.printf("%6s", nomesVertices.get(j));
        }
        System.out.println();
        
        for (int i = 0; i < n; i++) {
            System.out.printf("%6s", nomesVertices.get(i));
            for (int j = 0; j < n; j++) {
                String valor = Double.isInfinite(resultado.distancias[i][j]) ? "  INF" : String.format("%5.1f", resultado.distancias[i][j]);
                System.out.printf("%6s", valor);
            }
            System.out.println();
        }
    }

    public static void visualizarResultadoPrim(Prim.Resultado resultado, List<String> nomesVertices) {
        System.out.println("\nPRIM - ARVORE GERADORA MINIMA");
        System.out.printf("Peso total: %.2f\n", resultado.pesoTotal);
        
        System.out.println("\nArestas da AGM:");
        for (Aresta aresta : resultado.arestas) {
            System.out.println(nomesVertices.get(aresta.u) + " -- " + nomesVertices.get(aresta.v) + " (peso: " + String.format("%.2f", aresta.peso) + ")");
        }
    }

    public static void visualizarGrafo(LeituraDot.ResultadoLeitura dados) {
        System.out.println("Tipo: " + (dados.direcionado ? "Direcionado" : "Nao Direcionado"));
        System.out.println("Vertices: " + String.join(", ", dados.vertices));
    }
}