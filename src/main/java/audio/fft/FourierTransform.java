package audio.fft;

import org.jtransforms.fft.DoubleFFT_1D;
import org.jtransforms.fft.FloatFFT_1D;
import util.math.vector.Vector2;

import java.util.Arrays;

public class FourierTransform {

    public static Vector2[] of(float[] a) {
        FloatFFT_1D fftFunc = new FloatFFT_1D(a.length);
        float[] complexPairs = Arrays.copyOf(a, a.length*2);
        fftFunc.realForwardFull(complexPairs);
        return ComplexFftDataUniper.unzip(complexPairs);
    }

    public static Vector2[] of(double[] a) {
        DoubleFFT_1D fftFunc = new DoubleFFT_1D(a.length);
        double[] complexPairs = Arrays.copyOf(a, a.length*2);
        fftFunc.realForwardFull(complexPairs);
        return ComplexFftDataUniper.unzip(complexPairs);
    }
}
