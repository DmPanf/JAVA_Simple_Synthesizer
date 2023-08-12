import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.sound.midi.*;

public class App {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter filename of the melody you'd like to play (e.g., melody.txt):");
        String filename = scanner.nextLine();

        List<int[]> notes = readNotesFromFile(filename);

        playMelody(notes);
    }

    private static List<int[]> readNotesFromFile(String filename) throws Exception {
        List<int[]> notes = new ArrayList<>();

        for (String line : Files.readAllLines(Paths.get(filename))) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                int[] noteDetails = new int[] {
                    Integer.parseInt(parts[0].trim()),  // MIDI value
                    Integer.parseInt(parts[1].trim()),  // Duration
                    Integer.parseInt(parts[2].trim())   // Repetitions
                };
                notes.add(noteDetails);
            }
        }

        return notes;
    }

    private static void playMelody(List<int[]> notes) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        Receiver receiver = synthesizer.getReceiver();

        ShortMessage msg = new ShortMessage();

        for (int[] noteDetails : notes) {
            int midiValue = noteDetails[0];
            int duration = noteDetails[1];
            int repetitions = noteDetails[2];

            for (int i = 0; i < repetitions; i++) {
                msg.setMessage(ShortMessage.NOTE_ON, midiValue, 100);
                receiver.send(msg, -1);

                Thread.sleep(500L * duration);

                msg.setMessage(ShortMessage.NOTE_OFF, midiValue, 100);
                receiver.send(msg, -1);
            }
        }

        synthesizer.close();
    }
}
