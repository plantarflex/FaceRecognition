package ClusterPLZ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.common.distance.TanimotoDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

public class ClusterManager {
	
	List<Vector> dataSet;
	List<double[]> dataDouble;
	private double[] mean;
	private double[] deviation;
	List<List<Cluster>> results;
	List<Vector> representatives;
	public String inputPath;
	boolean isnormalized;
	double normalizationConstant;
	
	public ClusterManager(String _inputPath){
		this.inputPath = _inputPath;
		this.isnormalized = false;
	}
	
	public void setVectors(String inputPath) throws IOException{
		this.dataSet = new ArrayList<Vector>();
		this.dataDouble = new ArrayList<double[]>();
		InputStream in=new FileInputStream(inputPath);	      
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		String row;
		String[] temp1 = null;
		String[] temp2 = null;
		double[] temp3 = {0,0,0,0,0,0};
		double[] temp4 = {0,0,0,0,0,0};
		double[] meantemp = {0,0,0,0,0,0};
		mean = meantemp;
		double[] deviationtemp = {0,0,0,0,0,0};
		deviation = deviationtemp;
		double[] squareSum = {0,0,0,0,0,0};
		
		while ((row = bf.readLine()) != null) {
			temp1 = row.split("e");
			for(int i=0; i<temp1.length; i++){   
				temp2 =  temp1[i].split(" ");
				
				for(int j=0; j<temp2.length; j++){
					temp3[j] = Double.parseDouble(temp2[j]);
					mean[j] += temp3[j];
					squareSum[j] += temp3[j]*temp3[j];
				}
				dataDouble.add(new double[]{temp3[0],temp3[1],temp3[2],temp3[3],temp3[4],temp3[5]});
				//-------------------choose first four index------------
				
				//-----------------------------------------------------
				Vector v = new DenseVector(temp3);
				dataSet.add(v);
			}
		}
		for(int i=0; i<mean.length; i++){
			mean[i] /= (double)(temp1.length);
			deviation[i] = sqrt(squareSum[i]/(double)(temp1.length) - mean[i]*mean[i]);  
		}
		bf.close();
		if(isnormalized == true) this.normalizeData(normalizationConstant);
	}
	
	public void normalizeData(double newDeviation){
		for(double[] i : dataDouble){
			for(int j=0; j<mean.length; j++) {
				i[j] -= mean[j];
				i[j] /= deviation[j];
				i[j] *= newDeviation;
			}
		}
		int k=0;
		for(Vector i : dataSet){
			for(int j=0; j<i.size(); j++){
				i.set(j, dataDouble.get(k)[j]);
			}
			k++;
		}
		this.isnormalized = true;
		this.normalizationConstant = newDeviation;
	}
	
	public void setMean(){
		double[] meantemp = {0,0,0,0,0,0};
		mean = meantemp;
		for(double[] i : dataDouble){
			for(int j=0; j<mean.length; j++) mean[j] += i[j];
		}
		for(int j=0; j<mean.length; j++) mean[j] /= (double)(dataDouble.size());
	}
	
	public void setDeviation(){
		double[] squareSum = {0,0,0,0,0,0};
		
		for(double[] i : dataDouble){
			squareSum[0] += i[0]*i[0];
			squareSum[1] += i[1]*i[1];
			squareSum[2] += i[2]*i[2];
			squareSum[3] += i[3]*i[3];
			squareSum[4] += i[4]*i[4];
			squareSum[5] += i[5]*i[5];
		}
		for(int i=0; i<squareSum.length; i++){
			deviation[i] = sqrt(squareSum[i]/(double)(dataDouble.size())-mean[i]*mean[i]);
		}
	}
	
	public void printMean(){
		System.out.println("MEAN VALUE : {"
				+mean[0]+", "
				+mean[1]+", "
				+mean[2]+", "
				+mean[3]+", "
				+mean[4]+", "
				+mean[5]+"}");
	}
	
	public void printDeviation(){
		System.out.println("DEVIATION : {"
				+deviation[0]+", "
				+deviation[1]+", "
				+deviation[2]+", "
				+deviation[3]+", "
				+deviation[4]+", "
				+deviation[5]+"}");
	}
	
	public void printPoints(){
		System.out.println(dataSet.size());
		for(Vector i : dataSet){
			System.out.println(i);
		}
	}
	

	public void printDistance(int DistanceMeasure) throws IOException{
		int size = (dataSet.size()+1)*(dataSet.size()/2);
		System.out.println(dataSet.size());
		System.out.println(size);
		double[] dis = new double[size];
		int index=0;
		for(int i=0; i<dataSet.size(); i++){
			for(int j=i+1; j<dataSet.size(); j++){
				
				if(1==DistanceMeasure){
					EuclideanDistanceMeasure d = new EuclideanDistanceMeasure();
					dis[index] = d.distance(dataSet.get(i),dataSet.get(j));
				}else if (2==DistanceMeasure){
					SquaredEuclideanDistanceMeasure d = new SquaredEuclideanDistanceMeasure();
					dis[index] = d.distance(dataSet.get(i),dataSet.get(j));
				}else if (3==DistanceMeasure){
					ManhattanDistanceMeasure d = new ManhattanDistanceMeasure();
					dis[index] = d.distance(dataSet.get(i),dataSet.get(j));

				}else{
					TanimotoDistanceMeasure d = new TanimotoDistanceMeasure();
					dis[index] = d.distance(dataSet.get(i),dataSet.get(j));

				}
			index++;
			}
		}
		String kind = "";
		if(1==DistanceMeasure){
			kind = "Euclidean";
		}else if (2==DistanceMeasure){
			kind = "Squared";
		}else if (3==DistanceMeasure){
			kind = "Manhattan";

		}else{
			kind = "Tanimoto";

		}
		BufferedWriter bw = new BufferedWriter(
				new FileWriter("/Users/Administrator/Desktop/distance/"+kind+".txt"));
		for(double a : dis){
			bw.write(String.valueOf(a));
			bw.newLine();
		}
		bw.close();
		System.out.println("total : "+index+ "Calculation");
		for(int i=0;i<dis.length;i++){
			
		}
	}
	
	
}
