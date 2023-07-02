package pdi.operations.sequential;

import pdi.PGM;
import pdi.operations.areaWise.Mean;
import pdi.operations.elementWise.ScalarMultiplication;

/**
 * <h1>Realce <i>High-Boost</i></h1>
 * <p>
 * Realça uma imagem PGM utilizando o método <i>High-Boost</i>.
 * </p>
 */
public class HighBoostPGM {
    /*
     * A imagem PGM a ser realçada.
     */
    private final PGM pgm;
    /**
     * Tamanho da máscara utilizada para o filtro de média.
     */
    private final int meanSize;
    /**
     * Fator multiplicativo utilizado.
     */
    private final double k;

    /**
     * Cria uma operação de realce <i>High-Boost</i> com os parâmetros
     * 
     * @param pgm      A imagem PGM a ser realçada
     * @param meanSize Tamanho da máscara utilizada para o filtro de média
     * @param k        Fator multiplicativo utilizado
     */
    public HighBoostPGM(PGM pgm, int meanSize, double k) {
        this.pgm = pgm;
        this.meanSize = meanSize;
        this.k = k;
    }

    public PGM apply() {
        // Borra utilizando média
        PGM blurred = this.pgm.maskOperation(new Mean(meanSize));
        // Diferença
        PGM g = this.pgm.minus(blurred);
        // Multiplicando máscara por k
        PGM kg = g.elementWiseOperation(new ScalarMultiplication(this.k));
        // Somando máscara com imagem original
        return this.pgm.plus(kg);
    }
}
