package pdi.operations.sequential;

import pdi.PPM;
import pdi.operations.areaWise.Mean;
import pdi.operations.elementWise.ScalarMultiplication;

/**
 * <h1>Realce <i>High-Boost</i></h1>
 * <p>
 * Realça uma imagem PPM utilizando o método <i>High-Boost</i>.
 * </p>
 */
public class HighBoostPPM {
    /*
     * A imagem PPM a ser realçada.
     */
    private final PPM ppm;
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
     * @param ppm      A imagem PPM a ser realçada
     * @param meanSize Tamanho da máscara utilizada para o filtro de média
     * @param k        Fator multiplicativo utilizado
     */
    public HighBoostPPM(PPM ppm, int meanSize, double k) {
        this.ppm = ppm;
        this.meanSize = meanSize;
        this.k = k;
    }

    public PPM apply() {
        // Borra utilizando média
        PPM blurred = this.ppm.maskOperation(new Mean(meanSize));
        // Diferença
        PPM g = this.ppm.minus(blurred);
        // Multiplicando máscara por k
        PPM kg = g.elementWiseOperation(new ScalarMultiplication(this.k));
        // Somando máscara com imagem original
        return this.ppm.plus(kg);
    }
}
