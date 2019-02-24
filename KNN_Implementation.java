package knn_algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/*
 * Knn algorithm implementation
 */
public class KNN_Implementation {

	// created collection lists for storing train and test datasets of labels as well as features

	private List<double[]> tr_features = new ArrayList<>();
	private List<String> tr_label = new ArrayList<>();

	private List<double[]> tst_features = new ArrayList<>();
	private List<String> tst_label = new ArrayList<>();
	public int Underweight_no,Normal_no,Obese_no;
	
	/* sc object for getting user's input from screen */
	Scanner sc = new Scanner(System.in);
	int kvalue = 1;
	int Metric = 0;
	int totallabels = 0;

	/* loading the train data and trying to extract features and label for training */
	
	void loadtrainData(String filename) throws NumberFormatException, IOException {

		File file = new File(filename);
		try {
			BufferedReader readFile = new BufferedReader(new FileReader(file));
			String line;
			while ((line = readFile.readLine()) != null) {
				String[] split = line.split(",");
				double[] feature = new double[split.length - 1];
				for (int i = 0; i < split.length - 1; i++)
					feature[i] = Double.parseDouble(split[i]);
				tr_features.add(feature);
				tr_label.add(split[feature.length]);
			}
			readFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//now loading the test data and trying to extract features and labels for testing 
	 
	void loadtestData(String testfilename) throws NumberFormatException, IOException {

		File testfile = new File(testfilename);

		try {
			BufferedReader tst_readFile = new BufferedReader(new FileReader(testfile));
			PrintWriter pw = new PrintWriter("RealTestLabel.txt");
			String testline;
			while ((testline = tst_readFile.readLine()) != null) {

				String[] split = testline.split(",");
				double[] feature = new double[split.length - 1];
				for (int i = 0; i < split.length - 1; i++)
					feature[i] = Double.parseDouble(split[i]);
				tst_features.add(feature);
				tst_label.add(split[feature.length]);
				// writing original label for test data to file and counting number of label.
				pw.println(split[feature.length]);
				totallabels++;

			}
			pw.close();
			tst_readFile.close();
		}

		catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

	}

	// calculating the distance
	
	void distanceCalcualte() throws IOException {

		if (Metric == 1) {
			euclideanDistance();
			// calling the accuracy method in order to show accuracy of our model.
			accuracy();
		}

		else if (Metric == 2) {
			manhattanDistance();
			accuracy();
		}

		else {
			// if user selects an invalid option this should throw an error message.
			System.out.println("Invalid Selection");
			getKandMetrics();
			distanceCalcualte();
		}
	}

// Call Euclidean method for calculating the distance and write the output to the txt file
	 
	
	@SuppressWarnings("unchecked")
	void euclideanDistance() throws FileNotFoundException {
		Underweight_no=Normal_no=Obese_no=0;
		Distance euclidean = new Distance();

		Iterator<double[]> testITR = tst_features.iterator();

		PrintWriter pw = new PrintWriter("PredictedEuclideanResult.txt");

		while (testITR.hasNext()) {
			double testF[] = testITR.next();
			Iterator<double[]> trainITR = tr_features.iterator();
			int noOfobject = 0;
			ArrayList<DistanceandFeatures> ts = new ArrayList<>();
			while (trainITR.hasNext()) {
				double trainF[] = trainITR.next();
				double dist = 0;
				dist = euclidean.getEuclideanDistance(trainF, testF);

				String trainFeat = tr_label.get(noOfobject);
				DistanceandFeatures DfObject = new DistanceandFeatures(dist, trainFeat);
				ts.add(DfObject);
				Collections.sort(ts);
				noOfobject++;
			     }

   // Counter to count the top predicted labels based on k value chosen
			 
			int flag = 0, Underweight = 0, Normal = 0, Obese = 0;

			while (flag < kvalue) {
				DistanceandFeatures s = ts.get(flag);
				String s1 = s.getLabel();
				if (s1.equals("Underweight"))
					Underweight++;
				else if (s1.equals("Normal"))
					Normal++;
				else if (s1.equals("Obese"))
					Obese++;
				flag++;
			}

// Now counting the labels and selecting the highest label count as prediction label
	// and  write to the output file
			 
			if (Underweight > Normal && Underweight > Obese) {
				pw.println("Underweight");
				Underweight_no+=1;

			} else if (Normal > Underweight && Normal > Obese) {
				pw.println("Normal");
				Normal_no+=1;
			}

			else if (Obese > Underweight && Obese > Normal) {
				pw.println("Obese");
				Obese_no+=1;
			}				
		}
		pw.close();
		System.out.println("Underweight=" + Underweight_no);
		System.out.println("Normal=" + Normal_no);
		System.out.println("Obese=" + Obese_no);
	}

// Manhattan Distance
	 // Calling the Manhattan method for calculating distance and writing output to file.
	
	@SuppressWarnings("unchecked")
	void manhattanDistance() throws FileNotFoundException {
		Underweight_no=Normal_no=Obese_no=0;
		Distance euclidean = new Distance();

		Iterator<double[]> testITR = tst_features.iterator();

		PrintWriter pw = new PrintWriter("PredictedManhattanResult.txt");

		while (testITR.hasNext()) {
			double testF[] = testITR.next();
			Iterator<double[]> trainITR = tr_features.iterator();
			int noOfobject = 0;
			ArrayList<DistanceandFeatures> ts = new ArrayList<>();
			while (trainITR.hasNext()) {
				double trainF[] = trainITR.next();
				double dist = 0;
				dist = euclidean.getManhattanDistance(trainF, testF);

				String trainFeat = tr_label.get(noOfobject);
				DistanceandFeatures DfObject = new DistanceandFeatures(dist, trainFeat);
				ts.add(DfObject);
				Collections.sort(ts);
				noOfobject++;

			}

//counting predicted label based on k value
			 			
			int flag = 0, Underweight = 0, Normal = 0, Obese = 0;

			while (flag < kvalue) {
				DistanceandFeatures s = ts.get(flag);
				String s1 = s.getLabel();
				if (s1.equals("Underweight"))
					Underweight++;
				else if (s1.equals("Normal"))
					Normal++;
				else if (s1.equals("Obese"))
					Obese++;
				flag++;

			}

//counting label and selecting highest label count as prediction label and
 // write to output file
			if (Underweight > Normal && Underweight > Obese) {
				pw.println("Underweight");
				Underweight_no+=1;

			} else if (Normal > Underweight && Normal > Obese) {
				pw.println("Normal");
				Normal_no+=1;
			}

			else if (Obese > Underweight && Obese > Normal) {
				pw.println("Obese");
				Obese_no+=1;	
			}
			
				
		}
		pw.close();
		System.out.println("Underweight=" + Underweight_no);
		System.out.println("Normal=" + Normal_no);
		System.out.println("Obese=" + Obese_no);		
	}

//Method to get K value and Distance metrics.
	
	void getKandMetrics() {

		System.out.println("Enter the K value of KNN ");
		kvalue = sc.nextInt();
		// Restricted k value less 50.
		if (kvalue > 50) {
			System.out.println("K Value is out of range.");
			getKandMetrics();
		} else {

			System.out.println(
					"Select below distance metric(1 or 2)\n1 Eucildean Distance Metrics\n2 Manhanttan Distance Metrics");
			Metric = sc.nextInt();

		}

	}

// Calculating accuracy of the model - How good the model predicts the values.
	
	void accuracy() throws IOException {
		int count = 0;
		File file = null;
		if (Metric == 1) {
			file = new File("PredictedEuclideanResult.txt");
		}

		else if (Metric == 2) {
			file = new File("PredictedManhattanResult.txt");

		}

		BufferedReader rf = new BufferedReader(new FileReader(file));
		BufferedReader label = new BufferedReader(new FileReader(new File("RealTestLabel.txt")));
		String s = rf.readLine();
		while (s != null) {
			String lab = label.readLine();
			if (s.equals(lab)) {

			} else {
				count++;
			}

			s = rf.readLine();
		}

		System.out.println("Accuracy: " + ((float) 100 - (((float) count / totallabels) * 100)) + "%");
		rf.close();
		label.close();
	}

}

