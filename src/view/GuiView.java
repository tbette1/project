package view;

import javax.sound.midi.InvalidMidiDataException;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public interface GuiView extends View {

    /**
     * Sets the KeyListener for this GuiView.
     * @param kh keyHandler to be set.
     */
    void setKeyListener(KeyListener kh);

    /**
     * Sets the MouseListener for this GuiView.
     * @param mh MouseListener to be set.
     */
    void setMouseListener(MouseListener mh);

    /**
     * @return the tempo at which this view is to run.
     */
    int getTempo();

    /**
     * Occurs on each tick of the timer. (What the view needs to do
     * on each passing millisecond.
     * @return false if the song being played has finished, true otherwise.
     * @throws InvalidMidiDataException
     */
    boolean tick() throws InvalidMidiDataException;

    /**
     * @param p point on this view given in absolute coordinates.
     * @return grid coordinates on this view's grid displaying notes based on the
     *         point given in absolute coordinates.
     */
    Point getGridCoors(Point p);

    /**
     * Updates the current view by repainting.
     */
    void update();
}

