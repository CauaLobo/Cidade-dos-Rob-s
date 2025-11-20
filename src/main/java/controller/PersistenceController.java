package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import model.City;
import persistencia.Persistencia;
import java.io.FileNotFoundException;
import java.io.IOException;
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
}
