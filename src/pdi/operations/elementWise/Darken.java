package pdi.operations.elementWise;

/**
 * <h1>Operação de escurecimento</h1>
 * <p>
 * Escurece uma imagem subtraindo um valor constante de todos os pixels.
 * </p>
 */
public class Darken implements ElementWiseOperation {
    /**
     * Subtraendo utilizado, deve ser positivo.
     */
    private final int subtrahend;

    /**
     * Cria uma operação de escurecimento com o subtraendo especificado.
     * 
     * @param subtrahend Subtraendo utilizado, deve ser positivo.
     */
    public Darken(int subtrahend) {
        assert subtrahend > 0 : "Subtraendo deve ser maior que zero";
        this.subtrahend = subtrahend;
    }

    /**
     * Calcula o pixel, subtraindo o subtraendo do valor do pixel.
     * Caso o resultado seja menor que zero, retorna zero.
     */
    @Override
    public int apply(int value, int maxValue) {
        return Math.max(value - this.subtrahend, 0);
    }
}
