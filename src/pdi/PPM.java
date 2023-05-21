package pdi;

import java.io.*;
import java.util.Scanner;

public class PPM {
    private Channel redChannel;
    private Channel greenChannel;
    private Channel blueChannel;

    public PPM(Channel redChannel, Channel greenChannel, Channel blueChannel) {
        this.redChannel = redChannel;
        this.greenChannel = greenChannel;
        this.blueChannel = blueChannel;
    }

    public int getWidth() {
        return this.redChannel.width;
    }

    public int getHeight() {
        return this.redChannel.height;
    }

    public short getMaxValue() {
        return this.redChannel.getMaxValue();
    }

    public short getRed(int i, int j) {
        return this.redChannel.get(i, j);
    }

    public short getGreen(int i, int j) {
        return this.greenChannel.get(i, j);
    }

    public short getBlue(int i, int j) {
        return this.blueChannel.get(i, j);
    }

    public void setRed(int i, int j, short value) {
        this.redChannel.set(i, j, value);
    }

    public void setGreen(int i, int j, short value) {
        this.greenChannel.set(i, j, value);
    }

    public void setBlue(int i, int j, short value) {
        this.blueChannel.set(i, j, value);
    }

    public void saveFile(String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            // Por padrão, usa formato ASCII
            // Cabeçalho: P3, largura, altura, valor máximo
            fileWriter.write("P3\n");
            fileWriter.write(this.redChannel.width + " " + this.redChannel.height + "\n");
            fileWriter.write(this.redChannel.getMaxValue() + "\n");
            // Escreve cada valor do canal, separado por espaço
            // Quando chega ao fim de uma linha, pula linha no arquivo texto
            for (int i = 0; i < this.redChannel.height; i++) {
                for (int j = 0; j < this.redChannel.width; j++) {
                    fileWriter.write(this.redChannel.get(i, j) + " ");
                    fileWriter.write(this.greenChannel.get(i, j) + " ");
                    fileWriter.write(this.blueChannel.get(i, j) + " ");
                }
                fileWriter.write("\n");
            }
        }
    }
}
