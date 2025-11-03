package br.ufrn.util;

import br.ufrn.algoritmo.BellmanFord;
import br.ufrn.algoritmo.FloydWarshall;
import br.ufrn.algoritmo.Prim;
import br.ufrn.modelo.Aresta;

import java.util.List;

public class VisualizadorTerminal {

    public static void visualizarResultadoBellmanFord(BellmanFord.Resultado resultado, List<String> nomesVertices) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("BELLMAN-FORD - RESULTADOS");
        System.out.println("=".repeat(60));
        
        System.out.printf("Origem: %s (vertice %d)\n", nomesVertices.get(resultado.origem), resultado.origem);
        System.out.println("Ciclo negativo: " + (resultado.temCicloNegativo ? "SIM" : "NAO"));
        
        System.out.println("\n--- DISTANCIAS MINIMAS ---");
        for (int i = 0; i < resultado.n; i++) {
            String dist = Double.isInfinite(resultado.distancias[i]) ? "INF" : String.format("%.2f", resultado.distancias[i]);
            System.out.printf("%s -> %s: %s\n", nomesVertices.get(resultado.origem), nomesVertices.get(i), dist);
        }
        
        if (!resultado.temCicloNegativo) {
            System.out.println("\n--- CAMINHOS MINIMOS ---");
            for (int i = 0; i < resultado.n; i++) {
                if (i != resultado.origem && !Double.isInfinite(resultado.distancias[i])) {
                    List<Integer> caminho = resultado.reconstruirCaminho(i);
                    if (!caminho.isEmpty()) {
                        System.out.printf("%s -> %s: ", nomesVertices.get(resultado.origem), nomesVertices.get(i));
                        System.out.println(formatarCaminho(caminho, nomesVertices));
                    }
                }
            }
        }
        
        System.out.println("\n--- VETORES DE CONTROLE ---");
        System.out.println("Vertice | Distancia | Pai");
        System.out.println("--------|-----------|-----");
        for (int i = 0; i < resultado.n; i++) {
            String distStr = Double.isInfinite(resultado.distancias[i]) ? "INF" : String.format("%.2f", resultado.distancias[i]);
            String paiStr = resultado.pai[i] == -1 ? "-" : nomesVertices.get(resultado.pai[i]);
            System.out.printf("%-7s | %-9s | %s\n", nomesVertices.get(i), distStr, paiStr);
        }
    }

    public static void visualizarResultadoFloydWarshall(FloydWarshall.Resultado resultado, List<String> nomesVertices) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FLOYD-WARSHALL - RESULTADOS");
        System.out.println("=".repeat(60));
        
        System.out.println("Ciclo negativo: " + (resultado.temCicloNegativo ? "SIM" : "NAO"));
        
        System.out.println("\n--- MATRIZ DE DISTANCIAS ---");
        int n = resultado.distancias.length;
        
        // Cabeçalho
        System.out.printf("%6s", "");
        for (int j = 0; j < n; j++) {
            System.out.printf("%6s", nomesVertices.get(j));
        }
        System.out.println();
        
        // Valores
        for (int i = 0; i < n; i++) {
            System.out.printf("%6s", nomesVertices.get(i));
            for (int j = 0; j < n; j++) {
                String valor;
                if (Double.isInfinite(resultado.distancias[i][j])) {
                    valor = "  INF";
                } else {
                    valor = String.format("%5.1f", resultado.distancias[i][j]);
                }
                System.out.printf("%6s", valor);
            }
            System.out.println();
        }
        
        if (!resultado.temCicloNegativo) {
            System.out.println("\n--- EXEMPLOS DE CAMINHOS ---");
            int count = 0;
            for (int i = 0; i < n && count < 3; i++) {
                for (int j = 0; j < n && count < 3; j++) {
                    if (i != j && !Double.isInfinite(resultado.distancias[i][j])) {
                        List<Integer> caminho = resultado.reconstruirCaminho(i, j);
                        if (!caminho.isEmpty()) {
                            System.out.printf("%s -> %s: ", nomesVertices.get(i), nomesVertices.get(j));
                            System.out.println(formatarCaminho(caminho, nomesVertices));
                            count++;
                        }
                    }
                }
            }
        }
    }

    public static void visualizarResultadoPrim(Prim.Resultado resultado, List<String> nomesVertices) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PRIM - ARVORE GERADORA MINIMA");
        System.out.println("=".repeat(60));
        
        System.out.printf("Peso total: %.2f\n", resultado.pesoTotal);
        
        System.out.println("\n--- ARESTAS DA AGM ---");
        for (Aresta aresta : resultado.arestas) {
            System.out.printf("%s -- %s (peso: %.2f)\n", 
                nomesVertices.get(aresta.u), 
                nomesVertices.get(aresta.v), 
                aresta.peso);
        }
        
        System.out.println("\n--- ESTRUTURA DA ARVORE ---");
        for (int i = 0; i < resultado.pai.length; i++) {
            String paiStr = resultado.pai[i] == -1 ? "RAIZ" : nomesVertices.get(resultado.pai[i]);
            System.out.printf("%s <- %s\n", nomesVertices.get(i), paiStr);
        }
    }

    public static void visualizarGrafo(LeituraDot.ResultadoLeitura dados) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("INFORMACOES DO GRAFO");
        System.out.println("=".repeat(60));
        
        System.out.println("Tipo: " + (dados.direcionado ? "Direcionado" : "Nao Direcionado"));
        System.out.println("Vertices: " + dados.vertices.size());
        System.out.println("Arestas: " + dados.arestas.size());
        
        System.out.println("\n--- VERTICES ---");
        System.out.println(String.join(", ", dados.vertices));
        
        System.out.println("\n--- ARESTAS ---");
        for (LeituraDot.ArestaPonderada aresta : dados.arestas) {
            String simbolo = dados.direcionado ? "->" : "--";
            System.out.printf("%s %s %s (peso: %.2f)\n", 
                aresta.u, simbolo, aresta.v, aresta.peso);
        }
    }

    private static String formatarCaminho(List<Integer> caminho, List<String> nomesVertices) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < caminho.size(); i++) {
            sb.append(nomesVertices.get(caminho.get(i)));
            if (i < caminho.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
}