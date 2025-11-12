package repository;


import java.io.*;

public class Repositorio {
    // Nome do arquivo onde o estado do sistema será salvo
    private static final String NOME_ARQUIVO = "estoque_data.ser";

    /**
     * Salva o objeto passado (o estado da classe Main) no disco.
     * @param objeto O objeto a ser serializado e salvo.
     */
    public static void salvar(Object objeto) {
        try (FileOutputStream fos = new FileOutputStream(NOME_ARQUIVO);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(objeto);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    /**
     * Carrega o objeto (o estado da classe Main) do disco.
     * @return O objeto desserializado ou null se houver erro/o arquivo não existir.
     */
    public static Object carregar() {
        try (FileInputStream fis = new FileInputStream(NOME_ARQUIVO);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        } catch (FileNotFoundException e) {
            // Arquivo não encontrado, é a primeira execução.
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar os dados: " + e.getMessage());
            return null;
        }
    }
}