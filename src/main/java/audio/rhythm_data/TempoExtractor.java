package audio.rhythm_data;

import audio.fft.FourierTransform;
import util.data_structure.tupple.Tuple2;
import util.math.vector.Vector2;

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;
import java.util.Optional;

public class TempoExtractor {

    public static Optional<Tuple2<Bpm, Phase>> extract(final float[] soundData, final AudioFormat audioFormat) {
        final float dftResolution = audioFormat.getSampleRate() / soundData.length;
        final Vector2[] soundFft = Arrays.copyOfRange(FourierTransform.of(soundData), 0, (int)(3/dftResolution));
        final Optional<Bpm> bpmOpt = BpmExtractor.extract(soundFft, dftResolution);
        final Optional<Phase> phaseOpt = PhaseExtractor.extract(soundFft);
        if(bpmOpt.isEmpty() || phaseOpt.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Tuple2<>(bpmOpt.get(), phaseOpt.get()));
    }
}
