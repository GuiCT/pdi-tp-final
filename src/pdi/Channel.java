package pdi;


import pdi.operations.areaWise.Mask;
import pdi.operations.elementWise.ElementWiseOperation;

import java.util.ArrayList;
import java.util.List;

public class Channel {
    public final int width;
    public final int height;
    public final int maxValue;
    private final int[][] data;

    public Channel(int width, int height, int maxValue) {
        this.width = width;
        this.height = height;
        this.maxValue = maxValue;
        this.data = new int[height][width];
    }

    public void set(int i, int j, int value) {
        assert (value > 0): "Valor do pixel deve ser maior ou igual a zero";
        assert (value <= this.maxValue): "Valor do pixel deve ser menor ou igual ao valor máximo";

        this.data[i][j] = value;
    }

    public int get(int i, int j) {
        return this.data[i][j];
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public Channel cloneChannel() {
        return new Channel(this.width, this.height, this.maxValue);
    }

    public Channel copyChannel() {
        Channel newChannel = this.cloneChannel();
        for (int i = 0; i < newChannel.height; i++) {
            for (int j = 0; j < newChannel.width; j++) {
                newChannel.set(i, j, this.data[i][j]);
            }
        }

        return newChannel;
    }

    public Channel elementWiseOperation(ElementWiseOperation operation) {
        Channel newChannel = this.cloneChannel();
        for (int i = 0; i < newChannel.height; i++) {
            for (int j = 0; j < newChannel.width; j++) {
                newChannel.set(i, j, this.data[i][j]);
            }
        }

        return newChannel;
    }

    public Channel elementWiseOperation(ElementWiseOperation[] operations) {
        ElementWiseOperation compositeOperation = (value, maxValue) -> {
            int result = value;
            for (ElementWiseOperation operation : operations) {
                result = operation.apply(result, maxValue);
            }
            return result;
        };

        return this.elementWiseOperation(compositeOperation);
    }

    public Channel maskOperation(Mask operation) {
        Channel newChannel = this.cloneChannel();
        for (int i = 0; i < newChannel.height; i++) {
            for (int j = 0; j < newChannel.width; j++) {
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
                int result = this.data[i][j] - subtrahend.data[i][j];
                difference.set(i, j, Math.min(Math.max(0, result), this.maxValue));
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
                int result = this.data[i][j] + addend.data[i][j];
                sum.set(i, j, Math.min(Math.max(0, result), this.maxValue));
            }
        }

        return sum;
    }

    public int[] getHistogram() {
        int[] histogram = new int[this.maxValue + 1];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                histogram[this.data[i][j]]++;
            }
        }

        return histogram;
    }

    private List<Integer> selectPixels(int i, int j, int size) {
        List<Integer> pixels = new ArrayList<>();
        int startValue = -(size - 1) / 2;

        for (int ki = 0; ki < size; ki++) {
            for (int kj = 0; kj < size; kj++) {
                int iMoved = i + startValue + ki;
                int jMoved = j + startValue + kj;
                if (
                        (iMoved < 0 || iMoved >= this.height) ||
                                (jMoved < 0 || jMoved >= this.width)
                ) {
                    pixels.add(0);
                } else {
                    pixels.add(this.data[iMoved][jMoved]);
                }
            }
        }

        return pixels;
    }
}
