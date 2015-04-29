package simulator;

public class Sort {

	int diskSize;
	double bufferSize;
	double time;
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
	public void setTime(double time) {
		this.time = time;
	}
	
}
