package simulator;
import java.util.List;

import simulator.Task;

public class TaskList 
{
	List<Task> taskQ;

	public void addTask(Task t)
	{
		this.taskQ.add(t);
	}
}
