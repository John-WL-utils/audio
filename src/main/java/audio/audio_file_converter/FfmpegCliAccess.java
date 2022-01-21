package audio.audio_file_converter;

import os.win.powershell.Powershell;

public class FfmpegCliAccess {

    public static boolean notInstalled() {
        final String[] ffmpegCommandArgs = {"ffmpeg", "-version"};
        return Powershell.call(ffmpegCommandArgs) != 0;
    }

    public static void call(String... args) {
        final String[] ffmpegCommandArgs = {"ffmpeg", "-i", String.join(" ", args)};
        Powershell.call(ffmpegCommandArgs);
    }

    public static String atempoParameterGenerator(double atempoMultiplier) {
        double amountOfAtempoNeeded = Math.log(atempoMultiplier) / Math.log(2);
        final double lastAtempoAmount = Math.pow(2, amountOfAtempoNeeded % 1);
        final StringBuilder atempoCombined = new StringBuilder();
        final String atempoParamName = "atempo=";

        while(amountOfAtempoNeeded > 1) {
            amountOfAtempoNeeded--;
            atempoCombined.append(atempoParamName)
                    .append("2.0,");
        }
        while(amountOfAtempoNeeded < -1) {
            amountOfAtempoNeeded++;
            atempoCombined.append(atempoParamName)
                    .append("0.5,");
        }
        atempoCombined.append(atempoParamName)
                .append(lastAtempoAmount);

        return atempoCombined.toString();
    }
}
