package model;

/**
 * Created by torybettencourt on 4/22/16.
 */
public enum RelativePitch {
    C, CSharp, D, DSharp, E, F, FSharp, G, GSharp, A, ASharp, B;


    /**
     * @return the pitch corresponding to the given value
     * @param p [0,11] representing a pitch.
     */
    public static RelativePitch pitchFromVal(int p) {
        switch(p) {
            case 0:
                return RelativePitch.C;
            case 1:
                return RelativePitch.CSharp;
            case 2:
                return RelativePitch.D;
            case 3:
                return RelativePitch.DSharp;
            case 4:
                return RelativePitch.E;
            case 5:
                return RelativePitch.F;
            case 6:
                return RelativePitch.FSharp;
            case 7:
                return RelativePitch.G;
            case 8:
                return RelativePitch.GSharp;
            case 9:
                return RelativePitch.A;
            case 10:
                return RelativePitch.ASharp;
            default:
                return RelativePitch.B;
        }
    }

    @Override
    public String toString() {
        switch(this.name()) {
            case "CSharp" :
                return "C#";
            case "DSharp" :
                return "D#";
            case "FSharp" :
                return "F#";
            case "GSharp" :
                return "G#";
            case "ASharp" :
                return "A#";
            default :
                return this.name();
        }
    }
}
