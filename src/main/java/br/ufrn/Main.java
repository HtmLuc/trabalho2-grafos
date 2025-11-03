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
        // Configurar para suprimir warnings do Graphviz
        System.setProperty("org.slf4j.simpleLogger.log.guru.nidi", "error");
        
        System.out.println("DIM0549 GRAFOS - UFRN");
        System.out.println("Algoritmos: Prim, Bellman-Ford e Floyd-Warshall");
        
        Scanner scanner = new Scanner(System.in);
        
        try {
            boolean executando = true;
            
            while (executando) {
                System.out.println("\n" + "=".repeat(60));
                System.out.println("MENU PRINCIPAL - TIPO DE GRAFO");
                System.out.println("=".repeat(60));
                System.out.println("1) Grafo Direcionado");
                System.out.println("2) Grafo Nao Direcionado");
                System.out.println("3) Sair");
                System.out.println("=".repeat(60));
                System.out.print("Selecione uma opcao (1-3): ");
                
                String opcao = scanner.nextLine().trim();
                
                switch (opcao) {
                    case "1":
                        executarComGrafoDirecionado(scanner);
                        break;
                    case "2":
                        executarComGrafoNaoDirecionado(scanner);
                        break;
                    case "3":
                        executando = false;
                        System.out.println("\nSistema encerrado.");
                        break;
                    default:
                        System.out.println("Opcao invalida! Tente novamente.");
                }
            }
            
        } catch (Exception e) {
            System.out.println("\nERRO: Falha no sistema: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void executarComGrafoDirecionado(Scanner scanner) {
        try {
            System.out.println("\n" + ">".repeat(60));
            System.out.println("GRAFO DIRECIONADO");
            System.out.println(">".repeat(60));
            
            System.out.println("Carregando grafo direcionado...");
            
            // Carregar grafo direcionado
            LeituraDot leitor = new LeituraDot();
            LeituraDot.ResultadoLeitura dados = leitor.lerRecurso("samples/directed_graph.gv");
            
            // Visualizar informações básicas do grafo
            VisualizadorTerminal.visualizarGrafo(dados);
            
            // Converter nomes para índices
            List<String> nomesVertices = extrairVerticesEmOrdem(dados);
            Map<String, Integer> indiceVertice = new HashMap<>();
            for (int i = 0; i < nomesVertices.size(); i++) {
                indiceVertice.put(nomesVertices.get(i), i);
            }
            
            System.out.println("\nMAPA DE VERTICES (para referencia):");
            for (int i = 0; i < nomesVertices.size(); i++) {
                System.out.printf("  %d: %s\n", i, nomesVertices.get(i));
            }
            
            // Menu específico para grafos direcionados
            executarMenuGrafoDirecionado(scanner, dados, nomesVertices, indiceVertice);
            
        } catch (Exception e) {
            System.out.println("\nERRO: Falha ao carregar grafo direcionado: " + e.getMessage());
        }
    }

    private static void executarComGrafoNaoDirecionado(Scanner scanner) {
        try {
            System.out.println("\n" + ">".repeat(60));
            System.out.println("GRAFO NAO DIRECIONADO");
            System.out.println(">".repeat(60));
            
            System.out.println("Carregando grafo nao direcionado...");
            
            // Carregar grafo não direcionado
            LeituraDot leitor = new LeituraDot();
            LeituraDot.ResultadoLeitura dados = leitor.lerRecurso("samples/undirected_graph.gv");
            
            // Visualizar informações básicas do grafo
            VisualizadorTerminal.visualizarGrafo(dados);
            
            // Converter nomes para índices
            List<String> nomesVertices = extrairVerticesEmOrdem(dados);
            Map<String, Integer> indiceVertice = new HashMap<>();
            for (int i = 0; i < nomesVertices.size(); i++) {
                indiceVertice.put(nomesVertices.get(i), i);
            }
            
            System.out.println("\nMAPA DE VERTICES (para referencia):");
            for (int i = 0; i < nomesVertices.size(); i++) {
                System.out.printf("  %d: %s\n", i, nomesVertices.get(i));
            }
            
            // Menu específico para grafos não direcionados
            executarMenuGrafoNaoDirecionado(scanner, dados, nomesVertices, indiceVertice);
            
        } catch (Exception e) {
            System.out.println("\nERRO: Falha ao carregar grafo nao direcionado: " + e.getMessage());
        }
    }

    private static void executarMenuGrafoDirecionado(Scanner scanner, LeituraDot.ResultadoLeitura dados, 
                                                   List<String> nomesVertices, Map<String, Integer> indiceVertice) {
        
        boolean voltar = false;
        int origemPadrao = 0;
        
        while (!voltar) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("ALGORITMOS PARA GRAFOS DIRECIONADOS");
            System.out.println("=".repeat(60));
            System.out.println("1) Executar Bellman-Ford (Caminhos Minimos - Origem Unica)");
            System.out.println("2) Executar Floyd-Warshall (Caminhos Minimos - Todos Pares)");
            System.out.println("3) Executar todos os algoritmos disponiveis");
            System.out.println("4) Voltar ao menu principal");
            System.out.println("=".repeat(60));
            System.out.println("OBS: Prim nao disponivel para grafos direcionados");
            System.out.println("=".repeat(60));
            System.out.print("Selecione uma opcao (1-4): ");
            
            String opcao = scanner.nextLine().trim();
            
            switch (opcao) {
                case "1":
                    executarBellmanFord(dados, nomesVertices, indiceVertice, origemPadrao);
                    break;
                case "2":
                    executarFloydWarshall(dados, nomesVertices, indiceVertice);
                    break;
                case "3":
                    executarTodosAlgoritmosDirecionados(dados, nomesVertices, indiceVertice);
                    break;
                case "4":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opcao invalida! Tente novamente.");
            }
            
            if (!voltar && !opcao.equals("3")) {
                System.out.print("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void executarMenuGrafoNaoDirecionado(Scanner scanner, LeituraDot.ResultadoLeitura dados, 
                                                      List<String> nomesVertices, Map<String, Integer> indiceVertice) {
        
        boolean voltar = false;
        int origemPadrao = 0;
        
        while (!voltar) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("ALGORITMOS PARA GRAFOS NAO DIRECIONADOS");
            System.out.println("=".repeat(60));
            System.out.println("1) Executar Prim (Arvore Geradora Minima)");
            System.out.println("2) Executar Floyd-Warshall (Caminhos Minimos - Todos Pares)");
            System.out.println("3) Executar todos os algoritmos disponiveis");
            System.out.println("4) Voltar ao menu principal");
            System.out.println("=".repeat(60));
            System.out.println("OBS: Bellman-Ford nao recomendado para grafos nao direcionados");
            System.out.println("=".repeat(60));
            System.out.print("Selecione uma opcao (1-4): ");
            
            String opcao = scanner.nextLine().trim();
            
            switch (opcao) {
                case "1":
                    executarPrim(dados, nomesVertices, indiceVertice, origemPadrao);
                    break;
                case "2":
                    executarFloydWarshall(dados, nomesVertices, indiceVertice);
                    break;
                case "3":
                    executarTodosAlgoritmosNaoDirecionados(dados, nomesVertices, indiceVertice);
                    break;
                case "4":
                    voltar = true;
                    break;
                default:
                    System.out.println("Opcao invalida! Tente novamente.");
            }
            
            if (!voltar && !opcao.equals("3")) {
                System.out.print("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void executarTodosAlgoritmosDirecionados(LeituraDot.ResultadoLeitura dados, 
                                                          List<String> nomesVertices, 
                                                          Map<String, Integer> indiceVertice) {
        
        int origemPadrao = 0;
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("EXECUTANDO TODOS OS ALGORITMOS PARA GRAFO DIRECIONADO");
        System.out.println("Vertice padrao para origem: " + nomesVertices.get(origemPadrao) + " (indice 0)");
        System.out.println("=".repeat(60));
        
        // BELLMAN-FORD
        executarBellmanFord(dados, nomesVertices, indiceVertice, origemPadrao);
        
        // FLOYD-WARSHALL  
        executarFloydWarshall(dados, nomesVertices, indiceVertice);
        
        System.out.println("\nSUCESSO: Todos os algoritmos executados!");
    }

    private static void executarTodosAlgoritmosNaoDirecionados(LeituraDot.ResultadoLeitura dados, 
                                                             List<String> nomesVertices, 
                                                             Map<String, Integer> indiceVertice) {
        
        int origemPadrao = 0;
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("EXECUTANDO TODOS OS ALGORITMOS PARA GRAFO NAO DIRECIONADO");
        System.out.println("Vertice padrao para origem: " + nomesVertices.get(origemPadrao) + " (indice 0)");
        System.out.println("=".repeat(60));
        
        // PRIM
        executarPrim(dados, nomesVertices, indiceVertice, origemPadrao);
        
        // FLOYD-WARSHALL  
        executarFloydWarshall(dados, nomesVertices, indiceVertice);
        
        System.out.println("\nSUCESSO: Todos os algoritmos executados!");
    }

    private static void executarPrim(LeituraDot.ResultadoLeitura dados, 
                                   List<String> nomesVertices, 
                                   Map<String, Integer> indiceVertice, 
                                   int origem) {
        try {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("ALGORITMO DE PRIM - ARVORE GERADORA MINIMA");
            System.out.println("-".repeat(60));
            
            GrafoListaPonderado grafo = new GrafoListaPonderado(nomesVertices.size());
            for (LeituraDot.ArestaPonderada a : dados.arestas) {
                int u = indiceVertice.get(a.u);
                int v = indiceVertice.get(a.v);
                grafo.adicionarAresta(u, v, a.peso);
            }
            
            Prim.Resultado resultado = Prim.agm(grafo, origem);
            VisualizadorTerminal.visualizarResultadoPrim(resultado, nomesVertices);
            
        } catch (Exception e) {
            System.out.println("\nERRO no Prim: " + e.getMessage());
        }
    }

    private static void executarBellmanFord(LeituraDot.ResultadoLeitura dados, 
                                          List<String> nomesVertices, 
                                          Map<String, Integer> indiceVertice, 
                                          int origem) {
        try {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("ALGORITMO DE BELLMAN-FORD - CAMINHOS MINIMOS (ORIGEM UNICA)");
            System.out.println("-".repeat(60));
            System.out.println("Origem: " + nomesVertices.get(origem) + " (vertice " + origem + ")");
            
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
            System.out.println("\nERRO no Bellman-Ford: " + e.getMessage());
        }
    }

    private static void executarFloydWarshall(LeituraDot.ResultadoLeitura dados, 
                                            List<String> nomesVertices, 
                                            Map<String, Integer> indiceVertice) {
        try {
            System.out.println("\n" + "-".repeat(60));
            System.out.println("ALGORITMO DE FLOYD-WARSHALL - CAMINHOS MINIMOS (TODOS PARES)");
            System.out.println("-".repeat(60));
            
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
            System.out.println("\nERRO no Floyd-Warshall: " + e.getMessage());
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