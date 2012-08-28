package net.projectacc.RegionRecorder;

import org.bukkit.entity.Player;

public class RRecorder {
	
	private RegionRecorder plugin;

	public RRecorder(RegionRecorder plugin) {
		this.plugin = plugin;
		
	}
	
	public void addContent(String region ,String msg,Player player ) {
		String s = String.format("%s:%s", player.getName(), msg);
		plugin.getConfiguration().put(region, s);
	}

}
