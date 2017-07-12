package us.blockbox.uilib;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CooldownManager{
	private final JavaPlugin plugin;
	private final Set<UUID> cooling = new HashSet<>();
	private final long coolDuration;

	public CooldownManager(JavaPlugin plugin,long coolDuration){
		this.plugin = plugin;
		this.coolDuration = coolDuration;
	}

	public boolean add(UUID uuid){
		if(cooling.add(uuid)){
			new CooldownRemovalTask(uuid).runTaskLater(plugin,coolDuration);
			return true;
		}
		return false;
	}

	public boolean isCooling(UUID uuid){
		return cooling.contains(uuid);
	}

	private class CooldownRemovalTask extends BukkitRunnable{
		private final UUID uuid;

		private CooldownRemovalTask(UUID uuid){
			this.uuid = uuid;
		}

		@Override
		public void run(){
			cooling.remove(uuid);
		}
	}
}