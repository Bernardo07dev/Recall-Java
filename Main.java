import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import empresa.Empresa;
import arquivo.ArquivoPDF;
import insight.Insight;
import insight.Categoria;
import insight.Criticidade;

public class Main {
    private static List<Empresa> empresas = new ArrayList<>();
    private static Map<String, List<Insight>> insightsPorEmpresa = new HashMap<>();
    private static Map<String, Integer> qtdArquivosPorEmpresa = new HashMap<>();

    public static void main(String[] args) {
        while (true) {
            List<String> opcoesMenu = new ArrayList<>();
            opcoesMenu.add("➕ Criar Nova Empresa");

            for (Empresa e : empresas) {
                opcoesMenu.add("🏢 Acessar: " + e.getNome());
            }
            opcoesMenu.add("❌ Sair");

            String escolha = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecione uma opção:",
                    "Menu Principal",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoesMenu.toArray(),
                    opcoesMenu.get(0));


            if (escolha == null || escolha.equals("❌ Sair")) {
                break;
            }


            if (escolha.equals("➕ Criar Nova Empresa")) {
                String nome = JOptionPane.showInputDialog("Nome da Empresa:");
                String industry = JOptionPane.showInputDialog("Setor (Industry):");

                if (nome != null && industry != null && !nome.trim().isEmpty()) {
                    Empresa novaEmpresa = new Empresa(nome, industry);
                    empresas.add(novaEmpresa);
                    insightsPorEmpresa.put(nome, new ArrayList<>()); // Prepara a lista de insights
                    qtdArquivosPorEmpresa.put(nome, 0); // Prepara contador de arquivos
                    JOptionPane.showMessageDialog(null, "Empresa '" + nome + "' criada com sucesso!");
                }
            }

            else {
                String nomeEmpresa = escolha.replace("🏢 Acessar: ", "");
                Empresa empSelecionada = empresas.stream()
                        .filter(e -> e.getNome().equals(nomeEmpresa))
                        .findFirst()
                        .orElse(null);

                if (empSelecionada != null) {
                    abrirMenuEmpresa(empSelecionada);
                }
            }
        }
    }


    private static void abrirMenuEmpresa(Empresa empresa) {
        while (true) {
            String[] opcoes = {"📄 Adicionar PDF", "💡 Adicionar Insight", "📊 Ver Tudo (Resumo)", "🔙 Voltar ao Início"};

            String escolha = (String) JOptionPane.showInputDialog(
                    null,
                    "Painel da Empresa: " + empresa.getNome() + "\nO que deseja fazer?",
                    "Gerenciar Empresa",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]);

            if (escolha == null || escolha.equals("🔙 Voltar ao Início")) {
                break;
            }

            if (escolha.equals("📄 Adicionar PDF")) {
                String url = JOptionPane.showInputDialog("URL do arquivo PDF:");
                if (url != null && !url.trim().isEmpty()) {
                    empresa.adicionarArquivo(new ArquivoPDF(url));
                    qtdArquivosPorEmpresa.put(empresa.getNome(), qtdArquivosPorEmpresa.get(empresa.getNome()) + 1);
                    JOptionPane.showMessageDialog(null, "Arquivo adicionado à empresa!");
                }

            } else if (escolha.equals("💡 Adicionar Insight")) {
                String titulo = JOptionPane.showInputDialog("Digite o título do Insight:");
                if (titulo != null && !titulo.trim().isEmpty()) {
                    // Instancia um insight padrão para exemplo
                    Insight insight = new Insight(Categoria.OPERACIONAL, Criticidade.ALTA, titulo);
                    insightsPorEmpresa.get(empresa.getNome()).add(insight);
                    JOptionPane.showMessageDialog(null, "Insight adicionado com sucesso!");
                }

            } else if (escolha.equals("📊 Ver Tudo (Resumo)")) {
                StringBuilder resumo = new StringBuilder();
                resumo.append("🏢 EMPRESA: ").append(empresa.getNome()).append("\n");
                resumo.append("📂 Arquivos anexados: ").append(qtdArquivosPorEmpresa.get(empresa.getNome())).append("\n\n");
                resumo.append("💡 INSIGHTS GERADOS:\n");

                List<Insight> insights = insightsPorEmpresa.get(empresa.getNome());

                if (insights.isEmpty()) {
                    resumo.append("   Nenhum insight cadastrado ainda.\n");
                } else {
                    for (Insight i : insights) {
                        resumo.append("   - ").append(i.gerarTaskJira()).append("\n");
                    }
                }

                JOptionPane.showMessageDialog(null, resumo.toString(), "Visão Geral", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}