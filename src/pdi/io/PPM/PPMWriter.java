package pdi.io.PPM;

import pdi.PPM;

import java.io.*;

public class PPMWriter {
    private final PPM savedImage;
    private final int columns;
    private final int lines;
    private final int maxVal;
    private final File file;
    private FileWriter fileWriter;

    private PPMWriter(PPM savedImage, File file) {
        this.savedImage = savedImage;
        this.columns = savedImage.getWidth();
        this.lines = savedImage.getHeight();
        this.maxVal = savedImage.getMaxValue();
        this.file = file;
    }

    public static void writeASCII(PPM savedImage, String filePath) throws IOException {
        File file = new File(filePath);
        PPMWriter writer = new PPMWriter(savedImage, file);
        writer.writePPMASCII();
    }

    public static void writeBinary(PPM savedImage, String filePath) throws IOException {
        File file = new File(filePath);
        PPMWriter writer = new PPMWriter(savedImage, file);
        writer.writePPMBinary();
    }

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

    private void writePPMBinary() throws IOException {
        writeHeader(true);
        this.fileWriter.close();

        // BufferedOutputStream no modo append
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

    private void writePPMASCII() throws IOException {
        writeHeader(false);

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
