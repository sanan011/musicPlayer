import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String DEFAULT_PATH = "src\\east-duo-ringtone-mobcup-com-co-67202.wav";
    private static boolean looping = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path to audio file (press Enter for default): ");
        String inputPath = scanner.nextLine().trim();
        String filePath = inputPath.isEmpty() ? DEFAULT_PATH : inputPath;

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("❌ File not found: " + filePath);
            return;
        }

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(file)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            runMenu(scanner, clip);

        } catch (UnsupportedAudioFileException e) {
            System.out.println("❌ Unsupported audio format!");
        } catch (LineUnavailableException e) {
            System.out.println("❌ Audio line unavailable!");
        } catch (IOException e) {
            System.out.println("❌ IO error while playing the file!");
        } finally {
            System.out.println("👋 Bye!");
        }
    }

    private static void runMenu(Scanner scanner, Clip clip) {
        String response;

        do {
            System.out.println("\n🎵 Audio Player Menu:");
            System.out.println("P = Play");
            System.out.println("S = Stop");
            System.out.println("R = Reset");
            System.out.println("L = Toggle Loop (" + (looping ? "On" : "Off") + ")");
            System.out.println("T = Show current time");
            System.out.println("Q = Quit");
            System.out.print("Enter your choice: ");

            response = scanner.next().toUpperCase();

            switch (response) {
                case "P" -> {
                    clip.start();
                    if (looping) clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                case "S" -> clip.stop();
                case "R" -> clip.setMicrosecondPosition(0);
                case "L" -> {
                    looping = !looping;
                    System.out.println("🔁 Looping is now " + (looping ? "enabled" : "disabled"));
                }
                case "T" -> {
                    long positionSec = clip.getMicrosecondPosition() / 1_000_000;
                    System.out.println("⏱ Current position: " + positionSec + " sec");
                }
                case "Q" -> {
                    clip.stop();
                    clip.close();
                }
                default -> System.out.println("❓ Invalid choice!");
            }

        } while (!response.equals("Q"));
    }
}
