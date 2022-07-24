import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import com.sksamuel.scrimage.*;
import com.sksamuel.scrimage.nio.*;

class Main {

	// moved central variables to top so they can be accessed by all methods
	public static JFrame frame;
	public static BufferedImage image;
	
  public static void main(String[] args) {
		
    System.out.println("Good Morning World");

    program();
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
		
  }

  private static void createAndShowGUI() {
    // Still a work in progress

    // Create frame
    frame= new JFrame("Mosaic Maker");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(image.getWidth(), image.getHeight());
		frame.setResizable(false);

    JLabel label = new JLabel();
    label.setIcon(new ImageIcon(image));
    frame.getContentPane().add(label,BorderLayout.CENTER);

    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        // End any processes here such as the mosaic making
      }
    });

     // Compute ideal window size and show window.
    frame.pack();
    frame.setVisible(true);
    
  }
  
  // there's probably a better name than this
  private static void program() {
    File imageFile = new File("photo.jpeg");
    try {
			image = ImageIO.read(imageFile);
			
      // Mosaic stuff
		} catch (IOException e) {
			System.out.println(e);
		}
  }
}