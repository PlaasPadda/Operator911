package operator911.app;

public class Request {
	String type;
	String services;
	String id;
    float x;
    float y;

    public Request(String type, String services, String id, float x, float y) {
    	this.type = type;
        this.services= services;
        this.id = id;
        this.x = x;
        this.y = y;
    }

}
