package br.ufrn.util;

import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.parse.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class LeituraDot {

    public static final class ResultadoLeitura {
        public final boolean direcionado;
        public final LinkedHashSet<String> vertices = new LinkedHashSet<>();
        public final ArrayList<ArestaPonderada> arestas = new ArrayList<>();
        ResultadoLeitura(boolean direcionado) { this.direcionado = direcionado; }
    }

    public static final class ArestaPonderada {
        public final String u, v;
        public final double peso;
        public ArestaPonderada(String u, String v, double peso) {
            this.u = u; this.v = v; this.peso = peso;
        }
        @Override public String toString() { return u + " -> " + v + " (peso=" + peso + ")"; }
    }

    public ResultadoLeitura lerRecurso(String nomeArquivo) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(nomeArquivo);
        if (stream == null) {
            throw new IOException("Arquivo '" + nomeArquivo + "' não encontrado em resources.");
        }

        MutableGraph g = new Parser().read(stream);
        ResultadoLeitura out = new ResultadoLeitura(g.isDirected());

        for (MutableNode node : g.nodes()) {
            out.vertices.add(node.name().toString());
        }

        Set<String> vistos = new HashSet<>();
        for (MutableNode origem : g.nodes()) {
            String a = origem.name().toString();
            for (Link lig : origem.links()) {
                String b = nomeDoAlvo(lig);
                if (b == null) continue;

                double peso = extraiPeso(lig);

                if (g.isDirected()) {
                    String chave = a + "->" + b;
                    if (vistos.add(chave)) out.arestas.add(new ArestaPonderada(a, b, peso));
                } else {
                    String x = a.compareTo(b) <= 0 ? a : b;
                    String y = a.compareTo(b) <= 0 ? b : a;
                    String chave = x + "--" + y;
                    if (vistos.add(chave)) out.arestas.add(new ArestaPonderada(x, y, peso));
                }
            }
        }
        return out;
    }

    private static String nomeDoAlvo(Link lig) {
        LinkTarget alvo = lig.to();
        if (alvo instanceof MutableNode mn) return mn.name().toString();
        String s = String.valueOf(alvo);
        return s.isBlank() ? null : s;
    }

    private static double extraiPeso(Link lig) {
        Object w = lig.attrs().get("weight");
        if (w == null) w = lig.attrs().get("label");
        if (w == null) return 1.0;

        String txt = w.toString().trim();
        if ((txt.startsWith("\"") && txt.endsWith("\"")) || (txt.startsWith("'") && txt.endsWith("'"))) {
            txt = txt.substring(1, txt.length() - 1).trim();
        }
        try { return Double.parseDouble(txt); }
        catch (NumberFormatException e) { return 1.0; }
    }
}
