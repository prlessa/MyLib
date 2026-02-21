package com.mylib;

import com.mylib.database.ConexaoDB;
import com.mylib.view.JanelaPrincipal;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        ConexaoDB.getInstancia();

        SwingUtilities.invokeLater(() -> {
            JanelaPrincipal janela = new JanelaPrincipal();
            janela.setVisible(true);
        });
    }
}