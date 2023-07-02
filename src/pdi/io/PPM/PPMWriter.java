package pdi.io.PPM;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import pdi.PPM;

/**
 * <h1>Escritora de arquivos do formato PPM.</h1>
 * <p>
 * Encapsula estado de escrita, não podendo ser instanciada fora de seu próprio
 * escopo.
 * Dessa forma, toda vez que uma escrita é realizada, uma nova instância é
 * criada.
 * </p>
 */
public class PPMWriter {
    /**
     * Imagem a ser escrita.
     */
    private final PPM savedImage;
    /**
     * Número de colunas da imagem.
     */
    private final int columns;
    /**
     * Número de linhas da imagem.
     */
    private final int lines;
    /**
     * Valor máximo assumido por um pixel.
     */
    private final int maxVal;
    /**
     * Arquivo a ser escrito.
     */
    private final File file;
    /**
     * FileWriter utilizado para a escrita do arquivo.
     */
    private FileWriter fileWriter;

    /**
     * Construtor privado que recebe o arquivo a ser escrito e a imagem a ser
     * escrita.
     * 
     * @param savedImage Imagem a ser escrita.
     * @param file       Arquivo a ser escrito.
     */
    private PPMWriter(PPM savedImage, File file) {
        this.savedImage = savedImage;
        this.columns = savedImage.getWidth();
        this.lines = savedImage.getHeight();
        this.maxVal = savedImage.getMaxValue();
        this.file = file;
    }

    /**
     * Método estático que escreve uma imagem PPM no formato ASCII.
     * 
     * @param savedImage Imagem a ser escrita.
     * @param filePath   Caminho do arquivo a ser escrito.
     * @throws IOException Caso ocorra algum erro na escrita do arquivo.
     */
    public static void writeASCII(PPM savedImage, String filePath) throws IOException {
        File file = new File(filePath);
        PPMWriter writer = new PPMWriter(savedImage, file);
        writer.writePPMASCII();
    }

    /**
     * Método estático que escreve uma imagem PPM no formato binário.
     * 
     * @param savedImage Imagem a ser escrita.
     * @param filePath   Caminho do arquivo a ser escrito.
     * @throws IOException Caso ocorra algum erro na escrita do arquivo.
     */
    public static void writeBinary(PPM savedImage, String filePath) throws IOException {
        File file = new File(filePath);
        PPMWriter writer = new PPMWriter(savedImage, file);
        writer.writePPMBinary();
    }

    /**
     * Método privado que escreve o cabeçalho do arquivo PPM.
     * 
     * @param binary Indica se o arquivo é binário ou não.
     * @throws IOException Caso ocorra algum erro na escrita do arquivo.
     */
    private void writeHeader(boolean binary) throws IOException {
        this.fileWriter = new FileWriter(this.file);

        if (binary) {
            this.fileWriter.write("P6\n");
        } else {
            this.fileWriter.write("P3\n");
        }

        String widthAndHeight = this.columns + " " + this.lines + "\n";
        this.fileWriter.write(widthAndHeight);

        String maxValue = this.maxVal + "\n";
        this.fileWriter.write(maxValue);
    }

    /**
     * Método privado que escreve uma imagem PPM no formato binário.
     * 
     * @throws IOException Caso ocorra algum erro na escrita do arquivo.
     */
    private void writePPMBinary() throws IOException {
        writeHeader(true);
        this.fileWriter.close();

        // BufferedOutputStream no modo append
        // Escreve os bytes da imagem no arquivo
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.file, true));

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                bufferedOutputStream.write(this.savedImage.getRed(i, j));
                bufferedOutputStream.write(this.savedImage.getGreen(i, j));
                bufferedOutputStream.write(this.savedImage.getBlue(i, j));
            }
        }

        bufferedOutputStream.close();
    }

    /**
     * Método privado que escreve uma imagem PPM no formato ASCII.
     * 
     * @throws IOException Caso ocorra algum erro na escrita do arquivo.
     */
    private void writePPMASCII() throws IOException {
        writeHeader(false);

        // FileWriter utilizado no cabeçalho é reaproveitado
        // Visto que o restante do arquivo também é salvo em modo texto
        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.fileWriter.write(this.savedImage.getRed(i, j) + " ");
                this.fileWriter.write(this.savedImage.getGreen(i, j) + " ");
                this.fileWriter.write(this.savedImage.getBlue(i, j) + " ");
            }
            this.fileWriter.write("\n");
        }

        this.fileWriter.close();
    }
}
