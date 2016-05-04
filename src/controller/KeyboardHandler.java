package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by Elijah on 4/3/2016.
 */

/**
 * Class for key events
 */

public class KeyboardHandler implements KeyListener {
    private GuiController controller;
    private HashMap<Integer, Consumer<Integer>> typed;
    private HashMap<Integer, Consumer<Integer>> pressed;
    private HashMap<Integer, Consumer<Integer>> released;
    private Appendable logfile;

    public KeyboardHandler() {
        this.typed = new HashMap<Integer, Consumer<Integer>>();
        this.pressed = new HashMap<Integer, Consumer<Integer>>();
        this.released = new HashMap<Integer, Consumer<Integer>>();
    }


    /**
     * Default constructor for a keyboard handler that calls the more complex version
     * with system.out acting as the log
     * @param controller: controller
     */
    public KeyboardHandler(GuiController controller){
        this(controller,System.out);
    }

    /**
     * more complex version of the keyboard handler constructor. Called by the default constructor
     * a custom keyboard handler can be created with a specific appendable which can be used
     * for testing
     * @param controller: controller to link the event responses to
     * @param logfile: appendable to attach the input messages to
     */
    public KeyboardHandler(GuiController controller, Appendable logfile){
        this.controller = controller;
        this.typed = new HashMap<>();
        this.released = new HashMap<>();
        this.pressed = new HashMap<>();
        this.logfile = logfile;
    }

    /**
     * Handle the key typed event
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
        int k = e.getKeyCode();
        if(this.typed.containsKey(k)){
            this.typed.get(k).accept(k);
        }
    }

    /**
     * Handle the key pressed event
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if(this.pressed.containsKey(k)){
            this.pressed.get(k).accept(k);
        }
        else{
            this.printMessage("Key not supported");
        }
    }
    /**
     * Handle the key released event
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if(this.released.containsKey(k)){
            this.released.get(k).accept(k);
        }
    }
    /**
     * Adds a new key event and its corresponding action into their respective map field.
     * @param e key id
     * @param c runnable action corresponding to the key (a Consumer<Integer> that
     *          takes the key code)
     */
    public void addTypedEvent(int e, Consumer<Integer> c) {
        this.typed.put(e, c);
    }

    public void addPressedEvent(int e, Consumer<Integer> c) {
        this.pressed.put(e, c);
    }

    public void addReleasedEvent(int e, Consumer<Integer> c) {
        this.released.put(e, c);
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