/*
 * 
 * @author Stefanos Mountrakis (stvakis@gmail.com)
 */

package demo;

public class Constant {

	public final static boolean ENABLE_OUTPUT = true;
	public final static boolean OUTPUT_CSV = false;
	public final static double SCHEDULING_INTERVAL = 300;
	public final static double SIMULATION_LIMIT = 70 * 60 * 60;
	
	
	
	
	// ================== Users Parameters =================//

//	public final static double[] USERS_DELAY = { 0,1700};
	public final static double[] USERS_DELAY = { 0};

	// The Types of the VM broker has (starts from 0)
	public final static int[][] USR_TYPE_OWNED = { { 0, 1, 2,3 }, { 3,1, 2 } };
	// The Amount of the VM broker has
	public final static int[][] USR_VMNUM_OWNED = { { 100, 50,100,50 }, {15,15 , 15 } };
	// The Types of the Cloudlets broker has (starts from 0)
	public final static int[][] USR_CLOUDLET_TYPE = { { 3,2, 1, 0 }, { 4,3, 2 } };


	// ================== Cloudlet Parameters =================//

//	public final static int CLOUDLET_LENGTH[] = { (int) (100000*SIMULATION_LIMIT), (int) (20000*SIMULATION_LIMIT), (int) (3000*SIMULATION_LIMIT), (int) (3000*SIMULATION_LIMIT), (int) (3000*SIMULATION_LIMIT) };// * (int) SIMULATION_LIMIT;
	public final static int CLOUDLET_LENGTH[] = { 20000000, 4000000, 60000000,16000000 };// * (int) SIMULATION_LIMIT;
	public final static int CLOUDLET_FILESIZE[] = { 300, 300, 300, 300,300 };
	public final static int CLOUDLET_OUTPUTSIZE[] = { 300, 300, 300, 300,300 };
	public final static int CLOUDLET_PES[] = { 1, 1, 1, 1,1 };

	
	//This is for random utilization	(under construction)
	public final static long CLOUDLET_UTILIZATION_SEED[] = { 1, 1, 1, 1 };
	public final static long RANDOM_UTILIZATION = 0 ; //Set to 1 , requires TimeSharedOverSubscription & SchedulerDynamicWorkload (3,3) (buggy)
	// 1 Time Shared * 
	// 2 Space Shared
	// 3 TimeSharedOverSubscription <2>
	public final static int[] VM_ALLOCATION = { 1,1,1, 1,1 };
//	public final static int[] VM_ALLOCATION = { 2,2,2,2,2 };
//	public final static int[] VM_ALLOCATION = { 3,3,3, 3,3 };
	// 1 Time Shared
	// 2 Space Shared * <- set this for NOT RANDOM / WORKING FOR migrations / infinity run for full utilization
	// 3 SchedulerDynamicWorkload <- set this for RANDOM / WORKING FOR migrations (not for adding second user) <2>
//	public final static int[] CLOUD_ALLOCATION = { 1,1,1,1 };
//	public final static int[] CLOUD_ALLOCATION = { 2,2,2,2 };
	public final static int[] CLOUD_ALLOCATION = { 3,3,3,3 };


	// ================== Host Parameters =================//
	public final static int HOST_TYPES = 5;
//	public final static int[] VM_HOST_NUMBERS = { 0,0, 0, 11,0 }; //Homogeneous
	public final static int[] VM_HOST_NUMBERS = { 10,10, 10, 10,10 };

	public final static int[] HOST_MIPS = { 1500, 2000, 2500, 6000, 4200 };
	public final static int[] HOST_PES = { 1, 1, 2, 4, 4 };
	public final static int[] HOST_MAX_POWER = { 80, 120, 150, 162, 130 };
	public final static double[] HOST_MAX_POWER_PERCENT = { 0.3, 0.3, 0.3, 0.4,0.3 };
	public final static double[] HOST_MIN_UTIL = { 0.3, 0.3, 0.3, 0.3, 0.3 };//this is minimun mips for minimun power
//	public final static double[] HOST_MIN_UTIL = { -1,-1,-1, -1, -1 };//disable

	public final static int[] HOST_RAM = { 24576, 24576, 24576, 24576, 24576 };
	public final static int HOST_BW = 1000000; // 1 Gbit/s
	public final static int HOST_STORAGE = 1000000000; // 1 TB


	//To enable output Html set VM_POLICY to 1 and OUTPUTHTML to 1
	public final static int SCALEX =60*60;
	public final static int STEP_INTERVAL =10;
	public final static int MAXPOWERS =162;


	
	
	
	// ================== VM Parameters =================//

	public final static int[] VM_MIPS = { 750, 1000, 1500, 2000 };
	public final static int[] VM_PES = { 1, 1, 1, 1 };
	public final static int[] VM_RAM = { 512, 512, 1024, 1024 };
	public final static int VM_BW = 1000;
	public final static int VM_SIZE = 25000; // 2.5 GB

}
