package util;


import model.AbsolutePitch;
import model.MusicCompositionImpl;
import model.Note;

import java.util.ArrayList;
/**
 * Builder object for Tracks.
 */
public final class TrackBuilder implements CompositionBuilder<MusicCompositionImpl> {
    int tempo;
    ArrayList<Note> buildNotes;


    public TrackBuilder(){
        buildNotes = new ArrayList<Note>();
    }

    @Override
    public CompositionBuilder<MusicCompositionImpl> setTempo(int temp) {
        this.tempo = temp;
        return this;
    }

    @Override
    public CompositionBuilder<MusicCompositionImpl> addNote(int start, int end,
                                                   int instrument, int pitch, int volume) {
        AbsolutePitch ap = AbsolutePitch.getAbsPitchFromVal(pitch);
        Note n = new Note(ap, start, end - start, instrument, volume);
        buildNotes.add(n);
        return this;
    }

    @Override
    public MusicCompositionImpl build() {
        MusicCompositionImpl mci = new MusicCompositionImpl(this.tempo);
        for (Note n : buildNotes) {
            mci.addNote(n);
        }
        return mci;
    }
}