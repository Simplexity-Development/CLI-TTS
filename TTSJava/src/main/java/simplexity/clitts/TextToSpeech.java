package simplexity.clitts;

import simplexity.clitts.twitch.TwitchAuthServer;
import simplexity.clitts.twitch.TwitchMessaging;
import simplexity.clitts.util.Messages;

import java.io.IOException;
import java.util.Scanner;

public class TextToSpeech {


    private static final Scanner scanner = new Scanner(System.in);
    private static boolean runProgram = true;
    private static TextToSpeech instance;

    public static TextToSpeech getInstance() {
        if (instance == null) {
            instance = new TextToSpeech();
        }
        return instance;
    }

    public static void main(String[] args) {
        TTSConfig.getInstance().reloadConfig();
        System.out.println(Messages.startupMessage);
        SpeechHandling.voiceId = TTSConfig.getInstance().getDefaultVoice();
        SpeechHandling.awsRegion = TTSConfig.getInstance().getRegion();
        checkTwitch();
        while (runProgram) {
            System.out.println(Messages.enterText);
            String text = scanner.nextLine();
            switch (text) {
                case ("--exit") -> {
                    runProgram = false;
                    System.out.println(Messages.exitMessage);
                }
                case ("--help") -> System.out.println(Messages.helpMessage);
                case ("--reload") -> {
                    TTSConfig.getInstance().reloadConfig();
                    checkTwitch();
                    System.out.println(Messages.reloadMessage);
                }
                default -> {
                    if (TTSConfig.getInstance().getAccessID().isBlank() || TTSConfig.getInstance().getAccessID().isEmpty()) {
                        System.out.println(Messages.secretOrIDNotSet);
                        continue;
                    }
                    if (TTSConfig.getInstance().getAccessSecret().isBlank() || TTSConfig.getInstance().getAccessSecret().isEmpty()) {
                        System.out.println(Messages.secretOrIDNotSet);
                        continue;
                    }
                    SpeechHandling.getInstance().processSpeech(text);
                }
            }
        }
    }

    private static void checkTwitch() {
        if (TTSConfig.getInstance().connectToTwitch()) {
            String channel = TTSConfig.getInstance().getTwitchChannel();
            System.out.println(Messages.currentlyConnectedTo + channel + Messages.stayConnectedPrompt);
            boolean validInput = false;
            while (!validInput) {
                String input = scanner.nextLine();
                input = input.toLowerCase();
                switch (input) {
                    case ("y") -> {
                        validInput = true;
                        checkTwitchAuth();
                    }
                    case ("n") -> {
                        validInput = true;
                    }
                    default -> System.out.println(Messages.stayConnectedError);
                }
            }
            if (TTSConfig.getInstance().getTwitchAuthCode() == null) return;
            TwitchMessaging.getInstance().setupTwitch(TTSConfig.getInstance().getTwitchAuthCode());
        }
    }

    private static void checkTwitchAuth() {
        if (TTSConfig.getInstance().connectToTwitch()) {
            String authCode = TTSConfig.getInstance().getTwitchAuthCode();
            if (authCode == null || authCode.isBlank()) {
                System.out.println(Messages.needTwitchAuth);
                System.out.println(Messages.visitLink + Messages.twitchAuthLink);
                try {
                    TwitchAuthServer.run();
                } catch (IOException e) {
                    System.out.println(Messages.twitchAuthError);
                    e.printStackTrace();
                }
            }
        }
    }

}