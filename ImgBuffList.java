import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

/**
 * 
 */

/**
 * @author albertchan
 *
 */
public class ImgBuffList {
	/**Return an ArrayList of ImgBuff based on the scores
	 * @param posDirectory the Directory to positive image folder
	 * @param negDirectory the Directory to negative image folder
	 * @param scores the scores for picking images
	 * @return an ArrayList of ImgBuff
	 */
	public static ArrayList<ImgBuff> getImgListFromScore 
	(String posDirectory, String negDirectory, List<Double> scores) {
		File posFolder = new File(posDirectory);
		File negFolder = new File(negDirectory);
		File[] posImg = posFolder.listFiles();
		File[] negImg = negFolder.listFiles();
		
		ArrayList<ImgBuff> imgList = new ArrayList<ImgBuff>();
		
		for (int i=0; i<scores.size(); i++) {
			//assign image to imgList based on score
			if (scores.get(i).doubleValue() > 0.0) {
				//positive
				File posImg_ = posImg[(new Random().nextInt(posImg.length))];
				while (!FilenameUtils.getExtension(posImg_.getName()).equalsIgnoreCase("png")) {
					posImg_ = posImg[(new Random().nextInt(posImg.length))];
				}
				imgList.add(new ImgBuff(posImg_));
			} else if (scores.get(i).doubleValue() < 0.0) {
				//negative
				File negImg_ = negImg[(new Random().nextInt(negImg.length))];
				while (!FilenameUtils.getExtension(negImg_.getName()).equalsIgnoreCase("png")) {
					negImg_ = negImg[(new Random().nextInt(negImg.length))];
				}
				imgList.add(new ImgBuff(negImg_));
			} else {
				BufferedImage buffImg = new BufferedImage(750,750, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = buffImg.createGraphics();
				g2d.setColor(new Color(0x80, 0x80, 0x80));
				g2d.fillRect(0, 0, buffImg.getWidth(), buffImg.getHeight());
				imgList.add(new ImgBuff(buffImg));
			}  //neutral
		}
		return imgList;
		
	}
}
