package no.uib.info381.knn.visualizer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map.Entry;
import java.util.TreeMap;


/***
 * Renderer class for rendering datapoints
 * @author Madlion
 *
 */
public class PointRenderer extends Renderer{
	
	/***
	 * datapoint sets to be rendered
	 */
	private TreeMap<String, DataPoints> m_PointsSet;
	private TreeMap<String, Color> m_PointsSetColor;
	
	/***
	 * default color of points
	 */
	private Color m_Default;
	
	/***
	 * to draw or not to draw borders
	 */
	private boolean m_DrawBorder;
	
	/***
	 * draw X & Y axis or not
	 */
	private boolean m_DrawAxis;
	
	/***
	 * Booleans to decide if points should fit the axis (stretching) to fill the axis length
	 */
	private boolean m_FitX;
	private boolean m_FitY;
	
	/***
	 * This is used for storing max/min values of all the points
	 */
	private double m_MaxX;
	private double m_MaxY;
	private double m_MinX;
	private double m_MinY;
	
	/***
	 * axis labels used to give names to axis
	 */
	private TreeMap<Double,String> m_XLabels;
	private TreeMap<Double,String> m_YLabels;
	
	/***
	 * constructor with no parameters, default color is black
	 */
	public PointRenderer()
	{
		this(Color.black);
	}
	
	/***
	 * constructor with default color parameter
	 * @param dp
	 */
	public PointRenderer(Color defaultColor)
	{
		super();
		this.m_PointsSet = new TreeMap<String,DataPoints>();
		this.m_PointsSetColor = new TreeMap<String,Color>();
		this.m_Default = defaultColor;
		this.m_DrawBorder = true;
		this.m_FitX = true;
		this.m_FitY = true;
		this.m_XLabels = new TreeMap<Double,String>();
		this.m_YLabels = new TreeMap<Double,String>();
		this.m_DrawAxis = true;
		
		this.m_MaxX = 0;
		this.m_MaxY = 0;
		this.m_MinX = 0;
		this.m_MinY = 0;
	}

	/***
	 * return a clone (shallow copy) of the datapoints set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TreeMap<String, DataPoints> getPointsSet()
	{
		return (TreeMap<String, DataPoints>)this.m_PointsSet.clone();
	}
	
	/***
	 * get methods for max/min values of datapoints
	 * @return
	 */
	public double getMaxX() {return this.m_MaxX;}
	public double getMaxY() {return this.m_MaxY;}
	public double getMinX() {return this.m_MinX;}
	public double getMinY() {return this.m_MinY;}
	
	/***
	 * draw axis or not
	 * @param b
	 */
	public void setDrawAxis(boolean b)
	{
		this.m_DrawAxis = b;
	}
	
	/***
	 * sets the state of fit X and fit Y
	 * @param b
	 */
	public void setFitX(boolean b)
	{
		this.m_FitX = b;
	}
	public void setFitY(boolean b)
	{
		this.m_FitY = b;
	}
	
	/***
	 * add multiple labels to the axis labels, replacing existing ones
	 * @param labels
	 */
	public void addXLabels(TreeMap<Double,String> labels)
	{
		for (Entry<Double,String> e: labels.entrySet())
		{
			this.m_XLabels.put(e.getKey(), e.getValue());
		}
	}
	
	public void addYLabels(TreeMap<Double,String> labels)
	{
		for (Entry<Double,String> e: labels.entrySet())
		{
			this.m_YLabels.put(e.getKey(), e.getValue());
		}
	}
	
	/***
	 * add datapoints, if the group already exists, then new points will be added
	 * to this list (not replaced)
	 * @param group
	 * @param dp
	 */
	public void addDataPoints(String group, DataPoints dp)
	{
		if (dp==null || dp.isEmpty()) return;

		group = group.toLowerCase();
		if (!this.m_PointsSet.containsKey(group))
		{
			this.m_PointsSet.put(group, dp);
		} else
		{
			for (DataPoint p: dp)
				this.m_PointsSet.get(group).addPoint(p.getX(), p.getY());
		}
		
		// calculate min max values
		calculateMinMax();
	}
	
	private void calculateMinMax()
	{
		double maxX = 0, maxY = 0, minX = 0, minY = 0;
        boolean initMinMax = true;
        for (Entry<String, DataPoints> e: this.m_PointsSet.entrySet())
        {
        	DataPoints dp = e.getValue();
        	if (initMinMax)
        	{
        		maxX = dp.getMaxX();
        		maxY = dp.getMaxY();
        		minX = dp.getMinX();
        		minY = dp.getMinY();
        		initMinMax = false;
        	} else
        	{
	        	if (dp.getMaxX()>maxX) maxX = dp.getMaxX();
	        	if (dp.getMaxY()>maxY) maxY = dp.getMaxY();
	        	if (dp.getMinX()<minX) minX = dp.getMinX();
	        	if (dp.getMinY()<minY) minY = dp.getMinY();
        	}
        }
        
        // scale ratio to fit the points inside render area
        if (!this.m_FitX) minX = 0;
        if (!this.m_FitY) minY = 0;
        
        // store it for this renderer
        this.m_MaxX = maxX;
        this.m_MaxY = maxY;
        this.m_MinX = minX;
        this.m_MinY = minY;
	}
	
