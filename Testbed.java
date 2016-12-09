package Architecture;

import java.util.Hashtable;

public class Testbed extends Thread {
	//See the specifications of the VMAs in page 12 of the cluster computing journal
	
	private int simulationRun=0;
    private String dataCenterIP;	
	
	//private SMA[] SMA_medium=new SMA[8];
	//private SMA[] SMA_high=new SMA[6];
	private JobA[] virtual_machine=new JobA[300];
	private ServerA front_agent;
	
	private WorkloadGenerator myWorkload;
	//private Hashtable <Integer, SMA> SMAS; //This is maybe not required, we can access to the 
										   //SMA just by typing SMA[x].method. 
	
	private int startTime;
	
	public Testbed(int xstartTime, String dataCenterIP, int simulationRun){
        this.startTime=xstartTime;
        this.dataCenterIP = dataCenterIP;
        this.simulationRun=simulationRun;        
	}
	
	public int getStartingTime(){
		return this.startTime;
	}
	
	public void start(){   // Start the FA and the SMAs

		
		// First we create the Front Agent
		front_agent=new ServerA();
		
		
		this.run();
				
		//front_agent.printSMAS();
        //At this point all the SMAS are available
		
		//Generates the 300 VMAs
		//We need that the WorkLoadGenerator returns one by one the
		//VMA,
		// First choice. Bring the VMA to this code
		//Second choice. Update the WorkLoadGenerator to be here

		//Generates the 300 VMAs
	}
	
	/*Code added 2016 October 6th**/
	
    public void run(){    	
		int xCPU_Avaible,xMEM_Avaible;
		long xstart_time=0;
		long xexecution_time=0;
		long delay=0;
		long old_start_time;
		myWorkload=new WorkloadGenerator();
		/**
		 * During this execution, every time a new VMAs enter the simulation, the FA should designates a host SMA
		 * */		
		try{
		//for (int i=0;i<300;i++){
		for (int i=0;i<23;i++){
		//public VMA(int xId, int xCPU_Avaible, int xMEM_Avaible, float xstart_time, float xexecution_time)
		  xCPU_Avaible=myWorkload.generateRandomCPU();
		  xMEM_Avaible=myWorkload.generateRandomMEM();
		  old_start_time=xstart_time;
		  xstart_time=myWorkload.generateRandomStartTime(xstart_time);			
		  xexecution_time= myWorkload.generateRandomExecutionTime(xexecution_time);
		  delay=xstart_time-old_start_time;
		  Thread.sleep(delay);
		  virtual_machine[i]=new JobA(i, xCPU_Avaible, xMEM_Avaible, (float)xstart_time, (float)xexecution_time);
		  //Here we need to subscribe the VMA to the FA. Review if the listVMA is better to be a VMA object		  
		  front_agent.receiveJob(virtual_machine[i], xstart_time);
		  //virtual_machine[i].printVMA();
	}	
	}catch (Exception ex){
        System.out.println(ex);
     }
		
    }



	/*Code added 2016 October 6th**/
	

	
	public static void main(String args[]){						
		Testbed Simulation=new Testbed(0,"127.0.0.0",1);
		Simulation.start();
        //new Channel(0);
        //new Channel(1);
	}

}
