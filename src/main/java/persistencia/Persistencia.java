package persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe utilitaria responsavel por gerenciar a persistencia de dados no formato JSON.
 * <p>
 * Esta classe utiliza a biblioteca Jackson para serializar (salvar) e desserializar (carregar)
 * listas de objetos genericos (entidades) em arquivos, garantindo que os dados sejam
 * armazenados de forma permanente.
 */
public class Persistencia {

        /**
         * Instancia unica do ObjectMapper da biblioteca Jackson.
         * E estatica e final para garantir eficiencia e unicidade no processo de serializacao/desserializacao.
         */
        private static final ObjectMapper objectMapper = new ObjectMapper();

        /**
         * Serializa uma lista de entidades genericas e as salva em um arquivo JSON.
         * <p>
         * Este metodo sobrescreve o conteudo do arquivo se ele ja existir.
         * @param <T> O tipo de entidade que a lista contem.
         * @param entidade A lista de objetos T a ser salva.
         * @param nomeArqv O nome do arquivo JSON onde os dados serao escritos.
         * @throws IOException Se ocorrer um erro durante a escrita no arquivo (ex: falta de permissao).
         */
        public static <T> void salvar(List<T> entidade, String nomeArqv) throws IOException{
            objectMapper.writeValue(new File(nomeArqv), entidade);
        }

        /**
         * Desserializa o conteudo de um arquivo JSON para uma lista de entidades genericas.
         * <p>
         * Se o arquivo nao existir ou estiver vazio, retorna uma nova lista vazia para evitar erros.
         * @param <T> O tipo de entidade que a lista deve conter.
         * @param nomeArqv O nome do arquivo JSON a ser lido.
         * @param referencia O TypeReference que informa ao Jackson o tipo generico exato (List<T>).
         * @return Uma lista de objetos T carregada do arquivo.
         * @throws IOException Se ocorrer um erro durante a leitura do arquivo (ex: arquivo corrompido).
         */
        public static <T> List<T> carregar(String nomeArqv, TypeReference<List<T>> referencia) throws IOException {
            File arqv = new File(nomeArqv);

            if (!arqv.exists() || arqv.length() == 0){
                return new ArrayList<>();
            }

            return objectMapper.readValue(arqv, referencia);
        }
}

