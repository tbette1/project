package view;

import model.AbsolutePitch;
import model.MusicComposition;
import model.Note;
import util.AbsolutePitchComparator;
import util.NoteComparator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class ViewModelImpl implements ViewModel {
    private MusicComposition mc;
    private Point lastSelectedPoint;
    private boolean isRepeating;
    private int nextEnding;
    private ArrayList<Point> endings;
    private ArrayList<Point> repeats;

    public ViewModelImpl(MusicComposition mc) {
        this.mc = mc;
        this.isRepeating = false;
        this.nextEnding = -1;
        this.endings = new ArrayList<Point>();
        this.repeats = new ArrayList<Point>();
    }

    @Override
    public int getLength() {
        return this.mc.getLength();
    }

    @Override
    public ArrayList<Note> getNotesAt(int beat) {
        ArrayList<Note> playing = new ArrayList<Note>();
        for (Note n : mc.getAllNotes()) {
            if (n.playsAt(beat)) {
                playing.add(n);
            }
        }
        Collections.sort(playing, new NoteComparator());
        return playing;
    }

    @Override
    public ArrayList<AbsolutePitch> getFullRange() {
        ArrayList<AbsolutePitch> pitches = this.mc.getAllPitches();
        Collections.sort(pitches, new AbsolutePitchComparator());
        Collections.reverse(pitches);
        return pitches;
    }

    @Override
    public int getTempo() {
        return mc.getTempo();
    }

    @Override
    public void setSelectedPoint(Point p) {
        this.lastSelectedPoint = p;
    }

    @Override
    public Point getSelectedPoint() {
        return this.lastSelectedPoint;
    }

    @Override
    public Note getNoteAt(Point p) {
        AbsolutePitch target = this.getFullRange().get(p.y);
        for (Note n : this.mc.getAllNotes()) {
            AbsolutePitchComparator ac = new AbsolutePitchComparator();
            if ((ac.compare(target, n.getAbsolutePitch()) == 0) && n.playsAt(p.x)) {
                return n;
            }
        }
        throw new IllegalArgumentException("No such note at given point!");
    }

    @Override
    public ArrayList<Note> getNotesAtPitch(AbsolutePitch pitch) {
        ArrayList<Note> notes = new ArrayList<Note>();
        for (Note n : this.mc.getAllNotes()) {
            if (new AbsolutePitchComparator().compare(n.getAbsolutePitch(), pitch) == 0) {
                notes.add(n);
            }
        }
        Collections.sort(notes, new NoteComparator());
        return notes;
    }

    @Override
    public ArrayList<String> getNoteNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (AbsolutePitch p : this.getFullRange()) {
            names.add(p.getName());
        }
        return names;
    }

    @Override
    public void setIsRepeating(boolean rep) {
        this.isRepeating = rep;
    }

    @Override
    public boolean isRepeating() {
        return this.isRepeating;
    }

    @Override
    public void addRepeat(int start, int stop) {
        Point p = new Point(new Point(start, stop));
        if (this.isEntirelyInsideRepeat(start, stop)) {
            throw new IllegalArgumentException("Cannot add repeat inside of another repeat!");
        }
        if (!this.repeats.contains(p) && (p.getX() != p.getY())) {
            this.repeats.add(p);
        }

    }


    @Override
    public void addNote(Note n) {
        this.mc.addNote(n);
    }

    @Override
    public void removeNote(Note n) {
        this.mc.deleteNote(n);
    }

    @Override
    public void moveNote(Note n, int startDifference, int halfSteps) {
        if (startDifference != 0) {
            this.mc.moveNoteX(n, startDifference);
        }
        if (halfSteps != 0) {
            this.mc.moveNoteY(n, halfSteps);
        }
    }

    @Override
    public void changeNoteEnd(Note n, int difference) {
        this.mc.changeNoteDuration(n, difference);
    }

    @Override
    public Point getNextRepeat(int beat) {
        Point p = null;
        for (Point point : this.repeats) {
            if (point.getX() >= beat) {
                if (p == null) {
                    p = point;
                }
                else {
                    if (point.getX() < p.getX()) {
                        p = point;
                    }
                }
            }
        }
        return p;
    }

    @Override
    public int getStartRepeat(int endBeat) {
        int start = -1;
        for (Point p : this.repeats) {
            if (p.getY() == endBeat) {
                start = (int) p.getX();
            }
        }
        return start;
    }

    @Override
    public boolean isStartOfRepeat(int beat) {
        for (Point p : this.repeats) {
            if (p.getX() == beat) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEndOfRepeat(int beat) {
        for (Point p : this.repeats) {
            if (p.getY() == beat) {
                return true;
            }
        }
        return false;
    }


    private boolean isInsideRepeat(int beat) {

        for (Point p : repeats) {
            if (beat >= p.getX() && beat <= p.getY()) {
                return true;
            }
        }
        return false;
    }

    private boolean isEntirelyInsideRepeat(int start, int stop) {
        if (this.repeats.isEmpty()) {

            return false;
        }
        for (int i = start; i <= stop; i++) {
            if (!isInsideRepeat(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addEnding(int start) {
        System.out.println(endings.size());
        if (this.endings.isEmpty()) {
            this.endings.add(new Point(start, this.getLength() + 1));
            this.nextEnding = 0;
        }
        else {
            if (!endingContained(start)) {
                if (endingWithin(start) == null) {
                    Point newPoint = new Point(start, (int) this.endings.get(0).getX());
                    this.endings.add(0, newPoint);
                }
                else {
                    Point oldPoint = endingWithin(start);
                    int oldIndex = this.endings.indexOf(oldPoint);
                    this.endings.remove(oldPoint);
                    Point new1 = new Point((int) oldPoint.getX(), start);
                    Point new2 = new Point(start, (int) oldPoint.getY());
                    this.endings.add(oldIndex, new2);
                    this.endings.add(oldIndex, new1);
                }
            }
        }
    }

    @Override
    public boolean isEndOfRepeatingSection(int beat) {
        if (this.endings.isEmpty()) {
            return false;
        }
        return ((int) this.endings.get(0).getX() - 1 == beat);
    }


    private boolean endingContained(int start) {
        for (Point p : this.endings) {
            if (p.getX() == start || p.getY() == start) {
                return true;
            }
        }
        return false;
    }

    private Point endingWithin(int start) {
        for (Point p : this.endings) {
            if (p.getX() < start && p.getY() > start) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean isStartOfEnding(int beat) {
        for (Point p : this.endings) {
            if (p.getX() == beat) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEndOfEnding(int beat) {
        for (Point p : this.endings) {
            if (p.getY() == beat + 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getEndingNumber(int beat) {
        for (int i = 0; i < repeats.size(); i++) {
            Point p = repeats.get(i);
            if (beat >= p.getX() && beat <= p.getY()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getNextEndingBeat() throws IllegalArgumentException {
        if (this.nextEnding >= this.endings.size()) {
            throw new IllegalArgumentException("KEEP GOING");
        }
        return (int) this.endings.get(this.nextEnding).getX();
    }

    @Override
    public void setEndingToNext() {
        this.nextEnding ++;
    }

}
