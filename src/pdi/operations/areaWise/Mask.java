package pdi.operations.areaWise;

public abstract class Mask implements AreaWiseOperation {
    public final int size;

    public Mask(int size) {
        this.size = size;
    }
}
