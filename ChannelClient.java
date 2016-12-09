package Communication;



//The idea is to create a socket server object both in FA and in SMAs. This objects transport the VMAs to distribute
//among the SMAs groups

import java.io.*;
import java.net.*;
import Architecture.JobA;


public class ChannelClient {

	//static final String HOST="localhost";
	//static final int PUERTOCLIENTE = 5001;
	//static final int PUERTOSERVICIO = 5001;
	private String HOST;
	private int PUERTOSERVICIO=5000;
	private int PUERTOCLIENTE=5001;
	private int pide=0;
	
	
	//private VMA machineVirtual;
	
	public ChannelClient(int xPort) { // pide = 0 se comporta como servidor, pide=1 se comporta como cliente
		this.HOST="localhost";
		this.PUERTOCLIENTE=xPort;
		JobA xVMA=new JobA(1,1,1,1,1);
		this.envia(xVMA);
	}
	
	
	public void recibe(){
		try{
			ServerSocket skServidor = new ServerSocket(this.PUERTOSERVICIO);
			//as a server
			int cont=0;
			while (true && cont<5){
				cont++;
				System.out.println(cont);
				Socket myCliente=skServidor.accept();
				System.out.println("Atiendo a un cliente ");
				//communication channels with the client
				ObjectOutputStream ToClient = new ObjectOutputStream(myCliente.getOutputStream());
	            ObjectInputStream FromClient = new ObjectInputStream(myCliente.getInputStream());
	            JobA x,y;
	            x=(JobA)FromClient.readObject();
	            System.out.println("I receive request from Job: "+x.getId());
	            y=new JobA(2,2,2,2,2);	            
	            ToClient.writeObject((JobA)y);	            
	            System.out.println("I send the Job :"+y.getId());		           
	            //communication channels with the client				
			}								
		} catch(Exception e){
			System.out.println(e.getMessage());
		}				
		// as a server
	}
	
	public void envia(JobA xVMA){
		try{
			Socket skCliente= new Socket(this.HOST,this.PUERTOCLIENTE);
			// as a client
			ObjectOutputStream ToServer = new ObjectOutputStream(skCliente.getOutputStream());
          ObjectInputStream FromServer = new ObjectInputStream(skCliente.getInputStream());            
          JobA yVMA;
          //as a client
          //Send VMA xVMA to the server hosted at xHost at port xPort
			ToServer.writeObject((JobA)xVMA);
			System.out.println("Envie el job: "+xVMA.getId());
			yVMA=(JobA)FromServer.readObject();
			System.out.println("Recibi el jobA:"+yVMA.getId());
			skCliente.close();
		} catch(Exception e){
			System.out.println(e.getMessage());
		}				
		// as a server
	}	
	
	public static void main(String[] args){
		new ChannelClient(5000);

	}
}
