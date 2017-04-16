import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * @author Albert Chan
 *
 */
public class ImgBuff {
	private BufferedImage image;
	private int width;
	private int height;
	
	/**Create a ImgBuff using an existing BufferedImage
	 * @param img
	 */
	public ImgBuff(BufferedImage img) {
		this.image = img;
		this.width=img.getWidth();
		this.height=img.getHeight();
	}
	
	/** Create a ImgBuff object that store:
	 * (1) BufferedImage Object that has Color TYPE_INT_RGB
	 * (2) width in int value
	 * (3) height in int value
	 * @param input File object that direct the image to be stored 
	 */
	public ImgBuff(File input) {
		try {
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();
			//System.out.println("original" + image.getRGB(0, 0));
			BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			img2.getGraphics().drawImage(image, 0, 0, null);
			image = img2;
			//System.out.println("RGB" + image.getRGB(0, 0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the image Object
	 */
	public BufferedImage getImage() {
		
		return image;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	
}
