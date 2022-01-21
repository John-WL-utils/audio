package audio.bpm_analysis;

import audio.audio_file_converter.AudioFileConverter;
import audio.rhythm_data.*;
import audio.wav_untangler.ChannelType;
import audio.wav_untangler.WavDataUntangler;
import util.data_structure.tupple.Tuple2;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BpmFinder {

    public static Optional<List<Tuple2<Bpm, Phase>>> extractRhythmData(final File soundFile) {
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
        return computeBPMP(leftChannelBuffer, rightChannelBuffer, audioFormat);
    }

    private static Optional<List<Tuple2<Bpm, Phase>>> computeBPMP(float[] leftAudio, float[] rightAudio, final AudioFormat audioFormat) {
        final List<HeadBang> headBangs = HeadBangGenerator.generateHeadBangs(leftAudio, audioFormat);

        headBangs.forEach(headBang -> {
            System.out.println(headBang.get());
        });

        return Optional.empty();
    }
}
