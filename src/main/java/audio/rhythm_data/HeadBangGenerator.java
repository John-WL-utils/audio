package audio.rhythm_data;

import audio.rhythm_data.HeadBang;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeadBangGenerator {
    public static List<HeadBang> generateHeadBangs(final float[] audioData, final AudioFormat audioFormat) {
        final List<HeadBang> headBangs = new ArrayList<>();

        final float splitDataLength = 7; // amount of seconds to form 1 "window"
        final float rasterResolution = 1f/10; // amount of seconds that the "window" is shifted every iteration
        final int indexLength = (int) (audioFormat.getSampleRate() * splitDataLength); // amount of indexes to form 1 "window"
        final int indexGap = (int) (audioFormat.getSampleRate() * rasterResolution); // amount of indexes that the "window" is shifted every iteration

        final int amountOfPasses = (audioData.length - indexLength) / indexGap; // amount of windows

        for(int i = 0; i < amountOfPasses; i++) {
            final int startIndexOfSplitAudio = i*indexGap;
            final int endIndexOfSplitAudio = (i*indexGap)+(indexLength-1);
            final float[] audioSection = Arrays.copyOfRange(audioData, startIndexOfSplitAudio, endIndexOfSplitAudio);
            final var tempoOpt = TempoExtractor.extract(audioSection, audioFormat);
            if(tempoOpt.isEmpty()) {
                continue;
            }
            System.out.println(String. format("%.2f", i*100.0/amountOfPasses) + "%");
            final var tempo = tempoOpt.get();
            final Bpm bpmData = tempo.value1;
            final Phase phaseData = tempo.value2;
            final float baseSampleOffset = startIndexOfSplitAudio/audioFormat.getSampleRate();
            final float secondsPerBeat = bpmData.toSecondsPerBeat();
            //System.out.println(bpmData.get());
            final float quantizedHalfSplitDataLength = ((int)(splitDataLength/(2*secondsPerBeat) + 0.5))*secondsPerBeat;
            final HeadBang headBang = new HeadBang(phaseData.toTimeOffset(bpmData) + baseSampleOffset + quantizedHalfSplitDataLength);
            headBangs.add(headBang);
        }

        return headBangs;
    }
}
