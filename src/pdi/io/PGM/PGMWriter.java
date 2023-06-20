package pdi.io.PGM;

import pdi.PGM;

import java.io.*;

public class PGMWriter {
    private final PGM savedImage;
    private final int columns;
    private final int lines;
    private final int maxVal;
    private final File file;
    private FileWriter fileWriter;

    private PGMWriter(PGM savedImage, File file) {
        this.savedImage = savedImage;
        this.columns = savedImage.getWidth();
        this.lines = savedImage.getHeight();
        this.maxVal = savedImage.getMaxValue();
        this.file = file;
    }

    public static void writeASCII(PGM savedImage, String filePath) throws IOException {
        File file = new File(filePath);
        PGMWriter writer = new PGMWriter(savedImage, file);
        writer.writePGMASCII();
    }

    public static void writeBinary(PGM savedImage, String filePath) throws IOException {
        File file = new File(filePath);
        PGMWriter writer = new PGMWriter(savedImage, file);
        writer.writePGMBinary();
    }

    private void writeHeader(boolean binary) throws IOException {
        this.fileWriter = new FileWriter(this.file);

        if (binary) {
            this.fileWriter.write("P5\n");
        } else {
            this.fileWriter.write("P2\n");
        }

        String widthAndHeight = this.columns + " " + this.lines + "\n";
        this.fileWriter.write(widthAndHeight);

        String maxValue = this.maxVal + "\n";
        this.fileWriter.write(maxValue);
    }

    public void writePGMBinary() throws IOException {
        writeHeader(true);
        this.fileWriter.close();

        // BufferedOutputStream no modo append
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.file, true));

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                bufferedOutputStream.write(savedImage.get(i, j));
            }
        }

        bufferedOutputStream.close();
    }

    public void writePGMASCII() throws IOException {
        writeHeader(false);

        for (int i = 0; i < this.lines; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.fileWriter.write(this.savedImage.get(i, j) + " ");
            }
            this.fileWriter.write("\n");
        }

        this.fileWriter.close();
    }
}
