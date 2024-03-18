package dev.melncat.identitytheft;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class IdentityTheftListener implements Listener {
	private final IdentityTheft plugin;
	
	public IdentityTheftListener(IdentityTheft plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void on(AsyncPlayerPreLoginEvent event) {
		if (IdentityManager.getInstance().hasChangedIdentity(event.getUniqueId())) {
			PlayerProfile newProfile = Bukkit.createProfile(IdentityManager.getInstance().getChangedIdentity(event.getUniqueId()));
			newProfile.complete(true);
			newProfile.setProperty(new ProfileProperty("it_real", event.getUniqueId().toString()));
			event.setPlayerProfile(newProfile);
		}
	}
}
