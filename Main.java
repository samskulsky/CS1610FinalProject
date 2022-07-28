import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import com.sksamuel.scrimage.*;
import com.sksamuel.scrimage.color.*;
import com.sksamuel.scrimage.pixels.*;
import com.sksamuel.scrimage.nio.*;
import com.sksamuel.scrimage.filter.*;
import java.net.*;

class Main {

	public static JFrame frame;
  public static ImmutableImage mosaicImage;
	public static BufferedImage image;

	// will be used to store the average color for each segment of the image
	public static ArrayList<Tile> imageTiles = new ArrayList<Tile>(); // Only needs to be 1D to store every square
	
	//xx2
  public static void main(String[] args) throws MalformedURLException, IOException {
		
    System.out.println("Good Morning World");

    // Disabled to hide errors while I was testing
		Pexels.retrieveImage("light");
		
    loadMainImage();

    //mosaicImage = mosaicImage.contrast(2.0); // Testing

    MakeSquares(mosaicImage, 3, 6, 0, 0);

    System.out.println("Image Division Complete");
    
    for (Tile tile : imageTiles) {
      ImmutableImage tileImage = ImmutableImage.create(tile.length, tile.length);
      
      ytileImage = tileImage.map(pixel -> new RGBColor(tile.r, tile.g, tile.b).awt()); // This works
			//tileImage = Pexels.getImageFromListByColor(tile.r, tile.g, tile.b);
      mosaicImage = mosaicImage.overlay(tileImage, tile.x, tile.y);
    }

    System.out.println( imageTiles.size() +  " squares drawn");
    
    image = mosaicImage.awt();
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
		
  }

  public static void MakeSquares(ImmutableImage image, int minDepth, int maxDepth, int x, int y) {
    try {
      RGBColor detail = image.filter(new EdgeFilter()).filter(new GaussianBlurFilter(4)).brightness(2.0).average();
      if ((minDepth <= 0 && Math.max(Math.max(detail.red, detail.green), detail.blue) <= 0) || maxDepth <= 0) {
        
        RGBColor average = image.average();
        int imgR = average.red;
        int imgG = average.green;
        int imgB = average.blue;
        
        imageTiles.add(new Tile(x, y, imgR, imgG, imgB, image.width));
        return;
      }
    } catch (IOException e) {
      return;
    }

    int halfImgLength = image.width/2;

    /* Pattern:
     * A B
     * C D
     */
    
    MakeSquares(image.trimRight(halfImgLength).trimBottom(halfImgLength), minDepth - 1, maxDepth - 1, x, y);
    MakeSquares(image.trimLeft(halfImgLength).trimBottom(halfImgLength), minDepth - 1, maxDepth - 1, x + halfImgLength - 1, y);
    MakeSquares(image.trimRight(halfImgLength).trimTop(halfImgLength), minDepth - 1, maxDepth - 1, x, y + halfImgLength - 1);
    MakeSquares(image.trimLeft(halfImgLength).trimTop(halfImgLength), minDepth - 1, maxDepth - 1, x + halfImgLength - 1, y + halfImgLength - 1);
    
    
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