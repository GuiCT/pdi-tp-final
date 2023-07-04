package exemplos;

import java.io.IOException;

import pdi.PGM;
import pdi.io.PGM.PGMReader;
import pdi.io.PGM.PGMWriter;
import pdi.operations.areaWise.Laplacian;
import pdi.operations.sequential.ApplyLaplacian;

/**
 * Exemplo de aplicação do filtro Laplaciano em uma imagem.
 */
public class Laplace {
    public static void main(String[] args) throws IOException {
        // Abrindo arquivo
        PGM imagem = PGMReader.readPGM("exemplos/laplace/blurry_moon.pgm");
        // Filtro 0 1 0 1 -4 1 0 1 0
        PGM resultadoSemDiagonal = new PGM
        (
            new ApplyLaplacian(
                imagem.getChannel(),
                new Laplacian(false, false)
            ).apply()
        );
        // Filtro 1 1 1 1 -8 1 1 1 1
        PGM resultadoComDiagonal = new PGM
        (
            new ApplyLaplacian(
                imagem.getChannel(),
                new Laplacian(false, true)
            ).apply()
        );
        // Salvando resultados
        PGMWriter.writeASCII(resultadoSemDiagonal, "exemplos/laplace/blurry_moon_laplace_normal.pgm");
        PGMWriter.writeASCII(resultadoComDiagonal, "exemplos/laplace/blurry_moon_laplace_diagonal.pgm");
    }
}
