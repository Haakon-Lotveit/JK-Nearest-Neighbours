package no.uib.info381.knn.visualizer;

/***
 * class for a data point
 * @author Madlion
 *
 */
public class DataPoint
{
	private double x;
	private double y;
	
	/***
	 * constructor of a datapoint
	 * @param o - data object
	 * @param vx - x coordinate
	 * @param vy - y coordinate
	 */
	public DataPoint(double vx, double vy)
	{
		this.x = vx;
		this.y = vy;
	}
	
	public double getX()
	{
		return this.x;
	}
	
	public double getY()
	{
		return this.y;
	}
}