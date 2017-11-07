package loadbalancer;

import java.util.Queue;
import java.util.LinkedList; 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoadBalancer {

    
    //static int toss = 0;
    
    static int num_nodes = 5;
    
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Queue q1 = new LinkedList();
        Queue q2 = new LinkedList();
        
        // ask user for number of jobs (n);
        //int n = 20; 
        System.out.println("How many jobs would you like to schedule?");
        int n = Integer.parseInt(br.readLine());
        int nc = n;               
        
        // 1. TASK: Assign 'n' jobs to 2 queues -> q1 and q2;
        System.out.println("Thank you for the input.\nSince we have only 2 job queues available at the moment, we'll be assigning the jobs alternately to each queue.");
        while (nc>0){
            q1.add(new Job());
            nc--;
            if (nc==0) break;
            q2.add(new Job());
            nc--;
        }
        System.out.println("Here are the job queues: ");
        System.out.println("JQ_1: " + q1);
        System.out.println("JQ_2: " + q2);
        
        // 2. TASK: initialize nodes
        System.out.println("How many nodes would you like to initialize?");
        num_nodes = Integer.parseInt(br.readLine());
        Node arr[] = new Node[num_nodes];
        initializeNodes(arr);
        System.out.println("We have initialized " + num_nodes + " for you.");
        
        // 3. TASK: run load balancer
        System.out.println("Attempting to run the load balancing algorithm for the first time!");        
        runLoadBalancerV2(arr, q1, q2);
        System.out.println("LB call complete. Job queue after load balancing:");
        System.out.println("JQ_1: " + q1);
        System.out.println("JQ_2: " + q2);
        
        if (allJobsCompleted(q1, q2)){
            System.out.println("1st LB call --- All pending jobs have been completed.\nThank you for using our program!");
            return;
        }
        else
            System.out.println("There are a few pending jobs. Please free up some nodes manually.");
        
        while (true){
            System.out.println("________MENU________");
            System.out.println("1. Free first 5 nodes");
            System.out.println("2. Free first 10 nodes");
            System.out.println("3. Free last 5 nodes");
            System.out.println("4. Free all nodes");
            System.out.println("5. View job queues");
            System.out.println("6. View cluster status");
            System.out.println("7. View cluster history");
            System.out.println("8. Run load balancer");
            System.out.println("9. Terminate program");
            
            int ch = Integer.parseInt(br.readLine());
            
            // case 1 - free first 5 nodes
            if (ch == 1){
                int i=0, count = 0;
                for (; i<arr.length; i++){
                    arr[i].free(); //free the node
                    count++;
                    if (count == 5) break;
                }
                int nodes_cleared = count>i ? count : i;
                System.out.println(nodes_cleared + " nodes cleared.");
            }
            
            // case 2 - free first 10 nodes
            else if (ch == 2){
                int i=0, count = 0;
                for (; i<arr.length; i++){
                    arr[i].free();
                    count++;
                    if (count == 10) break;
                }
                int nodes_cleared = count>i ? count : i;
                System.out.println(nodes_cleared + " nodes cleared.");
            }
            
            // case 3 - free last 5 nodes
            else if (ch == 3){
                int i=arr.length-1, count = 0;
                for (; i>=0; i--){
                    arr[i].free();
                    count++;
                    if (count == 5) break;
                }
                int nodes_cleared = count>i ? count : i;
                System.out.println(nodes_cleared + " nodes cleared.");
            }
            
            // case 4 - free all nodes
            else if (ch == 4){
                int i=0;
                for (; i<arr.length; i++)
                    arr[i].free();
                System.out.println(i + " nodes cleared.");
            }
            
            // case 5 - view job queues
            else if (ch == 5){
                System.out.println("JQ_1: " + q1);
                System.out.println("JQ_2: " + q2);
            }
            
            // case 6 - view cluster status
            else if (ch == 6){
                System.out.print("NODE:  ");
                for (int i = 0; i<arr.length; i++)
                    System.out.print(arr[i].nodeID + "\t");
                System.out.println();
                
                System.out.print("JOB:   ");
                for (int i = 0; i<arr.length; i++)
                    System.out.print(arr[i].currentJob + "\t");
                System.out.println();
            }
            
            // case 7 - view cluster history
            else if(ch == 7){
                for (int i = 0; i<arr.length; i++)
                    System.out.println(arr[i].nodeID + ": " + arr[i].jobHistory + "\t");
            }   
            
            // case 8 - run load balancer
            else if (ch == 8){
                runLoadBalancerV2(arr, q1, q2);
                System.out.println("After load balancing::");
                System.out.println("JQ_1: " + q1);
                System.out.println("JQ_2: " + q2);
            }
            
            // case 9 - terminate the program
            else if (ch == 9){
                if (allJobsCompleted(q1, q2))
                    System.out.println("NOTE: All pending jobs have been completed.");
                else
                    System.out.println("FYI: There are a few pending jobs.");
            
                System.out.println("Thank you for using our program!");
                return;                
            }
            
            else{
                System.out.println("Invalid input. Please check your input, thanks.");
            }                
        }
    }
    
    public static boolean allJobsCompleted(Queue q1, Queue q2){
        if (q1.peek() == null) return true;
        if (q2.peek() == null) return true;
        return false;
    }
    
    public static Node getFreeNode(Node arr[]){
        for (int i=0; i<arr.length; i++){
            if (arr[i].isBusy == false)
                return arr[i];
        }
        return null;
    }
    
    static int toss = 0;
    public static Job getNextJob(Queue q1, Queue q2){
        Job j = null;
        if (toss == 0) // check Q1 first;
        {
            j = (Job) q1.poll();
            if (j == null) // Q1 is over
                j = (Job) q2.poll(); // if null, then both Qs over
            toss = 1;
        }
        else // check 
        {
            j = (Job) q2.poll();
            if (j == null)
                j = (Job) q1.poll();
            toss = 0;
        }
        return j;
    }
    
    public static void initializeNodes(Node arr[]){
        for(int i=0; i<arr.length; i++){
            arr[i] = new Node("N"+(i+1));        
        }
    }
    
    public static void runLoadBalancerV2(Node arr[], Queue q1, Queue q2){
        while (true){
            Node n = getFreeNode(arr);
            if (n == null){
                System.out.println("All nodes are busy. Please check after a while.");
                return;
            }

            Job j = getNextJob(q1, q2);
            if (j == null){
                System.out.println("All jobs completed. Queues empty!");
                // print queue history
                return;
            }

            n.addJob(j);
            System.out.println("Added job #" + j + " to node "  + n.nodeID);
        }
    }    
}