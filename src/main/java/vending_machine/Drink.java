package vending_machine;

public class Drink {

	private String name;
	private String cost;
	private int count;
	
	public Drink(String name, String cost, int count){
		this.name = name;
		this.cost = cost;
		this.count = count;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCost() {
		return cost;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
}
