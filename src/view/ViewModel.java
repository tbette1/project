package view;

import model.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by torybettencourt on 4/22/16.
 */
public interface ViewModel {
    /**
     * Should delegate to an internal
     * @return the length of the viewModel
     */
    int getLength();

    /**
     * Should delegate to an internal
     * @return a list of all notes at the given beat, ordered by pitch and start time.
     */
    ArrayList<Note> getNotesAt(int i);

    /**
     * Should delegate to an internal
     * @return the tempo of the model
     */
    int getTempo();

    /**
     * Gets the last point in the piece selected by the user.
     * The X coordinate is the beat and
     * the Y coordinate is the pitch
     * @return the selected point
     */
    Point getSelectedPoint();

    /**
     * Sets the last point in the piece selected by the user.
     * (X coordinate represents the beat, Y coordinate represents pitch.
     * @param p last point selected.
     */
    void setSelectedPoint(Point p);

    /**
     * Gets a list of all pitches in this model.
     * @return an ArrayList of all pitches in the model.
     */
    ArrayList<AbsolutePitch> getFullRange();

    /**
     * Gets the note at the given point where X represents
     * the beat, and Y represents the pitch.
     * @param p point from which to retrieve note.
     * @return the Note at that point.
     * @throws IllegalArgumentException if no such note exists at that point.
     */
    Note getNoteAt(Point p);

    /**
     * Gets all note played at this pitch.
     * @param pitch absolute pitch from which to retrieve notes.
     * @return an ArrayList of notes that play at that pitch.
     */
    ArrayList<Note> getNotesAtPitch(AbsolutePitch pitch);

    /**
     * Gets all note names.
     * @return an ArrayList of all note names.
     */
    ArrayList<String> getNoteNames();

    /**
     * Sets the isRepeating field to the given boolean.
     * @param rep whether or not this song is repeating.
     */
    void setIsRepeating(boolean rep);

    /**
     * Gets the isRepeating field.
     * @return whether or not this song is currently repeating.
     */
    boolean isRepeating();

    /**
     * Adds a repeat to this song at the given beats.
     * @param start the beat on which this song will start repeating.
     * @param stop the beat on which this song will stop repeating.
     */
    void addRepeat(int start, int stop);

    /**
     * Gets the next repeated section as a point.
     * @param beat gets the repeated section that occurs after this beat.
     * @return the next repeated section after the given beat as a point.
     *         if there is no next repeated section, returns null.
     */
    Point getNextRepeat(int beat);

    /**
     * Adds a note to this song.
     * @param n the note to be added.
     */
    void addNote(Note n);

    /**
     * Removes the given note from this song.
     * @param n the note to be removed.
     */
    void removeNote(Note n);

    /**
     * Moves this note to the new desired pitch and startTime
     * @param n note to be moved.
     * @param startDifference difference in start times.
     * @param halfsteps difference in half steps.
     */
    void moveNote(Note n, int startDifference, int halfsteps);


    /**
     * Changes the length of this note.
     * @param n note to be changed.
     * @param difference in beats.
     */
    void changeNoteEnd(Note n, int difference);

    /**
     * Gets the start beat for this repeat. NOTE: if the given endBeat is not the end of
     * a repeat, returns -1.
     * @param endBeat the end of a repeat.
     * @return the start beat of that repeat.
     */
    int getStartRepeat(int endBeat);

    /**
     * Returns true if the given beat is the start of a repeat.
     * @param beat beat to be checked for start of repeat.
     * @return true if the given beat is the start of a repeat, returns false otherwise.
     */
    boolean isStartOfRepeat(int beat);

    /**
     * Returns true if the given beat is the end of a repeat.
     * @param beat beat to be checked for end of repeat.
     * @return true if the given beat is the end of a repeat, returns false otherwise.
     */
    boolean isEndOfRepeat(int beat);

    /**
     * Adds an ending to this view model.
     */
    void addEnding(int start);

    /**
     * Returns true if the given beat is the end of the large repeating section before
     * a series of endings, returns false otherwise.
     */
    boolean isEndOfRepeatingSection(int beat);

    /**
     * Returns true if the given beat is the start of an ending.
     * @param beat beat to be checked for start of ending.
     * @return true if the given beat is the start of an ending, returns false otherwise.
     */
    boolean isStartOfEnding(int beat);

    /**
     * Returns true if the given beat is the end of an ending.
     * @param beat beat to be checked for end of ending.
     * @return true if the given beat is the end of an ending, returns false otherwise.
     */
    boolean isEndOfEnding(int beat);

    /**
     * Gets the number ending that the given beat falls under.
     * @param beat beat to be checked for presence in ending.
     * @return ending number if the given beat is contained in an ending, returns -1 otherwise.
     */
    int getEndingNumber(int beat);

    /**
     * Gets the beat on which the next ending starts.
     * NOTE: if there is no next ending, returns -1.
     * @return the beat on which the next ending starts, or -1 if there is no next ending.
     */
    int getNextEndingBeat();

    /**
     * Sets the nextEnding field for this model to the next ending.
     */
    void setEndingToNext();



}
