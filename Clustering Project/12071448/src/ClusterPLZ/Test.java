package ClusterPLZ;

import java.io.IOException;

import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.distance.TanimotoDistanceMeasure;

public class Test {

	public static void main(String[] args) throws IOException {
		String inputPath = "/Users/Administrator/Desktop/resultFinal.txt";
		
		// Construct each Clustering
		kmean k = new kmean(inputPath);
		fuzzykmean f = new fuzzykmean(inputPath);
		canopy c = new canopy(inputPath);
		dirichlet d = new dirichlet(inputPath);
		
		//k.printDistance(1);
		//k.printDistance(2);
		//k.printDistance(3);
		//k.printDistance(4);
		// Normalization before Clustering
		double deviationConstant = 10.0;
		k.normalizeData(deviationConstant);
		f.normalizeData(deviationConstant);
		c.normalizeData(deviationConstant);
		//d.normalizeData(deviationConstant);
				
		int numOfClusters = 2;
		int distanceMeasure = 1;
		
		if (1 == distanceMeasure){
			System.out.println("---------------------------------------------Euclidean DistanceMeasure--------------------------------------------------\n");
		}else if (2 == distanceMeasure){
			System.out.println("---------------------------------------Squared Euclidean DistanceMeasure------------------------------------------------\n");
		}else if (3 == distanceMeasure){
			System.out.println("---------------------------------------------Manhattan DistanceMeasure--------------------------------------------------\n");
		}else{
			System.out.println("---------------------------------------------Tanimoto DistanceMeasure---------------------------------------------------\n");
		}
		
		// Run CLustering
		System.out.println("---------K Mean Clustering---------");
		k.setClusters(numOfClusters, distanceMeasure);
		//k.printRadius();
		k.printNumOfPoints();
		//System.out.println("-------------------------------------------------K Mean Representative---------------------------------------------------");
		//k.findRepresentatives();
		
		
		System.out.println("------Fuzzy K Mean Clustering------");
		f.setClusters(numOfClusters, distanceMeasure);
		//f.printRadius();
		f.printNumOfPoints();
		//System.out.println("-------------------------------------------------Fuzzy K Mean Representative---------------------------------------------");
		//f.findRepresentatives();
		
		System.out.println("---------Canopy Clustering---------");
		c.setClusters(distanceMeasure);
		//c.printRadius();
		c.printNumOfPoints();
		//System.out.println("-------------------------------------------------Canopy Representative---------------------------------------------------");
		//c.findRepresentatives();
		
		
		System.out.println("--------Dirichlet Clustering-------");
		//d.setClusters();
		//d.printRadius();
		//d.printNumOfPoints();
		//System.out.println("-------------------------------------------------Dirichelt Representative------------------------------------------------");
		//d.findRepresentatives();
		
	}

}
