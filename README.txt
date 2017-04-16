Source Codes:
-conversionRun.java: the main method
-ImgBuff.java: class that hold and BufferedImage object and it’s width, height
-ImgBuffList.java: contain method to return a list of ImgBuff
-ImgBuffProcess.java: contain methods that generate a resulting ImgBuff from a ImgBuff List
-ScoreKeeper.java: keep a list of scores as well as method to read score
-SentiWordNetDemoCode.java: SentiWordNet’s example code for Java

Dependency:
library\commons-io-2.5.jar: for FileIO operations.
negative,positive\*.png: image sets for conversion.
SWN.ser: a serialized SentiWordNetDemoCode object
SentiWordNet_3.0.0_20130122.txt: data for SentiWordNet

Input:
poemsList\*.txt: poem sets for input.

Output:
process\ : output directory that show images used by the machine.
result\ : output directory for the resulting image and scores.

MISC:
conversion.jar: the Java runnable.