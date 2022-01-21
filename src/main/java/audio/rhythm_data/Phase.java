package audio.rhythm_data;

import java.util.concurrent.atomic.AtomicReference;

public class Phase extends AtomicReference<Float> {

    public Phase() {
        super();
    }

    public Phase(float value) {
        super(value);
    }

    public float toTimeOffset(final Bpm bpmData) {
        final double secondsPerBeat = bpmData.toSecondsPerBeat();
        return (float) (get()*secondsPerBeat/(Math.PI*2));
    }
}
