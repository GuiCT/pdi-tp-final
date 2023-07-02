package pdi.operations.elementWise;

/**
 * <h1>Inversão</h1>
 * <p>
 * Inverte os valores dos pixels de uma imagem, subtraindo cada valor do valor
 * máximo possível.
 * </p>
 */
public class Invert implements ElementWiseOperation {
    @Override
    public int apply(int value, int maxValue) {
        return maxValue - value;
    }
}
