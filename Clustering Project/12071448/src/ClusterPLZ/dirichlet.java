package ClusterPLZ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.dirichlet.DirichletClusterer;
import org.apache.mahout.clustering.dirichlet.models.GaussianClusterDistribution;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class dirichlet extends ClusterManager{
	List<Cluster[]> DCs;
	
	public dirichlet(String inputPath) throws IOException{
		super(inputPath);
		setVectors(inputPath);
	}
	
	public void setClusters(){
		List<Vector> sampleData = new ArrayList<Vector>();
		sampleData = dataSet;
		
		List<VectorWritable> points = new ArrayList<VectorWritable>();
		for (Vector sd : sampleData) {
			points.add(new VectorWritable(sd));
		}

		DirichletClusterer dc = new DirichletClusterer(points,
				new GaussianClusterDistribution(new VectorWritable(
						new DenseVector(2))), 1.0, 10, 2, 2);
		
		// generate Dirichlet Clusters
		DCs = dc.cluster(20);
		
		/*
		// print out
		for (Cluster cluster : DCs.get(DCs.size() - 1)) {
			System.out.println("Cluster id: " + cluster.getId() + " center: "
					+ cluster.getCenter().asFormatString());
		}
		*/
	}
	public void printRadius(){
		for(Cluster cluster : DCs.get(DCs.size() - 1)) {
			System.out.println("Cluster id: " + cluster.getId()
					+ " radius: " +
					cluster.getRadius().getLengthSquared());
		}
	}
	
	public void printNumOfPoints(){
		for(Cluster cluster : DCs.get(DCs.size() - 1)) {
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
		
		
		for(Cluster cluster : DCs.get(DCs.size()-1)){
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
