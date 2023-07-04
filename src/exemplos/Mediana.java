package exemplos;

import java.io.IOException;

import pdi.PGM;
import pdi.io.PGM.PGMReader;
import pdi.io.PGM.PGMWriter;
import pdi.operations.areaWise.Median;

/**
 * Exemplo de aplicação do filtro da mediana em uma imagem.
 */
public class Mediana {
    public static void main(String[] args) throws IOException {
        // Abrindo arquivo
        PGM imagem = PGMReader.readPGM("exemplos/mediana/placa_circuito.pgm");
        // Aplicando filtros da mediana de 3x3 e 5x5
        PGM resultadoMediana3 = imagem.maskOperation(new Median(3));
        PGM resultadoMediana5 = imagem.maskOperation(new Median(5));
        // Salvando resultados
        PGMWriter.writeASCII(resultadoMediana3, "exemplos/mediana/placa_circuito_mediana3.pgm");
        PGMWriter.writeBinary(resultadoMediana5, "exemplos/mediana/placa_circuito_mediana5.pgm");
    }
}
