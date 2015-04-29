package simulator;
import java.util.Queue;
import simulator.SubmittedTask;

public class SchedulerEntry 
{
	Queue<SubmittedTask> submitQ;
	MachineState state;
	public Queue<SubmittedTask> getSubmitQ() {
		return submitQ;
	}
	public void setSubmitQ(Queue<SubmittedTask> submitQ) {
		this.submitQ = submitQ;
	}
	public MachineState getState() {
		return state;
	}
	public void setState(MachineState state) {
		this.state = state;
	}	
}
