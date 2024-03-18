package dev.melncat.identitytheft;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IdentityManager {
	private static final IdentityManager INSTANCE = new IdentityManager();
	private final Map<UUID, UUID> changedIdentitiesMap = new ConcurrentHashMap<>();
	
	private IdentityManager() {}
	
	public boolean hasChangedIdentity(UUID uuid) {
		return changedIdentitiesMap.containsKey(uuid);
	}
	
	public UUID getChangedIdentity(UUID uuid) {
		return changedIdentitiesMap.get(uuid);
	}
	
	public void setChangedIdentity(UUID from, UUID to) {
		if (from.equals(to)) removeChangedIdentity(from);
		else changedIdentitiesMap.put(from, to);
	}
	
	public void removeChangedIdentity(UUID uuid) {
		changedIdentitiesMap.remove(uuid);
	}
	
	public static IdentityManager getInstance() {
		return INSTANCE;
	}
	
	
}
