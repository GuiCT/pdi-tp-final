package pdi.operations.elementWise;

/**
 * <h1>Multiplicação por escalar</h1>
 * <p>
 * Multiplica os valores dos pixels de uma imagem por um fator constante.
 * Utilizado na operação de <i>High-Boost</i>.
 * </p>
 */
public class ScalarMultiplication implements ElementWiseOperation {
    /**
     * Fator multiplicativo, deve ser positivo.
     */
    private final double factor;

    /**
     * Cria uma operação de multiplicação por escalar com o fator especificado.
     * 
     * @param factor Fator multiplicativo, deve ser positivo.
     */
    public ScalarMultiplication(double factor) {
        assert factor > 0 : "Fator deve ser maior a zero";
        this.factor = factor;
    }

    @Override
    public int apply(int value, int maxValue) {
        // Garante que o valor não ultrapasse o valor máximo
        return (int) Math.min(value * this.factor, maxValue);
    }
}
