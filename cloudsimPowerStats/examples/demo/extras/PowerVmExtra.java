/*
 * 
 * @author Stefanos Mountrakis (stvakis@gmail.com)
 */

package demo.extras;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.power.PowerVm;

public class PowerVmExtra extends PowerVm {

	private double deadline;
	private double maxMips;
	private double minMips;
	public PowerVmExtra(int id, int userId, double mips, int pesNumber,
			int ram, long bw, long size, int priority, String vmm,
			CloudletScheduler cloudletScheduler, double schedulingInterval,double deadline,double maxMips,double minMips) {
		super(id, userId, mips, pesNumber, ram, bw, size, priority, vmm,
				cloudletScheduler, schedulingInterval);
		this.setDeadline(deadline);
		this.setMaxMips(maxMips);
		this.setMinMips(minMips);		
		// TODO Auto-generated constructor stub
	}

	public void setNewMips(double mips){
		setMips(mips);
	}

	public double getMinMips() {
		return minMips;
	}

	public void setMinMips(double minMips) {
		this.minMips = minMips;
	}

	public double getMaxMips() {
		return maxMips;
	}

	public void setMaxMips(double maxMips) {
		this.maxMips = maxMips;
	}

	public double getDeadline() {
		return deadline;
	}

	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}
	
}
