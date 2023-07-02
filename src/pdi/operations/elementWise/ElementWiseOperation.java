package pdi.operations.elementWise;

/**
 * <h1>Operação elemento a elemento</h1>
 * <p>
 * Interface para operações que, para calcular o valor de um pixel, utilizam
 * apenas o valor de um único outro pixel e seu valor máximo possível.
 * </p>
 */
public interface ElementWiseOperation {
    /**
     * Calcula o valor de um pixel a partir do valor de outro pixel e do valor
     * 
     * @param value    Valor do pixel utilizado para calcular o valor do pixel de
     *                 saída
     * @param maxValue Valor máximo possível para um pixel
     * @return Valor do pixel de saída
     */
    int apply(int value, int maxValue);
}
