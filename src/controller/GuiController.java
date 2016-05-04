package controller;

import model.AbsolutePitch;
import model.Note;
import view.CompositeView;
import view.ViewModel;

import javax.sound.midi.InvalidMidiDataException;
import java.awt.*;
import java.awt.event.*;

/**
 * Controller for the music editor console UI. This takes user inputs and
 * acts on the view and then takes information from the view and shows it
 * to the user.
 */
public class GuiController {
    //Data for GuiController class
    private ViewModel vm; //get data from model
    private CompositeView view;

    //State of view trackers
    int currBeat = 0;
    javax.swing.Timer timer;
    //Input
    private KeyboardHandler kh;
    private MouseHandler mh;
    //mouseInput mi;
    private int pressedKey = 0;
    // last click of mouse in absolute coordinates
    private Point lastClick;
    //boolean to start/stop music
    private boolean isitPaused;


    /**
     * Constructs a controller for playing the given game model, with the given input and
     * output for communicating with the user.
     * @param vm: the music composition viewModel to play
     * @param view: the view to draw
     */

    public GuiController(ViewModel vm, CompositeView view){
        this.vm = java.util.Objects.requireNonNull(vm);
        this.view = java.util.Objects.requireNonNull(view);
        this.timer = new javax.swing.Timer(this.vm.getTempo() / 60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    if (!view.tick()) {
                        isitPaused = true;
                        timer.stop();
                        System.out.println("Done!");
                    }

                }
                catch (InvalidMidiDataException ie) {
                    ie.printStackTrace();
                }
            }
        });

        //initial state
        this.isitPaused = true;
        this.lastClick = null;

        kh = new KeyboardHandler(this, new StringBuilder());
        mh = new MouseHandler (this, new StringBuilder());

        // the actions dealing with the view//

        //make view go to start//
        //kh.addPressedEvent(KeyEvent.VK_HOME, view:: goToStart);
        // make view go to the end//
        //kh.addPressedEvent(KeyEvent.VK_END, view:://how ever we get to end in view );
        //scroll up with up arrow //
        // kh.addPressedEvent(KeyEvent.VK_UP, view:://how ever we scroll up in view );
        //scroll down with down arrow//
        // kh.addPressedEvent(KeyEvent.VK_DOWN, view:://how ever we scroll down in view );
        //scroll left with left arrow//