	/***
	 * add a single datapoint to a specific group identifier
	 * @param group
	 * @param dp
	 */
	public void addDataPoint(String group, DataPoint p)
	{
		group = group.toLowerCase();
		DataPoints dp = new DataPoints();
		dp.addPoint(p);
		this.addDataPoints(group, dp);
	}
	
	/***
	 * adds datapoints object to the list of datapoints objects with given group identifier
	 * if identifier does not exists, else replace the content
	 * @param group
	 * @param dp
	 */
	public void putDataPoints(String group, DataPoints dp)
	{
		if (dp==null || dp.isEmpty()) return;

		group = group.toLowerCase();
		this.m_PointsSet.put(group, dp);
	}
	
	/***
	 * sets default color
	 * @param c
	 */
	public void setDefaultColor(Color c)
	{
		this.m_Default = c;
	}
	
	/***
	 * enable/disable border drawing
	 * @param b
	 */
	public void setDrawBorder(boolean b)
	{
		this.m_DrawBorder = b;
	}
	
	/***
	 * sets color for certain group
	 * @param group
	 * @param c
	 */
	public void setGroupColor(String group, Color c)
	{
		group = group.toLowerCase();
		this.m_PointsSetColor.put(group, c);
	}
	
	/***
	 * get group color for a group name
	 * @param group
	 * @return
	 */
	public Color getGroupColor(String group)
	{
		group = group.toLowerCase();
		return this.m_PointsSetColor.get(group);
	}
	
	/***
	 * draw axis method
	 */
	private void drawAxis(Graphics2D g2d, double maxX, double minX, double maxY, double minY)
	{
        int w = this.getInnerWidth();
        int h = this.getInnerHeight();
		
    	int lnX = rX(0); // rX(0) is same as leftmargin
    	int lnY = rY(0);
        g2d.setColor(new Color(0xa0a0ff));
        int sections = 10;
        
        /***
         * render Y axis (alot of hack and slash code here)
         */
        lnX -= 5;
        g2d.drawLine(lnX, lnY, lnX, lnY+h);
        double deltaY = (maxY-minY)/sections;
        double deltaH = (double)h/sections;
        for (int i=0; i<=sections;i++)
        {
        	int y = (int)(i*deltaH);
        	g2d.drawLine(lnX, rY(y), lnX-3, rY(y));
        	String s = String.format("%.1f", (minY+i*deltaY));
        	// flips y axis for natural direction
        	y = y*-1+h;
        	g2d.drawString(s, lnX-6-(int)(s.length()*6.4), rY(y+4));
        }
        
        /***
         * render X axis
         */
        lnX = rX(0);
        lnY += 5;
        g2d.drawLine(lnX, lnY+h, lnX+w, lnY+h);
        double deltaX = (maxX-minX)/sections;
        double deltaW = (double)w/sections;
        for (int i=0; i<=sections;i++)
        {
        	int x = (int)(i*deltaW);
        	g2d.drawLine(rX(x), lnY+h, rX(x), lnY+h+3);
        	String s = String.format("%.1f", (minX+i*deltaX));
        	g2d.drawString(s, rX(x-8), lnY+15+h);
        }
	}

	/***
	 * Draw the graphics
	 * @param g
	 */
	public void draw(Graphics2D g) {
		if (this.m_PointsSet.isEmpty()) return;
		
        Graphics2D g2d = (Graphics2D) g;
        int pointSize = 4;
        
        // sets the width and height of rendering area (take in account margin)
        int w = this.getInnerWidth();
        int h = this.getInnerHeight();
        
        /***
         * rendering margin borders
         */
        if (this.m_DrawBorder)
        {
        	int lnX = rX(0); // rX(0) is same as leftmargin
        	int lnY = rY(0);
	        g2d.setColor(Color.DARK_GRAY);
	        g2d.drawLine(lnX, lnY, lnX, lnY+h);
	        g2d.drawLine(lnX, lnY+h, lnX+w, lnY+h);
	        g2d.drawLine(lnX+w, lnY+h, lnX+w, lnY);
	        g2d.drawLine(lnX+w, lnY, lnX, lnY);
        }
        
        /***
         * point rendering calculations
         */
        // finds the maximum & minimum rendering coordinates to fit points properly
        
        double  maxX = this.m_MaxX, 
        		maxY = this.m_MaxY, 
        		minX = this.m_MinX, 
        		minY = this.m_MinY;

        double wScale = (double)w/(maxX-minX);
        double hScale = (double)h/(maxY-minY);

        /***
         * rendering the points
         */
        for (Entry<String, DataPoints> e: this.m_PointsSet.entrySet())
        {
        	// sets the color (if defined) for points
        	if (this.m_PointsSetColor.containsKey(e.getKey()))
        	{
        		g2d.setColor(this.m_PointsSetColor.get(e.getKey()));
        	} else {
        		g2d.setColor(this.m_Default);
        	}
        	
        	// render points
        	DataPoints dp = e.getValue();
	        for (DataPoint p: dp)
	        {
	        	int x = (int)((p.getX()-minX)*wScale) - pointSize/2;
	        	int y = (int)((p.getY()-minY)*hScale);
	        	// flips the Y axis so we get natural visualization (zero at bottom)
	        	y = y*(-1)+h;
	        	// compensate for point size
	        	y = y - pointSize/2;
	        	g2d.fillOval(rX(x), rY(y), pointSize, pointSize);
	        }
        }
        
        /***
         * rendering axis
         */
        if (this.m_DrawAxis)
        	drawAxis(g2d, maxX, minX, maxY, minY);
	}
}
