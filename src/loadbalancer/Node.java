package loadbalancer;
import java.util.Queue;
import java.util.LinkedList;

public class Node {
    Queue jobHistory = new LinkedList();
    String nodeID;
    boolean isBusy = false;
    Job currentJob = null;
    
    Node(String nodeID){
        this.nodeID = nodeID;
    }
    
    public void addJob(Job j){
        jobHistory.add(j);
        currentJob = j;
        isBusy = true;
    }
    
    public void free(){
        isBusy = false;
        currentJob = null;
    }
    
    public Job viewCurrentJob(){
        return currentJob;
    }
}