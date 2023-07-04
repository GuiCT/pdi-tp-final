package pdi.io.PPM;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Scanner;

import pdi.Channel;
import pdi.PPM;

/**
 * <h1>Leitora de arquivos do formato PPM.</h1>
 * <p>
 * Encapsula estado de leitura, não podendo ser instanciada fora de seu próprio
 * escopo.
 * Dessa forma, toda vez que uma leitura é realizada, uma nova instância é
 * criada.
 * </p>
 */
public class PPMReader {
    /**
     * Arquivo a ser lido.
     */
    private File file;
    /**
     * Identificador do arquivo.
     */
    private String magicIdentifier;
    /**
     * Número de colunas da imagem.
     */
    private int columns;
    /**
     * Número de linhas da imagem.
     */
    private int lines;
    /**
     * Valor máximo assumido por um pixel.
     */
    private int maxVal;
    /**
     * Scanner utilizado para a leitura do arquivo.
     */
    private Scanner fileScanner;
    /**
     * BufferedInputStream utilizado para a leitura do arquivo.
     */
    private BufferedInputStream bufferedInputStream;

    /**
     * Método utilizado para a leitura de um arquivo PPM.
     * 
     * @param filePath Caminho do arquivo a ser lido.
     * @return PPM Objeto PPM com os dados lidos.
     * @throws IOException Caso ocorra algum erro na leitura do arquivo.
     */
    public static PPM readPPM(String filePath) throws IOException {
        PPMReader reader = new PPMReader();
        return reader.read(filePath);
    }

    /**
     * Método privado que utiliza a instância criada para ler o arquivo.
     * Não deve ser chamado diretamente.
     * 
     * @param filePath Caminho do arquivo a ser lido.
     * @return PPM Objeto PPM com os dados lidos.
     * @throws IOException Caso ocorra algum erro na leitura do arquivo.
     */
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

    /**
     * Método privado que valida o identificador do arquivo.
     * Isto é, verifica se o identificador mágico é P3 ou P6.
     * 
     * @return boolean true se o identificador for válido, false caso contrário.
     * @throws FileNotFoundException Caso o arquivo não seja encontrado.
     */
    private boolean validateIdentifier() throws FileNotFoundException {
        Scanner sc = new Scanner(
                new BufferedInputStream(
                        new FileInputStream(this.file)));
        this.magicIdentifier = sc.nextLine();
        sc.close();
        return this.magicIdentifier.matches("P[36]");
    }

    /**
     * Método privado que lê o cabeçalho do arquivo.
     * 
     * @return int Número de bytes lidos.
     * @throws FileNotFoundException Caso o arquivo não seja encontrado.
     */
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

    /**
     * Método privado que lê o arquivo PPM no formato ASCII.
     * 
     * @return PPM Objeto PPM com os dados lidos.
     * @throws FileNotFoundException Caso o arquivo não seja encontrado.
     */
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

    /**
     * Método privado que lê o arquivo PPM no formato RAW.
     * 
     * @return PPM Objeto PPM com os dados lidos.
     * @throws IOException Caso ocorra algum erro na leitura do arquivo.
     */
    private PPM readRaw() throws IOException {
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

        // Inicializando canais
        Channel redChannel = new Channel(this.columns, this.lines, this.maxVal);
        Channel greenChannel = new Channel(this.columns, this.lines, this.maxVal);
        Channel blueChannel = new Channel(this.columns, this.lines, this.maxVal);

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                redChannel.set(i, j, this.bufferedInputStream.read());
                greenChannel.set(i, j, this.bufferedInputStream.read());
                blueChannel.set(i, j, this.bufferedInputStream.read());
            }
        }

        this.bufferedInputStream.close();
        return new PPM(redChannel, greenChannel, blueChannel);
    }
}
