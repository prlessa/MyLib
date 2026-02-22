package com.mylib.view;

import com.mylib.dao.LivroDAOImp;
import com.mylib.model.Livro;
import com.mylib.model.StatusLeitura;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AbaEstante extends JPanel {

    private JTable tabela;
    private JTextField campoPesquisa;
    private DefaultTableModel modeloTabela;
    private LivroDAOImp dao;

    public AbaEstante() {
        dao = new LivroDAOImp();
        setLayout(new BorderLayout());

        // Painel superior com botão atualizar
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.add(new JLabel("Pesquisar:"));
        campoPesquisa = new JTextField(20);
        painelTopo.add(campoPesquisa);
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

        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(30);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(50);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabela.setRowSorter(sorter);

        DefaultTableCellRenderer centralizador = new DefaultTableCellRenderer();
        centralizador.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centralizador); // Genero
        tabela.getColumnModel().getColumn(4).setCellRenderer(centralizador); // Ano
        tabela.getColumnModel().getColumn(5).setCellRenderer(centralizador); // Status
        tabela.getColumnModel().getColumn(6).setCellRenderer(centralizador); // Avaliação

        campoPesquisa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = campoPesquisa.getText().trim();
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });

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

        btnAtualizar.addActionListener(e -> carregarDados());

        btnAlterarStatus.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro primeiro.");
                return;
            }
            int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
            int id = (int) modeloTabela.getValueAt(modelRow, 0);
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

        btnAvaliar.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro primeiro.");
                return;
            }
            int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
            int id = (int) modeloTabela.getValueAt(modelRow, 0);
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

        btnRemover.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro primeiro.");
                return;
            }
            int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
            int id = (int) modeloTabela.getValueAt(modelRow, 0);
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
        atualizarTabela(dao.buscarEstante());
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