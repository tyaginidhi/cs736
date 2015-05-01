package simulator;
import java.util.ArrayList;
import java.util.List;

import simulator.Task;

public class TaskList 
{
	private List<Task> taskQ = new ArrayList<Task>();

	public List<Task> getTaskQ() {
		return taskQ;
	}

	public void setTaskQ(List<Task> taskQ) {
		this.taskQ = taskQ;
	}
	
	public List<Task> getTaskQList()
	{
		return this.taskQ;
	}
}
