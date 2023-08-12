import javax.sound.midi.*;

public class SimpleApp {

    final static int[][] NOTES = {
        // E D# E D# E B D C A
        {64, 1, 1}, // E
        {63, 1, 1}, // D#
        {64, 1, 1}, // E
        {63, 1, 1}, // D#
        {64, 1, 1}, // E
        {71, 1, 1}, // B
        {69, 1, 1}, // A (replacing D for simplification)
        {60, 1, 1}, // C
        {57, 1, 1}, // A

        // C E A B E G# B C E
        {60, 1, 1}, // C
        {64, 1, 1}, // E
        {57, 1, 1}, // A
        {59, 1, 1}, // B
        {64, 1, 1}, // E
        {56, 1, 1}, // G#
        {59, 1, 1}, // B
        {60, 1, 1}, // C
        {64, 1, 1}, // E

        // A E D# E
        {57, 1, 1}, // A
        {64, 1, 1}, // E
        {63, 1, 1}, // D#
        {64, 1, 1}, // E
    };


    public static void main(String[] args)
            throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        Receiver receiver = synthesizer.getReceiver();

        ShortMessage msg = new ShortMessage();

        // Loop through each note in the NOTES array
        for (int[] noteDetails : NOTES) {
            int midiValue = noteDetails[0]; // MIDI value of the note
            int duration = noteDetails[1];  // Duration of the note in terms of 500ms increments
            int repetitions = noteDetails[2]; // Number of times to repeat the note

            // Play the note the specified number of times
            for (int i = 0; i < repetitions; i++) {
                msg.setMessage(ShortMessage.NOTE_ON, midiValue, 100); // Start the note
                receiver.send(msg, -1);
                
                Thread.sleep(400L * duration);  // Wait for the duration of the note

                msg.setMessage(ShortMessage.NOTE_OFF, midiValue, 100); // Stop the note
                receiver.send(msg, -1);
            }
        }
        synthesizer.close(); // Close the synthesizer after playing
    }
}
