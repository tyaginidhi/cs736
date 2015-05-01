package simulator;
import java.util.List;


public class Task {
	private double startTime;
	private String name;
	private List<Double> parameters;
	private double cost[] = new double[3];
	private double waitingTime[] = new double[3];
	
	public void setParameters(List<Double> parameters) {
		this.parameters = parameters;
	}
	public double[] getCost() {
		return cost;
	}
	public void setCost(double[] cost) {
		this.cost = cost;
	}
	public double[] getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(double[] waitingTime) {
		this.waitingTime = waitingTime;
	}
	long id;
	
	public double getStartTime() {
		return startTime;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Double> getParameters() {
		return parameters;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
}
