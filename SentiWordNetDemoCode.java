//    Copyright 2013 Petter T�nberg
//
//    This demo code has been kindly provided by Petter T�rnberg <pettert@chalmers.se>
//    for the SentiWordNet website.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

//Modified by: Albert Chan

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SentiWordNetDemoCode {

	private Map<String, Double> dictionary = null;
	
	/**Serialize the dictionary
	 * @param pathToSWN the name of object file. If null, then the file name is "SWN.ser"
	 */
	public void serializeDictionary (String pathToSWN) {
		String p;
		if (pathToSWN == null) {
			p = "SWN.ser";
		} else {
			p = pathToSWN;
		}
		try {
	         FileOutputStream fileOut =
	         new FileOutputStream(p);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this.dictionary);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in /tmp/employee.ser");
	      }catch(IOException i) {
	         i.printStackTrace();
	      }
	}
	
	/**Deserialize an existing dictionary
	 * @param pathToSWN path to existing dictionary object. If null, then it look for "SWN.ser"
	 */
	public void deserializeDictionary (String pathToSWN) {
		String p;
		if (pathToSWN == null) {
			p = "SWN.ser";
		} else {
			p = pathToSWN;
		}
		try {
	         FileInputStream fileIn = new FileInputStream(p);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         dictionary = (Map<String, Double>) in.readObject();
	         in.close();
	         fileIn.close();
	      }catch(IOException i) {
	         i.printStackTrace();
	      }catch(ClassNotFoundException c) {
	         System.out.println("SWN class not found");
	         c.printStackTrace();
	      }
	}

	
	/**Default Constructor
	 * 
	 */
	public SentiWordNetDemoCode() {
		super();
	}

	
	/**Build the SentiWordNet dictionary using existing Map object.
	 * @param dictionary the existing Map object
	 */
	public SentiWordNetDemoCode(Map<String, Double> dictionary) {
		super();
		this.dictionary = dictionary;
	}

	
	/**Build the SentiWordNet dictionary using known path to the .txt file of SentiWordNet.
	 * Written by Petter T�nberg
	 * @param pathToSWN path to the .txt file of SentiWordNet
	 * @throws IOException
	 */
	public SentiWordNetDemoCode(String pathToSWN) throws IOException {
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;

			String line;
			while ((line = csv.readLine()) != null) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: "
										+ lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#"
								+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,
								synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
					.entrySet()) {
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap
						.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csv != null) {
				csv.close();
			}
		}
	}

	/**Return a sentimental score based on SentiWordNet3.0
	 * @param word
	 * @param pos
	 * @return a double value score
	 */
	public double extract(String word, String pos) {
		return dictionary.get(word + "#" + pos);
	}
	
	
}