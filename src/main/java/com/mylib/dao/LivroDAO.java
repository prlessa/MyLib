package com.mylib.dao;

import com.mylib.model.Livro;
import com.mylib.model.StatusLeitura;

import java.util.List;

public interface LivroDAO {

    // Salva um livro novo no banco
    void salvar(Livro livro);
    void deletar(int id);
    void atualizar(Livro livro);

    // Busca todos os livros da biblioteca
    List<Livro> buscarBiblioteca();

    // Busca todos os livros da biblioteca ordenados
    List<Livro> buscarBibliotecaOrdenada(String ordenacaoPrimaria, String ordenacaoSecundaria);

    // Busca apenas os livros que estão na estante do usuário
    List<Livro> buscarEstante();

    // Busca os livros da estante com ordenação
    List<Livro> buscarEstanteOrdenada(String ordenacao);

    // Move um livro da biblioteca para a estante
    void adicionarNaEstante(int id);

    // Remove um livro da estante (não exclui da biblioteca)
    void removerDaEstante(int id);

    // Atualiza o status de leitura de um livro
    void atualizarStatus(int id, StatusLeitura status);

    // Atualiza a avaliação de um livro
    void atualizarAvaliacao(int id, int avaliacao);
}