package Architecture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;
import Communication.*;

public class ServerA {
	
	/**
	 * When a VM arrives to the FA. 
	 * 
	 * **/
	
	private int grupo;
	
	/***
	 * The FA does not contain the SMAs nor the VMAs, it just considers how to trace back them. 
	 * */
	private int curSMA;
	private Vector<NodeA> listSMA; //Store the identifier of the SMAS
	private  Vector<Integer> listVMA; //Store the identifier of the VMAS and 
	private ChannelClient outputRequest;
	private NodeA[] SMA=new NodeA[20];  // from 0 to 5 is a low SMA, from 6 to 13 is a medium SMA, from 14 to 19 is high

    //We will be able to trace a VMA historic by its object hostSMA
	
	/***
	 * The FA does not contain the SMAs nor the VMAs, it just considers how to trace back them. 
	 * */
	
	public ServerA()
	{
		curSMA=0;
		listSMA=new Vector<NodeA>();
		listVMA=new Vector<Integer>();
		this.setGPU();
	}
	
	public void setGPU(){
	
		//CPUs with 8 cores
		for (int i=0;i<6;i++){
			SMA[i]=new NodeA(i, 20, 48);
			//front_agent.subscribeSMA(i);
			subscribeSMA(SMA[i]);
			//SMAS.put(i, SMA_low[i]);
		}
		
		//There exists 8 hosts with 40 CORES and 96 GB.
		
		//GPUs with 256 cores (Quadro)
		for (int i=6;i<14;i++){  
			SMA[i]=new NodeA(i, 40, 96);
			//front_agent.subscribeSMA(i+6);
			subscribeSMA(SMA[i]);
			//SMAS.put(i+6, SMA_low[i]);
		}
		
		//There exists 6 hosts with 60 CORES and 144 GB.
				
		//GPUs K20 with 2496 cores 
		for (int i=14;i<20;i++){
			SMA[i]=new NodeA(i, 60, 144);
			//front_agent.subscribeSMA(i+14);
			subscribeSMA(SMA[i]);
			//SMAS.put(i+14, SMA_low[i]);
		}
		
	}
	
	public void restartNodes(){
		for (int i=0;i<20;i++){
			SMA[i].setFinishTime(0);
		}
	}
	
	public String receiveJob(JobA job, long timeArrival, int policy){
		String text="Received Job:"+job.getId()+" at time:"+timeArrival+" estimation time="+job.get_execution_time();
	    switch (policy){  //1 Round robin; 2 Best fit; 3 First come first serve
	       case 1:text=text+roundRobin(job, this.curSMA,timeArrival);
	       		  break;
	       case 2:text=text+roundRobin(job, this.curSMA,timeArrival);
	       		  break;
	       default: text=text+roundRobin(job, this.curSMA,timeArrival);
	       			break;
	    }		
	    curSMA++;
	    return text;
	}
	
	protected void assignVMA(JobA xVMA, NodeA yHost){
		yHost.receiveVMA(xVMA);
	}
	
	public void subscribeSMA(NodeA newSMA){
		listSMA.add(newSMA);
	}
	
	//Used when a SMA unsubscribe to such a group
	public void unsubscribeSMA(int idSMA){
		listSMA.remove((Integer)idSMA);
	}
	
	
	//This function need to trace the location of the VMA. Please modify the 
	//structure to include the SMA location in the listVMA function
	//Deadline 04-10-2016
	public void enterNewVMA(int idVMA, int idCurrentSMA){
		listVMA.add(new Integer(idVMA));
		//Here we stabilish the distribution policy
		//roundRobin();
	}

	/*
	 * Here we define the scheduling policies
	 * */	
	
	//public void printFile(PrintWriter printWriter, String text){
	public void printFile(String text){
		File file=new File("file.txt");
		try(  PrintWriter out = new PrintWriter( file)  ){
		    out.println(text);
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
	}
	
	public String roundRobin(JobA job, int i,float xTimeArrival){
	    String text="";
		NodeA xNode;
		float xAvailable=0;
		int index=i%20;
		xNode=listSMA.get(index);
	    xAvailable=xNode.isAvailable(xTimeArrival);
	    xAvailable=xAvailable+xNode.computeExecutionTime(job); //This is the estimated time to be return
	    if (xAvailable<job.getDeadline())
	    {
	    	xNode.receiveVMA(job);
	    	text="Se asigna a la SMA:"+xNode.getID()+" hold time: "+xAvailable;
	    	//text="GREAT Waiting time:"+xAvailable;
	    	//System.out.println("Se asigna a la SMA:"+xNode.getID()+" hold time: "+xAvailable);
	    }
	    else
	    {
	    	//text="SORRY Waiting time:"+xAvailable;
	    	text="No se pudo asignar el Job:"+job.getId()+" hold time:"+xAvailable;
	    	//System.out.println("No se pudo asignar el Job:"+job.getId()+" hold time:"+xAvailable);
	    }
	    return text;
	}
		

	/*
	 * Here we define the scheduling policies
	 * */		
	
	public void printSMAS(){
		   for (int i=0;i<listSMA.size();i++){
			   System.out.println("Elemento:"+listSMA.get(i).getID());
		   }
	}

}
