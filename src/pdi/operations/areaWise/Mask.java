package pdi.operations.areaWise;

public abstract class Mask implements AreaWiseOperation {
    public final int size;

    public Mask(int size) {
        assert (size % 2 == 1) : "Tamanho da máscara deve ser ímpar";
        this.size = size;
    }
}
