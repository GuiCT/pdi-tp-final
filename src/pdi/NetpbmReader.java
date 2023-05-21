package pdi;

import java.io.*;
import java.util.Scanner;

public class NetpbmReader {
    private String filePath;
    private File file;
    private String magicIdentifier;
    private int columns;
    private int lines;
    private short bpp;
    private Scanner fileScanner;

    private void readHeader() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(this.file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        this.fileScanner = new Scanner(bufferedInputStream);

        // Pulando até encontrar primeiro número inteiro (colunas)
        while (!this.fileScanner.hasNextInt()) {
            this.fileScanner.nextLine();
        }

        // Guarda número de colunas e linhas
        this.columns = this.fileScanner.nextInt();
        this.lines = this.fileScanner.nextInt();
        // Valor máximo assumido por um pixel
        short maxVal = this.fileScanner.nextShort();
        // Utiliza o número máximo assumido por um pixel para determinar o BPP
        this.bpp = (short) Math.ceil(Math.log(maxVal + 1) / Math.log(2));
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
        Channel grayChannel = new Channel(this.lines, this.columns, this.bpp);

        // Lendo valores ASCII
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                grayChannel.set(i, j, fileScanner.nextShort());
            }
        }

        fileScanner.close();
        return new PGM(grayChannel);
    }

    private PGM readPGMRaw() throws IOException {
        // Lê cabeçalho e lê informações sobre dimensionalidade e valor máximo
        readHeader();

        // Inicializando canal
        Channel grayChannel = new Channel(this.lines, this.columns, this.bpp);

        // TODO - Encontrar forma de ler byte a byte

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                // TODO - Definir função que lê byte a byte
                grayChannel.set(i, j, readNextPixel());
            }
        }

        this.fileScanner.close();
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
        Channel redChannel = new Channel(this.lines, this.columns, this.bpp);
        Channel greenChannel = new Channel(this.lines, this.columns, this.bpp);
        Channel blueChannel = new Channel(this.lines, this.columns, this.bpp);

        // Lendo valores ASCII
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                redChannel.set(i, j, fileScanner.nextShort());
                greenChannel.set(i, j, fileScanner.nextShort());
                blueChannel.set(i, j, fileScanner.nextShort());
            }
        }

        fileScanner.close();
        return new PPM(redChannel, greenChannel, blueChannel);
    }

    private PPM readPPMRaw() throws IOException {
        // Lê cabeçalho e lê informações sobre dimensionalidade e valor máximo
        readHeader();

        // Inicializando canais de cores
        Channel redChannel = new Channel(this.lines, this.columns, this.bpp);
        Channel greenChannel = new Channel(this.lines, this.columns, this.bpp);
        Channel blueChannel = new Channel(this.lines, this.columns, this.bpp);

        // TODO - Encontrar forma de ler byte a byte

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                // TODO - Definir função que lê byte a byte
                redChannel.set(i, j, readNextPixel());
                greenChannel.set(i, j, readNextPixel());
                blueChannel.set(i, j, readNextPixel());
            }
        }

        this.fileScanner.close();
        return new PPM(redChannel, greenChannel, blueChannel);
    }

    // =================================================================
    // Funções não implementadas
    // =================================================================

    private short readNextPixel() {
        // TODO - Lógica de leitura de byte a byte
        return 0;
    }
}
