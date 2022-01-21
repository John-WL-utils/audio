package audio.fft;

import util.math.vector.Vector2;

public class ComplexFftDataUniper {

    public static Vector2[] unzip(final double[] fftData) {
        final Vector2[] unzipped = new Vector2[fftData.length/2];

        for(int i = 0; i < unzipped.length; i++) {
            unzipped[i] = new Vector2(fftData[i*2], fftData[i*2+1]);
        }

        return unzipped;
    }

    public static Vector2[] unzip(final float[] fftData) {
        final Vector2[] unzipped = new Vector2[fftData.length/2];

        for(int i = 0; i < unzipped.length; i++) {
            unzipped[i] = new Vector2(fftData[i*2], fftData[i*2+1]);
        }

        return unzipped;
    }
}
