import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AdjacencyList {
    private ArrayList<CityObject> adjacencyList = new ArrayList<CityObject>();

    public AdjacencyList() {

    }
    
    public void parseInput(String url) {
        try {
            FileInputStream fStream = new FileInputStream(url);
            Scanner scan = new Scanner(fStream);
            
            scan.nextInt();
            scan.nextLine();

            while (scan.hasNext()) {
                String line = scan.nextLine();
                String[] data = line.split("\\|");
                int cost = Integer.parseInt(data[2]);
                int time = Integer.parseInt(data[3]);

                if (adjacencyList.isEmpty()) {  
                    //if adjacency list is empty, this must be the first data line
                    //meaning these two cities are new entities and must both be added to the AdjList
                    //city1 is data[0], city2 is data[1], cost and time are data[3 and 4]
                    
                    CityObject city1 = new CityObject(data[0], 0, 0, 0);
                    city1.getConnectingCities().add(new CityObject(data[1], cost, time, 0));
                
                    CityObject city2 = new CityObject(data[1], 0, 0, 1);
                    city2.getConnectingCities().add(new CityObject(data[0], cost, time, 0));

                    adjacencyList.add(city1);
                    adjacencyList.add(city2);

                } else {
                    //else there is already an adjList and we must check if the new data line is
                    //a new city, if it is not a new city, we must add the new adjacency to the reused city adjList
                    if (!containsNode(data[0])) 
                        adjacencyList.add(new CityObject(data[0], 0, 0, adjacencyList.size()));

                    if (!containsNode(data[1])) 
                        adjacencyList.add(new CityObject(data[1], 0, 0, adjacencyList.size()));

                    //now we have 2 new city entities if data[0 and 1] are not already nodes
                    //if ome of them was already a node, we must find that cities adjList and add the adjacency

                    for (int i = 0; i < adjacencyList.size(); i++) {
                        int size = adjacencyList.get(i).getConnectingCities().size();

                        if (adjacencyList.get(i).getName().equals(data[0]))
                            adjacencyList.get(i).getConnectingCities().add(new CityObject(data[1], cost, time, size));
                        
                        if (adjacencyList.get(i).getName().equals(data[1]))
                            adjacencyList.get(i).getConnectingCities().add(new CityObject(data[0], cost, time, size));
                    }
                }        
            }

            fStream.close();
            fStream = new FileInputStream("flightsRequested.txt");

            scan.close();
            scan = new Scanner(fStream);

            scan.nextInt(); scan.nextLine();

            int flightNum = 1;
            while (scan.hasNext()) {
                String line = scan.nextLine(); 
                String[] data = line.split("\\|");
                
                if (findCityName(data[0]) == -1) {
                    System.out.println("Origin " + data[0] +" does not exist");
                    continue;
                }

                if (findCityName(data[1]) == -1) {
                    System.out.println("Destination does not exist");
                    continue;
                }

                depthFirstSearch(findCityName(data[0]), findCityName(data[1]), data[2], flightNum);
                flightNum++;
            }

            scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() { return  adjacencyList.isEmpty(); }

    public void depthFirstSearch(int cityOrigin, int cityDestination, String key, int flightNum) {
        CityObject origin = adjacencyList.get(cityOrigin);
        CityObject destination = adjacencyList.get(cityDestination);

        int maxPaths = 2; // if there are fewer than 3 paths, print out all

        System.out.print("Flight " + flightNum + ": " + origin.getName() + ", " + destination.getName() + " (");
        if (key.equals("T")) System.out.println("Time)");
        else if (key.equals("C")) System.out.println("Cost)");
        else System.out.println("\nError. Invalid key.\n");

        ArrayList<Stack> paths = findAllRoutes(origin, destination);
        printEfficientRoutes(paths, maxPaths, key);
        
        System.out.println();
    }

    public void printEfficientRoutes(ArrayList<Stack> paths, int maxPaths, String key) {
        if (paths.isEmpty()) System.out.println("\nError. No flight paths could be found!\n");
        else {
            for (int i = 0; i < maxPaths && !paths.isEmpty(); i++) {
                Stack min = null;
                int minimum = paths.get(0).getValue(key);
                for (Stack path : paths) {
                    if (path.getValue(key) <= minimum) {
                        minimum = path.getValue(key);
                        min = path;
                    }
                }

                min.printPath(i, key);
                paths.remove(min);
            }
        }
    }

    public ArrayList<Stack> findAllRoutes(CityObject origin, CityObject destination) {
        ArrayList<Stack> paths = new ArrayList<Stack>();
        Stack currentPath = new Stack();

        currentPath.push(new CityObject(origin.getName(), 0, 0));
        currentPath.peek().setCityNumber(0);

        while (!currentPath.isEmpty()) {
            for (CityObject city : adjacencyList) {
                if (city.getName().equals(currentPath.peek().getName())) {
                    if (currentPath.peek().getCityNumber() < city.getConnectingCities().size()) {
                        CityObject next = city.getConnectingCities().get(currentPath.peek().getCityNumber());
                    
                        if (currentPath.exists(next)) {
                            currentPath.peek().increment();
                            break;
                        } else if(next.getName().equals(destination.getName())) {
                            currentPath.push(next);
                            paths.add(new Stack(currentPath));
                            currentPath.pop();
                            currentPath.peek().increment();
                            break;
                        } else {
                            currentPath.push(next);
                            currentPath.peek().setCityNumber(0);
                            break;
                        }
                    } else {
                        currentPath.pop();
                        if (!currentPath.isEmpty())
                            currentPath.peek().increment();
                        break;
                    }
                }
            }
        }

        return paths;
    }

    public boolean containsNode(String name) {
        boolean contained = false;
        for (int i = 0; i < adjacencyList.size(); i++) {
            if (adjacencyList.get(i).getName().equals(name)) {
                contained = true;
                break;
            }
        }

        return contained;
    }

    public int findCityName(String name){
        for(int i = 0; i < adjacencyList.size(); i++){
            if(adjacencyList.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    
    public void print() {
        System.out.println("\n+ Adjacency List +");
        System.out.println("==================\n");

        for (int i = 0; i < adjacencyList.size(); i++) {
            System.out.print(adjacencyList.get(i).getName());

            for (CityObject city : adjacencyList.get(i).getConnectingCities()) {
                System.out.print(" -> " + city.getName());                
            }

            System.out.println();
        }

        System.out.println();

        for (int i = 0; i < adjacencyList.size(); i++) {
            System.out.print(adjacencyList.get(i).getName());

            for (CityObject city : adjacencyList.get(i).getConnectingCities()) {
                System.out.print(" -> " + city.toString());                
            }

            System.out.println();
        }
    }
}

