package ClusterPLZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.common.distance.TanimotoDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

public class kmean extends ClusterManager{
	List<List<Cluster>> KMCs;
	
	public kmean(String inputPath) throws IOException {
		super(inputPath);
		setVectors(inputPath);
		List<List<Cluster>> KMCs = new ArrayList<List<Cluster>>();
		List<Vector> representatives = new ArrayList<Vector>();
	}
	
	public void setClusters(int k, int distanceMeasure) throws IOException{
		List<Vector> sampleData = new ArrayList<Vector>();
		sampleData = dataSet;
		
		List<Vector> randomPoints = RandomPointsUtil.chooseRandomPoints(
				sampleData, k);
		
		List<Cluster> clusters = new ArrayList<Cluster>();
		int clusterId = 0;
		for (Vector v : randomPoints) {
			clusters.add(new Cluster(v, clusterId++,
					new EuclideanDistanceMeasure()));
		}
		
		// generate K Mean Clusters
		if (1 == distanceMeasure){
			KMCs
			= KMeansClusterer.clusterPoints(sampleData, clusters,
					new EuclideanDistanceMeasure(), 3, 0.01);
		}else if (2 == distanceMeasure){
			KMCs
			= KMeansClusterer.clusterPoints(sampleData, clusters,
					new EuclideanDistanceMeasure(), 3, 0.01);
		}else if (3 == distanceMeasure){
			KMCs
			= KMeansClusterer.clusterPoints(sampleData, clusters,
					new ManhattanDistanceMeasure(), 3, 0.01);
		}else{
			KMCs
			= KMeansClusterer.clusterPoints(sampleData, clusters,
					new TanimotoDistanceMeasure(), 3, 0.01);
		}
				
		// EuclideanDistanceMeasure
		// SquaredEuclideanDistanceMeasure
		// ManhattanDistanceMeasure
		// TanimotoDistanceMeasure
		
		
		// print out
		for(Cluster cluster : KMCs.get(KMCs.size() - 1)) {
			System.out.println("Is converged? : "+cluster.isConverged());
			/*System.out.println("Cluster id: " + cluster.getId()
					+ " center: " +
					cluster.getCenter().asFormatString());
					*/
		}
		
	}
	public void printRadius(){
		for(Cluster cluster : KMCs.get(KMCs.size() - 1)) {
			System.out.println("Cluster id: " + cluster.getId()
					+ " radius: " +
					cluster.getRadius().getLengthSquared());
		}
	}
	
	public void printPointsInCluster(){
		EuclideanDistanceMeasure e = new EuclideanDistanceMeasure();
		int[] idNum={0,0,0};
		for(Vector i : dataSet){
			double temp1=10000;
			double temp2=10000;
			int id=200;
			for(Cluster cluster : KMCs.get(KMCs.size() - 1)) {
				temp2 = e.distance(i, cluster.getCenter());
				if( temp1 > temp2){
					temp1 = temp2;
					id = cluster.getId();
				}
			}
			idNum[id]++;
			System.out.println("ID : "+id+", vector = "+i.asFormatString());
		}
		for(int i=0; i<idNum.length; i++){
		System.out.println("Num of ID : "+i+"= "+idNum[i]);
		}
		
	}
	
	public void printNumOfPoints(){
		for(Cluster cluster : KMCs.get(KMCs.size() - 1)) {
			System.out.println("Cluster id: " + cluster.getId()
					+ " Number Of Points: " +
					cluster.getNumPoints());
		}
	}
	
	public void findRepresentatives(){
		EuclideanDistanceMeasure e = new EuclideanDistanceMeasure();
		List<Vector> temp = new ArrayList<Vector>();
		representatives = temp;
		double temp1 = 100.0;
		double temp2 = 100.0;
		
		
		for(Cluster cluster : KMCs.get(KMCs.size()-1)){
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
