/*
 * 
 * @author Stefanos Mountrakis (stvakis@gmail.com)
 */

package demo.createSimulation;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelStochastic;

import demo.Constant;
import demo.extras.CloudletExtra;
import demo.extras.PowerVmExtra;
import demo.files.GetPower;

public class CloudletCreate {

	public static ArrayList<CloudletExtra> createCloudletList(int brokerId, int usr,
			List<PowerVmExtra> vmList) {
		ArrayList<CloudletExtra> list = new ArrayList<CloudletExtra>();
		UtilizationModel utilizationModel;
		int i = 0;
		for (int type_of_cloudlet = 0; type_of_cloudlet < Constant.USR_CLOUDLET_TYPE[usr].length; type_of_cloudlet++) {


			for (int cloudlet_number = 0; cloudlet_number < Constant.USR_VMNUM_OWNED[usr][type_of_cloudlet]; cloudlet_number++) {
				
				UtilizationModel utilizationModelNull = new UtilizationModelNull();
					int j =  Constant.USR_CLOUDLET_TYPE[usr][type_of_cloudlet];
				if ((Constant.RANDOM_UTILIZATION == 1)) {
					utilizationModel = new UtilizationModelStochastic(
							Constant.CLOUDLET_UTILIZATION_SEED[j] * type_of_cloudlet);

				} else {
					utilizationModel = new UtilizationModelFull();
				}
				int cloudId = 1000 * (Constant.USR_CLOUDLET_TYPE[usr][type_of_cloudlet]) + 10000 * brokerId+100000*(type_of_cloudlet+1);
				int length=0;
				if(GetPower.getAllocName()=="NPA"){
					  length=(int) (100000*Constant.SIMULATION_LIMIT);
				}else {
					  length=Constant.CLOUDLET_LENGTH[j];
				}
				CloudletExtra cloudlet = new CloudletExtra(cloudId + cloudlet_number + 1,
						length, Constant.CLOUDLET_PES[j],
						Constant.CLOUDLET_FILESIZE[j],
						Constant.CLOUDLET_OUTPUTSIZE[j], utilizationModel,
						utilizationModelNull, utilizationModelNull);

				// this is for stohastic,gets the first cloudlets only

				if (Constant.RANDOM_UTILIZATION == 1) {
					if (i >= vmList.size()) {
						break;

					} else {
						Log.printLine(vmList.size() + " " + i);
						cloudlet.setUserId(brokerId);
						cloudlet.setVmId(vmList.get(i).getId());
						list.add(cloudlet);
						i++;
					}
				}

				if (Constant.RANDOM_UTILIZATION == 0) {
					if (i >= vmList.size()) {
						break;

					} else {
						cloudlet.setUserId(brokerId);
						cloudlet.setVmId(vmList.get(i).getId());
						list.add(cloudlet);
						i++;

					}

				}


			}
		}


		return list;
	}

}
