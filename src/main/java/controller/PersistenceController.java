package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import model.City;
import persistencia.Persistencia;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistenceController {


    public static void salvarCidade(City city, String nomeCidade) throws IOException{
        List<City> list = List.of(city);

       Persistencia.salvar(list, nomeCidade + ".json");
    }

    public static City carregarCidade(String nomeCidade) throws IOException {
        List<City> listaCidades = Persistencia.carregar(
                nomeCidade + ".json",
                new TypeReference<List<City>>() {} // Informa que é uma lista de objetos Cidade
        );

        if (listaCidades.isEmpty()) {
            throw new FileNotFoundException("Cidade não encontrada ou vazia.");
        }

        // Retorna a única instância de Cidade que estava no arquivo
        return listaCidades.get(0);
    }

    /**
     * Lista todas as cidades salvas (arquivos .json no diretório atual).
     * @return Lista com os nomes das cidades (sem a extensão .json)
     */
    public static List<String> listarCidadesSalvas() {
        List<String> cidades = new ArrayList<>();
        File diretorio = new File(".");
        File[] arquivos = diretorio.listFiles((dir, name) -> name.endsWith(".json"));
        
        if (arquivos != null) {
            for (File arquivo : arquivos) {
                String nome = arquivo.getName();
                // Remove a extensão .json
                if (nome.endsWith(".json")) {
                    cidades.add(nome.substring(0, nome.length() - 5));
                }
            }
        }
        return cidades;
    }

    /**
     * Deleta uma cidade salva.
     * @param nomeCidade Nome da cidade a ser deletada
     * @return true se a cidade foi deletada com sucesso, false caso contrário
     */
    public static boolean deletarCidade(String nomeCidade) {
        File arquivo = new File(nomeCidade + ".json");
        if (arquivo.exists()) {
            return arquivo.delete();
        }
        return false;
    }
}
