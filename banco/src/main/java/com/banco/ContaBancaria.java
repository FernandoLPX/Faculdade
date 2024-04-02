package com.banco;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class ContaBancaria {

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cpf")
    @JsonIdentityReference(alwaysAsId = true)
    private Cliente cliente;
    private double saldo;

    public ContaBancaria() {
        // Construtor padrão vazio necessário para desserialização JSON
    }

    public ContaBancaria(Cliente cliente) {
        this.cliente = cliente;
        this.saldo = 0;
    }

    public ContaBancaria(Cliente cliente, double saldo) {
        this.cliente = cliente;
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "ContaBancaria{" +
                "saldo=" + saldo +
                '}';
    }

    @JsonIgnore
    public Cliente getCliente() {
        return cliente;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
