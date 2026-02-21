package com.mylib.view;

import com.mylib.dao.LivroDAOImp;
import com.mylib.model.Livro;
import com.mylib.model.StatusLeitura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AbaEstante extends JPanel {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> ordenacao;
    private LivroDAOImp dao;

    public AbaEstante() {
        dao = new LivroDAOImp();
        setLayout(new BorderLayout());

        // Painel superior com ordenação
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.add(new JLabel("Ordenar por:"));
        ordenacao = new JComboBox<>(new String[]{"Título", "Autor", "Status"});
        painelTopo.add(ordenacao);
        JButton btnAtualizar = new JButton("Atualizar");
        painelTopo.add(btnAtualizar);
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

        // Conectar ordenação
        ordenacao.addActionListener(e -> carregarDados());

        // Conectar botão Atualizar
        btnAtualizar.addActionListener(e -> carregarDados());

        // Conectar botão Alterar Status
        btnAlterarStatus.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro primeiro.");
                return;
            }
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            StatusLeitura[] opcoes = StatusLeitura.values();
            StatusLeitura escolha = (StatusLeitura) JOptionPane.showInputDialog(
                    this, "Selecione o novo status:", "Alterar Status",
                    JOptionPane.PLAIN_MESSAGE, null, opcoes, opcoes[0]
            );
            if (escolha != null) {
                dao.atualizarStatus(id, escolha);
                carregarDados();
            }
        });

        // Conectar botão Avaliar
        btnAvaliar.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro primeiro.");
                return;
            }
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            String[] estrelas = {"1 ★", "2 ★", "3 ★", "4 ★", "5 ★"};
            String escolha = (String) JOptionPane.showInputDialog(
                    this, "Selecione a avaliação:", "Avaliar",
                    JOptionPane.PLAIN_MESSAGE, null, estrelas, estrelas[0]
            );
            if (escolha != null) {
                int nota = Integer.parseInt(escolha.substring(0, 1));
                dao.atualizarAvaliacao(id, nota);
                carregarDados();
            }
        });

        // Conectar botão Remover
        btnRemover.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro primeiro.");
                return;
            }
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            int confirmacao = JOptionPane.showConfirmDialog(
                    this, "Remover este livro da estante?", "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmacao == JOptionPane.YES_OPTION) {
                dao.removerDaEstante(id);
                carregarDados();
            }
        });

        carregarDados();
    }

    public void carregarDados() {
        String ordemSelecionada = (String) ordenacao.getSelectedItem();
        atualizarTabela(dao.buscarEstanteOrdenada(ordemSelecionada));
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