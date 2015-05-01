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
	
	public static MachineRawList readMachines()
	{
		MachineRawList macList = new MachineRawList();
		// read the initial stats file of each machine in the cluster
		// each row separated by semi colon contains (macid, disk size, nw bw, RAM, cores, mac cost, params
		// each task is separated by comma and each task is split by colon
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("stats.txt"));
		    String line;
		    String words[];
		    while ((line = reader.readLine())!=null)
		    {	
				  System.out.println(line);
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
				  macList.getMacList().add(m);
		    }
		    reader.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return macList;
	}
	public static TaskList readTasks()
	{
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
				  System.out.println(line);  
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
					 waitingMetric[i] = Double.parseDouble(waitingTime[i].toString());
				  }
				  t.setWaitingTime(waitingMetric);
				  
				  t.setId(Long.parseLong(words[5].toString()));
				  taskList.getTaskQ().add(t);
				}
		    reader.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return taskList;
	}
	
	public static void main(String args[])
	{
		List<SchedulerEntry> activeMachines  = new ArrayList<SchedulerEntry>();
		TaskList taskList = new TaskList();
		taskList = readTasks();
		MachineRawList macList = new MachineRawList();
		macList = readMachines();
			
		double global_time = 0;
		while(true)
		{
			// loop over all the active machines and remove all the tasks which have completed
			for(int x = 0; x < activeMachines.size() ; x++)
			{
				SchedulerEntry se = activeMachines.get(x);
				for(int y = 0; y < se.getSubmitQ().size(); y++)
				{
					SubmittedTask st = se.getSubmitQ().peek();
					if(st.getScheduledTime()+ st.getEstimatedTime() == global_time)
					{
						se.getState().setCores(se.getState().getCores()+st.getMacState().getCores());
						se.getState().setDiskSize(se.getState().getDiskSize()+st.getMacState().getDiskSize());
						se.getState().setMemory(se.getState().getMemory()+st.getMacState().getMemory());
						se.getState().setNwBandwidth(se.getState().getNwBandwidth()+st.getMacState().getNwBandwidth());
						se.getSubmitQ().remove(st);
						
						System.out.println(se.getState().getId()+ " " +st.getName() + " " + st.getId() + " "+
						st.getScheduledTime() + " " + st.getEstimatedTime() +" " +st.getStartTime() + " " + st.getMacState());
						// move the first waiting task to running, provided resources become available
					}
				}
			}
			for(int i = 0; i < taskList.getTaskQ().size();  i++ )
			{
				Task t = taskList.getTaskQ().get(i);
				System.out.println(t.getStartTime() + " " + global_time);
				if(t.getStartTime() <= global_time)
				{
					String taskName = t.getName();
					double waitTime[] = t.getWaitingTime();
					double cost[] = t.getCost();
					MachineRawList validMacList = new MachineRawList();
					MachineRaw validMac = new MachineRaw();
					System.out.println("maclistsize:" +  macList.getMacList().size());
					for(int j = 0; j < macList.getMacList().size(); j++)
					{
						double etc = 0.0;
						MachineRaw mr = macList.getMacList().get(j);
						if(taskName.equals("nqueens"))
						{
							// just need one core for this and every machine is guaranteed to have a core
							int n = (int)Math.round(t.getParameters().get(0));
							NQueensTask nq = (NQueensTask) mr.getTaskMap().get("nqueens");
							etc = nq.getTime(n, nq.getAlpha());
							nq.setTime(etc);
						}
						else if(taskName.equals("sort"))
						{
							// either not sufficient RAM or not sufficient disk space
							if( mr.getState().getMemory() < t.getParameters().get(0) || mr.getState().getDiskSize() < t.getParameters().get(1))
								continue;
							Sort sort = (Sort)mr.getTaskMap().get("sort");
							etc = sort.getTime(sort.getDiskSize(), sort.getBufferSize());
							sort.setTime(etc);
						}
						else if(taskName.equals("wc"))
						{
							// not enough RAM
							if( mr.getState().getMemory() < t.getParameters().get(0))
								continue;
							WordCount wc = (WordCount)mr.getTaskMap().get("wc");
							etc = wc.getTime(wc.getMemSize());
							wc.setTime(etc);
						}
						else if(taskName.equals("thread"))
						{
							// not sufficient cores in the machine
							if(mr.getState().getCores() < t.getParameters().get(0))
								continue;
							MultiThreads mt = (MultiThreads)mr.getTaskMap().get("thread");
							etc = mt.getTime(mt.getN());
							mt.setTime(etc);
						}
						else if(taskName.equals("iperf"))
						{
							// not enough nw bandwidth
							if(mr.getState().getNwBandwidth() < t.getParameters().get(0))
								continue;
							iPerf ip = (iPerf)mr.getTaskMap().get("iperf");
							etc = ip.getTime();
							ip.setTime(etc);
						}
						if(((global_time - t.getStartTime())+ etc) <= waitTime[0] && (mr.getState().getCost() <= cost[0] ))
						{
							// those machines where the estimated time to complete the task is lesser than the waiting 
							// time specified by the user or the cost the user agrees to pay is lesser than the cost 
							// of the machine
							validMac.setState(mr.getState());
							validMac.setTaskMap(mr.getTaskMap());
							validMacList.getMacList().add(validMac);	
						}
					}	
					
					boolean found = false;
					double lowestCost = Double.MAX_VALUE;
					SchedulerEntry activeMacChosen = new SchedulerEntry();
					MachineRaw bestMac = new MachineRaw();
					double etc = 0;
					SubmittedTask subTask = new SubmittedTask();
					double timeToScheduleNext_n = Double.MAX_VALUE;
					for(int k = 0; k < validMacList.getMacList().size(); k++)
					{
						for(int m = 0; m < activeMachines.size(); m++)
						{		
							if(activeMachines.get(m).getState().getId() == validMacList.getMacList().get(k).getState().getId())
							{
								double macCost = validMacList.getMacList().get(k).getState().getCost();
								double timeToScheduleNext = activeMachines.get(m).getTimeToScheduleNext();
								if(t.getName().equals("nqueens"))
								{
									if(activeMachines.get(m).getState().getCores() == 0)
									{
										subTask.setState(false);
										for(int z = 0; z < activeMachines.get(m).getSubmitQ().size(); z++)
										{
											SubmittedTask st = activeMachines.get(m).getSubmitQ().peek();
											if(st.getEstimatedTime()+st.getScheduledTime() <timeToScheduleNext_n)
												activeMacChosen.setTimeToScheduleNext(timeToScheduleNext_n);
										}
									}
									else
									{
										subTask.setState(true);
										activeMacChosen.setTimeToScheduleNext(global_time);										
									}
									NQueensTask nq = (NQueensTask) bestMac.getTaskMap().get("nqueens");
									etc = nq.getTime(); 
								}
								else if(t.getName().equals("sort"))
								{
									Sort sort = (Sort) bestMac.getTaskMap().get("sort");
									etc = sort.getTime(); 
								}
								else if(t.getName().equals("wc"))
								{
									WordCount wc = (WordCount) bestMac.getTaskMap().get("wc");
									etc = wc.getTime(); 
								}
								else if(t.getName().equals("thread"))
								{
									MultiThreads mt = (MultiThreads) bestMac.getTaskMap().get("thread");
									etc = mt.getTime();
								}
								else if(t.getName().equals("iperf"))
								{
									iPerf ip = (iPerf) bestMac.getTaskMap().get("iperf");
									etc = ip.getTime();
								}
																
								if(timeToScheduleNext+etc <= t.getStartTime()+waitTime[0])
								{
									if(cost[0] >= macCost && macCost < lowestCost)
									{
										found = true;
										bestMac = validMacList.getMacList().get(k);
										activeMacChosen = activeMachines.get(m);
										lowestCost = macCost;
									}
								}	
							}
						}
					}
										
					if(found)
					{
						subTask.setEstimatedTime(etc);
						subTask.setId(t.getId());
						subTask.setName(t.getName());
						subTask.setScheduledTime(global_time);
						subTask.setStartTime(t.getStartTime());
						MachineState subTaskMacState = bestMac.getState();
						// machines available resources get reduced based on the incoming task
						activeMacChosen.getState().setCores(activeMacChosen.getState().getCores()-subTaskMacState.getCores());
						activeMacChosen.getState().setDiskSize(activeMacChosen.getState().getDiskSize()-subTaskMacState.getDiskSize());
						activeMacChosen.getState().setMemory(activeMacChosen.getState().getMemory()-subTaskMacState.getMemory());
						activeMacChosen.getState().setNwBandwidth(activeMacChosen.getState().getNwBandwidth()-subTaskMacState.getNwBandwidth());
						// add the task to the submitted queue of the machine
						activeMacChosen.addSubmitTask(subTask);
						continue;
					}
					
					// best cost model
					// cant find any machine amongst the running machines,so pick the highest cost machine which
					// is closest to the user specified cost
					SchedulerEntry bestMachine = new SchedulerEntry();
					double highestCost = Double.MIN_VALUE;
					for(int k1 = 0; k1 < validMacList.getMacList().size(); k1++)
					{
						double macCost = validMacList.getMacList().get(k1).getState().getCost();
						if(macCost > highestCost)
						{
							highestCost = macCost;
							bestMac = validMacList.getMacList().get(k1);
						}
					}
					bestMachine.setState(bestMachine.getState());
					if(t.getName().equals("nqueens"))
					{
						NQueensTask nq = (NQueensTask) bestMac.getTaskMap().get("nqueens");
						subTask.setEstimatedTime(nq.getTime()); 
					}
					else if(t.getName().equals("sort"))
					{
						Sort sort = (Sort) bestMac.getTaskMap().get("sort");
						subTask.setEstimatedTime(sort.getTime()); 
					}
					else if(t.getName().equals("wc"))
					{
						WordCount wc = (WordCount) bestMac.getTaskMap().get("wc");
						subTask.setEstimatedTime(wc.getTime()); 
					}
					else if(t.getName().equals("thread"))
					{
						MultiThreads mt = (MultiThreads) bestMac.getTaskMap().get("thread");
						subTask.setEstimatedTime(mt.getTime());
					}
					else if(t.getName().equals("iperf"))
					{
						iPerf ip = (iPerf) bestMac.getTaskMap().get("iperf");
						subTask.setEstimatedTime(ip.getTime());
					}
					subTask.setId(t.getId());
					subTask.setScheduledTime(global_time);
					subTask.setStartTime(t.getStartTime());
					subTask.setState(true);
					subTask.setName(t.getName());
					// add the submitted task to the queue
					bestMachine.addSubmitTask(subTask);
					// since this is the first job added to the machine, another job can be scheduled immediately provided 
					// the job's parameters are met
					bestMachine.setTimeToScheduleNext(0);
					MachineState subTaskMacState = bestMac.getState();
					// machines available resources get reduced based on the incoming task
					bestMachine.getState().setCores(bestMachine.getState().getCores()-subTaskMacState.getCores());
					bestMachine.getState().setDiskSize(bestMachine.getState().getDiskSize()-subTaskMacState.getDiskSize());
					bestMachine.getState().setMemory(bestMachine.getState().getMemory()-subTaskMacState.getMemory());
					bestMachine.getState().setNwBandwidth(bestMachine.getState().getNwBandwidth()-subTaskMacState.getNwBandwidth());
					// this machine now gets into the list of currently active machines
					activeMachines.add(bestMachine);
				}
				else
				{
					global_time++;
					break;
				}
			}
		}
	}
}