package ClusterPLZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.fuzzykmeans.FuzzyKMeansClusterer;
import org.apache.mahout.clustering.fuzzykmeans.SoftCluster;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.common.distance.TanimotoDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

public class fuzzykmean extends ClusterManager{
	List<List<SoftCluster>> FKMCs;
	
	public fuzzykmean(String inputPath) throws IOException {
		super(inputPath);
		setVectors(inputPath);
	}
	public void setClusters(int k,int distanceMeasure) throws IOException{
		List<Vector> sampleData = new ArrayList<Vector>();
		sampleData = dataSet;
		List<Vector> randomPoints = RandomPointsUtil.chooseRandomPoints(sampleData, k);
		List<SoftCluster> clusters = new ArrayList<SoftCluster>();
		
		int clusterId = 0;
		for (Vector v : randomPoints) {
			clusters.add(new SoftCluster(v, clusterId++, new EuclideanDistanceMeasure()));
		}
		
		// generate Fuzzy K Means Clusters
		if (1 == distanceMeasure){
			FKMCs
			= FuzzyKMeansClusterer.clusterPoints(sampleData, clusters, new EuclideanDistanceMeasure(), 0.01, 3, 10); 
		}else if (2 == distanceMeasure){
			FKMCs
			= FuzzyKMeansClusterer.clusterPoints(sampleData, clusters, new SquaredEuclideanDistanceMeasure(), 0.01, 3, 10); 
		}else if (3 == distanceMeasure){
			FKMCs
			= FuzzyKMeansClusterer.clusterPoints(sampleData, clusters, new ManhattanDistanceMeasure(), 0.01, 3, 10); 
		}else{
			FKMCs
			= FuzzyKMeansClusterer.clusterPoints(sampleData, clusters, new TanimotoDistanceMeasure(), 0.01, 3, 10); 
		}
		
		
		// print out
		for(SoftCluster cluster : FKMCs.get(FKMCs.size() - 1)) {
			System.out.println("Is converged? : "+cluster.isConverged());
			/*
			System.out.println("Fuzzy Cluster id: " 
								+ cluster.getId()
								+ " center: " 
								+ cluster.getCenter().asFormatString());
								*/
		}
		
	}
	public void printRadius(){
		for(Cluster cluster : FKMCs.get(FKMCs.size() - 1)) {
			System.out.println("Cluster id: " + cluster.getId()
					+ " radius: " +
					cluster.getRadius().getLengthSquared());
		}
	}
	
	public void printNumOfPoints(){
		for(Cluster cluster : FKMCs.get(FKMCs.size() - 1)) {
			System.out.println("Cluster id: " + cluster.getId()
					+ " Number of Points: " +
					cluster.getNumPoints());
		}
	}

	public void findRepresentatives(){
		EuclideanDistanceMeasure e = new EuclideanDistanceMeasure();
		List<Vector> temp = new ArrayList<Vector>();
		representatives = temp;
		double temp1 = 100.0;
		double temp2 = 100.0;
		
		
		for(Cluster cluster : FKMCs.get(FKMCs.size()-1)){
			Vector tempV = new DenseVector();
			
			for(Vector vector : dataSet){
				temp2 = e.distance(cluster.getCenter(), vector);
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
