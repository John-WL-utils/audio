package audio_file_converter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class AudioFileConverter {

    public static File convert(
            final File initialAudioFile,
            final File destinationFolder,
            final AudioFileType outputFileType) {
        final String outputFilePath = destinationFolder.getAbsolutePath() + "\\" +
                initialAudioFile.getName().split("\\.")[0] +
                outputFileType.fileExtension();
        final File outputFile = new File(outputFilePath);

        FfmpegCliAccess.call(
                "\\\"" + initialAudioFile.getAbsolutePath() + "\\\"",
                "\\\"" + outputFile.getAbsolutePath() + "\\\"");

        return outputFile;
    }

    public static Optional<AudioInputStream> extractAudioInputStreamFromAudioFile(final File audioFile) {
        try {
            new File("cache\\audio.wav").delete();
            new File("cache").mkdir();
            final File wavFile = AudioFileConverter.convert(audioFile, new File("cache"), AudioFileType.WAVE);
            return Optional.of(AudioSystem.getAudioInputStream(wavFile));
        }
        catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
