package util;

import model.AbsolutePitch;
import java.util.Comparator;
/**
 * Created by torybettencourt on 4/23/16.
 */
public class AbsolutePitchComparator implements Comparator<AbsolutePitch> {

    /**
     * Compares two absolute pitches.
     * @param a1 an absolute pitch.
     * @param a2 another absolute pitch.
     * @return see compareTo(AbsolutePitch pitch) in absolute pitch class.
     */
    public int compare(AbsolutePitch a1, AbsolutePitch a2) {
        return a1.compareTo(a2);
    }
}
