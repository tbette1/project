package controller;


/**
 * Created by Elijah on 4/3/2016.
 */

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Class for handling mouse events
 */

public class MouseHandler implements MouseListener {
    private GuiController controller;
    private HashMap<Integer, Consumer<MouseEvent>> clicked;
    private Appendable logfile;


    /**
     * Default constructor for a mouse handler that calls the more complex version
     * with system.out acting as the log
     * @param controller: controller
     */
    public MouseHandler(GuiController controller){
        this(controller,System.out);
    }

    /**
     * more complex version of the mouse handler constructor. Called by the default constructor
     * a custom keyboard handler can be created with a specific appendable which can be used
     * for testing
     * @param controller: controller to link the event responses
     * @param log: the Appendable to attach the input messages to.
     */
    public MouseHandler(GuiController controller, Appendable log){
        this.controller = controller;
        this.clicked = new HashMap<>();
        this.logfile = log;
    }

    /**
     * When the mouse is clicked, this will check if any of the delegated keys
     * are being pressed. If a proper combination exists, it will run the combination.
     * @param e: mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // If left mouse button was clicked
        if(e.getButton() == MouseEvent.BUTTON1) {
            //if a key has an action associated with mouse click
            //run it
            int k = this.controller.getPressed();
            if (this.clicked.containsKey(k)){
                this.clicked.get(k).accept(e);
            }

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    /**
     * Add event and action to respective map field
     * @param e: event id
     * @param c:runnable action corresponding to event.
     */
    public void addClickedEvent(int e, Consumer<MouseEvent> c){
        this.clicked.put(e,c);
    }


    /**
     * Appends message into the log
     * @param message: what ever is happening
     */

    void printMessage(String message){
        try{
            this.logfile = this.logfile.append(message).append("\n");
        }catch (IOException e){
            // only if there is nothing to append
        }
    }

    /**
     * Prints the data stored in the logfile
     * @return the data as a string
     */
    protected String printLogFile(){
        return this.logfile.toString();
    }

}

