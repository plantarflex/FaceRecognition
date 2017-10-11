package ClusterPLZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.clustering.canopy.CanopyClusterer;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.common.distance.TanimotoDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

public class canopy extends ClusterManager {
	List<Canopy> CCs;
	public canopy(String inputPath) throws IOException {
		super(inputPath);
		setVectors(inputPath);
	}

	public void setClusters(int distanceMeasure) throws IOException{
		List<Vector> sampleData = new ArrayList<Vector>();
		sampleData = dataSet;
		//System.out.println("Size of dataSet : "+dataSet.size());
		
		
		// generate Canopy Clusters
		if (1 == distanceMeasure){
			CCs = CanopyClusterer.createCanopies(
					sampleData, new EuclideanDistanceMeasure(), 3.0, 1.5);
		}else if (2 == distanceMeasure){
			CCs = CanopyClusterer.createCanopies(
					sampleData, new SquaredEuclideanDistanceMeasure(), 3.0, 1.5);
		}else if (3 == distanceMeasure){
			CCs = CanopyClusterer.createCanopies(
					sampleData, new ManhattanDistanceMeasure(), 3.0, 1.5);
		}else{
			CCs = CanopyClusterer.createCanopies(
					sampleData, new TanimotoDistanceMeasure(), 3.0, 1.5);
		}
		

		/*
		// print out
		for (Canopy canopy : CCs) {
			System.out.println("Canopy id: "+canopy.getId()
								+" center: "
								+canopy.getCenter().asFormatString());
		}
		*/
		setVectors(inputPath);
		//System.out.println("Size of dataSet after clustering : "+dataSet.size());
	}
	public void printRadius(){
		for (Canopy canopy : CCs) {
			System.out.println("Cluster id: " + canopy.getId()
					+ " radius: " +
					canopy.getRadius().getLengthSquared());
		}
	}
	public void printNumOfPoints(){
		for(Canopy canopy : CCs) {
			System.out.println("Cluster id: " + canopy.getId()
					+ " Number of Points: " +
					canopy.getNumPoints());
		}
	}
	
	public void findRepresentatives(){
		System.out.println("Size of dataSet : "+dataSet.size());
		EuclideanDistanceMeasure e = new EuclideanDistanceMeasure();
		List<Vector> temp = new ArrayList<Vector>();
		representatives = temp;
		double temp1 = 100.0;
		double temp2 = 100.0;
		
		for(Canopy canopy : CCs){
			Vector tempV = new DenseVector();
			
			for(Vector vector : dataSet){
				temp2 = e.distance(canopy.getCenter(), vector);
				if (temp2 < temp1){
					temp1 = temp2;
					tempV = vector;
				}
			}
		representatives.add(tempV);
		temp1 = 100;
		}
		
		for(Vector i : representatives){
			System.out.println(i);
		}
	}
}
