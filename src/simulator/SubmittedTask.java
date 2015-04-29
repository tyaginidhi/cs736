package simulator;
import simulator.MachineState;

public class SubmittedTask 
{
	long id;
	boolean state;
	long startTime;
	long scheduledTime;
	long estimatedTime;
	MachineState macState; // machine resources as being utilized by this task

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
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(long scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	public long getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(long estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public MachineState getMacState() {
		return macState;
	}
	public void setMacState(MachineState macState) {
		this.macState = macState;
	}
}
