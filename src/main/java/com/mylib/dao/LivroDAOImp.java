package com.mylib.dao;

import com.mylib.database.ConexaoDB;
import com.mylib.model.Livro;
import com.mylib.model.StatusLeitura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAOImp implements LivroDAO {

    private final Connection conexao;

    public LivroDAOImp() {
        this.conexao = ConexaoDB.getInstancia().getConexao();
    }

    // Converte uma linha do ResultSet em um objeto Livro
    private Livro construirLivro(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("genero"),
                rs.getInt("ano"),
                StatusLeitura.valueOf(rs.getString("status")),
                rs.getInt("avaliacao"),
                rs.getInt("na_estante") == 1
        );
    }

    @Override
    public void salvar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, genero, ano) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, livro.getTitulo());
            stmt.setString(2, livro.getAutor());
            stmt.setString(3, livro.getGenero());
            stmt.setInt(4, livro.getAno());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar livro.", e);
        }
    }

    @Override
    public List<Livro> buscarBiblioteca() {
        return buscarBibliotecaOrdenada("Título");
    }

    @Override
    public List<Livro> buscarBibliotecaOrdenada(String ordenacao) {
        String ordem = switch (ordenacao) {
            case "Autor" -> "autor";
            case "Gênero" -> "genero";
            default -> "titulo";
        };
        String sql = "SELECT * FROM livros ORDER BY " + ordem;
        return executarConsulta(sql);
    }

    @Override
    public List<Livro> buscarEstante() {
        return buscarEstanteOrdenada("Título");
    }

    @Override
    public List<Livro> buscarEstanteOrdenada(String ordenacao) {
        String ordem = switch (ordenacao) {
            case "Autor" -> "autor";
            case "Status" -> "status";
            default -> "titulo";
        };
        String sql = "SELECT * FROM livros WHERE na_estante = 1 ORDER BY " + ordem;
        return executarConsulta(sql);
    }

    @Override
    public void adicionarNaEstante(int id) {
        executarUpdate("UPDATE livros SET na_estante = 1 WHERE id = " + id);
    }

    @Override
    public void removerDaEstante(int id) {
        executarUpdate("UPDATE livros SET na_estante = 0, status = 'META', avaliacao = 0 WHERE id = " + id);
    }

    @Override
    public void atualizarStatus(int id, StatusLeitura status) {
        executarUpdate("UPDATE livros SET status = '" + status.name() + "' WHERE id = " + id);
    }

    @Override
    public void atualizarAvaliacao(int id, int avaliacao) {
        executarUpdate("UPDATE livros SET avaliacao = " + avaliacao + " WHERE id = " + id);
    }

    // Métodos auxiliares
    private List<Livro> executarConsulta(String sql) {
        List<Livro> livros = new ArrayList<>();
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                livros.add(construirLivro(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar livros.", e);
        }
        return livros;
    }

    private void executarUpdate(String sql) {
        try (Statement stmt = conexao.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro.", e);
        }
    }
}