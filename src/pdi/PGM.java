package pdi;

import java.io.*;
import java.util.Scanner;
import java.util.stream.IntStream;

public class PGM {
    private Channel channel;

    public PGM(String filePath) throws FileNotFoundException, InvalidObjectException {
        // Abrindo arquivo e instanciando Scanner
        Scanner fileScanner = new Scanner(new File(filePath));
        // Verificando se é igual a P2 ou P5
        // Se não é, trata exceção
        String magicIdentifier = fileScanner.nextLine();
        if (!magicIdentifier.matches("P[25]")) {
            throw new InvalidObjectException("Arquivo não obedece ao cabeçalho PGM.");
        }

        // Ignora todas as entradas até chegar no próximo inteiro.
        while (!fileScanner.hasNextInt()) {
            fileScanner.next();
        }

        // Guarda número de colunas e linhas
        int columns = fileScanner.nextInt();
        int lines = fileScanner.nextInt();
        // Valor máximo assumido por um pixel
        int maxVal = fileScanner.nextInt();

        // Cria canal para armazenar os valores
        Channel channel = new Channel(columns, lines, (short) maxVal);

        // Se é formato P2, lê cada número decimal em ASCII e armazena
        if (magicIdentifier.matches("P2")) {
            for (int i = 0; i < lines; i++) {
                for (int j = 0; j < columns; j++) {
                    channel.set(i, j, fileScanner.nextShort());
                }
            }
        } else {
            // Leitura de bits depende do BPP (bits per pixel)
            // Utiliza o número máximo assumido por um pixel para determinar o BPP
            short bpp = (short) Math.ceil(Math.log(maxVal + 1) / Math.log(2));
            Byte currentByte = null;
            short bytePosition = 8;
            // Lê cada byte e armazena o valor inteiro a partir dos bits
            for (int i = 0; i < lines; i++) {
                for (int j = 0; j < columns; j++) {
                    short value = 0;
                    for (int k = 0; k < bpp; k++) {
                        // Se já leu todos os bits do byte, lê o próximo
                        if (bytePosition == 8) {
                            currentByte = fileScanner.nextByte();
                            bytePosition = 0;
                        }
                        value += currentByte & ((int) Math.pow(2, bytePosition));
                        bytePosition++;
                    }
                    channel.set(i, j, value);
                }
            }
        }

        // Fecha o scanner
        fileScanner.close();

        // Guarda o canal
        this.channel = channel;
    }

    public PGM(int width, int height, short maxValue) {
        this.channel = new Channel(width, height, maxValue);
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
