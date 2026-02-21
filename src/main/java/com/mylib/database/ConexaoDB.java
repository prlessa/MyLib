package com.mylib.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {

    private static ConexaoDB instancia;
    private Connection conexao;

    private ConexaoDB() {
        try {
            conexao = DriverManager.getConnection("jdbc:sqlite:mylib.db");
            inicializarBanco();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }

    public static ConexaoDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexaoDB();
        }
        return instancia;
    }

    public Connection getConexao() {
        return conexao;
    }

    private void inicializarBanco() throws SQLException {
        Statement stmt = conexao.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS livros (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo    TEXT NOT NULL,
                autor     TEXT NOT NULL,
                genero    TEXT,
                ano       INTEGER,
                status    TEXT DEFAULT 'META',
                avaliacao INTEGER DEFAULT 0,
                na_estante INTEGER DEFAULT 0
            )
        """);

        inserirDadosIniciais(stmt);
    }

    private void inserirDadosIniciais(Statement stmt) throws SQLException {
        var rs = stmt.executeQuery("SELECT COUNT(*) FROM livros");
        if (rs.next() && rs.getInt(1) == 0) {
            stmt.executeUpdate("""
                INSERT INTO livros (titulo, autor, genero, ano) VALUES
                ('Dom Casmurro', 'Machado de Assis', 'Romance', 1899),
                ('O Cortiço', 'Aluísio Azevedo', 'Realismo', 1890),
                ('Grande Sertão: Veredas', 'João Guimarães Rosa', 'Romance', 1956),
                ('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 'Romance', 1881),
                ('A Hora da Estrela', 'Clarice Lispector', 'Romance', 1977),
                ('O Senhor dos Anéis', 'J.R.R. Tolkien', 'Fantasia', 1954),
                ('1984', 'George Orwell', 'Distopia', 1949),
                ('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 'Fábula', 1943),
                ('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', 'Fantasia', 1997),
                ('Cem Anos de Solidão', 'Gabriel García Márquez', 'Realismo Mágico', 1967)
            """);
        }
    }
}