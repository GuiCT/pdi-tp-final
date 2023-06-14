package pdi.io;

import pdi.Channel;
import pdi.PGM;
import pdi.PPM;

import java.io.*;
import java.util.Scanner;

public class NetpbmReader {
    private String filePath;
    private File file;
    private String magicIdentifier;
    private int columns;
    private int lines;
    private int maxVal;
    private int bpp;
    private Scanner fileScanner;
    private BufferedInputStream bufferedInputStream;
    private int currentByte;
    private int bytePosition;

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

        // Utiliza o número máximo assumido por um pixel para determinar o BPP
        this.bpp = (int) Math.ceil(Math.log(maxVal + 1) / Math.log(2));

        // Retorna número de bytes no header
        return movedBytes;
    }

    // =================================================================
    // PGM
    // =================================================================

    public PGM readPGM(String filePath) throws IOException {
        this.filePath = filePath;
        this.file = new File(this.filePath);

        // Identificar qual tipo de PGM está sendo aberto
        if (!validatePGMIdentifier()) {
            throw new InvalidObjectException("Arquivo não obedece ao cabeçalho PGM.");
        }

        return switch (this.magicIdentifier) {
            case "P2" -> readPGMASCII();
            case "P5" -> readPGMRaw();
            default -> null;
        };
    }

    private boolean validatePGMIdentifier() throws IOException {
        Scanner sc = new Scanner(
                new BufferedInputStream(
                        new FileInputStream(this.filePath)
                )
        );
        this.magicIdentifier = sc.nextLine();
        sc.close();
        return this.magicIdentifier.matches("P[25]");
    }

    private PGM readPGMASCII() throws FileNotFoundException {
        // Lê cabeçalho e lê informações sobre dimensionalidade e valor máximo
        readHeader();

        // Inicializando canal
        Channel grayChannel = new Channel(this.columns, this.lines, this.maxVal);

        // Lendo valores ASCII
        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                grayChannel.set(i, j, this.fileScanner.nextInt());
            }
        }

        this.fileScanner.close();
        return new PGM(grayChannel);
    }

    private PGM readPGMRaw() throws IOException {
        // Lê cabeçalho e lê informações sobre dimensionalidade e valor máximo
        int movedBytes = readHeader();
        // Não utiliza mais o fileScanner
        this.fileScanner.close();

        this.bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file));
        long actuallyMovedBytes = this.bufferedInputStream.skip(movedBytes);

        // Verifica se o número de bytes pulados é igual ao número de bytes no header
        if (actuallyMovedBytes != movedBytes) {
            throw new IOException("Erro ao pular bytes do cabeçalho.");
        }

        // Inicializando canal
        Channel grayChannel = new Channel(this.lines, this.columns, this.maxVal);

        this.currentByte = -1;
        this.bytePosition = 8;
        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                grayChannel.set(i, j, readNextPixel());
            }
        }

        this.bufferedInputStream.close();
        return new PGM(grayChannel);
    }

    // =================================================================
    // PPM
    // =================================================================

    public PPM readPPM(String filePath) throws IOException {
        this.filePath = filePath;
        this.file = new File(this.filePath);

        // Identificar qual tipo de PPM está sendo aberto
        if (!validatePPMIdentifier()) {
            throw new InvalidObjectException("Arquivo não obedece ao cabeçalho PPM.");
        }

        return switch (this.magicIdentifier) {
            case "P3" -> readPPMASCII();
            case "P6" -> readPPMRaw();
            default -> null;
        };
    }

    private boolean validatePPMIdentifier() throws IOException {
        Scanner sc = new Scanner(
                new BufferedInputStream(
                        new FileInputStream(this.filePath)
                )
        );
        this.magicIdentifier = sc.nextLine();
        sc.close();
        return this.magicIdentifier.matches("P[36]");
    }

    private PPM readPPMASCII() throws FileNotFoundException {
        // Lê cabeçalho e lê informações sobre dimensionalidade e valor máximo
        readHeader();

        // Inicializando canais de cores
        Channel redChannel = new Channel(this.lines, this.columns, this.maxVal);
        Channel greenChannel = new Channel(this.lines, this.columns, this.maxVal);
        Channel blueChannel = new Channel(this.lines, this.columns, this.maxVal);

        // Lendo valores ASCII
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                redChannel.set(i, j, fileScanner.nextInt());
                greenChannel.set(i, j, fileScanner.nextInt());
                blueChannel.set(i, j, fileScanner.nextInt());
            }
        }

        fileScanner.close();
        return new PPM(redChannel, greenChannel, blueChannel);
    }

    private PPM readPPMRaw() throws IOException {
        // Lê cabeçalho e lê informações sobre dimensionalidade e valor máximo
        int movedBytes = readHeader();
        // Não utiliza mais o fileScanner
        this.fileScanner.close();

        this.bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file));
        long actuallyMovedBytes = this.bufferedInputStream.skip(movedBytes);

        // Verifica se o número de bytes pulados é igual ao número de bytes no header
        if (actuallyMovedBytes != movedBytes) {
            throw new IOException("Erro ao pular bytes do cabeçalho.");
        }

        // Inicializando canais de cores
        Channel redChannel = new Channel(this.lines, this.columns, this.maxVal);
        Channel greenChannel = new Channel(this.lines, this.columns, this.maxVal);
        Channel blueChannel = new Channel(this.lines, this.columns, this.maxVal);

        this.currentByte = -1;
        this.bytePosition = 8;
        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                redChannel.set(i, j, readNextPixel());
                greenChannel.set(i, j, readNextPixel());
                blueChannel.set(i, j, readNextPixel());
            }
        }

        this.bufferedInputStream.close();
        return new PPM(redChannel, greenChannel, blueChannel);
    }

    // =================================================================
    // Funções não implementadas
    // =================================================================

    private int readNextPixel() throws IOException {
        StringBuilder bits = new StringBuilder();
        for (int i = 0; i < bpp; i++) {
            if (this.bytePosition == 8) {
                this.currentByte = this.bufferedInputStream.read();
                this.bytePosition = 0;
            }
            bits.append((this.currentByte >> (7 - this.bytePosition)) & 1);
            this.bytePosition++;
        }

        return Integer.parseInt(bits.toString(), 2);
    }
}
