package no.uib.info381.knn.visualizer;
import java.util.ArrayList;
import java.util.Iterator;

public class DataPoints implements Iterable<DataPoint>
{
	/***
	 * a list of all the points
	 */
	private ArrayList<DataPoint> m_Points;
	
	/***
	 * the maximum x,y coordinates, these values dont shrink automatically
	 * when points are removed
	 * todo: maybe add an autofit method to resize
	 */
	private double m_maxX;
	private double m_maxY;
	
	/***
	 * minimum coordinates, equivalently to maximum, does not update
	 */
	private double m_minX;
	private double m_minY;
	
	/***
	 * constructor
	 */
	public DataPoints()
	{
		super();
		this.m_Points = new ArrayList<>();
	}
	
	/***
	 * add a point to the collection
	 * @param o - group of data (what type/class the data point belongs to)
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public void addPoint(double x, double y)
	{
		this.addPoint(new DataPoint(x, y));
	}
	
	/***
	 * add data point object instead of coordinates
	 * @param p
	 */
	public void addPoint(DataPoint p)
	{
		if (p==null) return;

		if (this.isEmpty())
		{
			this.m_maxX = p.getX();
			this.m_maxY = p.getY();
			this.m_minX = p.getX();
			this.m_minY = p.getY();
		} else
		{
			if (p.getX()>this.m_maxX) this.m_maxX = p.getX();
			if (p.getY()>this.m_maxY) this.m_maxY = p.getY();
			if (p.getX()<this.m_minX) this.m_minX = p.getX();
			if (p.getY()<this.m_minY) this.m_minY = p.getY();
		}
		this.m_Points.add(p);
	}
	
	/***
	 *  get methods for maxX and maxY
	 * @return
	 */
	public double getMaxX()
	{
		return this.m_maxX;
	}
	
	public double getMaxY()
	{
		return this.m_maxY;
	}
	
	/***
	 *  get methods for minX and minY
	 * @return
	 */
	public double getMinX()
	{
		return this.m_minX;
	}
	
	public double getMinY()
	{
		return this.m_minY;
	}
	
	/***
	 * remove a specific point
	 * @param p - point object to be removed
	 */
	protected void removePoint(DataPoint p)
	{
		this.m_Points.remove(p);
	}
	
	/***
	 * is this set empty?
	 * @return
	 */
	public boolean isEmpty()
	{
		return this.m_Points.isEmpty();
	}

	/***
	 * iterator
	 */
	@Override
	public Iterator<DataPoint> iterator() {
		return this.m_Points.iterator();
	}
}
