package com.mylib.view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal() {
        setTitle("MyLib — Minha Estante Virtual");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Minha Estante", new AbaEstante());
        abas.addTab("Biblioteca", new AbaBiblioteca());

        add(abas);
    }
}