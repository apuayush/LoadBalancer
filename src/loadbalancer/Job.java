package loadbalancer;
public class Job {
    int jid;
    int priority;
    int time;
    boolean isComplete;
    int exitStatus; // 0 - OK | 1 - Error
    
    Job(){
        jid = (int)(Math.random()*100);
        if (jid == 0) jid = (int)(Math.random()*100);
        isComplete = false;
        time = 100;
        priority = 1;
        exitStatus = 1;
    }    

    Job(int priority){
        this();
        this.priority = priority;
    }
    
    public String getStatus(){
        if (isComplete)
            return "COMPLETE";
        return "PROCESSING";
    }
    public void kill(){
        isComplete = true;
    }
    
    public void printStatus(){
        System.out.println(jid + "\t" + this.getStatus());
    }
    
    public String toString(){
        return (String.valueOf(jid));
    }
}
