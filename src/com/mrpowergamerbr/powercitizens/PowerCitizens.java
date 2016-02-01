package com.mrpowergamerbr.powercitizens;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

public class PowerCitizens extends PluginBase implements Listener {

	public ArrayList<NPC> npcs = new ArrayList<NPC>();
	
	public Config dreemurr = null;
	
	public void onEnable() {
		File  f = new File(getDataFolder() + "");
		f.mkdirs();
		
		dreemurr = new Config(new File(getDataFolder(), "dreemurr.yml"));

		dreemurr.save();

		getServer().getPluginManager().registerEvents(this, this);
		
		if (dreemurr.exists("NPCs")) {
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, Object> j = (LinkedHashMap<String, Object>) dreemurr.get("NPCs");
			for (Entry<String, Object> n1 : j.entrySet()) {
				String name = n1.getKey();
				NPC npc = new NPC();
				npc.x = Float.parseFloat((double) dreemurr.getDouble("NPCs." + name + ".X") + "");
				npc.y = Float.parseFloat((double) dreemurr.getDouble("NPCs." + name + ".Y") + "");
				npc.z = Float.parseFloat((double) dreemurr.getDouble("NPCs." + name + ".Z") + "");
				npc.skin = (String) dreemurr.getString("NPCs." + name + ".Skin");
				npc.worldName = (String) dreemurr.getString("NPCs." + name + ".World");
				npc.yaw = Float.parseFloat((double) dreemurr.getDouble("NPCs." + name + ".Yaw") + "");
				npc.pitch = Float.parseFloat((double) dreemurr.getDouble("NPCs." + name + ".Pitch") + "");
				npc.item = new Item((int) dreemurr.getInt("NPCs." + name + ".Item"));
				npc.name = (String) dreemurr.getString("NPCs." + name + ".Name");
				npc.m = this;
				
				npcs.add(npc);
				
				System.out.println(npc.name);
			}
		}
	}
	
	public void onDisable() {
		dreemurr.remove("NPCs");
		int index = 1;
		for (NPC npc : npcs) {
			System.out.println(npc.name);
			dreemurr.set("NPCs." + index + ".X", npc.x);
			dreemurr.set("NPCs." + index + ".Y", npc.y);
			dreemurr.set("NPCs." + index + ".Z", npc.z);
			dreemurr.set("NPCs." + index + ".Skin", npc.skin);
			dreemurr.set("NPCs." + index + ".World", npc.worldName);
			dreemurr.set("NPCs." + index + ".Yaw", npc.yaw);
			dreemurr.set("NPCs." + index + ".Pitch", npc.pitch);
			dreemurr.set("NPCs." + index + ".Item", npc.item.getId());
			dreemurr.set("NPCs." + index + ".Name", npc.name);
			index++;
		}
		
		dreemurr.save();
	}
	
	public void spawnAllNPCsFor(Player p) {
		for (NPC npc : npcs) {
			npc.spawn(p);
		}
	}
	
	public void spawnNPCsForAll(NPC npc) {
		for (Entry<String, Player> entrySet : getServer().getOnlinePlayers().entrySet()) {
			npc.spawn(entrySet.getValue());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		spawnAllNPCsFor(e.getPlayer());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().toLowerCase().contains("npc")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("§cSomente players podem usar este comando!"); 
				return true;
			}
			if (sender.hasPermission("PowerCitizens.Criar")) {
				if(args.length == 0) {
					sender.sendMessage("§8[§bPower§6NPCs§8] §a/npc criar <nome> <skin>");
					sender.sendMessage("§8[§bPower§6NPCs§8] §a/npc remover <nome>");
				}
				if (args.length == 1) {
					sender.sendMessage("§8[§bPower§6NPCs§8] §a/npc criar <nome> <skin>");
					sender.sendMessage("§8[§bPower§6NPCs§8] §a/npc remover <nome>");
				}
				if (args.length >= 2) {
					if (args[0].toLowerCase().equalsIgnoreCase("remover")) {
						sender.sendMessage("§8[§bPower§6NPCs§8] §aNPC removido com sucesso.");
					}
					if (args[0].toLowerCase().equalsIgnoreCase("criar")) {
						NPC npc = new NPC();
						npc.m = this;
						npc.name = args[1];
						npc.x = Float.parseFloat(((Entity) sender).getLocation().getX() + "");
						npc.y = Float.parseFloat(((Entity) sender).getLocation().getY() + "");
						npc.z = Float.parseFloat(((Entity) sender).getLocation().getZ() + "");
						npc.worldName = ((Entity) sender).getLocation().getLevel().getName();
						npc.yaw = Float.parseFloat(((Entity) sender).getLocation().getYaw() + "");
						npc.pitch = Float.parseFloat(((Entity) sender).getLocation().getPitch() + "");
						npc.item = ((EntityHuman) sender).getInventory().getItemInHand();
						if (args.length == 3) {
							npc.skin = args[2];
						}
						spawnNPCsForAll(npc);
						
						npcs.add(npc);
						
						sender.sendMessage("§8[§bPower§6NPCs§8] §aNPC criado com sucesso!");
					}
				}
			}

		}

		return false;

	}
}
