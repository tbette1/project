package controller;

import model.MusicComposition;
import util.MusicReader;
import util.TrackBuilder;
import view.CompositeView;
import view.ViewModel;
import view.ViewModelImpl;

import javax.sound.midi.InvalidMidiDataException;
import java.io.FileReader;
import java.io.IOException;


public final class MusicEditor {


    /**
     * Main Method for displaying a desired piece through the desired view/
     * Text: outputs the music file in text format
     * Graphic: Uses gui to output a representation of the view
     * Console: outputs the music file in audio format
     *
     * @param args: the name of the song and a view
     * @throws InvalidMidiDataException:
     * @throws IOException: invalid inputs
     *
     *
     */

    public static void main(String[] args) throws IOException {
        //Ensures that the inputs are valid
        if(args.length != 1){
            throw new IOException("Only type in the piece name.");
        }
        String argsA =  args[0];


        Readable toRead = new FileReader(argsA);

        MusicComposition model = MusicReader.parseFile(toRead, new TrackBuilder());


        ViewModel vm = new ViewModelImpl(model);
        try {
            CompositeView cv = new CompositeView(vm);
            GuiController gc = new GuiController(vm, cv);
            gc.run();
        }
        catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
