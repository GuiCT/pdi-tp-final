package pdi;


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
        Channel newChannel = new Channel(this.width, this.height, this.maxValue);
        for (int i = 0; i < this.height; i++) {
            System.arraycopy(this.data[i], 0, newChannel.data[i], 0, this.width);
        }
        return newChannel;
    }
}
