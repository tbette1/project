package view;

import model.MusicComposition;
import util.MusicReader;
import util.TrackBuilder;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.FileReader;
import java.io.IOException;
/**
 * Created by torybettencourt on 4/3/16.
 */
public class TestYo {

    public static void main (String[] args) throws MidiUnavailableException, IOException {
        Readable read = new FileReader("mary-little-lamb.txt");
        MusicComposition mc = MusicReader.parseFile(read, new TrackBuilder());
        ViewModel vm = new ViewModelImpl(mc);


        try {
            CompositeView cv = new CompositeView(vm);


        }
        catch (InvalidMidiDataException e) {
            System.out.println("ur fucked bro");
        }





    }
}
