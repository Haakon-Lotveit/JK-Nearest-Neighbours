package no.uib.info381.knn.visualizer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import no.uib.info381.knn.KNN;
import no.uib.info381.knn.dataloaders.CSVData;

public class KNNRenderer extends Renderer{
	
	/***
	 * grid size of the visualizer
	 */
	private int m_GridSize;
	
	/***
	 * size of k for kNN
	 */
	private int m_kSize;
	
	/***
	 * KNN classifier object
	 */
	private KNN m_Classifier;
	
	/***
	 * point renderer used to match KNN rendering with
	 */
	private PointRenderer m_PointRenderer;
	
	public KNNRenderer(PointRenderer renderer)
	{
		this.m_GridSize = 5;
		this.m_PointRenderer = renderer;
		this.m_kSize = 1;
	}
	
	/***
	 * sets k parameter for kNN, if illegal k, then do nothing
	 * @param k
	 */
	public void setK(int k)
	{
		if (k<1) return;
		this.m_kSize = k;
	}
	
	public void setGridSize(int size)
	{
		this.m_GridSize = size;
	}
	
	/***
	 * initialize classifier
	 */
	private void initCLassifier()
	{
		List<CSVData> data = DataHelper.getCSVDataFromPointRenderer(m_PointRenderer);
		this.m_Classifier = new KNN(data);
	}

	@Override
	protected void draw(Graphics2D g) {
		this.initCLassifier();
		
		// set matching rendering margin
		// this actually overrides any custom margin
		this.setBottomMargin(this.m_PointRenderer.getBottomMargin());
		this.setLeftMargin(this.m_PointRenderer.getLeftMargin());
		this.setRightMargin(this.m_PointRenderer.getRightMargin());
		this.setTopMargin(this.m_PointRenderer.getTopMargin());
		
		int w = this.getInnerWidth();
		int h = this.getInnerHeight();
		
		int gridSize = this.m_GridSize;
		
		// save the current AA rendering settings
		Object AA = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		Object tAA = g.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
		
		/***
		 * use low rendering settings for performance
		 */
		g.setRenderingHint(
    	    RenderingHints.KEY_ANTIALIASING,
    	    RenderingHints.VALUE_ANTIALIAS_OFF);
    	g.setRenderingHint(
    	    RenderingHints.KEY_TEXT_ANTIALIASING,
    	    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
    	/***
    	 * visualize nearest neighbor points
    	 */
    	double knnWMultiplier = (this.m_PointRenderer.getMaxX()-this.m_PointRenderer.getMinX())/w;
    	double knnHMultiplier = (this.m_PointRenderer.getMaxY()-this.m_PointRenderer.getMinY())/h;
		g.setColor(new Color(0x303030));
		for (int x=0; x*gridSize<w; x++)
		{
			int X = x*gridSize;
			double knnX = (double)X*knnWMultiplier+this.m_PointRenderer.getMinX();
			
			for (int y=0; y*gridSize<h; y++)
			{
				int Y = y*gridSize;
				double knnY = (double)Y*knnHMultiplier+this.m_PointRenderer.getMinY();
				Y = Y*-1+h;
				
				
				/***
				 * do KNN classifier on the current point
				 */
				CSVData point= new CSVData(new String[]{""+knnX,""+knnY,""},2);
				String group = this.m_Classifier.classify(point, this.m_kSize);
				Color pColor = this.m_PointRenderer.getGroupColor(group);
				if (pColor==null) pColor = Color.black;
				
				g.setColor(pColor);
				g.drawLine(this.rX(X), this.rY(Y), this.rX(X), this.rY(Y));
				g.drawLine(this.rX(X)+2, this.rY(Y), this.rX(X)+2, this.rY(Y));
				g.drawLine(this.rX(X)-2, this.rY(Y), this.rX(X)-2, this.rY(Y));
				g.drawLine(this.rX(X), this.rY(Y)+2, this.rX(X), this.rY(Y)+2);
				g.drawLine(this.rX(X), this.rY(Y)-2, this.rX(X), this.rY(Y)-2);
				//g.drawString(String.format("%.1f",knnY), this.rX(X), this.rY(Y));
			}
		}
		
		/***
		 * reset to previous rendering settings
		 */
		g.setRenderingHint(
    	    RenderingHints.KEY_ANTIALIASING,
    	    AA);
    	g.setRenderingHint(
    	    RenderingHints.KEY_TEXT_ANTIALIASING,
    	    tAA);
	}
	

}
