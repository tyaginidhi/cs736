package simulator;
import java.util.LinkedList;
import java.util.Queue;

import simulator.SubmittedTask;

public class SchedulerEntry 
{
	private Queue<SubmittedTask> submitQ = new LinkedList<SubmittedTask>();
	private MachineState state = new MachineState();
	private double timeToScheduleNext;
		
	public double getTimeToScheduleNext() {
		return timeToScheduleNext;
	}
	public void setTimeToScheduleNext(double timeToScheduleNext) {
		this.timeToScheduleNext = timeToScheduleNext;
	}
	public void addSubmitTask(SubmittedTask t)
	{
		this.submitQ.add(t);
	}
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
