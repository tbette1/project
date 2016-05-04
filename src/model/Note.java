package model;

/**
 * Created by torybettencourt on 4/22/16.
 */
public class Note {
    private AbsolutePitch absPitch;
    private int startTime;
    private int duration;
    private int instrument;
    private int volume;

    public Note(AbsolutePitch absPitch, int startTime, int duration, int instrument, int volume) {
        this.absPitch = absPitch;
        this.startTime = startTime;
        this.duration = duration;
        this.instrument = instrument;
        this.volume = volume;
    }

    /**
     * @return this note's absolute pitch.
     */
    public AbsolutePitch getAbsolutePitch() {
        return absPitch;
    }

    /**
     * @return this note's starting beat.
     */
    public int getStartTime() {
        return this.startTime;
    }

    /**
     * @return this note's duration.
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * @return this note's instrument.
     */
    public int getInstrument() {
        return this.instrument;
    }

    /**
     * @return this note's volume.
     */
    public int getVolume() {
        return this.volume;
    }

    /**
     * Changes this note's start time.
     * @param difference the difference in the start times.
     * @return a new Note with the desired start time.
     */
    public Note changeStartTime(int difference) {
        if (this.startTime + difference < 0) {
            throw new IllegalArgumentException("Cannot start this note before time 0.");
        }
        return new Note(this.absPitch, this.startTime + difference, this.duration, this.instrument, this.volume);
    }

    /**
     * Changes this note's duration.
     * @param difference the difference in the durations.
     * @return a new Note with the desired duration.
     */
    public Note changeDuration(int difference) {
        if (this.duration + difference <= 0) {
            throw new IllegalArgumentException("Cannot have a note of duration less than or equal to 0.");
        }
        return new Note(this.absPitch, this.startTime, this.duration + difference, this.instrument, this.volume);
    }

    /**
     * Changes this note's absolute pitch.
     * @param halfSteps the difference in pitches.
     * @return a new Note with the desired absolute pitch.
     */
    public Note changeAbsPitch(int halfSteps) {
        if (this.absPitch.getIntegerVal() + halfSteps < 0) {
            throw new IllegalArgumentException("Cannot have a note that low.");
        }
        return new Note(AbsolutePitch.getAbsPitchFromVal(this.absPitch.getIntegerVal() + halfSteps),
                            this.startTime, this.duration, this.instrument, this.volume);
    }

    /**
     * Gets the final beat on which this note plays.
     * @return the final beat of this note.
     */
    public int getEndOfNote() {
        return this.startTime + this.duration - 1;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Note)) {
            return false;
        }
        return (((Note) o).getAbsolutePitch().getIntegerVal() == this.getAbsolutePitch().getIntegerVal()) &&
                    (((Note) o).getStartTime() == this.getStartTime()) &&
                        (((Note) o).getDuration() == this.getDuration());
    }

    /**
     * Compares this note to the given note.
     * @param n
     * @return a value < 0 if this note is "less than" the given note.
     *                 = 0 if this note is "equivalent" to the given note.
     *                 > 0 if this note is "greater than" the given note.
     */
    public int compareTo(Note n) {
        if (n.getAbsolutePitch().getIntegerVal() == this.absPitch.getIntegerVal()) {
            if (n.getStartTime() == this.startTime) {
                if (n.getDuration() == this.duration) {
                    return 0;
                }
                return this.duration - n.getDuration();
            }
            return this.startTime - n.getStartTime();
        }
        return this.absPitch.getIntegerVal() - n.getAbsolutePitch().getIntegerVal();
    }

    /**
     * Determines whether or not this note is playing at the given beat.
     * @param beat beat to check if note is playing.
     * @return true if this note is playing at the given beat,
     *         returns false otherwise.
     */
    public boolean playsAt(int beat) {
        return (beat <= this.getEndOfNote() && beat >= this.getStartTime());
    }
}
