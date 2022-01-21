import audio.bpm_analysis.BpmFinder;
import org.junit.jupiter.api.Test;

import java.io.File;

public class BpmFinderTest {

    private static final File AUDIO_FILE = new File("src\\test\\java\\audio.wav");

    @Test
    public void bpmAnalyserReturnsAListOfBpmAndOffsets() {
        var bpmData = BpmFinder.extractRhythmData(AUDIO_FILE);
        assert bpmData.isPresent();
    }
}
