package simulator;
import simulator.MachineState;

public class SubmittedTask 
{
	private long id;
	private String name;
	private boolean state;
	private double startTime;
	private double scheduledTime;
	private double estimatedTime;
	private MachineState macState = new MachineState(); // machine resources as being utilized by this task
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public MachineState getMacState() {
		return macState;
	}
	public double getStartTime() {
		return startTime;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	public double getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(double scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	public double getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public void setMacState(MachineState macState) {
		this.macState = macState;
	}
}
