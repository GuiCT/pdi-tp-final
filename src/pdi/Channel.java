package pdi;


import pdi.operations.areaWise.Mask;
import pdi.operations.elementWise.ElementWiseOperation;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    private short data[][];
    private short maxValue;
    public final int width;
    public final int height;

    public Channel(int width, int height, short maxValue) {
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.data = new short[height][width];
    }

    public void set(int i, int j, short value) {
        if (value < 0) {
            throw new IllegalArgumentException("Valor do pixel deve ser maior ou igual a zero");
        } else if (value > this.maxValue) {
            throw new IllegalArgumentException("Valor do pixel deve ser menor ou igual ao valor máximo");
        } else {
            this.data[i][j] = value;
        }
    }

    public short get(int i, int j) {
        return this.data[i][j];
    }

    public short getMaxValue() {
        return this.maxValue;
    }

    public Channel cloneChannel() {
        return new Channel(this.width, this.height, this.maxValue);
    }

    public Channel elementWiseOperation(ElementWiseOperation operation) {
        Channel newChannel = this.cloneChannel();
        for (int i = 0; i < newChannel.height; i++) {
            for (int j = 0; j < newChannel.width; j++) {
                short value = this.data[i][j];
                newChannel.set(i, j, operation.apply(value, newChannel.maxValue));
            }
        }

        return newChannel;
    }

    public Channel maskOperation(Mask operation) {
        Channel newChannel = this.cloneChannel();
        for (int i = 0; i < newChannel.height; i++) {
            for (int j = 0; j < newChannel.width; j++) {
                short value = this.data[i][j];
                newChannel.set(i, j, operation.apply(
                        selectPixels(i, j, operation.size),
                        newChannel.maxValue
                ));
            }
        }

        return newChannel;
    }

    public Channel minus(Channel subtrahend) {
        assert this.width == subtrahend.width : "Largura deve ser a mesma";
        assert this.height == subtrahend.height : "Altura deve ser a mesma";
        assert this.maxValue == subtrahend.maxValue : "Valor máximo dos pixels deve ser igual";
        Channel difference = new Channel(this.width, this.height, this.maxValue);

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                difference.set(i, j, (short) Math.min(Math.max(0, this.data[i][j] - subtrahend.data[i][j]), this.maxValue));
            }
        }

        return difference;
    }

    public Channel plus(Channel addend) {
        assert this.width == addend.width : "Largura deve ser a mesma";
        assert this.height == addend.height : "Altura deve ser a mesma";
        assert this.maxValue == addend.maxValue : "Valor máximo dos pixels deve ser igual";
        Channel sum = new Channel(this.width, this.height, this.maxValue);

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                sum.set(i, j, (short) Math.min(Math.max(0, this.data[i][j] + addend.data[i][j]), this.maxValue));
            }
        }

        return sum;
    }

    private List<Short> selectPixels(int i, int j, int size) {
        List<Short> pixels = new ArrayList<>();
        int startValue = -(size - 1) / 2;

        for (int ki = 0; ki < size; ki++) {
            for (int kj = 0; kj < size; kj++) {
                int iMoved = i + startValue + ki;
                int jMoved = j + startValue + kj;
                if (
                        (iMoved < 0 || iMoved >= this.height) ||
                                (jMoved < 0 || jMoved >= this.width)
                ) {
                    pixels.add((short) 0);
                } else {
                    pixels.add(this.data[iMoved][jMoved]);
                }
            }
        }

        return pixels;
    }
}
