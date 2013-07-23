/*
 * 
 * @author Stefanos Mountrakis (stvakis@gmail.com)
 */

package demo.createSimulation;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

import demo.Constant;
import demo.extras.CloudletExtra;
import demo.extras.PowerVmExtra;

	public class GlobalBroker extends SimEntity {

		private static final int CREATE_BROKER = 0;
		private List<PowerVmExtra> vmList;
		private List<CloudletExtra> cloudletList;
		private DatacenterBroker broker;
		private int userId;
		public GlobalBroker(String name, int userId) {
			super(name);
			this.userId=userId;
		}

		@Override
		public void processEvent(SimEvent ev) {
			switch (ev.getTag()) {
			case CREATE_BROKER:
				setBroker(createBroker(super.getName()+"_"));

				//Create VMs and Cloudlets and send them to broker
				setVmList(VmCreate.createVmList(getBroker().getId(), userId));				
				setCloudletList(CloudletCreate.createCloudletList(getBroker().getId(), userId,getVmList()));

				broker.submitVmList(getVmList());
				broker.submitCloudletList(getCloudletList());
				CloudSim.resumeSimulation();
				break;

			default:
				Log.printLine(getName() + ": unknown event type");
				break;
			}
		}

		@Override
		public void startEntity() {
			Log.printLine(super.getName()+" is starting...");
			schedule(getId(), Constant.USERS_DELAY[userId], CREATE_BROKER);
		}

		@Override
		public void shutdownEntity() {
		}

		public List<PowerVmExtra> getVmList() {
			return vmList;
		}

		protected void setVmList(ArrayList<PowerVmExtra> arrayList) {
			this.vmList = arrayList;
		}

		public List<CloudletExtra> getCloudletList() {
			return cloudletList;
		}

		protected void setCloudletList(ArrayList<CloudletExtra> arrayList) {
			this.cloudletList = arrayList;
		}

		public DatacenterBroker getBroker() {
			return broker;
		}

		protected void setBroker(DatacenterBroker broker) {
			this.broker = broker;
		}
		
		
		private static DatacenterBroker createBroker(String name){

			DatacenterBroker broker = null;
			try {
				broker = new DatacenterBroker(name);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return broker;
		}
		

	}
	


