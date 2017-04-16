import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import org.apache.commons.io.*;
/**
 * @author albertchan
 *
 */
public class conversionRun{
		
	
	public static void main(String [] args) throws IOException {
		//pick one poem from folders.
		File poemFolder = new File("poemsList");
		File[] poemsList = poemFolder.listFiles();
		File poem = poemsList[new Random().nextInt(poemsList.length)];
		while (!FilenameUtils.getExtension(poem.getName()).equalsIgnoreCase("txt")) {
			poem = poemsList[new Random().nextInt(poemsList.length)];
		}
		
		//create log.txt
		new File ("result/" + FilenameUtils.removeExtension(poem.getName())).mkdir();
		File logFile = new File ("result/" + FilenameUtils.removeExtension(poem.getName()) + "/log.txt");
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		BufferedWriter logBW = new BufferedWriter (new FileWriter(logFile));
		
		//Create SWN Object
		SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode();
		sentiwordnet.deserializeDictionary(null);
		
		//create and use ScoreKeeper
		ScoreKeeper sk = new ScoreKeeper();
		
		sk.readScore(poem, sentiwordnet);
		
		//log keeping
		BufferedReader br = new BufferedReader(new FileReader(poem));
		String line; int count = 0; double scoreSum = 0.0;
		while ((line = br.readLine()) != null) {
			if(!line.isEmpty() && count < sk.getScorePerLine().size()) {
				logBW.write(line + ": " + String.valueOf(sk.getScorePerLine().get(count)));
				logBW.newLine();
				scoreSum += sk.getScorePerLine().get(count).doubleValue();
				count++;
			} else if (count >= sk.getScorePerLine().size())
				logBW.write("weird line counts");
		}
		logBW.write("<<overall>>: " + String.valueOf(scoreSum));
		
		
		//choose list of image
		ArrayList<ImgBuff> imgList = ImgBuffList.getImgListFromScore("positive", "negative", sk.getScorePerLine());
		
		//show what img is in used and put them in /process/
		new File ("process/" + FilenameUtils.removeExtension(poem.getName())).mkdir();
		for (int i=0; i<imgList.size(); i++){ 
			File processVisual = new File ("process/" + FilenameUtils.removeExtension(poem.getName())+ "/" + String.valueOf(i) + ".png");
			ImageIO.write(imgList.get(i).getImage(), "png", processVisual);
		}
		ImgBuff resultImage;
		if (scoreSum>=0.0) {
			resultImage = ImgBuffProcess.averagerV2(imgList); //this version of averaging tends to give brighter color.
		} else {
			resultImage = ImgBuffProcess.averager(imgList);  //simple averaging tends to give darker color.
		}
		
		
		File outputFile = new File ("result/" + FilenameUtils.removeExtension(poem.getName())+"/result.png");
		ImageIO.write(resultImage.getImage(), "png", outputFile);
		logBW.close();
		br.close();
	}
}
