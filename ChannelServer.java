package Communication;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Architecture.JobA;

public class ChannelServer {

	static final String HOST="localhost";
	static final int PUERTOCLIENTE = 5001;
	//static final int PUERTOSERVICIO = 5001;
	private int PUERTOSERVICIO=5000;
	private int pide=0;
	
	//private VMA machineVirtual;
	
	public ChannelServer (int xPort) { // pide = 0 se comporta como servidor, pide=1 se comporta como cliente
		this.PUERTOSERVICIO=xPort;
		receive();
	}
	
	
	public void receive(){
		//By default the typeOfMessage is 0
		//0 is a request of allocation
		//1 is the object itselft
		//2 is the finish of the service
		try{
			ServerSocket skServidor = new ServerSocket(this.PUERTOSERVICIO);
			//as a server
			int cont=0;			
			while (true){ //Determine the type of message				
				//cont++;
				System.out.println("Waiting for messages");
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
	
	public void envia(String xHost, int xPort,JobA xVMA){
		try{
			Socket skCliente= new Socket(xHost,xPort);
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
		new ChannelServer(5000);

	}
}
