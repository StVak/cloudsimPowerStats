/*
 * 
 * @author Stefanos Mountrakis (stvakis@gmail.com)
 */


package demo.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.List;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostStateHistoryEntry;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.power.PowerHost;

import demo.Constant;
import demo.extras.PowerHostExtra;
import demo.files.PowerMonitor;

public class ExportHtml {

	public static void printResults( String simParAttr[],
			List<PowerMonitor> monitors) throws IOException {
		exportHostStats(monitors);
		String monitorGraphs = "";
		String graphMonitor = "";		
		String menu = "<li><a href='./exports/combined.html'>Combined</a></li>";
		String inputNames = "'Non Power Aware',";
		String inputPower =PowerMonitor.npaPower+",";
		String inputNamesTr = "<tr><td>Non Power Aware</td><td style=\"text-align:center;\">" + String.format("%.2f",PowerMonitor.npaPower)  + "<td style=\"text-align:center;\">-</td><td style=\"text-align:center;\">-</td><td style=\"text-align:center;\">-</td></tr>";
		String simParTr = "";
		int i=0;
		for (PowerMonitor monitor : monitors) {
			monitor.PowerMonitorMath();
			menu += "<li><a href='./exports/" + monitor.getName() + ".html'>" + monitor
					.getName() + "</a></li>";

		}

		DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormat df2 = new DecimalFormat("###,###.###");

		// Header
		String header = "<!doctype html><html><head><title>CloudSim Simulation</title><link href='css/bootstrap.min.css' rel='stylesheet'><link href='css/bootstrap.css' rel='stylesheet'><script src='RGraph.common.core.js'></script><script src='RGraph.bar.js'></script><script src='RGraph.line.js'></script></script></head><div class='container'><div class='navbar'><div class='navbar-inner'><a class='brand' href='#'>Baklavas</a> <ul class='nav pull-right'><li class='active'><a href=./index.html>Home</a></li>" + menu + "</ul></div>";
		String footer = "</div></body></html>";

		// Building secondary pages
		String header2 = "<!doctype html><html><head><title>CloudSim Simulation</title><link href='../css/bootstrap.min.css' rel='stylesheet'><link href='../css/bootstrap.css' rel='stylesheet'><script src='../RGraph.common.core.js'></script><script src='../RGraph.common.tooltips.js'></script><script src='../RGraph.common.dynamic.js'></script><script src='../RGraph.bar.js'></script><script src='../RGraph.line.js'></script></script></head><body><div class='container'><div class='navbar'><div class='navbar-inner'><a class='brand' href='#'>Baklavas</a> <ul class='nav pull-right'><li><a href='../index.html'>Home</a></li>" + menu + "</ul></div>";
		String customHeader = header2.replaceAll("./exports/", "./");

		for (PowerMonitor monitor : monitors) {
			String[] hostCanvas = makeHostLine(monitor);
			File yourFile = new File(
					"output/exports/" + monitor.getName() + ".html");
			if (!yourFile.exists()) {
				yourFile.createNewFile();
			}

			customHeader = customHeader
					.replaceAll(
							"<li><a href='./" + monitor.getName() + ".html'>" + monitor
									.getName() + "</a></li>",
							"<li class='active'><a href='./" + monitor
									.getName() + ".html'>" + monitor.getName() + "</a></li>");

			monitorGraphs = "<script> window.onload = function (){" + makeLine(monitor) + hostCanvas[1] + "}</script>";
			graphMonitor += "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><h2 style='margin-top:40px;margin-bottom:30px;'>Average Power in active hosts (W)</h2><div class='span4'><canvas id='powerGraph" + monitor
					.getName() + "' width='1024' height='250'>[No canvas support]</canvas> <script></script></div></div>";
			graphMonitor += "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><h2 style='margin-top:40px;margin-bottom:30px;'>Average Utilization in active hosts (%)</h2><div class='span4'><canvas id='utilGraph" + monitor
					.getName() + "' width='1024' height='250'>[No canvas support]</canvas> <script></script></div></div>";
			graphMonitor += hostCanvas[0];

			try {

				PrintStream out = new PrintStream(new FileOutputStream(
						"output/exports/" + monitor.getName() + ".html"));

				out.println(customHeader + monitorGraphs + graphMonitor + footer);
				out.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			graphMonitor = "";
			customHeader = customHeader
					.replaceAll(
							"<li class='active'><a href='./" + monitor
									.getName() + ".html'>" + monitor.getName() + "</a></li>",
							"<li><a href='./" + monitor.getName() + ".html'>" + monitor
									.getName() + "</a></li>");

		//GRAPH (1stPage)
			inputNames += "'" + monitor.getName() + "'";
			inputPower += monitor.getDatacenterPower();
			
			inputNamesTr += "<tr><td>" + monitor.getName()  + "</td><td style=\"text-align:center;\"> " + df.format(monitor.getDatacenterPower()) + "</td><td style=\"text-align:center;\">" +
					""+df2.format(monitor.getVmListTotalInstCompleted())+"</td><td style=\"text-align:center;\">"+( (int)( monitor.getAvSLA()*100))+"%</td><td style=\"text-align:center;\">"+String.format("%d", monitor.getMigrations())+"</td></tr>";

			if (i != (monitors.size() - 1)) {
				inputNames += ",";
				inputPower += ",";
			}
			i++;
		}
		
		
	// Graph (1st page)

		String simParName[]={"Total Simulation Time", "Total Hosts","Total Vms","Total Cloudlets","Datacenter Mips Power","Total Mips Requested from VM"};

		for ( i = 0; i < simParName.length; i++) {
			simParTr += "<tr><td>" + simParName[i] + "</td><td>" + simParAttr[i] + "</td></tr>";
		}

		String graphs = "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><h1 style='margin-top:40px;margin-bottom:30px;'>Graph</h1><div class='span12'><script> window.onload = function (){" + "var data = [" + inputPower + "];var bar = new RGraph.Bar('myCanvas', data);bar.Set('chart.labels', [" + inputNames + "]);bar.Set('chart.gutter.left', 35); bar.Draw();}</script>" + "<canvas id='myCanvas' width='1000' height='450'>[No canvas support]</canvas></div></div>" +
				"<div class='row'>" + "<div class='span8'><table class='table table-bordered'><thead><tr><th>Scheduler Name</th><th>Power (kWh)</th><th>Total Instr Processed</th><th>Average SLA</tn><th>Migrations</th></thead><tbody>" + inputNamesTr + "</tbody></table></div><div class='span4'><table class='table table-bordered'><thead><tr><th>Simulation Information</th></thead><tbody>" + simParTr + "</tbody></table></div></div>";

		
		
		
		// Combined view

		String[] hostCompinedCanvas = makeHostCombinedLine(monitors);
		customHeader = header2.replaceAll("./exports/", "../exports/");
		customHeader = customHeader.replaceAll(
				"<li><a href='../exports/combined.html'>Combined</a></li>",
				"<li class='active'><a href='#'>Combined</a></li>");
		monitorGraphs = "<script> window.onload = function (){" + hostCompinedCanvas[1] + "}</script>";
		graphMonitor = "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><h2 style='margin-top:40px;margin-bottom:30px;'>Average Power in active hosts (W)</h2><div class='span4'><canvas id='powerGraph' width='1024' height='250'>[No canvas support]</canvas></div></div>";
		graphMonitor += "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><h2 style='margin-top:40px;margin-bottom:30px;'>Average Utilization in active hosts (%)</h2><div class='span4'><canvas id='utilGraph' width='1024' height='250'>[No canvas support]</canvas></div></div>";
		graphMonitor += hostCompinedCanvas[0];
		try {

			PrintStream out = new PrintStream(new FileOutputStream(
					"output/exports/combined.html"));

			out.println(customHeader + monitorGraphs + graphMonitor + footer);
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// ends here

	
		// Printing Hosts
		String host = "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><h1 style='margin-top:40px;margin-bottom:30px;'>Hosts</h1>";
		for ( i = 0; i < Constant.HOST_TYPES; i++) {
			if (Constant.VM_HOST_NUMBERS[i] == 0) {
				continue;
			}
			host += "<div class='span4'><table class='table table-bordered'><thead><tr><th>Host type " + i + " </th></tr></thead><tbody>" + "<tr><td>Hosts Used</td><td>" + Constant.VM_HOST_NUMBERS[i] + "</td></tr>" + "<tr><td>Mips</td><td>" + Constant.HOST_MIPS[i] + "</td></tr>" + "<tr><td>Cores</td><td>" + Constant.HOST_PES[i] + "</td></tr>" + "<tr><td>Ram</td><td>" + Constant.HOST_RAM[i] + "</td></tr>" + "<tr><td>Bandwidth</td><td>" + Constant.HOST_BW + "</td></tr>" + "<tr><td>Storage</td><td>" + Constant.HOST_STORAGE + "</td></tr>" + "<tr><td>Max Power</td><td>" + Constant.HOST_MAX_POWER[i] + "</td></tr>" + "<tr><td>Static Power</td><td>" + df
					.format((Constant.HOST_MAX_POWER_PERCENT[i] * Constant.HOST_MAX_POWER[i])) + "</td></tr></tbody></table></div>";
		}
		host += "</div>";

		// Printing Vm Characteristics
		String vm = "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><h1 style='margin-top:40px;margin-bottom:30px;'>Vm</h1>";
		for ( i = 0; i < Constant.VM_MIPS.length; i++) {
			vm += "<div class='span4'><table class='table table-bordered'><thead><tr><th>Vm type " + (i + 1) + "</th></tr></thead><tbody>" + "<tr><td>Mips</td><td>" + Constant.VM_MIPS[i] + "</td></tr>" + "<tr><td>Cores</td><td>" + Constant.VM_PES[i] + "</td></tr>" + "<tr><td>Ram</td><td>" + Constant.VM_RAM[i] + "</td></tr>" + "<tr><td>Bandwidth</td><td>" + Constant.VM_BW + "</td></tr>" + "<tr><td>Storage</td><td>" + Constant.VM_SIZE + "</td></tr></tbody></table></div>";
		}
		vm += "</div>";

		// Printing Cloudlet Characteristics
		String cloudlet = "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><h1 style='margin-top:40px;margin-bottom:30px;'>Cloudlets</h1>";
		for ( i = 0; i < Constant.CLOUDLET_LENGTH.length; i++) {
			cloudlet += "<div class='span4'><table class='table table-bordered'><thead><tr><th>Cloutlet type " + (i + 1) + "</th></tr></thead><tbody>" + "<tr><td>Length (MI)</td><td>" + Constant.CLOUDLET_LENGTH[i] + "</td></tr>" + "<tr><td>Input filesize</td><td>" + Constant.CLOUDLET_FILESIZE[i] + "</td></tr>" + "<tr><td>Output filesize</td><td>" + Constant.CLOUDLET_OUTPUTSIZE[i] + "</td></tr>" + "<tr><td>PES</td><td>" + Constant.CLOUDLET_PES[i] + "</td></tr></tbody></table></div>";
		}
		cloudlet += "</div>";

		// Printing Users Characteristics
		String users = "<div class='row' style='border-top: 2px solid #eeeeee;margin-bottom:50px;'><h1 style='margin-top:40px;margin-bottom:30px;'>Users</h1>";
		String vmUser = "";

		for ( i = 0; i < Constant.USERS_DELAY.length; i++) {
			for (int k = 0; k < Constant.USR_VMNUM_OWNED[i].length; k++) {

				vmUser += "<tr><td>Vm Type " + (Constant.USR_TYPE_OWNED[i][k] + 1) + " with Cloudlet Type " + (Constant.USR_CLOUDLET_TYPE[i][k] + 1)+"</td><td>" + Constant.USR_VMNUM_OWNED[i][k] + "</td></tr>";
			}

		/*	for (int k = 0; k < Constant.USR_CLOUDLET_TYPE[i].length; k++) {
				//vmUser += "<tr><td>Cloudlet Type " + (Constant.USR_CLOUDLET_TYPE[i][k] + 1) + "</td><td>" + Constant.USR_VMNUM_OWNED[i][k] + "</td></tr>";
				vmUser += "<tr><td>Cloudlet Type " + (Constant.USR_CLOUDLET_TYPE[i][k] + 1) + "</td><td>" + Constant.USR_VMNUM_OWNED[i][k] + "</td></tr>";
			}
*/
			users += "<div class='span4'><table class='table table-bordered'><thead><tr><th>User " + (i + 1) + "</th></tr></thead><tbody>" + "<tr><td>Time</td><td>" + Constant.USERS_DELAY[i] + "</td></tr>" + vmUser + "</tbody></table></div>";
			vmUser = "";
		}
		users += "</div>";

		try {

			PrintStream out = new PrintStream(new FileOutputStream(
					"output/index.html"));

			out.println(header + graphs + host + vm + users + cloudlet + footer);
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Log.enable();
		Log.printLine("All files writen!");
		Log.printLine("<--- Baklavas -->");
		Log.disable();
	}

	private static String makeLine(PowerMonitor monitor) {
		String customTooltipsUtilAv = monitor.getAverUtil().replaceAll(" ",
				"'<b>" + monitor.getName() + "</b><br/>");
		customTooltipsUtilAv = customTooltipsUtilAv.replaceAll(",", "',");
		String customTooltipsPowerAv = monitor.getAverPower().replaceAll(" ",
				"'<b>" + monitor.getName() + "</b><br/>");
		customTooltipsPowerAv = customTooltipsPowerAv.replaceAll(",", "',");

		customTooltipsPowerAv = "AverPower" + monitor.getName() + ".Set('chart.tooltips', [" + customTooltipsPowerAv + "']);";
		customTooltipsUtilAv = "Util" + monitor.getName() + ".Set('chart.tooltips', [" + customTooltipsUtilAv + "']);";
		customTooltipsPowerAv = "";
		customTooltipsUtilAv = "";
		String powerUtilHtml = "var AverPower" + monitor.getName() + " = new RGraph.Line('powerGraph" + monitor
				.getName() + "', [" + monitor.getAverPower() + "]);AverPower" + monitor
				.getName() + ".Set('chart.labels', [" + monitor.getTimes() + "]);" + customTooltipsPowerAv + "AverPower" + monitor
				.getName() + ".Set('chart.colors', ['" + monitor.getColor() + "']);AverPower" + monitor
				.getName() + ".Set('chart.linewidth', 3);AverPower" + monitor
				.getName() + ".Set('chart.ymax', " + Constant.MAXPOWERS + ");AverPower" + monitor
				.getName() + ".Set('chart.shadow', true);AverPower" + monitor
				.getName() + ".Draw();" + "var Util" + monitor.getName() + " = new RGraph.Line('utilGraph" + monitor
				.getName() + "', [" + monitor.getAverUtil() + "]);" + "Util" + monitor
				.getName() + ".Set('chart.labels', [" + monitor.getTimes() + "]);Util" + monitor
				.getName() + ".Set('chart.colors', ['" + monitor.getColor() + "']);Util" + monitor
				.getName() + ".Set('chart.ymax', 100);" + customTooltipsUtilAv + "Util" + monitor
				.getName() + ".Set('chart.linewidth', 3);Util" + monitor
				.getName() + ".Set('chart.shadow', true);Util" + monitor
				.getName() + ".Draw();";
		return powerUtilHtml;
	}

	public static String[] makeHostLine(PowerMonitor monitor) {
		List<Host> hostList = monitor.getHostList();
		double step = Constant.STEP_INTERVAL;
		double sched_int = Constant.SCHEDULING_INTERVAL;

		String[] graphMonitor = new String[2];
		graphMonitor[0] = "";
		graphMonitor[1] = "";

		int pos = 1;
		for (Host h : hostList) {
			PowerHostExtra big = (PowerHostExtra) h;
			int nameVar = big.getId();

			graphMonitor[0] += "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'><div class='span6' style=''><h3 style='margin-top:40px;margin-bottom:30px;'>Utilization (%) at host " + nameVar + "</h3><canvas id='util" + nameVar + "' width='450' height='250'>[No canvas support]</canvas> <script></script></div>";
			graphMonitor[0] += "<div class='span6' style=''><h3 style='margin-top:40px;margin-bottom:30px;'>Power (W) at host " + nameVar + "</h3><canvas id='power" + nameVar + "' width='450' height='250'>[No canvas support]</canvas> <script></script></div></div>";

			boolean firstTime = true;
			String averUtil = "";
			String averPower = "";
			String times = "";
			double previoustime = 0;
			int deigmata = (int) (monitor.getMaxSimTime() / sched_int / step);

			for (HostStateHistoryEntry host : big.getStateHistory()) {
				if (host.getTime() >= Constant.STEP_INTERVAL) {
					previoustime = host.getTime();
					break;
				}
			}
Log.enable();
			for (HostStateHistoryEntry host : big.getStateHistory()) {
				if ((host.getTime() < (previoustime)) && !firstTime) {
					Log.printLine(host.getTime() + "  <--------dropped " + previoustime);
					continue;
				}
				if (!firstTime) {
					times += ",";
					averUtil += ",";
					averPower += ",";
				}
				firstTime = false;
				if ((int) host.getTime() <= ((int) (previoustime + sched_int))) {

					if (pos == deigmata) {
						times += "'" + ((int) (host.getTime() / Constant.SCALEX)) + "'";
						pos = 1;
					} else {
						times += "''";
						pos++;

					}
					
					averPower +=" " + String.format("%.2f",  big.getPowerModel().getPower(host.getAllocatedMips() / big.getTotalMips()));
					averUtil +=" " + String.format("%.2f",  (host.getAllocatedMips() * 100 / big .getTotalMips()));
					previoustime = host.getTime();
				} else {
					while (previoustime < (host.getTime() - 2*sched_int)) {
						
						Log.printLine((host.getTime() - sched_int) + "  --------zeroed " + previoustime + " monitor " + monitor.getName());
						previoustime += sched_int;
						if (pos == deigmata) {
							times += "'" + ((int) (previoustime / Constant.SCALEX)) + "',";
							pos = 1;
						} else {
							times += "'',";
							pos++;
						}
						averPower += "0,";
						averUtil += "0,";
					}
					if (pos == deigmata) {
						times += "'" + ((int) (host.getTime() / Constant.SCALEX)) + "'";
						pos = 1;
					} else {
						times += "''";
						pos++;
					}
					averPower +=" " + String.format("%.2f",  big.getPowerModel().getPower(host.getAllocatedMips() / big.getTotalMips()));
					averUtil +=" " + String.format("%.2f",  (host.getAllocatedMips() * 100 / big .getTotalMips()));
					previoustime = host.getTime();
				}

			}
			// This is for differnent Ending Time
			// Log.printLine("PREVIOUS TIME = " + previoustime);
			if (previoustime < monitor.getMaxSimTime()) {
				while (previoustime < (monitor.getMaxSimTime() - sched_int)) {
					previoustime += sched_int;
					if (pos == deigmata) {
						times += ",'" + ((int) (previoustime / Constant.SCALEX)) + "'";
						pos = 1;
					} else {
						times += ",''";
						pos++;
					}
					averPower += "0, ";
					averUtil += "0, ";
				}
				previoustime += sched_int;
				if (pos == deigmata) {
					times += ",'" + ((int) (previoustime / Constant.SCALEX)) + "'";
				} else {
					times += ",''";
				}
				averPower += "0";
				averUtil += "0";

			}
			// ends here
Log.disable();
			String customTooltipsUtil = averUtil.replaceAll(" ",
					"'<b>" + monitor.getName() + "</b><br/>");
			customTooltipsUtil = customTooltipsUtil.replaceAll(",", "',");

			String customTooltipsPower = averPower.replaceAll(" ",
					"'<b>" + monitor.getName() + "</b><br/>");
			customTooltipsPower = customTooltipsPower.replaceAll(",", "',");
			double maxPower=0;
			for (Host hfind:hostList){
				PowerHost hmax= (PowerHost) hfind;
				if (hmax.getId()==nameVar){
					maxPower=hmax.getMaxPower();
				}
			}
			
		
			graphMonitor[1] += "var Power" + nameVar + " = new RGraph.Line('power" + nameVar + "', [" + averPower + "]);Power" + nameVar + ".Set('chart.tooltips', [" + customTooltipsPower + "']);Power" + nameVar + ".Set('chart.labels', [" + times + "]);Power" + nameVar + ".Set('chart.colors', ['" + monitor
					.getColor() + "']);" + "Power" + nameVar + ".Set('chart.linewidth', 3);Power" + nameVar + ".Set('chart.shadow', true);Power" + nameVar + ".Set('chart.ymax', " + maxPower + ");Power" + nameVar + ".Draw();" + "var Util" + nameVar + " = new RGraph.Line('util" + nameVar + "', [" + averUtil + "]);Util" + nameVar + ".Set('chart.ymax', 100);Util" + nameVar + ".Set('chart.linewidth', 3);Util" + nameVar + ".Set('chart.tooltips', [" + customTooltipsUtil + "']);Util" + nameVar + ".Set('chart.colors', ['" + monitor
					.getColor() + "']);Util" + nameVar + ".Set('chart.shadow', true);" + "Util" + nameVar + ".Set('chart.labels', [" + times + "]);Util" + nameVar + ".Draw();";
		}

		return graphMonitor;
	}

	// returns [0]=canvas , [1]=js
	private static String[] makeHostCombinedLine(List<PowerMonitor> monitorList) {
		int hostnum = 0;
		double step = Constant.STEP_INTERVAL;
		double sched_int = Constant.SCHEDULING_INTERVAL;
		boolean firstMonitor = true;
		String[] graphMonitor = new String[2];
		String powerUtilHtml = "";
		graphMonitor[0] = "";
		graphMonitor[1] = "";
		int pos = 1;
		for (PowerMonitor monitor : monitorList) {
			String customTooltipsUtilAv = monitor.getAverUtil().replaceAll(" ",
					"'<b>" + monitor.getName() + "</b><br/>");
			customTooltipsUtilAv = customTooltipsUtilAv.replaceAll(",", "',");
			String customTooltipsPowerAv = monitor.getAverPower().replaceAll(
					" ", "'<b>" + monitor.getName() + "</b><br/>");
			customTooltipsPowerAv = customTooltipsPowerAv.replaceAll(",", "',");
			powerUtilHtml += "var AverPower" + monitor.getName() + " = new RGraph.Line('powerGraph', [" + monitor
					.getAverPower() + "]);AverPower" + monitor.getName() + ".Set('chart.labels', [" + monitor
					.getTimes() + "]);AverPower" + monitor.getName() + ".Set('chart.colors', ['" + monitor
					.getColor() + "']);AverPower" + monitor.getName() + ".Set('chart.linewidth', 3);AverPower" + monitor
					.getName() + ".Set('chart.ymax', " + Constant.MAXPOWERS + ");" +
			// "AverPower" + monitor.getName() +
			// ".Set('chart.tooltips', [" + customTooltipsPowerAv +
			// "']);" +
			"AverPower" + monitor.getName() + ".Set('chart.shadow', true);AverPower" + monitor
					.getName() + ".Draw();" + "var Util" + monitor.getName() + " = new RGraph.Line('utilGraph', [" + monitor
					.getAverUtil() + "]);" + "Util" + monitor.getName() + ".Set('chart.labels', [" + monitor
					.getTimes() + "]);Util" + monitor.getName() + ".Set('chart.colors', ['" + monitor
					.getColor() + "']);" +
			// "Util"+ monitor.getName() + ".Set('chart.tooltips', [" +
			// customTooltipsUtilAv + "']);" +
			"Util" + monitor.getName() + ".Set('chart.ymax', 100);	Util" + monitor
					.getName() + ".Set('chart.linewidth', 3);Util" + monitor
					.getName() + ".Set('chart.shadow', true);Util" + monitor
					.getName() + ".Draw();";
			hostnum = 0;
			for (double[] big : monitor.getAllInfo()) {
				int hostId = (int) big[0];
				String nameVar = hostId + monitor.getName();
				if (firstMonitor) {
					graphMonitor[0] += "<div class='row' style='border-top: 1px solid #eeeeee;margin-bottom:50px;'>" + "<div class='span6' style='margin-right:4%;'><h3 style='margin-top:40px;margin-bottom:30px;'>Utilization (%) at host " + hostId + "</h3><canvas id='util" + hostId + "' width='450' height='250'>[No canvas support]</canvas> <script></script></div>";
					graphMonitor[0] += "<div class='span6' style=''><h3 style='margin-top:40px;margin-bottom:30px;'>Power (W) at host " + hostId + "</h3><canvas id='power" + hostId + "' width='450' height='250'>[No canvas support]</canvas> <script></script></div></div>";
				}
				boolean firstTime = true;
				String averUtil = "";
				String averPower = "";
				double previoustime = monitor.getMaxSimTime();
				int deigmata = (int) (monitor.getMaxSimTime() / sched_int / step);
				for (double[] host : monitor.getAllInfo()) {
					if (host[0] == big[0]) {
						if (!firstTime) {
							averUtil += ",";
							averPower += ",";
						}
						firstTime = false;
						if ((int) host[4] <= ((int) (previoustime + sched_int))) {
							if (pos == deigmata) {
								pos = 1;
							} else {
								pos++;

							}
							
							
							averPower +=" " + String.format("%.2f",  host[1]);
							averUtil +=" " + String.format("%.2f",  host[3]);
							previoustime = host[4];
						} else {
							while (previoustime < (host[0] - 2*sched_int)) {
								previoustime += sched_int;
								if (pos == deigmata) {
									pos = 1;
								} else {
									pos++;
								}
								averPower += "0,";
								averUtil += "0,";
							}
							if (pos == deigmata) {
								pos = 1;
							} else {
								pos++;
							}
							averPower +=" " + String.format("%.2f",  host[1]);
							averUtil +=" " + String.format("%.2f",  host[3]);
							previoustime = host[4];
						}
					}
				}
				String customTooltipsUtil = averUtil.replaceAll(" ",
						"'<b>" + monitor.getName() + "</b><br/>");
				customTooltipsUtil = customTooltipsUtil.replaceAll(",", "',");

				String customTooltipsPower = averPower.replaceAll(" ",
						"'<b>" + monitor.getName() + "</b><br/>");
				customTooltipsPower = customTooltipsPower.replaceAll(",", "',");
				graphMonitor[1] += "var Power" + nameVar + " = new RGraph.Line('power" + hostId + "', [" + averPower + "]);Power" + nameVar + ".Set('chart.labels', [" + monitor
						.getTimes() + "]);Power" + nameVar + ".Set('chart.colors', ['" + monitor
						.getColor() + "']);Power" + nameVar + ".Set('chart.linewidth', 3);Power" + nameVar + ".Set('chart.shadow', true);Power" + nameVar + ".Set('chart.ymax', " + big[2] + ");" +
				// "Power" + nameVar + ".Set('chart.tooltips', [" +
				// customTooltipsPower + "']);" +
				"Power" + nameVar + ".Draw();" + "var Util" + nameVar + " = new RGraph.Line('util" + hostId + "', [" + averUtil + "]);Util" + nameVar + ".Set('chart.ymax', 100);Util" + nameVar + ".Set('chart.linewidth', 3);Util" + nameVar + ".Set('chart.colors', ['" + monitor
						.getColor() + "']);Util" + nameVar + ".Set('chart.shadow', true);" +
				// "Util" + nameVar + ".Set('chart.tooltips', [" +
				// customTooltipsUtil + "']);" +
				"Util" + nameVar + ".Set('chart.labels', [" + monitor
						.getTimes() + "]);Util" + nameVar + ".Draw();";

				if (hostnum == (monitor.getHostNum() - 1)) {
					break;
				}

				hostnum++;
			}
			firstMonitor = false;
		}
		graphMonitor[1] += powerUtilHtml;
		return graphMonitor;
	}
	
	private static void exportHostStats(List<PowerMonitor> monitors) throws IOException{
		for (PowerMonitor monitor :monitors ) {
			boolean dir = (new File("output/graphs/"+monitor.getName())).mkdirs();	
			String time="";
			String history="";
			String historyPower="";

			for (Host hosta:  monitor.getHostList( )){
				PowerHostExtra host=(PowerHostExtra) hosta;
				historyPower="";
				history="";
				time="";				
				for (HostStateHistoryEntry entry : host.getStateHistory() ){
					time+= entry.getTime() + System.getProperty("line.separator");
					history+= (entry.getAllocatedMips()*100/host.getTotalMips())+ System.getProperty("line.separator");
					historyPower+= host.getPowerModel().getPower(entry.getAllocatedMips()/host.getTotalMips())+ System.getProperty("line.separator");
				}
				
				
				
				
				File yourFile = new File(
						"output/graphs/"+monitor.getName() +"/"+ host.getId() + "Util.txt");
				File yourFileT = new File(
						"output/graphs/"+monitor.getName()  +"/"+ host.getId() + "Power.txt");
				if (!yourFileT.exists()) {
					yourFileT.createNewFile();}
					try {

						PrintStream out = new PrintStream(new FileOutputStream(
								"output/graphs/"+monitor.getName() +"/"+ host.getId() + "Util.txt"));
						out.println(history);
						out.close();
						
						 out = new PrintStream(new FileOutputStream(
								"output/graphs/"+monitor.getName() +"/"+ host.getId() + "Power.txt"));
						out.println(historyPower);
						out.close();			

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				
				
				
				
			}
			File timeFile = new File(
					"output/graphs/"+monitor.getName()  +"/_Times.txt");
			if (!timeFile.exists()) {
				timeFile.createNewFile();}
				try {

					PrintStream out = new PrintStream(new FileOutputStream(
							"output/graphs/"+monitor.getName()  +"/_Times.txt"));
					out.println(time);
					out.close();					

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			
			
			
		}	
		
	}

}
