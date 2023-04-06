import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;
import java.applet.AudioClip;
import java.applet.Applet;

/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game in a Compact GUI Window
 * 
 * Features:
 * - Compact GUI Window
 * - Full Tic-Tac-Toe Game
 * - Able to Swtich Teams and Keep Score
 * - Score Display
 * - Current Turn Display
 * - Winner Display and Pop Up Winner Display
 * - Sounds Effects for each user and game over
 * - Special PNG X and Os
 * 
 * @author Lakshman Chelliah 101227387
 * @version April 6th 2023
 */

public class TicTacToeFrame extends TicTacToe implements ActionListener { 
    
    static TicTacToeFrame game;

    private static JFrame frame;
    private JPanel p = new JPanel();

    //Buttons for the TicTacToe Options
    private JButton buttons[] = new JButton[9];
    //Variable for the which Icon to Show
    private boolean isXTurn = true;
    //X Icon
    private ImageIcon X = new ImageIcon(this.getClass().getResource("x.png"));
    //O Icon
    private ImageIcon O = new ImageIcon(this.getClass().getResource("o.png"));
    //Window Icon
    private ImageIcon img = new ImageIcon("tictactoeicon.png");

    //Used for Sound Effects
    private AudioClip click;
    

    //Label to display current turn
    private JLabel labelTurn;
    //Label to display score
    private JLabel labelScore;

    //Ints to hold Score
    private int xWins = 0;
    private int oWins = 0;

    //Reset Menu Button
    private JMenuItem resetItem;
   
    //Quit Menu Button
    private JMenuItem quitItem;

    //Switch Teams Menu Button
    private JMenuItem switchItem;


    /**
     * Construts TicTacToeFrame and Buttons, Menu Buttons, and Labels
     */
    public TicTacToeFrame()
    { 
       //Create Frame
        frame = new JFrame("TicTacToe");
        frame.setPreferredSize(new Dimension(500,500));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set Frame Icon
        frame.setIconImage(img.getImage());

        //Add Menu Bar & Menu Buttons
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar); // add menu bar 

        JMenu fileMenu = new JMenu("Options"); // create a menu
        menubar.add(fileMenu); // and add to our menu bar
        
        switchItem = new JMenuItem("Swtich Teams"); //item called "Switch Teams"
        fileMenu.add(switchItem); 

        resetItem = new JMenuItem("New"); //item called "New"
        fileMenu.add(resetItem); 

