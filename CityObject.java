import java.util.ArrayList;

public class CityObject {
    private String name; 
    private int cost, time, cityNumber;
    private boolean visited;
    private ArrayList<CityObject> connectingCities;

    public CityObject(String name, int cost, int time) {
        this.name = name;
        this.cost = cost;
        this.time = time;

        connectingCities = new ArrayList<CityObject>();
        cityNumber = 0;
        visited = false;
    }

    public CityObject(String name, int cost, int time, int cityNumber) {
        this.name = name;
        this.cost = cost;
        this.time = time;

        connectingCities = new ArrayList<CityObject>();
        this.cityNumber = cityNumber;

        visited = false;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public ArrayList<CityObject> getConnectingCities() {
        return connectingCities;
    }

    public void setConnectingCities(ArrayList<CityObject> connectingCities) {
        this.connectingCities = connectingCities;
    }

    public int getValue(String type) {
        if (type.equals("T")) return time;
        else return cost;
    }

    public int getCityNumber() {
        return cityNumber;
    }

    public void setCityNumber(int cityNumber) {
        this.cityNumber = cityNumber;
    }

    public void increment() {
        this.cityNumber += 1;
    }

    @Override
    public String toString() {
        return "CityObject { name = " + name + ", cityNumber = " + cityNumber + ", cost = "  + cost + ", time = "  + time + " }";
    }
}

