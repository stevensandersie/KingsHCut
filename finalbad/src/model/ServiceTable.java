package model;

public class ServiceTable {
	private String id, name;
	private int price, duration;
	public ServiceTable(String id, String name, int price, int duration) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.duration = duration;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}
