package com.banco;

import java.util.List;

import com.banco.dados.JsonFileManager;

public class Banco {

    private static Banco instancia;
    private final String nome = "Banco Nacional";
    private List<ContaBancaria> contasBancarias;

    private Banco() {
        // Carregar a lista de contas bancarias do JSON
        contasBancarias = JsonFileManager.carregarContasBancarias();
    }

    // Singleton porquê só pode existir UM Banco
    public static Banco getInstance() {
        if (instancia == null) {
            instancia = new Banco();
        }
        return instancia;
    }

    public Cliente cadastrarCliente(String nome, String sobrenome, long cpf) {
        var cliente = new Cliente(nome, sobrenome, cpf);
        contasBancarias.add(cliente.getConta());
        JsonFileManager.adicionarCliente(cliente);
        return cliente;
    }

    public double verificarSaldo(Cliente cliente) {
        return cliente.getConta().getSaldo();
    }

    public void depositar(Cliente cliente, double valor) {
        double novoSaldo = cliente.getConta().getSaldo() + valor;
        cliente.getConta().setSaldo(novoSaldo);
        JsonFileManager.atualizarSaldo(cliente.getCpf(), novoSaldo);
    }

    public boolean sacar(Cliente cliente, double valor) {
        double saldoAtual = cliente.getConta().getSaldo();
        if (valor <= saldoAtual) {
            double novoSaldo = saldoAtual - valor;
            cliente.getConta().setSaldo(novoSaldo);
            JsonFileManager.atualizarSaldo(cliente.getCpf(), novoSaldo);
            return true;
        } else
            return false;
    }

    public Cliente verificarCliente(long cpf) {
        for (ContaBancaria contaBancaria : contasBancarias) {
            if (contaBancaria.getCliente().getCpf() == cpf)
                return contaBancaria.getCliente();
        }
        return null;
    }

    public void exibirClientesEmContasBancarias() {
        System.out.println("Clientes da lista de contas bancárias:");
        for (int i = 0; i < contasBancarias.size(); i++)
            System.out.println(contasBancarias.get(i).getCliente());
    }

    public String getNome() {
        return nome;
    }

    public List<ContaBancaria> getContasBancarias() {
        return contasBancarias;
    }

    public void setContasBancarias(List<ContaBancaria> contasBancarias) {
        this.contasBancarias = contasBancarias;
    }

}
