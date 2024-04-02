package com.banco.dados;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.banco.Cliente;
import com.banco.ContaBancaria;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonFileManager {
    // O Java usa o diretório raiz "resources"?
    private static final String FILE_PATH_ABS = "/home/app/banco/src/main/resources/clientes.json";
    // private static final String FILE_PATH = "clientes.json";

    public static void salvarContasBancarias(List<ContaBancaria> contasBancarias) {
        File file = new File(FILE_PATH_ABS);

        // Configurar o ObjectMapper do Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        // Formatação bonita do JSON
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Se existir somente um elemento no array, remove os colchetes []
        objectMapper.enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);

        // Transformar o formato de lista de contas bancarias para lista de clientes,
        // para gravar corretamente no JSON
        Cliente[] clientes = new Cliente[contasBancarias.size()];
        for (int i = 0; i < clientes.length; i++) {
            clientes[i] = contasBancarias.get(i).getCliente();
        }

        // Serializar o objeto Java para JSON e gravá-lo em um arquivo
        try {
            // Substitui o arquivo inteiro com o novo conteúdo de lista de clientes
            objectMapper.writeValue(file, clientes);
            System.out.println("Arquivo JSON criado com sucesso em: " + FILE_PATH_ABS);
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo JSON: " + e.getMessage());
        }
    }

    public static List<ContaBancaria> carregarContasBancarias() {
        List<ContaBancaria> contasBancarias = new ArrayList<>();

        File file = new File(FILE_PATH_ABS);
        if (!file.exists()) {
            System.out.println("Arquivo não encontrado: " + FILE_PATH_ABS);
            return contasBancarias; // Retorna uma lista vazia se o arquivo não existir
        }

        // Inicializar o ObjectMapper do Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        Cliente[] clientes = null;
        try {
            // Desserializar o conteúdo do arquivo JSON em um array de clientes
            clientes = objectMapper.readValue(file, Cliente[].class);

            // Exibir os clientes
            // System.out.println("Clientes lidos do arquivo JSON:");
            // for (Cliente cliente : clientes) {
            //     System.out.println(cliente);
            // }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
        }

        for (Cliente cliente : clientes) {
            cliente = new Cliente(cliente.getNome(), cliente.getSobrenome(), cliente.getCpf(),
                    new ContaBancaria(cliente, cliente.getConta().getSaldo()));
            contasBancarias.add(cliente.getConta());
        }

        return contasBancarias;
    }

    public static void exibirClientesDoJSON(List<ContaBancaria> contasBancarias) {
        System.out.println("Clientes da lista de contas bancárias:");
        for (int i = 0; i < contasBancarias.size(); i++)
            System.out.println(contasBancarias.get(i).getCliente());

        // for (ContaBancaria contaBancaria : contasBancarias)
        // for (Cliente cliente : clientes) {
        // System.out.println(cliente);
        // }
    }

    public static void criarArquivo(Cliente[] clientes) {

        File file = new File(FILE_PATH_ABS);
        if (file.exists()) {
            System.out.println("Arquivo " + FILE_PATH_ABS + " já existe.");
            // file.delete(); // Exclui o arquivo existente
            return;
        }

        // Configurar o ObjectMapper do Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        // Formatação bonita do JSON
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Se existir somente um elemento no array, remove os colchetes []
        objectMapper.enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);

        // Serializar o objeto Java para JSON e gravá-lo em um arquivo
        try {
            objectMapper.writeValue(file, clientes);
            System.out.println("Arquivo JSON criado com sucesso em: " + FILE_PATH_ABS);
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo JSON: " + e.getMessage());
        }
    }

    public static void adicionarCliente(Cliente novoCliente) {

        // Configurar o ObjectMapper do Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        // Formatação bonita do JSON
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Se existir somente um elemento no array, remove os colchetes []
        objectMapper.enable(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);

        // Abrir o arquivo JSON para leitura e escrita
        File file = new File(FILE_PATH_ABS);

        if (!file.exists()) {

            System.out.println("| Arquivo " + FILE_PATH_ABS + " não existe.");
            System.out.println("| Criando um novo.");

            // Se o arquivo não existe, necessário adicionar um cliente de teste
            // Para formar um array e a formatação do JSON ficar completa
            Cliente[] clientes = new Cliente[2];
            clientes[0] = new Cliente(novoCliente);
            clientes[1] = novoCliente;
            clientes[0].setNome("TesteNome");
            clientes[0].setSobrenome("TesteSobreNome");
            clientes[0].setCpf(99999999999L);

            // Serializar o objeto Java para JSON e gravá-lo em um arquivo
            try {
                objectMapper.writeValue(file, clientes);
                System.out.println("| Arquivo JSON criado com sucesso em: " + FILE_PATH_ABS);
            } catch (IOException e) {
                System.err.println("| Erro ao criar o arquivo JSON: " + e.getMessage());
            }
            // file.delete(); // Exclui o arquivo existente
            // return;
        } else {
            // Se o arquivo já existe, então adiciona o cliente desse jeito
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
                // Posicionar o cursor no final do arquivo -2
                randomAccessFile.seek(randomAccessFile.length() - 2);

                // Escrever o novo cliente no formato JSON
                if (randomAccessFile.length() > 0) {
                    randomAccessFile.writeBytes(", "); // Adicionar uma vírgula se o arquivo não estiver vazio
                }
                // Escreve os dados do cliente no arquivo
                randomAccessFile.writeBytes(objectMapper.writeValueAsString(novoCliente));

                // Posicionar o cursor no final do arquivo
                randomAccessFile.seek(randomAccessFile.length());

                // Escreve "]" para fechar o array do JSON
                randomAccessFile.writeBytes(" ]");

                System.out.println("| Novo cliente adicionado com sucesso.");
            } catch (IOException e) {
                System.err.println("| Erro ao adicionar o novo cliente: " + e.getMessage());
            }
        }

    }

    public static void atualizarSaldo(long cpf, double novoSaldo) {
        // Configurar o ObjectMapper do Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Formatação bonita do JSON

        // Abrir o arquivo JSON para leitura e escrita
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FILE_PATH_ABS, "rw")) {
            // Posicionar o cursor no início do arquivo
            randomAccessFile.seek(0);

            // Inicializar uma string para armazenar o conteúdo do arquivo JSON
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            // Ler cada linha do arquivo JSON e armazená-la na string
            while ((line = randomAccessFile.readLine()) != null) {
                jsonBuilder.append(line).append("\n");
            }

            // Encontrar a posição do CPF do cliente no arquivo JSON
            int cpfIndex = jsonBuilder.indexOf("\"cpf\" : " + cpf + ",");
            if (cpfIndex != -1) {
                // Encontrar a posição do saldo do cliente
                int saldoIndex = jsonBuilder.indexOf("\"saldo\" : ", cpfIndex);
                int saldoEndIndex = jsonBuilder.indexOf("\n", saldoIndex);

                // Substituir o saldo antigo pelo novo saldo na string do JSON
                jsonBuilder.replace(saldoIndex + 10, saldoEndIndex, Double.toString(novoSaldo));

                // Escrever a string atualizada de volta no arquivo JSON
                randomAccessFile.seek(0);
                randomAccessFile.writeBytes(jsonBuilder.toString());
                System.out.println("Saldo do cliente atualizado com sucesso.");
            } else {
                System.err.println("Cliente com CPF " + cpf + " não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar o saldo do cliente: " + e.getMessage());
        }
    }

}
