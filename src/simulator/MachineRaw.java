package simulator;

import java.util.Map;

public class MachineRaw 
{
	MachineState state;
	Map<String, Object> taskMap;
		
	public Map<String, Object> getTaskMap() {
		return taskMap;
	}
	public void setTaskMap(Map<String, Object> taskMap) {
		this.taskMap = taskMap;
	}
	public MachineState getState() {
		return state;
	}
	public void setState(MachineState state) {
		this.state = state;
	}
}
