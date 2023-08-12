import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.sound.midi.*;

public class SimpleSynthesizer {

    // Mapping notes to their MIDI values
    private static final byte C = 60; // Do
    private static final byte D = 62; // Re
    private static final byte E = 64; // Mi
    private static final byte F = 65; // Fa
    private static final byte G = 67; // Sol
    private static final byte A = 69; // La
    private static final byte B = 70; // Si

    public static void main(String[] args)
            throws MidiUnavailableException, InvalidMidiDataException, InterruptedException, FileNotFoundException {
        // Path to the file containing notes
        File file = new File("melody.txt");
        Scanner fileScanner = new Scanner(file);
        StringBuilder sb = new StringBuilder();

        // Read the file contents
        while (fileScanner.hasNextLine()) {
            sb.append(fileScanner.nextLine());
            sb.append(" ");
        }

        // Split the notes into an array
        String[] notesFromFile = sb.toString().trim().split(" ");

        // Initialize the MIDI synthesizer
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        Receiver receiver = synthesizer.getReceiver();

        // Play the notes
        playNotes(receiver, notesFromFile);
    }

    // Method to play a sequence of notes
    private static void playNotes(Receiver receiver, String[] notes)
            throws InvalidMidiDataException, InterruptedException {
        for (String note : notes) {
            ShortMessage msg = new ShortMessage();
            msg.setMessage(ShortMessage.NOTE_ON, convertToId(note), 100);
            receiver.send(msg, -1);

            Thread.sleep(500); // Duration of each note

            msg.setMessage(ShortMessage.NOTE_OFF, convertToId(note), 100);
            receiver.send(msg, -1);
        }
    }

    // Convert note letters to MIDI note values
    private static int convertToId(String note) {
        switch (note) {
            case "A":
                return A;
            case "B":
                return B;
            case "C":
                return C;
            case "D":
                return D;
            case "E":
                return E;
            case "F":
                return F;
            case "G":
                return G;
            default:
                System.out.println("Entered incorrect note: " + note);
                return A;  // Default note in case of an error
        }
    }
}
