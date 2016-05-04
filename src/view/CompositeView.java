package view;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
public final class CompositeView {
    private MidiView mView;
    private GraphicView gView;
    private ViewModel vm;
    private boolean justContinue;
    JScrollPane scrollPane;
    int barLoc;
    JPanel playBar;
    private JPanel lowerPanel;
    Rectangle viewRect;
    private KeyListener kh;
    private MouseListener mh;

    /**
     * Constructs a new CompositeView
     *
     */
    public CompositeView(ViewModel vm) throws InvalidMidiDataException {
        this.vm = vm;
        try {
            this.mView = new MidiView(vm);
        }
        catch (MidiUnavailableException e) {
            // do nothing.
        }
        this.gView = new GraphicView(vm);
        justContinue = false;
    }

    /**
     * @return the X value of the location of the play bar at start time.
     */
    int getBarXStartLoc() {
        return 32;
    }

    /**
     * Determines the number of notes and the height of all of the measure's in
     * the song shown by this composite view to determine the desired height of the
     * playbar.
     *
     * @return the height of the playBar.
     */
    int getBarHeight() {
        return this.gView.getViewWidth();
    }

    /**
     * Determines the desired y value of the playbar (that does not change).
     *
     * @return the Y value of the playbar.
     */
    int getBarYLoc() {
        BeatPanel b = new BeatPanel(-1);
        JLabel beatLabel = new JLabel(1 + "");
        String font = beatLabel.getFont().getFontName();
        beatLabel.setFont(new Font(font, Font.PLAIN, 10));
        b.add(beatLabel, BorderLayout.SOUTH);
        return b.getPreferredSize().height;
    }

    /**
     * @return a JPanel representing a playbar of the desired height.
     */
    JPanel getPlayBarImage() {
        JPanel p = new JPanel();
        p.setBackground(Color.RED);
        p.setPreferredSize(new Dimension(4, this.getBarHeight()));
        return p;
    }

    /**
     * To be called with every tick of a timer.
     * @return true if the song is still playing,
     *         false if the song is over.
     */
    public boolean tick() throws InvalidMidiDataException {

        Insets insets = gView.getDisplayPanel().getInsets();
        Dimension d = playBar.getPreferredSize();
        if (this.vm.isStartOfRepeat(getBeat())) {
            if (this.vm.isRepeating() == false) {
                this.vm.setIsRepeating(true);
            }
            else {
                this.vm.setIsRepeating(false);
            }
        }

        else if (this.vm.isEndOfEnding(getBeat())) {
            this.vm.setEndingToNext();
            try {
                this.vm.getNextEndingBeat();
                this.barLoc = this.barLocFromBeat(0);
            }
            catch (IllegalArgumentException e) {
                justContinue = true;
            }
        }

        else if (this.vm.isEndOfRepeatingSection(getBeat()) && !justContinue) {
            this.barLoc = this.barLocFromBeat(this.vm.getNextEndingBeat()) - 1;
        }

        if (this.vm.isEndOfRepeat(getBeat()) && this.vm.isRepeating() && !justContinue) {
            this.barLoc = this.barLocFromBeat(this.vm.getStartRepeat(getBeat()));
            justContinue = true;
        }
        else {
            barLoc += 1;
        }
        if (this.vm.isEndOfRepeat(getBeat()) && this.vm.isRepeating() && !justContinue) {
            this.barLoc = this.barLocFromBeat(this.vm.getStartRepeat(getBeat()));
            justContinue = true;
        }
        playBar.setBounds(insets.left + barLoc, insets.top + getBarYLoc(), d.width, d.height);

        if ((barLoc - 32) % gView.getMeasureWidth() == 0) {
            mView.playNotesAtBeat(getGridCoors(new Point(barLoc, 50)).x);
        }
        if (getBeat() == this.vm.getLength()) {
            return false;
        }
        return true;
    }

    /**
     * @return the beat of this song based on the barLoc variable.
    */
    public int getBeat() { return (barLoc - 32) / gView.getMeasureWidth(); }

    /**
     * Sets the barloc according to the given beat.
     * @param beat current beat.
*   */
    private int barLocFromBeat(int beat) { return beat * (gView.getMeasureWidth()) + 32; }


    /**
     * Initializes everything for this CompositeView.
     */
    public void initialize() {
        this.barLoc = this.getBarXStartLoc();
        playBar = this.getPlayBarImage();
        this.gView.getDisplayPanel().add(playBar);
        Insets insets = this.gView.getDisplayPanel().getInsets();
        Dimension d = playBar.getPreferredSize();
        playBar.setBounds(insets.left + getBarXStartLoc(), insets.top + getBarYLoc(), d.width, d.height);
        this.setKeyListener(kh);
        this.gView.setMouseListener(mh);
        this.setMouseListener(mh);
        playBar.setVisible(true);
    }

    /**
     * @return the tempo of the MidiView song.
     */
    public int getTempo() {
        return this.mView.getTempo();
    }

    public void display() {
        initialize();
        this.setKeyListener(kh);
        this.setMouseListener(mh);
        gView.display();
    }


    public Point getGridCoors(Point p) {
        return gView.getGridCoors(p);
    }

    public void setKeyListener(KeyListener l) {
        this.gView.addKeyListener(l);
    }

    public void setMouseListener(MouseListener l) {
        this.gView.setMouseListener(l);
    }

    /**
     * using the horizontal scroll panel, sets screen to the start
     *
     */
    public void goToStart(Integer k){
        scrollPane.getHorizontalScrollBar().setValue(0);
    }

    /**
     * using the horizontal scroll panel, sets screen to Home
     *
     */
    public void goToEnd(Integer k){
        int max = scrollPane.getHorizontalScrollBar().getMaximum();
        scrollPane.getHorizontalScrollBar().setValue(max);
    }

    public int beatWidth() { return gView.getMeasureWidth(); }

    public int beatHeight() { return gView.getMeasureHeight(); }

    /**
     * allows the user to scroll right
     * @param k
     */
    public void scrollRight(Integer k) {
        int curValue = scrollPane.getHorizontalScrollBar().getValue();
        int nxtValue = Math.max(0,
                curValue + viewRect.x);
        scrollPane.getHorizontalScrollBar().setValue(nxtValue);
    }


    public void update() {
        this.gView.repaint();
    }

}
