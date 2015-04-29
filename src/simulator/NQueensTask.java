package simulator;

public class NQueensTask 
{
	int n;
	double alpha;
	double time;
	
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
