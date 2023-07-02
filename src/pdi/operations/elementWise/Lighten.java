package pdi.operations.elementWise;

/**
 * <h1>Operação de clareamento</h1>
 * <p>
 * Clareia uma imagem somando um valor constante a todos os pixels.
 * </p>
 */
public class Lighten implements ElementWiseOperation {
    /**
     * Valor somado, deve ser positivo.
     */
    private final int summand;

    /**
     * Cria uma operação de clareamento com o somando especificado.
     * 
     * @param summand Valor somado, deve ser positivo.
     */
    public Lighten(int summand) {
        assert summand > 0 : "Valor somado deve ser maior que zero";
        this.summand = summand;
    }

    @Override
    public int apply(int value, int maxValue) {
        return Math.min(value + this.summand, maxValue);
    }
}
