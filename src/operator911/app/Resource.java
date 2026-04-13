package operator911.app;

public class Resource{
	String id;
	String type;
	float x;
	float y;
	boolean available;

    public Resource(String id, String type, float x, float y, boolean available) {
    	this.id = id;
    	this.type = type;
    	this.x = x;
    	this.y = y;
    	this.available = available;
    }

}
