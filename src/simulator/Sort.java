package simulator;

public class Sort {

	private int diskSize;
	private double bufferSize;
	private double time;
	public int getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(int diskSize) {
		this.diskSize = diskSize;
	}
	public double getBufferSize() {
		return bufferSize;
	}
	public void setBufferSize(double bufferSize) {
		this.bufferSize = bufferSize;
	}
	public double getTime(int diskSize, double bufferSize) {
		return time;
	}
	public double getTime()
	{
		return this.time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	
}
