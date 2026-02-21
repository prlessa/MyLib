package com.mylib.model;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private String genero;
    private int ano;
    private StatusLeitura status;
    private int avaliacao;
    private boolean naEstante;

    // Construtor para criar um livro novo (sem ID)
    public Livro(String titulo, String autor, String genero, int ano) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.ano = ano;
        this.status = StatusLeitura.META;
        this.avaliacao = 0;
        this.naEstante = false;
    }

    // Construtor para carregar um livro já existente no banco (com ID)
    public Livro(int id, String titulo, String autor, String genero, int ano,
                 StatusLeitura status, int avaliacao, boolean naEstante) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.ano = ano;
        this.status = status;
        this.avaliacao = avaliacao;
        this.naEstante = naEstante;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getGenero() { return genero; }
    public int getAno() { return ano; }
    public StatusLeitura getStatus() { return status; }
    public int getAvaliacao() { return avaliacao; }
    public boolean isNaEstante() { return naEstante; }

    // Setters
    public void setStatus(StatusLeitura status) { this.status = status; }
    public void setAvaliacao(int avaliacao) { this.avaliacao = avaliacao; }
    public void setNaEstante(boolean naEstante) { this.naEstante = naEstante; }

    @Override
    public String toString() {
        return titulo + " — " + autor + " (" + ano + ")";
    }
}