package pdi.operations.elementWise;

/**
 * <h1>Operação de correção Gama</h1>
 * <p>
 * Corrige o brilho de uma imagem multiplicando o valor de cada pixel por uma
 * constante e elevando o resultado a uma potência.
 * </p>
 * <p>
 * r(p) = c * p ^ gamma
 * </p>
 */
public class Gamma implements ElementWiseOperation {
    /**
     * Fator escalar utilizado
     */
    private final double c;
    /**
     * Potência utilizada
     */
    private final double gamma;

    /**
     * Cria uma operação de correção gama com os parâmetros especificados.
     * 
     * @param c     Fator escalar utilizado
     * @param gamma Potência utilizada
     */
    public Gamma(double c, double gamma) {
        this.c = c;
        this.gamma = gamma;
    }

    @Override
    public int apply(int pixel, int maxValue) {
        // Realiza a normalização do valor de entrada, encaixando-o
        // no intervalo de 0 até 1
        double normalizedPixelValue = (double) pixel / maxValue;
        // Aplica a correção gama
        double calculatedNormalizedValue = c * Math.pow(normalizedPixelValue, gamma);
        // Desnormaliza o valor calculado, encaixando-o no intervalo
        // de 0 até o valor máximo
        return (int) (calculatedNormalizedValue * maxValue);
    }
}
