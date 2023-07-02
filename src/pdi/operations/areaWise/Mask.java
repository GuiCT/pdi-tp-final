package pdi.operations.areaWise;

/**
 * <h1>Operação com <i>kernel</i></h1>
 * <h2>(ou "máscara")</h2>
 * <p>
 * Operações que utilizam uma máscara para alterar o valor de um pixel.
 * Nesse projeto, todas as operações de filtragem espacial são dessa família.
 * </p>
 */
public abstract class Mask implements AreaWiseOperation {
    /**
     * Tamanho da máscara, deve ser número ímpar.
     */
    public final int size;

    /**
     * Construtor que inicializa a máscara, validando seu tamanho.
     * 
     * @param size Tamanho da máscara.
     */
    public Mask(int size) {
        assert (size % 2 == 1) : "Tamanho da máscara deve ser ímpar";
        this.size = size;
    }
}
