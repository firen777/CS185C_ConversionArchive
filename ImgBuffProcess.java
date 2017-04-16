import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;



public class ImgBuffProcess {
	
	/**Average the color value of a list of ImgBuff and produce a new ImgBuff 
	 * @param list the list of ImgBuff to be averaged
	 * @return a new ImgBuff
	 */
	public static ImgBuff averager (List<ImgBuff> list) {
		int maxWidth = 0;
		int maxHeight = 0;
		
		for (int i=0; i<list.size(); i++){ //set the max width, height in the list of ImgBuff
			maxWidth = (maxWidth > list.get(i).getWidth()) ? maxWidth : list.get(i).getWidth();		
			maxHeight = (maxHeight > list.get(i).getHeight()) ? maxHeight : list.get(i).getHeight();
		}
		
		int[] resultPixelsR = new int[maxWidth*maxHeight];
		int[] resultPixelsG = new int[maxWidth*maxHeight];
		int[] resultPixelsB = new int[maxWidth*maxHeight];
		
		for (int x=0; x<list.size(); x++){ //summing value
			int[] data1 = ((DataBufferInt)list.get(x).getImage().getRaster().getDataBuffer()).getData();
			for (int j=0; j<data1.length; j++) {
				resultPixelsR[j]+= (data1[j] >> 16 & 0xFF);
				resultPixelsG[j]+= (data1[j] >> 8  & 0xFF);
				resultPixelsB[j]+= (data1[j]       & 0xFF);
			}
		}
		
		for (int i=0; i<resultPixelsR.length; i++){ //averaging value
			resultPixelsR[i]/=list.size();
			resultPixelsG[i]/=list.size();
			resultPixelsB[i]/=list.size();
		}
		
		BufferedImage img = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
		
		
		for (int i=0; i<maxWidth; i++){
			for (int j=0; j<maxHeight; j++) {
				int resultPixel = (resultPixelsR[i + maxWidth*j] << 16)|
								  (resultPixelsG[i + maxWidth*j] <<  8)|
								  (resultPixelsB[i + maxWidth*j]);
				img.setRGB(i, j, resultPixel);
			}
		}
		
		return (new ImgBuff(img));
	}
	
	/**Average the color value of a list of ImgBuff and produce a new ImgBuff
	 * averagerV2 used the approach where resultColor = sqrt( sum(Color^2) / n ) 
	 * instead of resultColor = sum(Color)/n.
	 * 
	 * See reference: https://www.youtube.com/watch?v=LKnqECcg6Gw
	 * @param list the list of ImgBuff to be averaged
	 * @return a new ImgBuff
	 */
	public static ImgBuff averagerV2 (List<ImgBuff> list) {
		int maxWidth = 0;
		int maxHeight = 0;
		
		for (int i=0; i<list.size(); i++){ //set the max width, height in the list of ImgBuff
			maxWidth = (maxWidth > list.get(i).getWidth()) ? maxWidth : list.get(i).getWidth();		
			maxHeight = (maxHeight > list.get(i).getHeight()) ? maxHeight : list.get(i).getHeight();
		}
		int[] resultPixelsR2 = new int[maxWidth*maxHeight];
		int[] resultPixelsG2 = new int[maxWidth*maxHeight];
		int[] resultPixelsB2 = new int[maxWidth*maxHeight];
		
		for (int x=0; x<list.size(); x++){ //summing value
			int[] data1 = ((DataBufferInt)list.get(x).getImage().getRaster().getDataBuffer()).getData();
			for (int j=0; j<data1.length; j++) {
				resultPixelsR2[j]+= ((data1[j] >> 16 & 0xFF)*(data1[j] >> 16 & 0xFF));
				resultPixelsG2[j]+= ((data1[j] >>  8 & 0xFF)*(data1[j] >>  8 & 0xFF));
				resultPixelsB2[j]+= ((data1[j]       & 0xFF)*(data1[j]       & 0xFF));
			}
		}
		
		for (int i=0; i<resultPixelsR2.length; i++){ //averaging value
			resultPixelsR2[i]/=list.size();
			resultPixelsG2[i]/=list.size();
			resultPixelsB2[i]/=list.size();
			resultPixelsR2[i]=(int) Math.sqrt(resultPixelsR2[i]);
			resultPixelsG2[i]=(int) Math.sqrt(resultPixelsG2[i]);
			resultPixelsB2[i]=(int) Math.sqrt(resultPixelsB2[i]);
		}
		
		BufferedImage img = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
		
		
		for (int i=0; i<maxWidth; i++){
			for (int j=0; j<maxHeight; j++) {
				int resultPixel = (resultPixelsR2[i + maxWidth*j] << 16)|
								  (resultPixelsG2[i + maxWidth*j] <<  8)|
								  (resultPixelsB2[i + maxWidth*j]);
				img.setRGB(i, j, resultPixel);
			}
		}
		
		return (new ImgBuff(img));
	}
	
	

}