//        kh.addPressedEvent(KeyEvent.VK_LEFT, view:: windowScroll);
        //scroll right with right arrow//
        // kh.addPressedEvent(KeyEvent.VK_RIGHT, view:://how ever we scroll right in view );
        //Pause/Play the music ...."space"

        // TO START / STOP THE SONG W/ SPACEBAR
        kh.addPressedEvent(KeyEvent.VK_SPACE, (Integer k) ->{
            if (this.isitPaused) {
                this.isitPaused = false;
                timer.start();
            }
            else if(!this.isitPaused){
                this.isitPaused = true;
                timer.stop();
            }
        });

        //allow clicking to add note when this key is pressed//
        kh.addPressedEvent(KeyEvent.VK_A, this :: pressChange);
        //allow clicking to delete note when this key is pressed//
        kh.addPressedEvent(KeyEvent.VK_D, this :: pressChange);
        //allow clicking to delete note when this key is pressed//
        kh.addPressedEvent(KeyEvent.VK_M, this :: pressChange);
        //allow clicking to delete note when this key is pressed//
        kh.addPressedEvent(KeyEvent.VK_C, this :: pressChange);
        //allow clicking to delete note when this key is pressed//
        kh.addPressedEvent(KeyEvent.VK_R, this :: pressChange);
        //allow clicking to delete note when this key is pressed//
        kh.addPressedEvent(KeyEvent.VK_E, this :: pressChange);
        kh.addPressedEvent(KeyEvent.VK_T, this :: pressChange);
        /**interactions with mouse clicks. **/


        mh.addClickedEvent(KeyEvent.VK_T, (MouseEvent e) -> {
            System.out.println("t");
            System.out.println(e.getPoint() + ",  " + this.view.getGridCoors(e.getPoint()));
        });
        //if A is being pressed w/ click: add note
        mh.addClickedEvent(KeyEvent.VK_A, (MouseEvent e) ->{
            try {
                this.addNote(e.getPoint());
            }
            catch (IllegalArgumentException ia) {
                ia.printStackTrace();
            }

            this.view.update();
            this.pressedKey = 0;
        });
        //if D is being pressed w/ click: delete note
        mh.addClickedEvent(KeyEvent.VK_D, (MouseEvent e) ->{
            try {
                this.removeNote(e.getPoint());
            }
            catch (IllegalArgumentException ia) {
                ia.printStackTrace();
            }

            this.view.update();
            this.pressedKey = 0;
        });
        //if M is being pressed w/ click: move this note (works horizontally and vertically)
        mh.addClickedEvent(KeyEvent.VK_M, (MouseEvent e) ->{
            if (this.lastClick == null) {
                this.lastClick = e.getPoint();
            }
            else {
                try {
                    this.moveNote(lastClick, e.getPoint());
                }
                catch (IllegalArgumentException ia) {
                    ia.printStackTrace();
                }

                this.view.update();
                this.lastClick = null;
                this.pressedKey = 0;
            }
        });
        //if C is being pressed w/ click: edit the end of this note.
        mh.addClickedEvent(KeyEvent.VK_C, (MouseEvent e) ->{
            if (this.lastClick == null) {
                this.lastClick = e.getPoint();
            }
            else {
                try {
                    this.changeNoteEnd(lastClick, e.getPoint());
                }
                catch (IllegalArgumentException ia) {
                    ia.printStackTrace();
                }
                this.view.update();
                this.lastClick = null;
                this.pressedKey = 0;
            }
        });
        //if R is being pressed w/ click: adds a repeat at that beat
        // NOTE: if first end of repeat has already been added, adds second repeat.
        mh.addClickedEvent(KeyEvent.VK_R, (MouseEvent e) ->{
            if (this.lastClick == null) {
                this.lastClick = e.getPoint();
            }
            else {
                try {
                    this.addRepeat(lastClick, e.getPoint());

                }
                catch (IllegalArgumentException ia) {
                    ia.printStackTrace();
                }
                this.view.update();
                this.lastClick = null;
                this.pressedKey = 0;
            }
        });

        //if E is being pressed w/ click: adds an ending at that beat.
        // NOTE: if start of ending has already been placed, adds end of ending.
        mh.addClickedEvent(KeyEvent.VK_E, (MouseEvent e) ->{
            System.out.println(this.view.getGridCoors(e.getPoint()));
            this.addEnding(e.getPoint());
            this.view.update();
            this.lastClick = null;
            this.pressedKey = 0;

        });
    }

    public void addNote(Point p) {
        Point n = this.view.getGridCoors(p);
        AbsolutePitch ap = this.vm.getFullRange().get(n.y);
        this.vm.addNote(new Note(ap, n.x, 1, 1, 60));
    }

    public void removeNote(Point p) {
        this.vm.removeNote(this.vm.getNoteAt(this.view.getGridCoors(p)));
    }

    public void moveNote(Point p1, Point p2) {
        this.vm.moveNote(this.vm.getNoteAt(this.view.getGridCoors(p1)),
                            (p2.x - p1.x) / this.view.beatWidth(),
                                (p2.y - p1.y) / this.view.beatHeight());
    }

    public void changeNoteEnd(Point p1, Point p2) {
        this.vm.changeNoteEnd(this.vm.getNoteAt(this.view.getGridCoors(p1)),
                                (p2.x - p1.x) / this.view.beatWidth());
    }

    //LEVEL 1!
    public void addRepeat(Point p1, Point p2) {
        int beginning = (int) this.view.getGridCoors(p1).getX();
        int end = (int) this.view.getGridCoors(p2).getX();

        if (beginning == end) {
            if (end % 4 != 3) {
                System.out.println (beginning % 4 + ", " + end % 4);
                throw new IllegalArgumentException("Repeat must end at end of a measure!");
            }
            this.vm.addRepeat(0, end);
        }

        else if (beginning % 4 != 0) {
            throw new IllegalArgumentException("Beginning of repeat must be beginning of measure!");
        }

        else if (end % 4 != 3) {
            throw new IllegalArgumentException("End of repeat must be end of measure!");
        }

        this.vm.addRepeat(beginning, end);
    }


    public void addEnding(Point p1) {
        int beginning = (int) this.view.getGridCoors(p1).getX();

        if (beginning % 4 != 0) {
            throw new IllegalArgumentException("Beginning of ending must be beginning of measure!");
        }
        this.vm.addEnding(beginning);
    }

    public void pressChange(Integer k) {
        if (this.pressedKey == k) {
            this.pressedKey = 0;
        }
        else {
            this.pressedKey = k;
        }
    }

    public int getPressed() {
        return this.pressedKey;
    }

    public void run() {
        view.setKeyListener(kh);
        view.setMouseListener(mh);
        view.display();
    }
}