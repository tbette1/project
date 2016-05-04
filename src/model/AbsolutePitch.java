package model;

/**
 * Created by torybettencourt on 4/22/16.
 */
public class AbsolutePitch {
    RelativePitch pitch;
    int octave;

    public AbsolutePitch(RelativePitch pitch, int octave) {
        this.pitch = pitch;
        this.octave = octave;
    }

    /**
     * Gets an AbsolutePitch from the given value.
     * @param value the value of this absolute pitch.
     * @return a new AbsolutePitch according to the given value.
     */
    public static AbsolutePitch getAbsPitchFromVal(int value) {
        return new AbsolutePitch(RelativePitch.pitchFromVal(value % 12), value / 12);
    }

    /**
     * Gets an integer value for this AbsolutePitch.
     * @return integer value of this absolute pitch.
     */
    public int getIntegerVal() {
        return this.octave * 12 + this.pitch.ordinal();
    }

    /**
     * @return the name of this pitch.
     */
    public String getName() {
        return this.pitch.toString() + this.octave + "";
    }

    /**
     * Compares this absolute pitch to another absolute pitch.
     * @param p another absolute pitch.
     * @return a value < 0 if this absolute pitch is "less than" the given pitch.
     *                 = 0 if this absolute pitch is "equivalent to" the given pitch.
     *                 > 0 if this absolute pitch is "greater than" the given pitch.
     */
    public int compareTo(AbsolutePitch p) {
        return this.getIntegerVal() - p.getIntegerVal();
    }
}
