package net.projectacc.RegionRecorder;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RRCommander implements CommandExecutor {

	private RegionRecorder plugin;
	public WorldGuardPlugin wgp;

	public RRCommander(RegionRecorder plugin, WorldGuardPlugin wgp) {
		this.plugin = plugin;
		this.wgp = wgp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

		Player player = (Player) ((sender instanceof Player) ? sender : null);
		if (player == null)
			return false;

		if (!player.isOp()) {
			player.sendMessage(ChatColor.DARK_RED + "You can't use this command!");
			return true;
		}

		if (args.length == 1 && args[0].equals("new")) {
			if (isPlayerInRegion(player)) {
				ProtectedRegion region = this.getRegion(player);
				player.sendMessage(ChatColor.BLUE + "Chats:");
				for (String s :plugin.getConfiguration().getMessages(region.getId())) {
					s = s.replace(":", " : " + ChatColor.GREEN);
					player.sendMessage(ChatColor.AQUA + s);
				}				
			} else {
				player.sendMessage(ChatColor.RED + "Not in region");
			}
		}

		if (args.length >= 1 && args[0].equalsIgnoreCase("-old")) {
			if (isPlayerInRegion(player)) {
				ProtectedRegion region = this.getRegion(player);
				player.sendMessage(ChatColor.BLUE + "Chats:");
				List<String> list = plugin.getConfiguration().getOldMessages(region.getId());
				if (list != null)
				if (!list.isEmpty()) {
					for (String s :list) {
						s = s.replace(":", " : " + ChatColor.GREEN);
						player.sendMessage(ChatColor.AQUA + s);
					}	
				} else {
					player.sendMessage(ChatColor.RED + "No chats to display.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "Not in region");
			}
		}

		return true;
	}

	private boolean isPlayerInRegion (Player player) {
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
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	private ProtectedRegion getRegion(Player player) {
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
					return region.getValue();
				}
			}
		}
		return null;
	}

}
