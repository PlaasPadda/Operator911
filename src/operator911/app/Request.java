package operator911.app;

public class Request {
	String type;
	String services;
    float x;
    float y;

    public Request(String type, String services, float x, float y) {
    	this.type = type;
        this.services= services;
        this.x = x;
        this.y = y;
    }

}
