package no.uib.info381.knn.visualizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import no.uib.info381.knn.convenience.Table;
import no.uib.info381.knn.dataloaders.CSVData;

/***
 * Datahelper class contains methods to aid data stuffs
 * @author Madlion
 *
 */
public final class DataHelper {
	
	/***
	 * Create a list a CSVData by using point data from a point renderer
	 * @param renderer
	 * @return
	 */
	public static List<CSVData> getCSVDataFromPointRenderer(PointRenderer renderer)
	{
		TreeMap<String,DataPoints> pointsSet = renderer.getPointsSet();
		List<CSVData> dataobjects = new LinkedList<CSVData>();
		for (Entry<String,DataPoints> e: pointsSet.entrySet())
		{
			for (DataPoint p: e.getValue())
			{
				CSVData data = new CSVData(new String[]{""+p.getX(),""+p.getY(),e.getKey()}, 2);
				dataobjects.add(data);
			}
		}
		return dataobjects;
	}
	/***
	 * Shortcutmethod using attribute names
	 * @param data
	 * @param attrX - attribute to represent the X axis
	 * @param attrY - attribute to represent the Y axis
	 * @param attrXType - datatype of attribute X
	 * @param attrYType - datatype of attribute Y
	 * @return
	 */
	public static DataPoints dataPointsFromTableAttributes(Table data, String attrX, String attrY, DataType typeX, DataType typeY, ArrayList<Integer> rows)
	{
		int indexX = data.GetAttributeIndex(attrX);
		int indexY = data.GetAttributeIndex(attrY);
		return dataPointsFromTableAttributes(data,indexX,indexY,typeX,typeY, rows);
	}

	/***
	 * Method to create datapoints from 2d table attributes
	 * NOTE: datatypes are CONTINUOUS, NOMINAL or ORDINAL
	 * @param data
	 * @param attrX - attribute to represent the X axis
	 * @param attrY - attribute to represent the Y axis
	 * @param attrXType - datatype of attribute X
	 * @param attrYType - datatype of attribute Y
	 * @param indexes - rows to use as datapoints
	 * @return
	 */
	public static DataPoints dataPointsFromTableAttributes(Table data, int attrX, int attrY, DataType typeX, DataType typeY, ArrayList<Integer> rows)
	{	
		DataPoints dp = new DataPoints();

		// these are used for nominals
		double x=-1;
		double y=-1;
		TreeMap<String, Double> xNominals = new TreeMap<String, Double>();
		TreeMap<String, Double> yNominals = new TreeMap<String, Double>();
		
		// loop through each entry and get the proper attributes
		for (int row=0; row<data.RowSize(); row++)
		{
			String xData = data.GetCellContent(attrX, row);
			String yData = data.GetCellContent(attrY, row);
			if (xData!=null && yData!=null)
			{
				// skips if current data is empty
				if (xData.isEmpty()) continue;
				if (yData.isEmpty()) continue;

				
				// new datapoint coords
				double px =-1;
				double py =-1;

				// for x data
				switch (typeX)
				{
					// parse value as coordinate
					case DATATYPE_CONTINUOUS:
						px = Double.parseDouble(xData);
						break;
					// for each distinct value increase coordinate
					case DATATYPE_NOMINAL:
						if (!xNominals.containsKey(xData))
							xNominals.put(xData, ++x);
						px = xNominals.get(xData);
						break;
					// same as nominal
					case DATATYPE_ORDINAL:
						if (!xNominals.containsKey(xData))
							xNominals.put(xData, ++x);
						px = xNominals.get(xData);
						break;
				}
				
				// for y data
				switch (typeY)
				{
					// parse value as coordinate
					case DATATYPE_CONTINUOUS:
						py = Double.parseDouble(yData);
						break;
					// for each distinct value increase coordinate
					case DATATYPE_NOMINAL:
						if (!yNominals.containsKey(yData))
							yNominals.put(yData, ++y);
						py = yNominals.get(yData);
						break;
					// same as nominal
					case DATATYPE_ORDINAL:
						if (!yNominals.containsKey(yData))
							yNominals.put(yData, ++y);
						py = yNominals.get(yData);
						break;
				}
				if (px!=-1 && py!=-1)
				{
					// create only datapoints of the allowed indexes
					if (rows.contains(row) || rows == null)
						dp.addPoint(px,py);
				}
			}
		}
		return dp;
	}
}
