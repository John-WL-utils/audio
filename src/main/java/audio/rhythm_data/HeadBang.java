package audio.rhythm_data;

import java.util.concurrent.atomic.AtomicReference;

public class HeadBang extends AtomicReference<Float> {

    public HeadBang() {
        super();
    }

    public HeadBang(final float time) {
        super(time);
    }

}
