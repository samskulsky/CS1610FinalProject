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
  public static ImmutableImage mosaicImage;
	public static BufferedImage image;
	//xx2
  public static void main(String[] args) {
		
    System.out.println("Good Morning World");

    loadMainImage();
    mosaicImage = mosaicImage.contrast(2.0); // Testing
    image = mosaicImage.awt();
    
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
    
		/* Was able to display the image in the panel
     * but had to use BufferedImage instead of ImmutableImage
     * for now... also, I don't know how we want to deal
     * with image size because it is currently too big
     * for browser window.
		 */

    /* Image has been resized for display.
    /* We'll probably do all the mosaic stuff purely using ImmutableImage.
    /* There's a scrimage operation called Overlay which we can use to cover an image with another image.
    */

    float imgWidth = image.getWidth(null);
    float imgHeight = image.getHeight(null);
    float maxLength = Math.max(imgWidth, imgHeight);
    int targetSize = 500;
    int scaledWidth = targetSize * (int) (imgWidth/maxLength);
    int scaledHeight = targetSize * (int) (imgHeight/maxLength);
    Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT);
    
		//frame.setSize(scaledImage.getWidth(null), scaledImage.getHeight(null));
		frame.setResizable(false);
       
    JLabel label = new JLabel();
    label.setIcon(new ImageIcon(scaledImage));
    frame.getContentPane().add(label,BorderLayout.CENTER);

    // I'm going to keep this for testing. We could use it later to save the image to a file.
    JButton startButton= new JButton("Save Image");
    startButton.setFont(startButton.getFont().deriveFont(20.0f));
    frame.add(startButton, BorderLayout.PAGE_END);
    
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
  private static void loadMainImage() {
    File imageFile = new File("photo.jpeg");
    try {
      mosaicImage = ImmutableImage.loader().fromFile(imageFile);
		} catch (IOException e) {
			System.out.println(e);
		}
  }
}