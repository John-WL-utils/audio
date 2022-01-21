package audio.rhythm_data;

import java.util.concurrent.atomic.AtomicReference;

public class Bpm extends AtomicReference<Float> {

    public Bpm() {
        super();
    }

    public Bpm(float value) {
        super(value);
    }

    public float toSecondsPerBeat() {
        return 60/get();
    }
}
