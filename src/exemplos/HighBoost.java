package exemplos;

import java.io.IOException;

import pdi.PGM;
import pdi.io.PGM.PGMReader;
import pdi.io.PGM.PGMWriter;
import pdi.operations.sequential.HighBoostPGM;

/**
 * Exemplo de aplicação do filtro High Boost em uma imagem.
 */
public class HighBoost {
    public static void main(String[] args) throws IOException {
        // Abrindo arquivo
        PGM imagem = PGMReader.readPGM("exemplos/highboost/dipxe_texto.pgm");
        // Testando vários k's
        for (double k = 2.0; k <= 10.0; k += 1.0) {
            PGM resultado = new HighBoostPGM(imagem, 3, k).apply();
            // Salvando
            PGMWriter.writeASCII(
                    resultado,
                    String.format(
                            "exemplos/highboost/dipxe_texto_HIGH_BOOST_k_%.0f.pgm",
                            k));
        }
    }
}
