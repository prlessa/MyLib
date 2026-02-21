package com.mylib.model;

    public enum StatusLeitura {

        META("Meta de Leitura"),
        LENDO("Lendo"),
        LIDO("Lido");

        private final String descricao;

        StatusLeitura(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }

