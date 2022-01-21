package audio.rhythm_data;

import audio.rhythm_data.Phase;
import util.math.vector.Vector2;

import java.util.Arrays;
import java.util.Optional;

public class PhaseExtractor {

    public static Optional<Phase> extract(Vector2[] complexFftData) {
        return Arrays.stream(complexFftData)
                .reduce((v1, v2) -> v1.magnitudeSquared() > v2.magnitudeSquared() ? v1 : v2)
                .map(Vector2::atan2)
                .map(Double::floatValue)
                .map(Phase::new);
    }
}
