import javax.sound.midi.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        // Use Swing to open a file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Choose a melody file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getAbsolutePath();
            List<int[]> notesFromFile = readNotesFromFile(fileName);
            playMelody(notesFromFile);
        }
    }

    private static List<int[]> readNotesFromFile(String filename) throws IOException {
        List<int[]> notes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int[] noteDetails = new int[]{
                            Integer.parseInt(parts[0].trim()),
                            Integer.parseInt(parts[1].trim()),
                            Integer.parseInt(parts[2].trim())
                    };
                    notes.add(noteDetails);
                }
            }
        }
        return notes;
    }

    private static void playMelody(List<int[]> notesList) throws MidiUnavailableException, InterruptedException, InvalidMidiDataException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        Receiver receiver = synthesizer.getReceiver();
        ShortMessage msg = new ShortMessage();

        for (int[] noteDetails : notesList) {
            int note = noteDetails[0];
            int duration = noteDetails[1];  // Duration of the note in terms of 500ms increments
            int repeat = noteDetails[2];    // How many times the note should be repeated

            for (int i = 0; i < repeat; i++) {
                msg.setMessage(ShortMessage.NOTE_ON, note, 100);
                receiver.send(msg, -1);
                Thread.sleep(300L * duration);
                msg.setMessage(ShortMessage.NOTE_OFF, note, 100);
                receiver.send(msg, -1);
            }
        }

        synthesizer.close();
    }
}
