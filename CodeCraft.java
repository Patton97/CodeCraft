
import java.awt.EventQueue;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


//NOTES FOR NEXT TIME
//JEditorPane
//https://stackoverflow.com/questions/37443717/jtextarea-autoformat-like-netbeans-ide

public class CodeCraft extends JFrame implements ActionListener{   
    public Level level;
    public javax.swing.Timer timer;    
    public int iFrame = 0;
    
    private static int NUM_LEVELS = 5;    
    
    private JButton btnPlay;
    private JButton[] levelButtons = new JButton[NUM_LEVELS];
    
    public CodeCraft() {
        initUI();
        //Clear Frames (safety)
        Rover.getRover().clearFrames();
        //Collect frames from isntructions
        LevelTwo usercode = new LevelTwo();
        usercode.main(); 
    }

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

    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run() {
                CodeCraft ex = new CodeCraft();
                ex.setVisible(true);
            }
        }
        );
    }
    
    public void addComponentsToPane(Container pane)
    {       
        //Add Level to screen (CENTER)
        initLevel(pane);
        
        //Add control button to screen
        btnPlay = new JButton(new ImageIcon("play.png"));
        pane.add(btnPlay, BorderLayout.SOUTH);
        btnPlay.addActionListener(this);
        
        //Add Level Menu
        JPanel right = new JPanel(new GridBagLayout());
        right.setLayout(new GridLayout(NUM_LEVELS,1));

        //Add buttons for all levels, starting with level one
        // level zero is reserved for testing/demos
        for(int i = 1; i < NUM_LEVELS; i++)
        {
            levelButtons[i] = new JButton("Level " + i); //Add button to array
            levelButtons[i].addActionListener(this);
            right.add(levelButtons[i]); //Add button to screen
        }
        
        //Add menu to pane
        pane.add(right, BorderLayout.EAST);
    }
    
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
        for( int i = 0; i< NUM_LEVELS; i++)
        {
            if(ae.getSource() == levelButtons[i])
            {
                Level.getLevel().loadLevel(i);
                System.out.println(i);
            }
        }
        //Control Buttons
        if(ae.getSource() == btnPlay)
        {
            initTimer();
        }
    }
}