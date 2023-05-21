package pdi;

import java.io.*;

public class PGM {
    private Channel grayChannel;

    PGM(Channel grayChannel) {
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
}
