package no.uib.info381.knn.visualizer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import no.uib.info381.knn.convenience.Table;
import no.uib.info381.knn.convenience.TableHelper;
import no.uib.info381.knn.visualizer.PointVisualizer.RenderArea;

public final class VisualizerTest {
	public static void test() {
		// create renderer and visualizer
		PointRenderer pointRenderer = new PointRenderer();
		PointVisualizer visualizer = new PointVisualizer();
		RenderArea renderArea = visualizer.RenderArea();
		renderArea.setBackground(new Color(0x101010));
		renderArea.addRenderer(pointRenderer);
		
	
		Table dataset = TableHelper.ReadTableFromCSV("testdata/csv/assistdata.csv");
		dataset.print(13);
	
		// split the data into classifications so we can color different class differently
		TreeMap<String,ArrayList<Integer>> classifications = TableHelper.SplitAttribute(dataset, "KÖN");
		float colorDistribution = 1.0f / classifications.size();
		int i = 0;
		for (Entry<String,ArrayList<Integer>> dataclass: classifications.entrySet()) {
			ArrayList<Integer> subset = dataclass.getValue();
			String subsetName = dataclass.getKey();
			DataPoints dp = DataHelper.dataPointsFromTableAttributes(dataset, "VIKT", "ÅLDER", DataType.DATATYPE_CONTINUOUS, DataType.DATATYPE_CONTINUOUS, subset);
			pointRenderer.addDataPoints(subsetName, dp);
			pointRenderer.setGroupColor(subsetName, Color.getHSBColor(i*colorDistribution, 1.0f, 1.0f));
	
			// set darkgray color if the data is missing
			if (subsetName.isEmpty())
				pointRenderer.setGroupColor(subsetName, Color.DARK_GRAY);
			i++;
		}
	
		pointRenderer.setMargin(20);
		pointRenderer.setRightMargin(30);
		pointRenderer.setLeftMargin(50);
		pointRenderer.setBottomMargin(30);
		//pointRenderer.setFitX(false);
		//pointRenderer.setFitY(false);
		//pointRenderer.setDrawBorder(false);
		visualizer.setTitle("Point Visualizer");
		visualizer.Visualize();
	}
}
