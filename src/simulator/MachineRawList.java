package simulator;
import java.util.List;

import simulator.MachineRaw;

public class MachineRawList 
{
	List<MachineRaw> macList;
	
	public void addMachine(MachineRaw m)
	{
		this.macList.add(m);
	}
}