package no.uib.info381.knn.visualizer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public abstract class Renderer{

	/***
	 * margin of render area
	 */
	private int m_leftMargin;
	private int m_rightMargin;
	private int m_topMargin;
	private int m_bottomMargin;
	
	private JPanel m_Area;
	
	public Renderer()
	{
		// set margins
		this.m_leftMargin =0;
		this.m_rightMargin =0;
		this.m_topMargin =0;
		this.m_bottomMargin =0;
		this.m_Area = null;
	}
	
	/***
	 * main rendering method method
	 */
	public void render(Graphics g, JPanel area)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(
    	    RenderingHints.KEY_ANTIALIASING,
    	    RenderingHints.VALUE_ANTIALIAS_ON);
    	g2d.setRenderingHint(
    	    RenderingHints.KEY_TEXT_ANTIALIASING,
    	    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    	this.m_Area = area;

    	this.draw(g2d);
	}
	
	/***
	 * abstract method to implement, this method draw the graphics
	 * @param g - graphics object
	 * @param w - width of drawing area
	 * @param h - height of drawing area
	 */
	protected abstract void draw(Graphics2D g);
	
	/***
	 * get inner width inside margin
	 * @return
	 */
	public int getInnerWidth()
	{
		return this.getWidth()-(this.getLeftMargin()+this.getRightMargin());
	}
	
	/***
	 * get inner height inside margin
	 * @return
	 */
	public int getInnerHeight()
	{
		return this.getHeight()-(this.getTopMargin()+this.getBottomMargin());
	}
	
	/***
	 * get width
	 * @return
	 */
	public int getWidth()
	{
		if (m_Area==null) return -1;
		
        Dimension size = m_Area.getSize();
        Insets insets = m_Area.getInsets();
        int w = size.width - insets.left - insets.right;
        return w;
	}
	
	/***
	 * get height
	 * @return
	 */
	public int getHeight()
	{
		if (m_Area==null) return -1;
		
        Dimension size = m_Area.getSize();
        Insets insets = m_Area.getInsets();
        int h = size.height - insets.top - insets.bottom;
        return h;
	}
	
	
	/***
	 * Methods used to convert realtive coordinate to absolute coordinate
	 */
	public int rX(int x)
	{
		return this.m_leftMargin+x;
	}
	
	public int rY(int y)
	{
		return this.m_topMargin+y;
	}
	
	/***
	 * sets the render area margin
	 * @param margin
	 */
	public void setMargin(int margin)
	{
		setLeftMargin(margin);
		setTopMargin(margin);
		setRightMargin(margin);
		setBottomMargin(margin);
	}
	
		public void setLeftMargin(int margin)
		{
			this.m_leftMargin =margin;
		}
		public void setTopMargin(int margin)
		{
			this.m_topMargin =margin;
		}
		public void setBottomMargin(int margin)
		{
			this.m_bottomMargin =margin;
		}
		public void setRightMargin(int margin)
		{
			this.m_rightMargin =margin;
		}
		
	/***
	 * get methods
	 */
		public int getLeftMargin()
		{
			return this.m_leftMargin;
		}
		public int getTopMargin()
		{
			return this.m_topMargin;
		}
		public int getBottomMargin()
		{
			return this.m_bottomMargin;
		}
		public int getRightMargin()
		{
			return this.m_rightMargin;
		}
}
