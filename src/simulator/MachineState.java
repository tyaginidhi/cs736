package simulator;

public class MachineState 
{
	private long id;
	private int cores;
	private double cost;
	private long diskSize;
	private long nwBandwidth;
	private long memory;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCores() {
		return cores;
	}
	public void setCores(int cores) {
		this.cores = cores;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public long getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(long diskSize) {
		this.diskSize = diskSize;
	}
	public long getNwBandwidth() {
		return nwBandwidth;
	}
	public void setNwBandwidth(long nwBandwidth) {
		this.nwBandwidth = nwBandwidth;
	}
	public long getMemory() {
		return memory;
	}
	public void setMemory(long memory) {
		this.memory = memory;
	}
}