        quitItem = new JMenuItem("Quit"); //item called "Quit"
        fileMenu.add(quitItem); 


      
        // this allows us to use shortcuts (e.g. Ctrl-R, Ctrl-Q, Ctrl-S)
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // to save typing
        switchItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
        resetItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, SHORTCUT_MASK));
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
      
        // listen for menu selections
        switchItem.addActionListener(new ActionListener() // create an anonymous inner class
            {   //Switches Score so Player O can now be Player X and keep their score and vis-versa  
                public void actionPerformed(ActionEvent event)
                {
                    int temp = xWins;
                    xWins = oWins;
                    oWins = temp;
                    resetGame();
                }
            } 
        ); 
        resetItem.addActionListener(new ActionListener() // create an anonymous inner class
            { // Resets Score and the game
                public void actionPerformed(ActionEvent event)
                {
                    xWins = 0;
                    oWins = 0;
                    resetGame();
                }
            }
        );
        quitItem.addActionListener(new ActionListener() // create an anonymous inner class
            { // Exits application
                public void actionPerformed(ActionEvent event)
                {
                    System.exit(0); // quit
                }
            } 
        ); 

        //Initalize Label to display Turn
        //Display X's Turn First    
        labelTurn = new JLabel("Current Turn: X");
        labelTurn.setHorizontalAlignment(JLabel.CENTER); // right justified

        //Initalize Label to display Wins
        labelScore = new JLabel("X Wins: 0" + String.format("%-20s","") +  "O Wins: 0");
        labelScore.setHorizontalAlignment(JLabel.CENTER); // right justified

        //Container to hold Labels
        Container contentPane = frame.getContentPane(); 
        contentPane.setLayout(new BorderLayout()); // use border layout (default)
        contentPane.add(labelTurn,BorderLayout.SOUTH); // west side 
        contentPane.add(labelScore,BorderLayout.NORTH); // west side 
        
        //Set up the Layout for the 9 Buttons (3x3)
        p.setLayout(new GridLayout(3, 3));
        
        //Initalize all Buttons
        for (int i = 0; i < 9; i++){
            buttons[i] = new JButton();
            p.add(buttons[i]);
            buttons[i].addActionListener((ActionListener) TicTacToeFrame.this);
        }    
       
        frame.add(p);
        frame.setVisible(false);
        frame.pack();
       
   }

   /**
     * ActionListener for buttons for TicTacToe
     *
     * @param ActionEvent e 
     */
   public void actionPerformed(ActionEvent e) {
        
        //Check if any of the buttons were clicked
        //If so...
        // - Disable Button
        // - Switch Turn
        // - Change labelTurn
        for (int i = 0; i < 9; i++){
            if (e.getSource() == buttons[i]) {
                
                buttons[i].setEnabled(false);
                
                if (isXTurn == true){
                    buttons[i].setIcon(X);
                    isXTurn = false;
                    labelTurn.setText("Current Turn: O");
                    URL buttonClick = TicTacToeFrame.class.getResource("xmove.wav"); // beep
                    click = Applet.newAudioClip(buttonClick);
                    click.play();
                     
                } else {
                    buttons[i].setIcon(O);
                    isXTurn = true;
                    labelTurn.setText("Current Turn: X");
                    URL buttonClick = TicTacToeFrame.class.getResource("omove.wav"); // beep
                    click = Applet.newAudioClip(buttonClick);
                    click.play();
                     
                }

                //Translates button inputs buy reading button location and translates to 2 digit coordinates(ex. 0, 2)
                getInputforGame(buttons[i].getX(), buttons[i].getY());

                //Debugging
                //System.out.println(buttons[i].getX());
                //System.out.println(buttons[i].getY());

                //Exit Loop once we found the button we needed
                break;
            }
            
        }

        //Check for Winner from TicTacToe.java
        if (winner != EMPTY){
            //Set Current Turn to None
            
            //Disable all buttons
            for (int i = 0; i < 9; i++){
                buttons[i].setEnabled(false);
            }
            
            //Play game end sound effect
            URL buttonClick = TicTacToeFrame.class.getResource("endgame.wav"); // beep
            click = Applet.newAudioClip(buttonClick);
            click.play(); // just plays clip once

            
            if (winner == TIE){ //If its a tie show custom message on popup
                labelTurn.setText("TIE GAME");
                JOptionPane.showMessageDialog(null, "TIE GAME","Game Over!", JOptionPane.INFORMATION_MESSAGE);
            } else { //Show Winner Name
                //See who wins and add to their score
                if (winner == PLAYER_X){
                    xWins++; 
                    labelTurn.setText( "X" + " is the Winner!");
                    JOptionPane.showMessageDialog(null, "X" + " is the Winner!","Game Over!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    oWins++;
                    labelTurn.setText( "O" + " is the Winner!");
                    JOptionPane.showMessageDialog(null, "O" + " is the Winner!","Game Over!", JOptionPane.INFORMATION_MESSAGE);
                }

            }

            //Reset the round for the next
            resetGame();
        }
 
    }
  
    /**
     * Converts location of Listened Button (ex. 398, 231) to coordinate system used by parent class
     *
     * @param int x location
     * @param int y location
     * 
     */
    private void getInputforGame(int x, int y){
        //X Coord
        int xfinal = 0;
        //Y Coord
        int yfinal = 0;

        //Looks for location and translate to coord that TicTacToe.java can understand
        if (x == 161){
            xfinal = 1;
        } else if (x == 322) {
            xfinal = 2;
        }

        if (y == 135){
            yfinal = 1;
        } else if (y == 270) {
            yfinal = 2;
        } 

        //Debugging
        //System.out.println(yfinal + " " + xfinal);
        //Runs the logic in TicTacToe.java
        selectSquare(yfinal,xfinal);
    }

    /**
     * Resets game for the next round
     */
    private void resetGame(){
        //Resets all Values to be ready for the next round
        
        
        
        for (int i = 0; i < 9; i++){
            buttons[i].setEnabled(true);
            buttons[i].setIcon(null);
        }
        isXTurn = true;
        labelScore.setText("X Wins: " + xWins + String.format("%-20s","") +  "O Wins: " + oWins);
        labelTurn.setText("Current Turn: X");
        playGame();
    }

    /**
     * Resets parent class logic
     */
    private static void newGame(){
        //Clears logic
        game.clearBoard();
        
    }
    
    
    private void startGame(){
        frame.setVisible(true);
    }
    
    public void playGame()
    {
        clearBoard();
        frame.setVisible(true);
        // clear the board
    } 
    
    public static void main(String[] args) {
        game = new TicTacToeFrame();
        newGame();
        frame.setVisible(true);
    }

   
    
    
}

