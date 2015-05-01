package simulator;
import java.util.ArrayList;
import java.util.List;

import simulator.MachineRaw;

public class MachineRawList 
{
	private List<MachineRaw> macList = new ArrayList<MachineRaw>();
	
	public List<MachineRaw> getMacList() {
		return macList;
	}

	public void setMacList(List<MachineRaw> macList) {
		this.macList = macList;
	}
}