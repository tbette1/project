package view;

import model.Note;

import javax.sound.midi.*;
/**
 * view responsible for the playback of audio based on the musical composition.
 */
public final class MidiView implements View {
    //synthesizer that deals with the interpretation of the MIDI data
    private Synthesizer synth;

    //receiver recieves a and interperates it for raw sound
    private Receiver receiver;
    private ViewModel vm;

    //tempo of the track
    private int tempo;

    // choose which constructor
    private boolean test;
    public MidiView(ViewModel vm) throws MidiUnavailableException {
        this.vm = vm;
        this.tempo = vm.getTempo();
        test = false;
        try {
            this.synth = MidiSystem.getSynthesizer();
            this.receiver = synth.getReceiver();
            this.synth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public MidiView(ViewModel vm, boolean test) {
        test = true;
        try {
            //make mock midi device
            this.synth = MidiSystem.getSynthesizer();
            this.receiver = new MockReciever();
            this.synth.open();
            this.vm = vm;
            this.tempo = vm.getTempo();

            ((MockReciever) this.receiver).setTempo(this.tempo);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public String returnLog(){
        return ((MockReciever) this.receiver).getLog();
    }


    public void display(){

    }

    /**
     * Plays all notes at the given beat.
     * @param beat beat at which to play all notes.
     * @throws InvalidMidiDataException
     */
    public void playNotesAtBeat(int beat) throws InvalidMidiDataException{
        for (Note n : this.vm.getNotesAt(beat)){
            if (n.getStartTime() == beat) {
                int pitch = n.getAbsolutePitch().getIntegerVal();
                playNote(n.getInstrument(), n.getDuration(), pitch, n.getVolume());
                playNote(n.getInstrument(), n.getDuration(), pitch, n.getVolume());

            }
        }
    }



    /**
     * Plays a single note.
     * @param instrument instrument of this note.
     * @param length length of this note.
     * @param pitch pitch of this note.
     * @param volume volume of this note.
     * @throws InvalidMidiDataException
     */
    public void playNote(int instrument, int length, int pitch, int volume)
            throws InvalidMidiDataException {
        MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON, instrument - 1, pitch, volume);
        MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF, instrument - 1, pitch, volume);
        this.receiver.send(start, 0);
        this.receiver.send(stop,
                this.synth.getMicrosecondPosition() + (tempo * length));


    }

    /**
     * Closes this reciever.
     */
    public void close(){
        this.receiver.close();
    }

    /**
     * @return tempo of this song.
     */
    int getTempo() {
        return this.tempo;
    }

}



