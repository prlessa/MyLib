package com.mylib.view;

import com.mylib.dao.LivroDAOImp;
import com.mylib.model.Livro;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AbaBiblioteca extends JPanel {


    private JTable tabela;
    private JTextField campoPesquisa;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> ordenacaoPrimaria;
    private JComboBox<String> ordenacaoSecundaria;
    private LivroDAOImp dao;

    public AbaBiblioteca() {
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
        String[] colunas = {"ID", "Título", "Autor", "Gênero", "Ano", "Estante"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.getColumnModel().getColumn(0).setMinWidth(0);
        tabela.getColumnModel().getColumn(0).setMaxWidth(0);

        tabela.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(180);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(50);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(60);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabela);
        tabela.setRowSorter(sorter);

        DefaultTableCellRenderer centralizador = new DefaultTableCellRenderer();
        centralizador.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centralizador); // Genero
        tabela.getColumnModel().getColumn(4).setCellRenderer(centralizador); // Ano
        tabela.getColumnModel().getColumn(5).setCellRenderer(centralizador); // Estante

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
        JButton btnAdicionarEstante = new JButton("Adicionar na Estante");
        JButton btnNovoLivro = new JButton("Novo Livro");
        painelBotoes.add(btnAdicionarEstante);
        painelBotoes.add(btnNovoLivro);
        add(painelBotoes, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> carregarDados());

        btnAdicionarEstante.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro primeiro.");
                return;
            }
            int modelRow = tabela.convertRowIndexToModel(linhaSelecionada);
            int id = (int) modeloTabela.getValueAt(modelRow, 0);
            dao.adicionarNaEstante(id);
            carregarDados();
        });

        btnNovoLivro.addActionListener(e -> {
            JTextField campoTitulo = new JTextField();
            JTextField campoAutor = new JTextField();
            JTextField campoGenero = new JTextField();
            JTextField campoAno = new JTextField();

            Object[] campos = {
                    "Título:", campoTitulo,
                    "Autor:", campoAutor,
                    "Gênero:", campoGenero,
                    "Ano:", campoAno
            };

            int resultado = JOptionPane.showConfirmDialog(
                    this, campos, "Novo Livro", JOptionPane.OK_CANCEL_OPTION
            );

            if (resultado == JOptionPane.OK_OPTION) {
                String titulo = campoTitulo.getText().trim();
                String autor = campoAutor.getText().trim();
                String genero = campoGenero.getText().trim();
                String anoTexto = campoAno.getText().trim();

                if (titulo.isEmpty() || autor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Título e Autor são obrigatórios.");
                    return;
                }

                int ano = 0;
                if (!anoTexto.isEmpty()) {
                    try {
                        ano = Integer.parseInt(anoTexto);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Ano deve ser um número válido.");
                        return;
                    }
                }

                dao.salvar(new Livro(titulo, autor, genero, ano));
                carregarDados();
            }
        });

        carregarDados();
    }

    public void carregarDados() {
        atualizarTabela(dao.buscarBiblioteca());
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
                    livro.isNaEstante() ? " (S)" : "(N)"
            });
        }
    }
}