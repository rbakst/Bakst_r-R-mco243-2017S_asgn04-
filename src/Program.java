import java.util.ArrayList;
import java.util.Random;

public class Program
{

	final static int NUM_PROCS = 6; // How many concurrent processes
	final static int TOTAL_RESOURCES = 30; // Total resources in the system
	final static int MAX_PROC_RESOURCES = 13; // Highest amount of resources any process could need
	final static int ITERATIONS = 30; // How long to run the program
	static Random rand = new Random();
	
	public static void main(String[] args)
	{
		
		// The list of processes:
		ArrayList<Proc> processes = new ArrayList<Proc>();
		for (int i = 0; i < NUM_PROCS; i++)
			processes.add(new Proc(MAX_PROC_RESOURCES - rand.nextInt(3))); // Initialize to a new Proc, with some small range for its max
		
		int availableResources = 0;
		Proc currProc = null;
		int totalHeldResources = 0;
		int currRequest;
		int claim;
		// Run the simulation:
		for (int i = 0; i < ITERATIONS; i++)
		{
			// loop through the processes and for each one get its request
			for (int j = 0; j < processes.size(); j++)
			{
				currProc = processes.get(j);
				availableResources = TOTAL_RESOURCES - totalHeldResources;
				// Get the request
				currRequest = currProc.resourceRequest(availableResources);
				claim = currProc.getMaxResources() - currProc.getHeldResources();
				
				// just ignore processes that don't ask for resources
				if (currRequest == 0)
					continue;
				
				if (currRequest < 0)
				{
					totalHeldResources += currRequest;
					System.out.println("Process " + j + " reliquished " + -currRequest +" resources." );
				}
				
				else if(claim > availableResources)
				{
					System.out.println("Process " + j + " requested " + currRequest +", denied." );
				}
				else
				{
					System.out.println("Process " + j + " requested " + currRequest +", granted." );
					currProc.addResources(currRequest);
					totalHeldResources += currRequest;
					
					//if (currRequest == claim)
					//I decided to omit this since although in this case the process has all the resources it needs,
					//it still needs time to complete its process and then reliquish its own resources (which based on the code, happens once a 
					//request is made on the process again). I didn't want to artificially make an additional request on the process so that it
					//would relinquish its resources, so we need to wait until the process "requests" again (when the loop reaches it)
					//and then its resources are relinquished.
					
				}
				
				// At the end of each iteration, give a summary of the current status:
				System.out.println("\n***** STATUS *****");
				System.out.println("Total Available: " + (TOTAL_RESOURCES - totalHeldResources));
				for (int k = 0; k < processes.size(); k++)
					System.out.println("Process " + k + " holds: " + processes.get(k).getHeldResources() + ", max: " +
							processes.get(k).getMaxResources() + ", claim: " + 
							(processes.get(k).getMaxResources() - processes.get(k).getHeldResources()));
				System.out.println("***** STATUS *****\n");
				
			}
		}

	}

}