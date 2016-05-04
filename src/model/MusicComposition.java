package model;

import java.util.ArrayList;
public interface MusicComposition {

    /**
     * Gets all Notes that are played in this composition.
     * @return an ArrayList containing all notes in this composition.
     */
    ArrayList<Note> getAllNotes();


    /**
     * Gets all AbsolutePitches in this composition.
     * @return an ArrayList containing all absolute pitches in this composition.
     */
    ArrayList<AbsolutePitch> getAllPitches();

    /**
     * Gets the lowest pitch in this composition.
     * @return the lowest pitch.
     */
    AbsolutePitch getLowestPitch();

    /**
     * Gets the highest pitch in this composition.
     * @return the highest pitch.
     */
    AbsolutePitch getHighestPitch();

    /**
     * Adds the given note to this composition.
     * @param n note to be added.
     */
    void addNote(Note n);

    /**
     * Deletes the given note from this composition.
     * @param n note to be deleted.
     */
    void deleteNote(Note n);

    /**
     * Moves the given note to the new desired pitch.
     * @param n note to be moved.
     * @param halfSteps difference in half steps.
     */
    void moveNoteY(Note n, int halfSteps);

    /**
     * Moves the given note to the new desired start time.
     * @param n note to be moved.
     * @param startDifference difference in start time.
     */
    void moveNoteX(Note n, int startDifference);

    /**
     * Changes the given note's duration.
     * @param n note to be changed.
     * @param newDuration new duration of the note.
     */
    void changeNoteDuration(Note n, int newDuration);

    /**
     * Gets the tempo of this composition.
     * @return the tempo of this composition.
     */
    int getTempo();

    /**
     * Sets the tempo of this composition.
     * @param tempo the desired tempo.
     */
    void setTempo(int tempo);

    /**
     * Gets the length of this composition.
     * @return the length of this composition.
     */
    int getLength();

    /**
     * Adds a full song to the beginning of this composition.
     * @param song the song to add to this composition.
     */
    void addSong(MusicComposition song);

}
