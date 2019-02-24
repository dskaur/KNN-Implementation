package knn_algorithm;

import java.io.IOException;
import java.util.Scanner;

public class KNN_main {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		System.out.println("Implementation of KNN algorthim ");
		Scanner sc=new Scanner(System.in);
		KNN_Implementation knn_algo=new KNN_Implementation();
		System.out.println("Enter training data file");
		String train_file=sc.nextLine();
		System.out.println("Enter test data file");
		String test_file=sc.nextLine();
		knn_algo.getKandMetrics();
		knn_algo.loadtrainData(train_file);
		knn_algo.loadtestData(test_file);
		knn_algo.distanceCalcualte();
		sc.close();
	}

}