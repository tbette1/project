package model;

/**
 * Created by torybettencourt on 4/22/16.
 */
import java.util.ArrayList;
public class MusicCompositionImpl implements MusicComposition {
    private ArrayList<Note> notes;
    private int tempo;

    public MusicCompositionImpl(int tempo) {
        this.notes = new ArrayList<Note>();
        this.tempo = tempo;
    }

    @Override
    public ArrayList<Note> getAllNotes() {
        return this.notes;
    }

    @Override
    public AbsolutePitch getLowestPitch() {
        AbsolutePitch lowest = notes.get(0).getAbsolutePitch();
        for (Note n : notes) {
            if (n.getAbsolutePitch().getIntegerVal() < lowest.getIntegerVal()) {
                lowest = n.getAbsolutePitch();
            }
        }
        return lowest;
    }

    @Override
    public AbsolutePitch getHighestPitch() {
        AbsolutePitch highest = notes.get(0).getAbsolutePitch();
        for (Note n : notes) {
            if (n.getAbsolutePitch().getIntegerVal() > highest.getIntegerVal()) {
                highest = n.getAbsolutePitch();
            }
        }
        return highest;
    }

    @Override
    public ArrayList<AbsolutePitch> getAllPitches() {
        ArrayList<AbsolutePitch> pitches = new ArrayList<AbsolutePitch>();
        for (int i = getLowestPitch().getIntegerVal(); i <= getHighestPitch().getIntegerVal(); i++) {
            pitches.add(AbsolutePitch.getAbsPitchFromVal(i));
        }
        return pitches;
    }

    @Override
    public void addNote(Note n) {
        if (!notes.contains(n)) {
            notes.add(n);
        }
    }

    @Override
    public void deleteNote(Note n) {
        if (!notes.contains(n)) {
            throw new IllegalArgumentException("Note is not contained in this composition.");
        }
        notes.remove(n);
    }

    @Override
    public void moveNoteX(Note n, int startDifference) {
        if (!notes.contains(n)) {
            throw new IllegalArgumentException("Note is not contained in this composition.");
        }
        notes.set(notes.indexOf(n), n.changeStartTime(startDifference));
    }

    @Override
    public void moveNoteY(Note n, int halfSteps) {
        if (!notes.contains(n)) {
            throw new IllegalArgumentException("Note is not contained in this composition.");
        }
        notes.set(notes.indexOf(n), n.changeAbsPitch(halfSteps));
    }

    @Override
    public void changeNoteDuration(Note n, int difference) {
        if (!notes.contains(n)) {
            throw new IllegalArgumentException("Note is not contained in this composition.");
        }
        notes.set(notes.indexOf(n), n.changeDuration(difference));
    }

    @Override
    public int getTempo() {
        return this.tempo;
    }

    @Override
    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    @Override
    public int getLength() {
        int last = this.notes.get(0).getEndOfNote();
        for (Note n : this.notes) {
            if (n.getEndOfNote() > last) {
                last = n.getEndOfNote();
            }
        }
        return last;
    }

    @Override
    public void addSong(MusicComposition song) {
        this.notes.addAll(song.getAllNotes());
    }

}
