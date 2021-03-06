package no.uib.info381.knn.visualizer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

import no.uib.info381.knn.KNN;
import no.uib.info381.knn.convenience.Table;
import no.uib.info381.knn.convenience.TableHelper;
import no.uib.info381.knn.dataloaders.CSVData;
import no.uib.info381.knn.visualizer.PointVisualizer.RenderArea;

public final class VisualizerTest {
	public static void test() {
		PointVisualizer visualizer = new PointVisualizer();
		RenderArea renderArea = visualizer.RenderArea();
		renderArea.setBackground(new Color(0x101010));
		
		/***
		 * create point renderer
		 */
		PointRenderer pointRenderer = new PointRenderer();
		// initialize renderer, this must happen before adding points
		// else bugs may occur
		pointRenderer.setMargin(20);
		pointRenderer.setRightMargin(30);
		pointRenderer.setLeftMargin(50);
		pointRenderer.setBottomMargin(30);
		//pointRenderer.setFitX(false);
		//pointRenderer.setFitY(false);
		
		/***
		 * create knn renderer
		 */
		KNNRenderer knnrenderer = new KNNRenderer(pointRenderer);
		knnrenderer.setGridSize(4);
		knnrenderer.setK(15);

		/***
		 * add renderers to visualizer
		 */
		renderArea.addRenderer(knnrenderer);
		renderArea.addRenderer(pointRenderer);
		
	
		Table dataset = TableHelper.ReadTableFromCSV("testdata/csv/assistdata.csv");
		dataset.print(13);
	
		// split the data into classifications so we can color different class differently
		TreeMap<String,ArrayList<Integer>> classifications = TableHelper.SplitAttribute(dataset, "K�N");
		float colorDistribution = 1.0f / classifications.size();
		int i = 0;
		for (Entry<String,ArrayList<Integer>> dataclass: classifications.entrySet()) {
			ArrayList<Integer> subset = dataclass.getValue();
			String subsetName = dataclass.getKey();
			DataPoints dp = DataHelper.dataPointsFromTableAttributes(dataset, "�LDER", "L�NGD", DataType.DATATYPE_CONTINUOUS, DataType.DATATYPE_CONTINUOUS, subset);
			pointRenderer.addDataPoints(subsetName, dp);
			pointRenderer.setGroupColor(subsetName, Color.getHSBColor(i*colorDistribution, 1.0f, 1.0f));
	
			// set darkgray color if the data is missing
			if (subsetName.isEmpty())
				pointRenderer.setGroupColor(subsetName, Color.DARK_GRAY);
			i++;
		}
		
		
		 // fixed point test
		/*
		pointRenderer.addDataPoint("1", new DataPoint(10,10));
		pointRenderer.addDataPoint("2", new DataPoint(5,2));
		pointRenderer.addDataPoint("3", new DataPoint(4,0));
		pointRenderer.addDataPoint("1", new DataPoint(2,6));
		pointRenderer.addDataPoint("1", new DataPoint(8,2));
		pointRenderer.addDataPoint("1", new DataPoint(1,12));
		pointRenderer.addDataPoint("5", new DataPoint(2,8));
		pointRenderer.addDataPoint("8", new DataPoint(5,1));
		pointRenderer.addDataPoint("9", new DataPoint(7,0));
		pointRenderer.addDataPoint("10", new DataPoint(2,1));
		pointRenderer.addDataPoint("11", new DataPoint(3,9));
		pointRenderer.addDataPoint("12", new DataPoint(7,4));
		pointRenderer.addDataPoint("13", new DataPoint(9,6));
		pointRenderer.addDataPoint("14", new DataPoint(11,9));
		for (int i=1;i<=14;i++)
		{
			pointRenderer.setGroupColor(""+i, Color.getHSBColor(i*(1.0f/14), 1.0f, 1.0f));
		}*/
		
		
		 // random point test
		/*
		Random rgn = new Random();
		for (int i=0; i<5; i++)
		{
			pointRenderer.addDataPoint("a", new DataPoint(rgn.nextDouble(),rgn.nextDouble()));
		}
		pointRenderer.setGroupColor("a", Color.green);
		for (int i=0; i<5; i++)
		{
			pointRenderer.addDataPoint("b", new DataPoint(rgn.nextDouble(),rgn.nextDouble()));
		}
		pointRenderer.setGroupColor("b", Color.red);*/
		
		
		
		 // debug for neighbor distance visualization
		/*
		List<CSVData> pts = new ArrayList<>();
		KNN cl = new KNN(DataHelper.getCSVDataFromPointRenderer(pointRenderer));
		double x = 7; double y = 5;
		CSVData dtp = new CSVData(new String[]{""+x,""+y,""}, 2);
		String dtclass = cl.classify(dtp, 1, pts);
		pointRenderer.addDataPoint(dtclass, new DataPoint(x,y));
		
		for (CSVData dt: pts)
		{
			pointRenderer.addDataPoint("n", new DataPoint(dt.getAttribute(0),dt.getAttribute(1)));
			pointRenderer.setGroupColor("n", Color.green);
		}*/

		visualizer.setTitle("INFO381 kNN-Visualizer");
		visualizer.Visualize();
	}
}
