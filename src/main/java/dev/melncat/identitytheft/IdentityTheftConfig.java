package dev.melncat.identitytheft;

import org.bukkit.configuration.file.FileConfiguration;

public class IdentityTheftConfig {
	private FileConfiguration config;
	
	public IdentityTheftConfig(FileConfiguration config) {
		this.config = config;
	}
	
	public boolean opProtection() {
		return config.getBoolean("op-protection");
	}
	
	public void setConfig(FileConfiguration config) {
		this.config = config;
	}
}
