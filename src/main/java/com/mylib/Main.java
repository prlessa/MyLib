package com.mylib;

import com.mylib.dao.LivroDAOImp;
import com.mylib.database.ConexaoDB;
import com.mylib.model.Livro;
import com.mylib.view.JanelaPrincipal;
import javax.swing.SwingUtilities;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ConexaoDB.getInstancia();

        // Teste rápido do DAO
        LivroDAOImp dao = new LivroDAOImp();
        List<Livro> livros = dao.buscarBiblioteca();
        for (Livro livro : livros) {
            System.out.println(livro);
        }

        SwingUtilities.invokeLater(() -> {
            JanelaPrincipal janela = new JanelaPrincipal();
            janela.setVisible(true);
        });
    }
}