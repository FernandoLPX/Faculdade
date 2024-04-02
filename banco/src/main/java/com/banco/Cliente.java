package com.banco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "nome", "sobrenome", "cpf", "conta" })
public class Cliente {

    @JsonProperty("cpf")
    private long cpf;
    private String nome, sobrenome;
    private ContaBancaria conta;

    public Cliente(Cliente cliente) {
        // Esse construtor é necessário somente para a ocasião
        // onde o arquivo JSON não existe e é necessário
        // criar um cliente para preencher o primeiro lugar
        this.cpf = cliente.getCpf();
        this.nome = cliente.getNome();
        this.sobrenome = cliente.getSobrenome();
        this.conta = new ContaBancaria(this);
    }

    public Cliente() {
        // Construtor padrão vazio necessário para desserialização JSON
    }

    public Cliente(String nome, String sobrenome, long cpf, ContaBancaria conta) {
        // Esse construtor é necessário por causa da serialização do JSON
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.conta = conta;
    }

    public Cliente(String nome, String sobrenome, long cpf) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.conta = new ContaBancaria(this);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                "\n\tsobrenome='" + sobrenome + '\'' +
                ",\n\tcpf=" + cpf +
                ",\n\t" + conta + // Chama o toString() da ContaBancaria
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public long getCpf() {
        return cpf;
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    public ContaBancaria getConta() {
        return conta;
    }

}
