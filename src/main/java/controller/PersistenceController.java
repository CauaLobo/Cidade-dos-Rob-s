package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import model.City;
import persistencia.Persistencia;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsável por gerenciar a persistência de dados do jogo.
 * 
 * <p>Fornece métodos para salvar, carregar, listar e deletar cidades.
 * Todos os arquivos são salvos no diretório "saves/" no formato JSON.
 * 
 * @author Sistema Cidade dos Robôs
 * @version 1.0
 */
public class PersistenceController {
    
    // Diretório fixo para salvar os arquivos de cidades
    private static final String DIRETORIO_SAVES = "saves";
    
    /**
     * Retorna o diretório de saves, criando-o se não existir.
     * @return O diretório de saves
     */
    private static File getDiretorioSaves() {
        File diretorio = new File(DIRETORIO_SAVES);
        if (!diretorio.exists()) {
            diretorio.mkdirs(); // Cria o diretório se não existir
        }
        return diretorio;
    }
    
    /**
     * Retorna o caminho completo do arquivo da cidade.
     * @param nomeCidade Nome da cidade
     * @return O arquivo completo com caminho
     */
    private static File getArquivoCidade(String nomeCidade) {
        return new File(getDiretorioSaves(), nomeCidade + ".json");
    }

    /**
     * Salva uma cidade em um arquivo JSON.
     * 
     * @param city A cidade a ser salva
     * @param nomeCidade Nome da cidade (usado como nome do arquivo)
     * @throws IOException Se houver erro ao salvar o arquivo
     */
    public static void salvarCidade(City city, String nomeCidade) throws IOException{
        List<City> list = List.of(city);
        File arquivo = getArquivoCidade(nomeCidade);
        Persistencia.salvar(list, arquivo.getAbsolutePath());
    }

    /**
     * Carrega uma cidade de um arquivo JSON.
     * 
     * @param nomeCidade Nome da cidade a ser carregada
     * @return A cidade carregada
     * @throws IOException Se houver erro ao carregar o arquivo ou se a cidade não for encontrada
     */
    public static City carregarCidade(String nomeCidade) throws IOException {
        File arquivo = getArquivoCidade(nomeCidade);
        
        if (!arquivo.exists()) {
            throw new FileNotFoundException("Cidade não encontrada: " + nomeCidade);
        }
        
        List<City> listaCidades = Persistencia.carregar(
                arquivo.getAbsolutePath(),
                new TypeReference<List<City>>() {} // Informa que é uma lista de objetos Cidade
        );

        if (listaCidades.isEmpty()) {
            throw new FileNotFoundException("Cidade não encontrada ou vazia: " + nomeCidade);
        }

        // Retorna a única instância de Cidade que estava no arquivo
        return listaCidades.get(0);
    }

    /**
     * Lista todas as cidades salvas (arquivos .json no diretório de saves).
     * @return Lista com os nomes das cidades (sem a extensão .json)
     */
    public static List<String> listarCidadesSalvas() {
        List<String> cidades = new ArrayList<>();
        File diretorio = getDiretorioSaves();
        
        if (diretorio.exists() && diretorio.isDirectory()) {
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
        }
        return cidades;
    }

    /**
     * Deleta uma cidade salva.
     * @param nomeCidade Nome da cidade a ser deletada
     * @return true se a cidade foi deletada com sucesso, false caso contrário
     */
    public static boolean deletarCidade(String nomeCidade) {
        File arquivo = getArquivoCidade(nomeCidade);
        if (arquivo.exists() && arquivo.isFile()) {
            return arquivo.delete();
        }
        return false;
    }
}
