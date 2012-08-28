package net.projectacc.RegionRecorder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class RRConfiguration {

	public RegionRecorder plugin;
	private FileConfiguration config;

	private HashMap<World, List<String>> map = new HashMap<World,List<String>>();

	//<Region name, data> data = "Player: Message"
	private HashMap<String, List<String>> oldMessages = new HashMap<String, List<String>>();
	private HashMap<String, List<String>> messages = new HashMap<String, List<String>>();

	public RRConfiguration (RegionRecorder plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
	}

	public void load() {
		List<World> w = plugin.getServer().getWorlds();
		for (World world : w) {
			try {
				if (!config.contains("this is")) {
					config.set("this is", "CONFIG!");
					List<String> l = new ArrayList<String>();
					l.add("region1");
					l.add("region2");
					config.set(world.getName(),l);
					plugin.saveConfig();
				}
				List<String> list = (List<String>) config.getList(world.getName());
				map.put(world, list);
			} catch(Exception e) {}

			File dir = new File("plugins/RegionRecorder/" + world.getName());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			for (String s : dir.list()) {
				
				File dir2 = new File(dir, s+"/");
				for (File f2 : dir2.listFiles()) {
					try {
						String nameFile = f2.getName().replace(".txt", "");
						
						Scanner scan = new Scanner(f2);

						while (scan.hasNextLine()) {
							String s3 = scan.nextLine();
							SimpleDateFormat simpleDateformat=new SimpleDateFormat("MM-dd-yy");
							String d = simpleDateformat.format(new Date());

							if (d.equals(s)) {
								if (messages.containsKey(nameFile)) {
									List<String> list = messages.get(nameFile);
									list.add(s3);
									messages.put(nameFile, list);
								} else {
									List<String> list = new ArrayList<String>();
									list.add(s3);
									messages.put(nameFile, list);
								}
							} else {
								if (oldMessages.containsKey(nameFile)) {
									List<String> list = oldMessages.get(nameFile);
									list.add(s3);
									oldMessages.put(nameFile, list);
								} else {
									List<String> list = new ArrayList<String>();
									list.add(s3);
									oldMessages.put(nameFile, list);
								}
							}
						}
						scan.close();
					} catch (FileNotFoundException e) {}
				}
			}
		}

	}

	public List<String> getRegionNames(World world) {
		if (map.containsKey(world)) {

			return map.get(world);
		} else {
			return null;
		}


	}

	public void save() {
		for (World w : plugin.getServer().getWorlds()) {
			save(w);			
		}
	}

	public void save(World w) {
		SimpleDateFormat simpleDateformat=new SimpleDateFormat("MM-dd-yy");
		String d = simpleDateformat.format(new Date());

		List<File> files = new ArrayList<File>();

		for (Map.Entry<String, List<String>> m : messages.entrySet()) {
			File f = new File("plugins/RegionRecorder/" + w.getName() + "/" + d + "/" + m.getKey() + ".txt");

			if (!f.exists()) {
				try {
					System.out.println(f.getParentFile().mkdirs());
					System.out.println(f.createNewFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
			}
			try {
				FileWriter fstream = new FileWriter(f);				
				BufferedWriter out = new BufferedWriter(fstream);

				for (String s : m.getValue()) {
					out.write(s);
					out.newLine();
				}
				out.close();
				fstream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void put(String region, String s) {
		List<String> l = new ArrayList<String>();

		if (messages.containsKey(region)) {
			l = messages.get(region);
		}		
		l.add(s);

		messages.put(region, l);		
	}
	
	public List<String> getMessages(String s) {
		List<String> l = messages.get(s);
		//Collections.sort(l);
		return l;
	}
	
	public List<String> getOldMessages(String s) {
		List<String> l = oldMessages.get(s);
		//Collections.sort(l);
		return l;
	}
}
