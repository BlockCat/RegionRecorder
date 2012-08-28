package net.projectacc.RegionRecorder;

import java.util.List;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RegionRecorder extends JavaPlugin{
	
	public WorldGuardPlugin wgp;
	private RRConfiguration conf; 
	private RRListener listener;
	
	public void onEnable() {
		wgp = getWorldGuard();
		conf = new RRConfiguration(this);
		conf.load();
		listener = new RRListener(wgp, this);
		this.getServer().getPluginManager().registerEvents(new RRListener(wgp, this), this);
		this.getCommand("RR").setExecutor(new RRCommander(this, wgp));
	}
	
	public void onDisable() {
		conf.save();
	}
	
	
	private WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    // WorldGuard may not be loaded
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	        return null; // Maybe you want throw an exception instead
	    }
	 
	    return (WorldGuardPlugin) plugin;
	}
	
	public List<String> getRegionNames(World world) {
		return conf.getRegionNames(world);
	}
	
	public RRConfiguration getConfiguration() {
		return conf;
	}
	
	

}
