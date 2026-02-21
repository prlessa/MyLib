package com.mylib.view;

import com.mylib.model.Livro;
import com.mylib.model.StatusLeitura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AbaEstante extends JPanel {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> Ordenacao;

    public AbaEstante() {
        setLayout(new BorderLayout());

        // Painel superior com ordenação
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.add(new JLabel("Ordenar por:"));
        Ordenacao = new JComboBox<>(new String[]{"Título", "Autor", "Status"});
        painelTopo.add(Ordenacao);
        add(painelTopo, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Título", "Autor", "Gênero", "Ano", "Status", "Avaliação"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.getColumnModel().getColumn(0).setMinWidth(0);
        tabela.getColumnModel().getColumn(0).setMaxWidth(0);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Painel inferior com botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAlterarStatus = new JButton("Alterar Status");
        JButton btnAvaliar = new JButton("Avaliar");
        JButton btnRemover = new JButton("Remover da Estante");
        painelBotoes.add(btnAlterarStatus);
        painelBotoes.add(btnAvaliar);
        painelBotoes.add(btnRemover);
        add(painelBotoes, BorderLayout.SOUTH);

        dadosTeste();
    }

    private void dadosTeste() {
        List<Livro> livros = List.of(
                new Livro(1, "Dom Casmurro", "Machado de Assis", "Romance", 1899,
                        StatusLeitura.LIDO, 5, true),
                new Livro(2, "1984", "George Orwell", "Distopia", 1949,
                        StatusLeitura.LENDO, 0, true),
                new Livro(3, "O Pequeno Príncipe", "Saint-Exupéry", "Fábula", 1943,
                        StatusLeitura.META, 0, true)
        );
        atualizarTabela(livros);
    }

    public void atualizarTabela(List<Livro> livros) {
        modeloTabela.setRowCount(0);
        for (Livro livro : livros) {
            modeloTabela.addRow(new Object[]{
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getGenero(),
                    livro.getAno(),
                    livro.getStatus().getDescricao(),
                    livro.getAvaliacao() == 0 ? "—" : livro.getAvaliacao() + " ★"
            });
        }
    }
}