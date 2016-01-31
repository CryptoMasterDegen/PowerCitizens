package com.mrpowergamerbr.powercitizens;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityData;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;

public class NPC {
	public String name;
	public String worldName;
	float x, y, z, pitch, yaw;
	Item item;
	Random rand;
	PowerCitizens m;
	public String skin;

	public NPC() {
		rand = new Random();
	}

	public void despawn(Player p) {
		
	}
	
	@SuppressWarnings("rawtypes")
	public void spawn(Player p) {
		AddPlayerPacket npc = new AddPlayerPacket();

		npc.username = name;
		npc.x = x;
		npc.y = y;
		npc.z = z;
		npc.eid = Entity.entityCount + rand.nextInt(10000);
		npc.uuid = UUID.randomUUID();
		npc.item = item;
		npc.pitch = pitch;
		npc.yaw = yaw;
		npc.metadata = new HashMap<Integer, EntityData>();
		npc.speedX = 0;
		npc.speedY = 0;
		npc.speedZ = 0;

		if (skin != null || !skin.equals("null")) {
			BufferedImage img = null;
			try {
				System.out.println("lalala");
				img = ImageIO.read(new File(m.getDataFolder(), skin + ".png"));
				Skin s = p.getSkin();
				s.parseBufferedImage(img);
				npc.putSkin(s);
				if (img != null) {
					Skin s1 = new Skin(img);
					PlayerListPacket pk = new PlayerListPacket();
					pk.type = PlayerListPacket.TYPE_ADD;
					pk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(npc.uuid, npc.eid, npc.username, s1)};
					p.dataPacket(pk);
				}
			} catch (IOException|NullPointerException e) {
				System.out.println("npe");
			}
		}
		p.dataPacket(npc);

	}
}
