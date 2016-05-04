package view;
import model.AbsolutePitch;
import model.Note;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Objects;
public class GraphicView extends JFrame implements View {
    ViewModel vm;
    private JPanel displayPanel;
    MouseListener mListener;
    KeyListener kListener;
    double endOfWindowLoc;
    JScrollPane scrollPane;
    Point startOfGrid = new Point(32, 13);


    /**
     * Constructs a new GraphicView object.
     * @param m model to be represented by this view.
     */
    public GraphicView(ViewModel m) {
        this.vm = Objects.requireNonNull(m);
        this.displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        scrollPane = new JScrollPane();

        JViewport view = new JViewport();
        view.setViewSize(new Dimension(this.getViewWidth(), this.getViewHeight()));

        view.setOpaque(false);
        view.setView(displayPanel);
        scrollPane.setViewport(view);

        this.add(scrollPane);
        this.pack();
        this.setSize(new Dimension(950, 500));
        this.endOfWindowLoc = this.getViewWidth();
        this.setResizable(false);
        this.mListener = null;
        this.kListener = null;
    }

    /**
     *  Creates the editor view grid.
     */
    private JPanel createEditorGrid() {
        JPanel basePanel = new JPanel();
        basePanel.setBackground(Color.BLACK);
        int rows = this.vm.getFullRange().size();
        int cols = this.vm.getLength() + (this.vm.getLength() % 4);
        basePanel.setLayout(new GridLayout(rows, cols + 1, 0, 0));
        for (int i = 0; i < rows; i++) {
            AbsolutePitch p = this.vm.getFullRange().get(i);
            ArrayList<Note> notesAt = this.vm.getNotesAtPitch(p);
            for (int j = 0; j < cols; j++) {
                Border border = null;
                if (this.vm.isStartOfRepeat(j)) {
                    border = new CompoundBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK),
                            new MatteBorder(0, 1, 0, 0, Color.CYAN));
                }
                else if (this.vm.isEndOfRepeat(j)) {
                    border = new CompoundBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK),
                            new MatteBorder(0, 0, 0, 1, Color.CYAN));
                }
                else if (this.vm.isStartOfEnding(j)) {
                    border = new CompoundBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK),
                            new MatteBorder(0, 1, 0, 0, Color.MAGENTA));
                }
                else if (this.vm.isEndOfEnding(j - 1)) {
                    border = new CompoundBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK),
                            new MatteBorder(0, 1, 0, 0, Color.MAGENTA));
                }
                else if (j % 4 == 0) {
                    border = new MatteBorder(1, 1, 1, 0, Color.BLACK);
                }
                else if (j % 4 == 3) {
                    border = new MatteBorder(1, 0, 1, 1, Color.BLACK);
                }
                else {
                    border = new MatteBorder(1, 0, 1, 0, Color.BLACK);
                }
                BeatPanel x = new BeatPanel(j % 4);
                if (notesAt.isEmpty()) {
                    x.setBackground(Color.LIGHT_GRAY);
                }
                else {
                    if (!notesAt.get(0).playsAt(j)) {
                        x.setBackground(Color.LIGHT_GRAY);
                    }

                    else if (notesAt.get(0).getStartTime() == j) {
                        x.setBackground(Color.BLACK);
                    }
                    else {
                        x.setBackground(Color.GREEN);
                    }
                    if (notesAt.get(0).getEndOfNote() == j) {
                        notesAt.remove(0);
                    }
                }
                x.setBorder(border);
                basePanel.add(x);
            }
        }
        return basePanel;
    }

    /**
     * @return a panel containing the beat numbers for the song.
     */
    private JPanel beatDisplay(){
        JPanel beatsPanel = new JPanel();
        int length = this.vm.getLength() + (4 - (this.vm.getLength() % 4));
        beatsPanel.setLayout(new GridLayout(1, length));
        beatsPanel.add(new BeatPanel(-1));
        for (int i = 0; i < length; i++) {
            BeatPanel b = new BeatPanel(-1);
            b.setLayout(new BorderLayout());
            if (i % 16 == 0) {
                JLabel beatLabel = new JLabel(i + "");
                String font = beatLabel.getFont().getFontName();
                beatLabel.setFont(new Font(font, Font.PLAIN, 10));
                b.add(beatLabel, BorderLayout.SOUTH);
            }
            else if (this.vm.isStartOfEnding(i - 1)) {
                JLabel endingLabel = new JLabel(this.vm.getEndingNumber(i) + "");
                String font = endingLabel.getFont().getFontName();
                endingLabel.setBackground(Color.MAGENTA);
                endingLabel.setFont(new Font(font, Font.ITALIC, 10));
                b.add(endingLabel, BorderLayout.SOUTH);
            }
            beatsPanel.add(b);
        }
        return beatsPanel;
    }

    /**
     * @return a panel containing all note names in this song.
     */

    private JPanel noteNameDisplay() {
        JPanel namePanel = new JPanel();
        ArrayList<String> notes = this.vm.getNoteNames();
        namePanel.setLayout(new GridLayout(notes.size(), 1));
        for (int i = 0; i < notes.size(); i++) {
            BeatPanel b = new BeatPanel(-1);
            b.setLabel(notes.get(i));
            b.setBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK));
            namePanel.add(b);
        }
        return namePanel;
    }

    /**
     * @return the displayPanel.
     */
    JPanel getDisplayPanel() {
        return this.displayPanel;
    }
    /**
     * Initializes this graphic view to have an editor grid, note name displays,
     * and beat counts.
     */
    public void initialize(){
        JPanel grid = createEditorGrid();
        displayPanel.add(grid, BorderLayout.CENTER);
        displayPanel.add(noteNameDisplay(), BorderLayout.WEST);
        displayPanel.add(beatDisplay(), BorderLayout.NORTH);

    }

    public Dimension getMaxSize() {
        return new Dimension(1500, 1500);
    }

    public int getViewWidth() {
        return this.getPreferredSize().width;
    }

    public int getViewHeight() {
        return this.getPreferredSize().height;
    }


    /**
     * @return the width of a single beat panel on this view.
     */
    public int getMeasureWidth() {
        return ((this.getPreferredSize().width - 32) / this.vm.getLength());
    }

    /**
     * @return the height of a beat panel on this view.
     */
    public int getMeasureHeight() {
        return ((this.getPreferredSize().height - 13) / this.vm.getFullRange().size()) * 2;
    }

    /**
     *
     * @param p a point in absolute coordinates on this view.
     * @return a point representing the corresponding gridSquare on this gridLayout
     */
    public Point getGridCoors(Point p) {
        int x = p.x - (this.getMeasureWidth() * 4);
        int y = p.y - startOfGrid.y;

        if (x < 0  || y < 0) {
            return new Point(-1, -1);
        }

        int xGrid = (int) Math.floor(x / (this.getMeasureWidth() - 2));
        int yGrid = (y / (this.getMeasureHeight() / 3)) - 1;
        if (yGrid < 0) {
            yGrid = 0;
        }
        return new Point(xGrid, yGrid);
    }


    @Override
    public Dimension getPreferredSize(){
        int width = 15 * this.vm.getLength() + 40;
        int height = 40 * this.vm.getFullRange().size() + 28;
        return new Dimension(width, height);
    }

    /**
     * Displays this graphic view.
     */
    public void display() {
        initialize();
        this.setVisible(true);
    }

    public void setKeyListener(KeyListener l) {

        this.kListener = l;
        this.displayPanel.addKeyListener(l);
    }

    public void setMouseListener(MouseListener l) {
        this.mListener = l;
        this.displayPanel.addMouseListener(l);
    }

    @Override
    public void repaint() {
        initialize();
        this.display();
    }

}
