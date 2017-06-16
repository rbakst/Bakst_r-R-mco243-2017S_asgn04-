import java.util.ArrayList;
import java.util.Random;

public class Program
{

	final static int NUM_PROCS = 6; // How many concurrent processes
	final static int TOTAL_RESOURCES = 30; // Total resources in the system
	final static int MAX_PROC_RESOURCES = 13; // Highest amount of resources any process could need
	final static int ITERATIONS = 30; // How long to run the program
	static Random rand = new Random();
	private static int availableResources = 0;
	private static int totalHeldResources = 0;
	
	public static void main(String[] args)
	{
		
		// The list of processes:
		ArrayList<Proc> processes = new ArrayList<Proc>();
		for (int i = 0; i < NUM_PROCS; i++)
			processes.add(new Proc(MAX_PROC_RESOURCES - rand.nextInt(3))); // Initialize to a new Proc, with some small range for its max
		
		//int availableResources = 0;
		Proc currProc = null;
		
		int currRequest;
		int claim;
		boolean grant = true;
		
		
		int testAvailResources;
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
				else 
				{
					//grant = testRequest(processes, currProc, claim);
						
					
					if (claim <= availableResources)
					{
						ArrayList<Boolean> parallel = new ArrayList<Boolean> (processes.size());//all false
						for (boolean p : parallel)
						{
							p = false;
						}
						testAvailResources = availableResources - claim;
						while(parallel.contains(false) && grant)
						{
							grant = false;
							//loop through all resources and ensure one will be able to finish with this number available, outer loop will continue to test for rest
							for (int x = 0; x < processes.size(); ++x)
							{
								if ((processes.get(x).getMaxResources()-processes.get(x).getHeldResources()) < testAvailResources)//enough for this process to finish, add its resourcces back
								{
									testAvailResources += processes.get(x).getMaxResources();
									parallel.set(x, true);
									grant = true;
									break;
								}
							}
							
						}
						
						if (grant)
						{
							System.out.println("Process " + j + " requested " + currRequest +", granted." );
							currProc.addResources(currRequest);
							totalHeldResources += currRequest;
						}
						
					
					//if (currRequest == claim)
					//I decided to omit this since although in this case the process has all the resources it needs,
					//it still needs time to complete its process and then reliquish its own resources (which based on the code, happens once a 
					//request is made on the process again). I didn't want to artificially make an additional request on the process so that it
					//would relinquish its resources, so we need to wait until the process "requests" again (when the loop reaches it)
					//and then its resources are relinquished.
					
				}
					if (claim > availableResources || !grant)
					{
						System.out.println("Process " + j + " requested " + currRequest +", denied." );
					}
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