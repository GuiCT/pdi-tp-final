package pdi;

import pdi.operations.ElementWiseOperation;

public class PGM {
    private Channel grayChannel;

    public PGM(Channel grayChannel) {
        this.grayChannel = grayChannel;
    }

    public int getWidth() {
        return this.grayChannel.width;
    }

    public int getHeight() {
        return this.grayChannel.height;
    }

    public short getMaxValue() {
        return this.grayChannel.getMaxValue();
    }

    public short get(int i, int j) {
        return this.grayChannel.get(i, j);
    }

    public void set(int i, int j, short value) {
        this.grayChannel.set(i, j, value);
    }

    public PGM clonePGM() {
        Channel newGrayChannel = this.grayChannel.cloneChannel();
        return new PGM(newGrayChannel);
    }

    public PGM elementWiseOperation(ElementWiseOperation operation) {
        PGM newPGM = this.clonePGM();
        for (int i = 0; i < newPGM.getWidth(); i++) {
            for (int j = 0; j < newPGM.getHeight(); j++) {
                short value = this.get(i, j);
                newPGM.set(i, j, operation.apply(value, newPGM.getMaxValue()));
            }
        }

        return newPGM;
    }
}
