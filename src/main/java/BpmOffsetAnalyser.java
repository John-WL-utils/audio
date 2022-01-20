import audio_file_converter.AudioFileConverter;
import org.jtransforms.fft.FloatFFT_1D;
import rhythm_data.BPM;
import rhythm_data.Offset;
import util.data_structure.tupple.Tuple2;
import wav_untangler.ChannelType;
import wav_untangler.WavDataUntangler;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BpmOffsetAnalyser {

    public static Optional<List<Tuple2<BPM, Offset>>> extractRhythmData(final File soundFile) {
        final Optional<AudioInputStream> audioInputStreamOpt =
                AudioFileConverter.extractAudioInputStreamFromAudioFile(soundFile);
        if(audioInputStreamOpt.isEmpty()) {
            return Optional.empty();
        }
        final AudioInputStream audioInputStream = audioInputStreamOpt.get();
        final AudioFormat audioFormat = audioInputStream.getFormat();
        final byte[] audioData;
        try {
            audioData = audioInputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        final float[] leftChannelBuffer = WavDataUntangler.extractChannel(ChannelType.LEFT, audioData, audioFormat);
        final float[] rightChannelBuffer = WavDataUntangler.extractChannel(ChannelType.RIGHT, audioData, audioFormat);
        final Optional<Tuple2<Double, Double>> bpmAndOffsetOpt = findBpmOfSoundBuffer(leftChannelBuffer, rightChannelBuffer, audioFormat);
        if(bpmAndOffsetOpt.isEmpty()) {
            return Optional.empty();
        }
        Tuple2<Double, Double> bpmAndOffset = bpmAndOffsetOpt.get();

        final BPM bpm = new BPM();
        bpm.set(bpmAndOffset.value1);
        final Offset offset = new Offset();
        offset.set(bpmAndOffset.value2);
        return Optional.of(List.of(new Tuple2<>(bpm, offset)));
    }

    private static Optional<Tuple2<Double, Double>> findBpmOfSoundBuffer(float[] unused, float[] a, final AudioFormat audioFormat) {
        final double dftResolution = audioFormat.getSampleRate() / a.length;
        final float[] complexPairs = fftOf(a);
        final float[] amplitudesSquared = computeAmplitudesSquaredFromComplexPairs(complexPairs);

        final int smallestFrequencyIndex = (int)(1/dftResolution);
        final int biggestFrequencyIndex = (int)(5/dftResolution);

        float biggestAmplitudeYet = 0;
        int bestIndexSoFar = -1;
        for(int i = smallestFrequencyIndex; i <= biggestFrequencyIndex; i++) {
            if(amplitudesSquared[i] > biggestAmplitudeYet) {
                biggestAmplitudeYet = amplitudesSquared[i];
                bestIndexSoFar = i;
            }
        }
        if(bestIndexSoFar == -1) {
            return Optional.empty();
        }
        final double phase = Math.atan2(complexPairs[bestIndexSoFar*2 + 1], complexPairs[bestIndexSoFar*2]);

        return Optional.of(new Tuple2<>(bestIndexSoFar * dftResolution * 60, phase >= 0 ? phase : 2*Math.PI + phase));
    }

    private static float[] fftOf(float[] a) {
        FloatFFT_1D fftFunc = new FloatFFT_1D(a.length);
        float[] complexPairs = Arrays.copyOf(a, a.length*2);
        fftFunc.realForwardFull(complexPairs);
        return complexPairs;
    }

    private static float[] computeAmplitudesSquaredFromComplexPairs(float[] complexPairs) {
        float[] amplitudesSquared = new float[complexPairs.length/2];

        for(int i = 0; i < amplitudesSquared.length; i++) {
            amplitudesSquared[i] = complexPairs[i*2] * complexPairs[i*2] + complexPairs[i*2+1] * complexPairs[i*2+1];
        }

        return amplitudesSquared;
    }
}
