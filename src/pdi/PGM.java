package pdi;

import pdi.operations.areaWise.Mask;
import pdi.operations.elementWise.ElementWiseOperation;

public class PGM {
    private final Channel grayChannel;

    public PGM(Channel grayChannel) {
        this.grayChannel = grayChannel;
    }

    public int getWidth() {
        return this.grayChannel.width;
    }

    public int getHeight() {
        return this.grayChannel.height;
    }

    public int getMaxValue() {
        return this.grayChannel.maxValue;
    }

    public Channel getChannel() {
        return this.grayChannel;
    }

    public int get(int i, int j) {
        return this.grayChannel.get(i, j);
    }

    public void set(int i, int j, int value) {
        this.grayChannel.set(i, j, value);
    }

    public PGM copyPGM() {
        Channel newGrayChannel = this.grayChannel.copyChannel();
        return new PGM(newGrayChannel);
    }

    public PGM elementWiseOperation(ElementWiseOperation operation) {
        Channel newChannel = this.grayChannel.elementWiseOperation(operation);

        return new PGM(newChannel);
    }

    public PGM maskOperation(Mask operation) {
        Channel newChannel = this.grayChannel.maskOperation(operation);

        return new PGM(newChannel);
    }

    public PGM plus(PGM addend) {
        return new PGM(this.grayChannel.plus(addend.grayChannel));
    }

    public PGM minus(PGM subtrahend) {
        return new PGM(this.grayChannel.minus(subtrahend.grayChannel));
    }

    public Channel extractChannel() {
        return this.grayChannel.copyChannel();
    }
}
