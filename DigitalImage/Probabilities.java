import iptoolkit.*;
import java.util.Arrays;

public class Probabilities {

    public static void main(String[] args) throws Exception {

        IntImage src = new IntImage("");
        MainWindow mw = new MainWindow(100, 100);

        IntImage dst = reduceTo16GLs(src);

        double[] ihist = getProbabilities(dst);
        double entropy = getEntropy(ihist);

        String entropyString = String.valueOf(entropy);
        mw.println(entropyString);
        mw.println("prob " + (Arrays.toString(ihist)));

        Histogram hSrc = new Histogram(dst); //creates histogram

        // Displays an image representation
        IntImage histImage;
        histImage = hSrc.makeImage();
        histImage.displayImage(600, 300);

        src.displayImage(300, 100); // location of where image displays


    }//main

    static IntImage reduceTo16GLs(IntImage in) {
        IntImage imageOut = new IntImage(in.getRows(), in.getCols());
        for (int r = 0; r < in.getRows(); r++) {
            for (int c = 0; c < in.getCols(); c++) {
                imageOut.pixels[r][c] = (in.pixels[r][c] / 16) * 16;
            }
        }
        return imageOut;
    }

    static int[] getFrequencies(IntImage in) {
        int[] hist = new int[256];

        for (int r = 0; r < in.getRows(); r++) {
            for (int c = 0; c < in.getCols(); c++) {
                hist[in.pixels[r][c]]++;
            }
        }
        return hist;
    }

    static double[] getProbabilities(IntImage in) {
        double[] hist;
        int[] iHist = new int[256];
        int nonZero;
        int nRows = in.getRows();
        int nCols = in.getCols();

        iHist = getFrequencies(in);
        nonZero = countNonZeroGLs(iHist);
        hist = new double[nonZero];
        for (int i = 0, j = 0; i < 256; i++) {
            if (iHist[i] != 0) {
                hist[j++] = ((double) iHist[i]) / (nRows * nCols);
            }
        }
        return hist;
    }

    static int countNonZeroGLs(int[] frequencies) {
        int count = 0;
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] != 0) {
                count++;
            }
        }
        return count;
    }

    static double getEntropy(double[] probabilities) {
        double ent = 0.0;
        for (int i = 0; i < probabilities.length; i++) {
            ent += probabilities[i] *log2(probabilities[i]);
        }
        return -ent;
    }

    static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    static double getRMSError(IntImage img1, IntImage img2) {
        // Assumes both images are the same size
        double rmsError;
        double diff;
        double diffSqrdTotal = 0.0;

        int nRows = img1.getRows();
        int nCols = img1.getCols();

        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                diff = img1.pixels[r][c] - img2.pixels[r][c];
                diffSqrdTotal += diff * diff;
            }
        }
        rmsError = Math.sqrt(diffSqrdTotal / (nRows * nCols));
        return rmsError;
    }
}




