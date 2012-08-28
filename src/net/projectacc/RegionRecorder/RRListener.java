package net.projectacc.RegionRecorder;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RRListener implements Listener {

	public WorldGuardPlugin wgp;
	private RegionRecorder plugin;
	private RRecorder recorder;

	public RRListener(WorldGuardPlugin wgp, RegionRecorder plugin) {
		this.wgp = wgp;
		this.plugin = plugin;
		recorder = new RRecorder(plugin);
	}

	@EventHandler
	public void onAsyncPlayerChat (AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		Map<String,ProtectedRegion> regions =	wgp.getRegionManager(player.getWorld()).getRegions();
		
		for (Map.Entry<String, ProtectedRegion> region : regions.entrySet()) 
		{
			System.out.println(plugin.getRegionNames(player.getWorld()));
			if (plugin.getRegionNames(player.getWorld()).contains(region.getKey())) {
				int x,y,z;
				x = (int) player.getLocation().getX();
				y = (int) player.getLocation().getY();
				z = (int) player.getLocation().getZ();
				if (region.getValue().contains(x, y, z)) {
					recorder.addContent(region.getValue().getId() ,event.getMessage(), player);
					plugin.getConfiguration().save(player.getWorld());
				}
			}
		}
	}

}
