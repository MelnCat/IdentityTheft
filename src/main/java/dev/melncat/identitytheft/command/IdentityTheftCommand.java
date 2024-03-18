package dev.melncat.identitytheft.command;

import dev.melncat.identitytheft.IdentityManager;
import dev.melncat.identitytheft.IdentityTheft;
import dev.melncat.identitytheft.IdentityTheftConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class IdentityTheftCommand implements CommandExecutor, TabCompleter {
	private final IdentityTheft plugin;
	
	public IdentityTheftCommand(IdentityTheft plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.YELLOW + plugin.getName() + " " + ChatColor.GRAY + "v" + ChatColor.GREEN + plugin.getDescription().getVersion() + "\n"
				+ ChatColor.GRAY + "Made by " + ChatColor.GREEN + plugin.getDescription().getAuthors().get(0));
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("identitytheft.command.identitytheft.reload")) {
				sendMissingPermission(sender, "identitytheft.command.identitytheft.reload");
				return true;
			}
			plugin.reloadConfig();
			plugin.getITConfig().setConfig(plugin.getConfig());
			sender.sendMessage(ChatColor.YELLOW + "IdentityTheft configuration successfully reloaded.");
		} else if (args[0].equalsIgnoreCase("become")) {
			if (!sender.hasPermission("identitytheft.command.identitytheft.become")) {
				sendMissingPermission(sender, "identitytheft.command.identitytheft.become");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInsufficient arguments provided.\n&cSyntax: /it become <player>"));
				return true;
			}
			if (((Player) sender).getPlayerProfile().hasProperty("it_real")) {
				sender.sendMessage(ChatColor.RED + "You cannot use this while your identity is changed.\n" + ChatColor.RED + "Reset it first with /it reset.");
				return true;
			}
			UUID target = playerFromString(args[1]);
			if (target == null) {
				sender.sendMessage(ChatColor.RED + args[1] + " is not a valid player.");
				return true;
			}
			if (plugin.getITConfig().opProtection()
				&& !sender.isOp()
				&& Bukkit.getOperators().stream().anyMatch(x -> x.getUniqueId().equals(target))
			) {
				sender.sendMessage(ChatColor.RED + "You cannot change your identity into an operator.");
				return true;
			}
			IdentityManager.getInstance().setChangedIdentity(((Player) sender).getUniqueId(), target);
			((Player) sender).kickPlayer("Please rejoin for changes to apply.");
		} else if (args[0].equalsIgnoreCase("set")) {
			if (!sender.hasPermission("identitytheft.command.identitytheft.set")) {
				sendMissingPermission(sender, "identitytheft.command.identitytheft.set");
				return true;
			}
			if (args.length < 3) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInsufficient arguments provided.\n&cExample: /it set <from> <to>"));
				return true;
			}
			UUID from = playerFromString(args[1]);
			if (from == null) {
				sender.sendMessage(ChatColor.RED + args[1] + " is not a valid player.");
				return true;
			}
			UUID to = playerFromString(args[2]);
			if (to == null) {
				sender.sendMessage(ChatColor.RED + args[2] + " is not a valid player.");
				return true;
			}
			if (plugin.getITConfig().opProtection()
				&& !sender.isOp()
				&& Bukkit.getOperators().stream().anyMatch(x -> x.getUniqueId().equals(from) || x.getUniqueId().equals(to))
			) {
				sender.sendMessage(ChatColor.RED + "You cannot change the identities of operators.");
				return true;
			}
			IdentityManager.getInstance().setChangedIdentity(from, to);
			sender.sendMessage(ChatColor.YELLOW
				+ "The player "
				+ ChatColor.WHITE
				+ args[1]
				+ ChatColor.YELLOW
				+ " has successfully been changed to "
				+ ChatColor.WHITE
				+ args[2]
				+ ChatColor.YELLOW
				+ ".");
			if (Bukkit.getPlayer(from) != null) Bukkit.getPlayer(from).kickPlayer("Disconnected");
		} else if (args[0].equalsIgnoreCase("reset")) {
			if (!sender.hasPermission("identitytheft.command.identitytheft.reset")) {
				sendMissingPermission(sender, "identitytheft.command.identitytheft.reset");
				return true;
			}
			if (args.length < 2) {
				if (!(sender instanceof Player)) {
					
					sender.sendMessage(ChatColor.RED + "You must be a player to use /it reset without arguments.");
					return true;
				}
				;
				if (IdentityManager.getInstance().hasChangedIdentity(getRealPlayer(((Player) sender).getUniqueId()))) {
					IdentityManager.getInstance().removeChangedIdentity(getRealPlayer(((Player) sender).getUniqueId()));
					((Player) sender).kickPlayer("Please rejoin for changes to apply.");
				} else sender.sendMessage(ChatColor.RED + "Your identity is not altered.");
				return true;
			}
			if (!sender.hasPermission("identitytheft.command.identitytheft.reset.others")) {
				sendMissingPermission(sender, "identitytheft.command.identitytheft.reset.others");
				return true;
			}
			UUID target = playerFromString(args[1]);
			if (target == null) {
				sender.sendMessage(ChatColor.RED + args[1] + " is not a valid player.");
				return true;
			}
			if (IdentityManager.getInstance().hasChangedIdentity(target)) {
				IdentityManager.getInstance().removeChangedIdentity(target);
				sender.sendMessage(ChatColor.YELLOW + "The identity of " + args[1] + " has been reset.");
			} else sender.sendMessage(ChatColor.RED + "The identity of " + args[1] + "is not altered.");
		}
		return true;
	}
	private UUID getRealPlayer(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (player == null) return uuid;
		if (player.getPlayerProfile().hasProperty("it_real"))
			return UUID.fromString(player.getPlayerProfile().getProperties().stream().filter(x -> x.getName().equals("it_real")).findFirst().get().getValue());
		return uuid;
	}
	
	private void sendMissingPermission(CommandSender sender, String permission) {
		sender.sendMessage(ChatColor.RED + "You do not have permission to perform this command.\n"
			+ ChatColor.RED + "Missing permission "
			+ ChatColor.DARK_RED + permission);
	}
	
	private UUID playerFromString(String str) {
		try {
			return UUID.fromString(str);
		} catch (IllegalArgumentException e) {
			return Bukkit.getPlayerUniqueId(str);
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String name, String[] args) {
		List<String> suggestions = new ArrayList<>();
		if (args.length == 1) {
			if (sender.hasPermission("identitytheft.command.identitytheft.reload")) suggestions.add("reload");
			if (sender.hasPermission("identitytheft.command.identitytheft.become")) suggestions.add("become");
			if (sender.hasPermission("identitytheft.command.identitytheft.set")) suggestions.add("set");
			if (sender.hasPermission("identitytheft.command.identitytheft.reset")) suggestions.add("reset");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("become") || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("reset"))
				suggestions.addAll(listPlayerNames());
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set")) suggestions.addAll(listPlayerNames());
		}
		return suggestions.stream().filter(x -> x.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
	}
	
	private List<String> listPlayerNames() {
		return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
	}
}
