/*
 * 
 * @author Stefanos Mountrakis (stvakis@gmail.com)
 */

package demo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumMigrationTime;

import demo.files.ExportHtml;
import demo.files.GetPower;
import demo.files.PowerMonitor;

public class Main {
	private static List<PowerMonitor> monitorList = new ArrayList<PowerMonitor>();

	public static void main(String[] args) throws FileNotFoundException {

		Log.printLine("Baklavas XP...");
		OutputStream output = new FileOutputStream("output-text.txt");
		// Log.setOutput(output);  //This exports simulation log to file
		try {

			Collection<Object> conArgs = new ArrayList<Object>();
			//All Schedulers must be under  vmSchedulers
		
			//GetPower(ShortName, enable log (1-0),ClassName,Constructor Arguments)
			
			
			//Round Robin
			 GetPower RR = new GetPower("RR",0,"pink","RoundRobinVmAllocationPolicy",conArgs);
			 conArgs.clear();
			
		
				//DVFS
			GetPower DVFS = new GetPower("DVFS", 0, "Red", "DVFS" ,conArgs);
			conArgs.clear();

			
			//Single Threshold
			//Single theshold constructor (hostList <- this goes auto ,vmSelectionPolicy,utilizationThreshold);
			conArgs.add(new PowerVmSelectionPolicyMinimumMigrationTime()); // adding vmSelectionPolicy
			conArgs.add(0.8);// adding utilizationThreshold
			GetPower ST = new GetPower("ST", 0, "yellow", "PowerVmAllocationPolicyMigrationStaticThreshold", conArgs);

			
			GetPower npa = new GetPower("NPA", 0, "nocolor", null,null); // must be last, it runs at maximun of simulation time

			Log.printLine("Baklavas finished!");
			
			//Adding some static parameters from one GetPowerObject
			String simParAttr[] = { "" + PowerMonitor.maximunSimulation + "",
					"" + DVFS.getHostList().size() + "",
					"" + DVFS.getVmListSize() + "",
					"" + DVFS.getCloudletListSize() + "",
					"" + DVFS.getHostListTotalPower() + "",
					"" + DVFS.getMonitor().getVmListTotalMips() + "" };
			
			ExportHtml.printResults(simParAttr, monitorList);

		}

		catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}

	}
public static void AddToMonitorList(PowerMonitor monitor){
	monitorList.add(monitor);
}
	
	/**
	 * Prints the Cloudlet objects
	 * 
	 * @param list
	 *            list of Cloudlets
	 */
	public static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time" + indent + "USER");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet
						.getVmId() + indent + indent + indent + dft
						.format(cloudlet.getActualCPUTime()) + indent + indent + dft
						.format(cloudlet.getExecStartTime()) + indent + indent + indent + dft
						.format(cloudlet.getFinishTime()) + indent + cloudlet
						.getUserId());
			}
		}

	}

}
