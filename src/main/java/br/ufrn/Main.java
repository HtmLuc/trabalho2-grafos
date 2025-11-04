package br.ufrn;

import br.ufrn.algoritmo.BellmanFord;
import br.ufrn.algoritmo.FloydWarshall;
import br.ufrn.algoritmo.Prim;
import br.ufrn.modelo.GrafoListaPonderado;
import br.ufrn.util.LeituraDot;
import br.ufrn.util.VisualizadorTerminal;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.log.guru.nidi", "error");
        
        System.out.println("GRAFOS - UFRN");
        
        Scanner scanner = new Scanner(System.in);
        
        try {
            while (true) {
                System.out.println("\nEscolha o grafo:");
                System.out.println("1 - Direcionado");
                System.out.println("2 - Nao direcionado"); 
                System.out.println("3 - Sair");
                System.out.print("Opcao: ");
                
                String opcao = scanner.nextLine().trim();
                
                if (opcao.equals("1")) {
                    executarGrafoDirecionado();
                } else if (opcao.equals("2")) {
                    executarGrafoNaoDirecionado();
                } else if (opcao.equals("3")) {
                    System.out.println("Saindo...");
                    break;
                } else {
                    System.out.println("Opcao invalida!");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void executarGrafoDirecionado() {
        try {
            System.out.println("\n--- GRAFO DIRECIONADO ---");
            
            LeituraDot leitor = new LeituraDot();
            LeituraDot.ResultadoLeitura dados = leitor.lerRecurso("samples/directed_graph.gv");
            
            // Informações básicas
            System.out.println("Vertices: " + dados.vertices.size() + ", Arestas: " + dados.arestas.size());
            
            List<String> nomesVertices = extrairVerticesEmOrdem(dados);
            Map<String, Integer> indiceVertice = new HashMap<>();
            for (int i = 0; i < nomesVertices.size(); i++) {
                indiceVertice.put(nomesVertices.get(i), i);
            }
            
            int origem = 0;
            
            // Executar algoritmos
            System.out.println("\nExecutando Bellman-Ford...");
            executarBellmanFord(dados, nomesVertices, indiceVertice, origem);
            
            System.out.println("\nExecutando Floyd-Warshall...");
            executarFloydWarshall(dados, nomesVertices, indiceVertice);
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void executarGrafoNaoDirecionado() {
        try {
            System.out.println("\n--- GRAFO NAO DIRECIONADO ---");
            
            LeituraDot leitor = new LeituraDot();
            LeituraDot.ResultadoLeitura dados = leitor.lerRecurso("samples/undirected_graph.gv");
            
            // Informações básicas
            System.out.println("Vertices: " + dados.vertices.size() + ", Arestas: " + dados.arestas.size());
            
            List<String> nomesVertices = extrairVerticesEmOrdem(dados);
            Map<String, Integer> indiceVertice = new HashMap<>();
            for (int i = 0; i < nomesVertices.size(); i++) {
                indiceVertice.put(nomesVertices.get(i), i);
            }
            
            int origem = 0;
            
            // Executar algoritmos
            System.out.println("\nExecutando Prim...");
            executarPrim(dados, nomesVertices, indiceVertice, origem);
            
            System.out.println("\nExecutando Floyd-Warshall...");
            executarFloydWarshall(dados, nomesVertices, indiceVertice);
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void executarPrim(LeituraDot.ResultadoLeitura dados, 
                                   List<String> nomesVertices, 
                                   Map<String, Integer> indiceVertice, 
                                   int origem) {
        try {
            GrafoListaPonderado grafo = new GrafoListaPonderado(nomesVertices.size());
            for (LeituraDot.ArestaPonderada a : dados.arestas) {
                int u = indiceVertice.get(a.u);
                int v = indiceVertice.get(a.v);
                grafo.adicionarAresta(u, v, a.peso);
            }
            
            Prim.Resultado resultado = Prim.agm(grafo, origem);
            VisualizadorTerminal.visualizarResultadoPrim(resultado, nomesVertices);
            
        } catch (Exception e) {
            System.out.println("Erro no Prim: " + e.getMessage());
        }
    }

    private static void executarBellmanFord(LeituraDot.ResultadoLeitura dados, 
                                          List<String> nomesVertices, 
                                          Map<String, Integer> indiceVertice, 
                                          int origem) {
        try {
            List<BellmanFord.Arco> arestas = new ArrayList<>();
            for (LeituraDot.ArestaPonderada a : dados.arestas) {
                int u = indiceVertice.get(a.u);
                int v = indiceVertice.get(a.v);
                if (dados.direcionado) {
                    BellmanFord.adicionarArestaDirecionada(arestas, u, v, a.peso);
                } else {
                    BellmanFord.adicionarArestaNaoDirecionada(arestas, u, v, a.peso);
                }
            }
            
            BellmanFord.Resultado resultado = BellmanFord.caminhosMinimosAPartirDe(
                nomesVertices.size(), arestas, origem);
            
            VisualizadorTerminal.visualizarResultadoBellmanFord(resultado, nomesVertices);
            
        } catch (Exception e) {
            System.out.println("Erro no Bellman-Ford: " + e.getMessage());
        }
    }

    private static void executarFloydWarshall(LeituraDot.ResultadoLeitura dados, 
                                            List<String> nomesVertices, 
                                            Map<String, Integer> indiceVertice) {
        try {
            int n = nomesVertices.size();
            double[][] matriz = FloydWarshall.matrizVazia(n);
            
            for (LeituraDot.ArestaPonderada a : dados.arestas) {
                int u = indiceVertice.get(a.u);
                int v = indiceVertice.get(a.v);
                if (dados.direcionado) {
                    FloydWarshall.adicionarArestaDirecionada(matriz, u, v, a.peso);
                } else {
                    FloydWarshall.adicionarArestaNaoDirecionada(matriz, u, v, a.peso);
                }
            }
            
            FloydWarshall.Resultado resultado = FloydWarshall.apsp(matriz);
            VisualizadorTerminal.visualizarResultadoFloydWarshall(resultado, nomesVertices);
            
        } catch (Exception e) {
            System.out.println("Erro no Floyd-Warshall: " + e.getMessage());
        }
    }

    private static List<String> extrairVerticesEmOrdem(LeituraDot.ResultadoLeitura dados) {
        List<String> vertices = new ArrayList<>();
        Set<String> adicionados = new HashSet<>();
        
        for (LeituraDot.ArestaPonderada aresta : dados.arestas) {
            if (!adicionados.contains(aresta.u)) {
                vertices.add(aresta.u);
                adicionados.add(aresta.u);
            }
            if (!adicionados.contains(aresta.v)) {
                vertices.add(aresta.v);
                adicionados.add(aresta.v);
            }
        }
        
        for (String vertice : dados.vertices) {
            if (!adicionados.contains(vertice)) {
                vertices.add(vertice);
                adicionados.add(vertice);
            }
        }
        
        return vertices;
    }
}