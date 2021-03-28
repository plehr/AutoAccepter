package model;

public abstract class Event {
	public int id;
	public int globalID;
	public String time;
	public String type;

	public abstract String toString();
}
