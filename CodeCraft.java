import java.awt.EventQueue;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CodeCraft extends JFrame implements ActionListener{   
    public Level level;
    public javax.swing.Timer timer;
    //Not counting the demo level 0. 
    //If the last level is level 5, NUM_LEVELS = 5.
    private static int NUM_LEVELS = 5;         
    private int currentLevel = 0;
    private JButton btnPlay;
    private JButton[] levelButtons = new JButton[NUM_LEVELS+1]; //+1 accounts for demo level 0
    
    /**
     * Constructor redirects to initUI, no parameters required
     * 
     * This function is aimed at providing the student with a easy, understandable
     * way to restart the game each time, rather than confusing them with weird
     * function calls like "void main(args[])".
     */
    public CodeCraft() {initUI();}

    /**
     * Initalises the UI of the program
     */
    private void initUI(){
        //Create JFrame
        super.setTitle("CodeCraft");
        super.setSize(1280, 720);
        setLocationRelativeTo(null);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(super.getContentPane());
        
        //Pack & enable
        super.pack();
        super.setVisible(true);
    }
    
    /**
     * Initialises the repaint timer.
     * 
     * If rover is finished, the timer is stopped and then restarted to flush current simulation.
     */
    private void initTimer()
    {
        timer = new Timer(500, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                if(Rover.getRover().finished)
                {
                    timer.stop();
                }
                else
                {
                    Level.getLevel().updateRover();
                }
            }
        });
        timer.start();
    }

    /**
     * Standard main method redirects to constructor.
     * Used for if constructor fails and/or if package is run via .jar
     */
    private static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                CodeCraft codecraft = new CodeCraft();
                codecraft.setVisible(true);
            }
        }
        );
    }
    
    /**
     * Adds buttons, images, levels to the overall container pane
     * 
     * @param pane The container in which the components are held
     */
    private void addComponentsToPane(Container pane)
    {       
        //Add Level to screen (CENTER)
        initLevel(pane);
        
        //Add control button to screen
        btnPlay = new JButton(new ImageIcon("Images/play.png"));
        //pane.add(btnPlay, BorderLayout.SOUTH);
        btnPlay.addActionListener(this);
        
        //Add Level Menu
        JPanel right = new JPanel(new GridBagLayout());
        right.setLayout(new GridLayout(NUM_LEVELS+1,1));

        //Add buttons for all levels, starting with level one
        // level zero is reserved for testing/demos
        for(int i = 1; i <= NUM_LEVELS; i++)
        {
            levelButtons[i] = new JButton("Level " + i); //Add button to array
            levelButtons[i].addActionListener(this);
            right.add(levelButtons[i]); //Add button to screen
        }
        
        //Add play button after last level
        right.add(btnPlay);
        //Add menu to pane
        pane.add(right, BorderLayout.EAST);
    }
    
    /**
     * Initialises the level drawn to screen. 
     * Default level 0 is selected, level panel is resized and positioned.
     * 
     * @param pane The container in which the Level's JPanel is held
     */
    private void initLevel(Container pane)
    {   
        Level.getLevel().setPreferredSize(new Dimension(750, 750));
        pane.add(Level.getLevel(), BorderLayout.CENTER);
           
        //Send to Level
        Level.getLevel().loadLevel(0);
        
        Level.getLevel().repaint();
    }
    
    /**
     * Handles button presses.
     * There should be a better way of doing this, but unfortunately Java doesn't
     * seem to like declaring multiple action listeners. 
     * 
     * @param ae ActionEvent which caused this to be called.
     */
    public void actionPerformed(ActionEvent ae)
    {
        //Level buttons
        // level zero is reserved for testing/demos, and therefore has no button
        for(int i = 1; i<= NUM_LEVELS; i++)
        {
            if(ae.getSource() == levelButtons[i])
            {
                currentLevel = i;
                Level.getLevel().loadLevel(i);
            }
        }
        //Control Buttons
        if(ae.getSource() == btnPlay)
        {
            //If timer exists, stop it
            if(timer != null)
            {
                timer.stop();
            }
            
            //Reload current level, restart timer
            Level.getLevel().loadLevel(currentLevel);
            initTimer();
        }
    }
}