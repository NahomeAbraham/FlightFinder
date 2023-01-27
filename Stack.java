import java.util.ArrayList;

public class Stack {
  public ArrayList<CityObject> visited;
  public int totalCost, totalTime;

  public Stack() {
    this.visited = new ArrayList<CityObject>();
    this.totalCost = this.totalTime = 0;
  }

  public Stack(ArrayList<CityObject> cities) {
    this.visited = cities;
    this.totalCost = this.totalTime = 0;
  }

  public Stack(Stack copy) {
    this.visited = new ArrayList<CityObject>(copy.visited);

    this.totalCost = copy.totalCost;
    this.totalTime = copy.totalTime;
  }

  public CityObject pop() {
    CityObject last = visited.get(visited.size() - 1);
    updateWeight(-last.getCost(), -last.getTime());
    visited.remove(visited.size() - 1);

    return last;
  }

  public int push(CityObject city) {
    visited.add(city);
    updateWeight(city.getCost(), city.getTime());

    return visited.indexOf(city);
  }

  public CityObject peek() {
    return visited.get(visited.size() - 1);
  }

  public boolean exists(CityObject query) {
    for (CityObject city : visited) {
      if (city.getName().equals(query.getName()))
        return true;
    }

    return false;
  }

  public void printPath(int index, String quantifier) {
    System.out.print("Path " + (index + 1) + ": ");

    for (int i = 0; i < visited.size(); i++) {
      if (i != 0) System.out.print(" -> ");
      System.out.print(visited.get(i).getName());
    }

    System.out.println(". Time: " + totalTime + " Cost: " + totalCost);
  }

  public boolean isEmpty() { return visited.isEmpty(); }

  public void updateWeight(int cost, int time) { 
    totalCost += cost; totalTime += time; 
  }

  /* Flight 1: Dallas, Houston (Time)
  Path 1: Dallas -> Houston. Time: 51 Cost: 101.00
  Path 2: Dallas -> Austin -> Houston. Time: 86 Cost: 193.00 */

  public String toString() {
    return String.valueOf(totalCost);
  }

  public int getValue(String quantifier) {
    return quantifier.equals("C") ? totalCost : quantifier.equals("T") ? totalCost : -1;
  }
}

