package pdi;

import java.io.*;
import java.util.Scanner;
import java.util.stream.IntStream;

public class PGM {
    private Channel channel;

    PGM(Channel channel) {
        this.channel = channel;
    }

    public void saveFile(String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            // Por padrão, usa formato ASCII
            // Cabeçalho: P2, largura, altura, valor máximo
            fileWriter.write("P2\n");
            fileWriter.write(this.channel.width + " " + this.channel.height + "\n");
            fileWriter.write(this.channel.getMaxValue() + "\n");
            // Escreve cada valor do canal, separado por espaço
            // Quando chega ao fim de uma linha, pula linha no arquivo texto
            for (int i = 0; i < this.channel.height; i++) {
                for (int j = 0; j < this.channel.width; j++) {
                    fileWriter.write(this.channel.get(i, j) + " ");
                }
                fileWriter.write("\n");
            }
        }
    }
}
