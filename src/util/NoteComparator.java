package util;

import model.Note;

import java.util.Comparator;
/**
 * Created by torybettencourt on 4/23/16.
 */
public class NoteComparator implements Comparator<Note> {

    /**
     * Compares two notes.
     * @param n1 a note.
     * @param n2 another note.
     * @return see compareTo(Note n) in Note class.
     */
    public int compare(Note n1, Note n2) {
        return n1.compareTo(n2);
    }
}
