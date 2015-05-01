package simulator;

public class NQueensTask 
{
	private int n;
	private double alpha;
	private double time;
	private int cores = 1;
	
	public int getCores() {
		return cores;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public double getAlpha() {
		return alpha;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	public double getTime()
	{
		return this.time;
	}
	public double getTime(int n, double alpha) 
	{		
		this.time = n * alpha; 
		return this.time;
	}	
	public void setTime(double time)
	{
		this.time = time;
	}
}
