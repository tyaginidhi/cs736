package simulator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulator.SchedulerEntry;

public class Scheduler 
{
	public static void main(String args[])
	{
		List<SchedulerEntry> activeMachines  = new ArrayList<SchedulerEntry>();
		TaskList taskList = new TaskList();
		// reading the file containing a trace of incoming tasks
		// each row contains (start time, task name, parameters, cost, waiting time, id)
		// each field is semi-colon separated and parameters are separated by comma
		// cost & waiting time is separated by comma
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"));
		    String line;
		    String words[];
		    while ((line = reader.readLine()) != null)
		    {	
		      words = line.split(";");
		      Task t = new Task();
		      t.setStartTime(Long.parseLong(words[0].toString()));
		      t.setName(words[1]);
		      
		      String params[] = words[2].split(",");
		      List<Double> paramsList = new ArrayList<Double>();
		      for(int i = 0; i < params.length; i++)
		    	  paramsList.add(Double.parseDouble(params[i]));
		      t.setParameters(paramsList);
		      
		      String cost[] = words[3].split(",");
		      double costMetric[] = new double[3];
		      for(int i = 0; i < cost.length; i++)
		      {
		    	 costMetric[i] = Double.parseDouble(cost[i].toString());
		      }
		      t.setCost(costMetric);
		      
		      String waitingTime[] = words[4].split(",");
		      double waitingMetric[] = new double[3];
		      for(int i = 0; i < waitingTime.length; i++)
		      {
		    	 costMetric[i] = Double.parseDouble(waitingTime[i].toString());
		      }
		      t.setWaitingTime(waitingMetric);
		      
		      t.setId(Long.parseLong(words[5].toString()));
		      taskList.addTask(t);
		    }
		    reader.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		MachineRawList macList = new MachineRawList();
		// read the initial stats file of each machine in the cluster
		// each row separated by semi colon contains (macid, disk size, nw bw, RAM, cores, mac cost, params
		// each task is separated by comma and each task is split by colon
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("stats.txt"));
		    String line;
		    String words[];
		    while ((line = reader.readLine()) != null)
		    {	
			  words = line.split(";");
			  MachineRaw m = new MachineRaw();
			  MachineState ms = new MachineState();
			  ms.setId(Long.parseLong(words[0].toString()));
			  ms.setDiskSize(Long.parseLong(words[1].toString()));
			  ms.setNwBandwidth(Long.parseLong(words[2].toString()));
			  ms.setMemory(Long.parseLong(words[3].toString()));
			  ms.setCores(Integer.parseInt(words[4].toString()));
			  ms.setCost(Double.parseDouble(words[5].toString()));
			  m.setState(ms);
			  
			  String tasks[] = words[6].split(",");
			  Map<String, Object> taskMap = new HashMap<String, Object>();
			  for(int i = 0; i < tasks.length; i++)
			  {
				  String params[] = tasks[i].split(":");
				  if(params[0].equals("nqueens"))
				  {
					NQueensTask nqueens = new NQueensTask();
					nqueens.setAlpha(Double.parseDouble(params[1].toString()));
					taskMap.put(params[0], nqueens);
				  }
				  else if(params[0].equals("sort"))
				  {
					  Sort sort = new Sort();
					  sort.setBufferSize(Double.parseDouble(params[1].toString()));
					  taskMap.put(params[0], sort);
				  }
				  else if(params[0].equals("wc"))
				  {
					  WordCount wc = new WordCount();
					  wc.setMemSize(Double.parseDouble(params[1].toString()));
					  taskMap.put(params[0], wc);
				  }
				  else if(params[0].equals("iperf"))
				  {
					  iPerf ip = new iPerf();
					  taskMap.put(params[0], ip);
				  }
				  else if(params[0].equals("thread"))
				  {
					  MultiThreads mt = new MultiThreads();
					  taskMap.put(params[0], mt);
				  }
			  }
			  m.setTaskMap(taskMap);
			  macList.addMachine(m);
		    }
		    reader.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		//
		double global_time = 0;
		for(int i = 0; i < taskList.taskQ.size();  i++ )
		{
			Task t = taskList.taskQ.get(i);
			if(t.getStartTime() <= global_time)
			{
				String taskName = t.getName();
				MachineRawList validMacList = new MachineRawList();
				MachineRaw validMac = new MachineRaw();
				
				for(int j = 0; j < macList.macList.size(); j++)
				{
					double etc = 0.0;
					MachineRaw mr = macList.macList.get(j);
					if(taskName.equals("nqueens"))
					{
						int n = Integer.parseInt(t.parameters.get(0).toString());
						NQueensTask nq = (NQueensTask) mr.taskMap.get("nqueens");
						etc = nq.getTime(n, nq.getAlpha());
						nq.setTime(etc);
					}
					else if(taskName.equals("sort"))
					{
						Sort sort = (Sort)mr.taskMap.get("sort");
						etc = sort.getTime(sort.getDiskSize(), sort.getBufferSize());
						sort.setTime(etc);
					}
					else if(taskName.equals("wc"))
					{
						WordCount wc = (WordCount)mr.taskMap.get("wc");
						etc = wc.getTime(wc.getMemSize());
						wc.setTime(etc);
					}
					else if(taskName.equals("thread"))
					{
						MultiThreads mt = (MultiThreads)mr.taskMap.get("thread");
						etc = mt.getTime(mt.getN());
						mt.setTime(etc);
					}
					else if(taskName.equals("iperf"))
					{
						iPerf ip = (iPerf)mr.taskMap.get("iperf");
						etc = ip.getTime();
						ip.setTime(etc);
					}
					double waitTime[] = t.getWaitingTime();
					double cost[] = t.getCost();
					if(((global_time - t.getStartTime())+ etc) < waitTime[0] && (mr.getState().getCost() < cost[0] ))
					{
						// those machines where the estimated time to complete the task is lesser than the waiting 
						// time specified by the user or the cost the user agrees to pay is lesser than the cost 
						// of the machine
						validMac.state = mr.state;
						validMac.taskMap = mr.taskMap;
						validMacList.addMachine(validMac);	
					}
					else	
						continue;					
				}	
				for(int k = 0; k < validMacList.macList.size(); k++)
				{
					boolean Found = true;
					for(int m = 0; m < activeMachines.size(); m++)
					{
						if(activeMachines.get(m).state.getId() == validMacList.macList.get(k).getState().getId())
						{
							
						}
					}
					
				}
				
			}
		}
	}
}



