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
