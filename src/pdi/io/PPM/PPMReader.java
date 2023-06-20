package pdi.io.PPM;

import pdi.Channel;
import pdi.PPM;
import java.io.*;
import java.util.Scanner;

public class PPMReader {
    private File file;
    private String magicIdentifier;
    private int columns;
    private int lines;
    private int maxVal;
    private Scanner fileScanner;

    public static PPM readPPM(String filePath) throws IOException {
        PPMReader reader = new PPMReader();
        return reader.read(filePath);
    }

    private PPM read(String filePath) throws IOException {
        this.file = new File(filePath);

        // Identificar qual tipo de PPM está sendo aberto
        if (!validateIdentifier()) {
            throw new InvalidObjectException("Arquivo não obedece ao cabeçalho PPM.");
        }

        return switch (this.magicIdentifier) {
            case "P3" -> readASCII();
            case "P6" -> readRaw();
            default -> null;
        };
    }

    private boolean validateIdentifier() throws FileNotFoundException {
        Scanner sc = new Scanner(
                new BufferedInputStream(
                        new FileInputStream(this.file)
                )
        );
        this.magicIdentifier = sc.nextLine();
        sc.close();
        return this.magicIdentifier.matches("P[36]");
    }

    private int readHeader() throws FileNotFoundException {
        FileInputStream scannerFIS = new FileInputStream(this.file);
        BufferedInputStream scannerBIS = new BufferedInputStream(scannerFIS);
        this.fileScanner = new Scanner(scannerBIS);
        int movedBytes = 0;

        // Pulando até encontrar primeiro número inteiro (colunas)
        while (!this.fileScanner.hasNextInt()) {
            String ignoredLine = this.fileScanner.nextLine();
            movedBytes += ignoredLine.length() + 1;
        }

        // Guarda número de colunas e linhas
        String columnsLines = this.fileScanner.nextLine();
        movedBytes += columnsLines.length() + 1;

        // Extraindo valores de colunas e linhas
        Scanner columnsLinesScanner = new Scanner(columnsLines);
        this.columns = columnsLinesScanner.nextInt();
        this.lines = columnsLinesScanner.nextInt();
        columnsLinesScanner.close();

        // Valor máximo assumido por um pixel
        String maxValLine = this.fileScanner.nextLine();
        movedBytes += maxValLine.length() + 1;

        // Extraindo valor máximo assumido por um pixel
        Scanner maxValScanner = new Scanner(maxValLine);
        this.maxVal = maxValScanner.nextInt();
        maxValScanner.close();

        // Retorna número de bytes no header
        return movedBytes;
    }

    private PPM readASCII() throws FileNotFoundException {
        // Lê cabeçalho e lê informações sobre dimensionalidade e valor máximo
        readHeader();

        // Inicializando canais
        Channel redChannel = new Channel(this.columns, this.lines, this.maxVal);
        Channel greenChannel = new Channel(this.columns, this.lines, this.maxVal);
        Channel blueChannel = new Channel(this.columns, this.lines, this.maxVal);

        // Lendo valores ASCII
        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                redChannel.set(i, j, this.fileScanner.nextInt());
                greenChannel.set(i, j, this.fileScanner.nextInt());
                blueChannel.set(i, j, this.fileScanner.nextInt());
            }
        }

        this.fileScanner.close();
        return new PPM(redChannel, greenChannel, blueChannel);
    }

    private PPM readRaw() throws IOException {
        // Lê cabeçalho e lê informações sobre dimensionalidade e valor máximo
        int movedBytes = readHeader();
        // Não utiliza mais o fileScanner
        this.fileScanner.close();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file));
        long actuallyMovedBytes = bufferedInputStream.skip(movedBytes);

        // Verifica se o número de bytes pulados é igual ao número de bytes no header
        if (actuallyMovedBytes != movedBytes) {
            throw new IOException("Erro ao pular bytes do cabeçalho.");
        }

        // Inicializando canais
        Channel redChannel = new Channel(this.lines, this.columns, this.maxVal);
        Channel greenChannel = new Channel(this.lines, this.columns, this.maxVal);
        Channel blueChannel = new Channel(this.lines, this.columns, this.maxVal);

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                redChannel.set(i, j, bufferedInputStream.read());
                greenChannel.set(i, j, bufferedInputStream.read());
                blueChannel.set(i, j, bufferedInputStream.read());
            }
        }

        bufferedInputStream.close();
        return new PPM(redChannel, greenChannel, blueChannel);
    }
}
