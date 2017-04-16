import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author albertchan
 *
 */
public class ScoreKeeper {
	private List<Double> scorePerLine;
	
	/**Default constructor. Initialize a empty score List
	 * 
	 */
	public ScoreKeeper(){
		this.scorePerLine = new ArrayList<Double>();
	}
	
	/** get the Score List.
	 * @return ScorePerLine List
	 */
	public List<Double> getScorePerLine() {
		return scorePerLine;
	}

	/**read and store scores in List from poem
	 * @param poem the .txt File this method is going to read
	 * @param swn  SentiWordNet object that will be used for analysis tool
	 */
	public void readScore(File poem, SentiWordNetDemoCode swn) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(poem));
			String line;
			while ((line = br.readLine()) != null) {	//each line
				if(!line.isEmpty()) {
					double overAll = 0.0;
					double wordsCount = 0.0;//# of word in a sentence
					
					// space separation, ignore all punctuation.
					String[] data = line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
					
					for (String x : data) {  //each word
						double typeCount = 0.0; //# of type of such word
						double ad = 0.0; 		//adj score
						double vb = 0.0; 		//verb score
						double noun = 0.0;		//noun score
						double adv = 0.0;
						try { //adj
							ad += swn.extract(x, "a");
							typeCount += 1.0;	//such word belong to adj. +1 to type.
							System.out.println(x + "#a "+ad);
						} catch (NullPointerException e){ //not in the SentiWordNet
							//System.out.println(x + "#a not in Data");
						}
						//same for verb & noun
						try { //verb
							vb += swn.extract(x, "v");
							typeCount += 1.0;
							System.out.println(x + "#v "+ vb);
						} catch (NullPointerException e){
							//System.out.println(x + "#v not in Data");
						}
						try { //noun
							noun += swn.extract(x, "n");
							typeCount += 1.0;
							System.out.println(x + "#n "+noun);
						} catch (NullPointerException e){
							//System.out.println(x + "#n not in Data");
						}
						try { //adverb
							adv += swn.extract(x, "r");
							typeCount += 1.0;
							System.out.println(x + "#r "+adv);
						} catch (NullPointerException e){
							//System.out.println(x + "#n not in Data");
						}
						if (typeCount != 0.0) {
							overAll += (ad+vb+noun+adv)/typeCount;
						}
						wordsCount += 1.0;
					}
					overAll /= wordsCount;
					System.out.println("****"+ overAll + "****");
					System.out.println("//=============================================//");
					
					this.scorePerLine.add(overAll);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

}
