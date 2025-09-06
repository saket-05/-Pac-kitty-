
import javax.swing.JFrame;
public class App {
    public static void main(String[] args) throws Exception {
       int rowCount=21;
       int columnCount=19;
       int tileSize=32;
       int boardWidth= columnCount*tileSize;
       int boardHeight=rowCount*tileSize;

       JFrame frame=new JFrame("Pac Kitty🐱💖");
       //frame.setVisible(true);//the created frame will be visible
       frame.setSize(boardWidth,boardHeight);//dimensions of frame
       frame.setLocationRelativeTo(null);//frame location will be at centre of screen
       frame.setResizable(false);//user cannot resize frame using mouse 
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//the window will be closed on pressing close button


       PacKitty packitty =new PacKitty();
       frame.add(packitty);//adds packitty game to the frame;
       frame.pack();//makes sure the game is fit to screen to the frame
       packitty.requestFocus();
       frame.setVisible(true);// sets game to be visible

       //closes audio files after game window is closed
frame.addWindowListener(new java.awt.event.WindowAdapter() {
    @Override public void windowClosing(java.awt.event.WindowEvent e) {
        packitty.stopAudio();
    }
});

    }
}
