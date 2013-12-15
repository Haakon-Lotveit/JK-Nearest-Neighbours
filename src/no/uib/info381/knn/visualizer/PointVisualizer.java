package no.uib.info381.knn.visualizer;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PointVisualizer extends JFrame
{
	private static final long serialVersionUID = -3958739702573563027L;
	
	/***
	 * data points to visualize
	 */
	private RenderArea m_RenderArea;
	
	/***
	 * constructor
	 */
	public PointVisualizer() {
		this.m_RenderArea = new RenderArea();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(this.m_RenderArea);
        setSize(800, 500);
        setLocationRelativeTo(null);
    }
	
	/***
	 * return the render area
	 * @return
	 */
	public RenderArea RenderArea()
	{
		return this.m_RenderArea;
	}
    
	/***
	 * show the visualizer window
	 */
    public void Visualize()
    {
		this.setVisible(true);
    }
    
    /***
     * This class defines render area and handles rendering
     * @author Madlion
     *
     */
    public class RenderArea extends JPanel
    {
		private static final long serialVersionUID = 2110890625319875059L;
		private ArrayList<Renderer> m_Renderers;
    	
    	public RenderArea()
    	{
    		super();
    		this.m_Renderers = new ArrayList<Renderer>();
    	}
    	
    	@Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Renderer r: this.m_Renderers)
            	r.render(g, this);
        }
    	
    	/***
    	 * add a new renderer
    	 * @param r
    	 */
    	public void addRenderer(Renderer r)
    	{
    		this.m_Renderers.add(r);
    	}
    }
}
