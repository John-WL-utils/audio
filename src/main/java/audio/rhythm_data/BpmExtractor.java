package audio.rhythm_data;

import util.math.vector.Vector2;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class BpmExtractor {

    public static Optional<Bpm> extract(final Vector2[] complexFftData, final double fftResolution) {
        return Arrays.stream(complexFftData)
                .reduce((v1, v2) -> v1.magnitudeSquared() > v2.magnitudeSquared() ? v1 : v2)
                .map(sinusData -> Arrays.stream(complexFftData).collect(Collectors.toList()).indexOf(sinusData))
                .map(index -> index * fftResolution)
                .map(bps -> bps*60)
                .map(Double::floatValue)
                .map(Bpm::new);
    }
}
