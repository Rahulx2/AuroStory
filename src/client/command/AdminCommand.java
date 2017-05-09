/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation version 3 as published by
the Free Software Foundation. You may not use, modify or distribute
this program under any other version of the GNU Affero General Public
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client.command;

import client.MapleCharacter;
import client.MapleClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map.Entry;
import net.channel.ChannelServer;
import tools.DatabaseConnection;
import server.life.MapleMonster;
import server.MapleOxQuiz;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import server.maps.MapleMap;
import tools.MaplePacketCreator;
import tools.HexTool;
import tools.StringUtil;

class AdminCommand {

    static boolean execute(MapleClient c, String[] splitted, char heading) {
        MapleCharacter player = c.getPlayer();
        if (splitted[0].equalsIgnoreCase("gc")) {
            System.gc();
		} else if (splitted[0].equalsIgnoreCase("pnpc")) {
            int npcId = Integer.parseInt(splitted[1]);
            MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (npc != null && !npc.getName().equalsIgnoreCase("MISSINGNO")) {
                npc.setPosition(player.getPosition());
                npc.setCy(ypos);
                npc.setRx0(xpos + 50);
                npc.setRx1(xpos - 50);
                npc.setFh(fh);
                npc.setCustom(true);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "n");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    player.dropMessage("Failed to save NPC to the database");
                }
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            } else {
                player.dropMessage("You have entered an invalid Npc-Id");
            }
        } else if (splitted[0].equalsIgnoreCase("pmob")) {
            int npcId = Integer.parseInt(splitted[1]);
            int monsterId;
            int mobTime = Integer.parseInt(splitted[2]);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (splitted[2] == null) {
                mobTime = 0;
            }
            MapleMonster mob = MapleLifeFactory.getMonster(npcId);
            if (mob != null && !mob.getName().equalsIgnoreCase("MISSINGNO")) {
                mob.setPosition(player.getPosition());
                mob.setCy(ypos);
                mob.setRx0(xpos + 50);
                mob.setRx1(xpos - 50);
                mob.setFh(fh);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid, mobtime ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "m");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.setInt(11, mobTime);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    player.dropMessage("Failed to save MOB to the database");
                }
                player.getMap().addMonsterSpawn(mob, mobTime);
            } else {
                player.dropMessage("You have entered an invalid Npc-Id");
            }
        }  else if (splitted[0].equalsIgnoreCase("!npc")) {
            MapleNPC npc = MapleLifeFactory.getNPC(Integer.parseInt(splitted[1]));
            if (npc != null) {
                npc.setPosition(player.getPosition());
                npc.setCy(player.getPosition().y);
                npc.setRx0(player.getPosition().x + 50);
                npc.setRx1(player.getPosition().x - 50);
                npc.setFh(player.getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
            }
        } else if (splitted[0].equalsIgnoreCase("!saveall")) {
            for (ChannelServer chan : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : chan.getPlayerStorage().getAllCharacters()) {
                    chr.saveToDB(true);
                }
            }
            player.dropMessage("Save Complete.");
        } else if (splitted[0].equalsIgnoreCase("npc")) {
            MapleNPC npc = MapleLifeFactory.getNPC(Integer.parseInt(splitted[1]));
            if (npc != null) {
                npc.setPosition(player.getPosition());
                npc.setCy(player.getPosition().y);
                npc.setRx0(player.getPosition().x + 50);
                npc.setRx1(player.getPosition().x - 50);
                npc.setFh(player.getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                player.getMap().addMapObject(npc);
                player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
        } else if (splitted[0].equalsIgnoreCase("ox")) {
            if (splitted[1].equalsIgnoreCase("on") && player.getMapId() == 109020001) {
                player.getMap().setOx(new MapleOxQuiz(player.getMap()));
                player.getMap().getOx().sendQuestion();
                player.getMap().setOxQuiz(true);
            } else {
                player.getMap().setOxQuiz(false);
                player.getMap().setOx(null);
            }
        } else if (splitted[0].equalsIgnoreCase("pinkbean")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8820001), player.getPosition());
        } else if (splitted[0].equalsIgnoreCase("playernpc")) {
            player.playerNPC(c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]), Integer.parseInt(splitted[2]));
        } else if (splitted[0].equalsIgnoreCase("reloadAllMaps")) {
            for (MapleMap map : c.getChannelServer().getMapFactory().getMaps().values()) {
                MapleMap newMap = c.getChannelServer().getMapFactory().getMap(map.getId(), true, true, true, true, true);
                for (MapleCharacter ch : map.getCharacters()) {
                    ch.changeMap(newMap);
                }
                newMap.respawn();
                map = null;
            }
        } else if (splitted[0].equalsIgnoreCase("setRates")) {
            double[] newRates = new double[4];
            if (splitted.length != 5) {
                player.dropMessage("!setrates syntax: <EXP> <DROP> <BOSSDROP> <MESO>. If field is unneeded, put -1 so for example for just an EXP rate change: !setrates 50 -1 -1 -1. Negative numbers multiply base EXP rate so for 2x EXP do !setrates -2 -1 -1 -1.");
                return true;
            } else {
                for (int i = 1; i < 5; i++) {
                    try {
                        int rate = Integer.parseInt(splitted[i]);
                        newRates[i - 1] = rate;
                    } catch (NumberFormatException nfe) {
                        player.dropMessage("There was an error with one of the arguments provided. Please only use numeric values.");
                        return true;
                    }
                }
            }
            try {
                c.getChannelServer().getWorldInterface().changeRates(newRates[0], newRates[1], newRates[2], newRates[3]);
            } catch (Exception e) {
                c.getChannelServer().reconnectWorld();
            }
        } else if (splitted[0].equalsIgnoreCase("reloadmapspawns")) {
            for (Entry<Integer, MapleMap> map : c.getChannelServer().getMapFactory().getMaps().entrySet()) {
                map.getValue().respawn();
            }
        } else if (splitted[0].equalsIgnoreCase("setgmlevel")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setGM(Integer.parseInt(splitted[2]));
            player.message("Done.");
            victim.getClient().disconnect();
        } else if (splitted[0].equalsIgnoreCase("packet")) {
            if (!(splitted[1].equalsIgnoreCase("send") || splitted[1].equalsIgnoreCase("recv"))) {
                player.dropMessage("Syntax helper: !packet <send/recv> <packet>");
                return true;
            }
            boolean send = splitted[1].equalsIgnoreCase("send");
            byte[] packet;
            try {
                packet = HexTool.getByteArrayFromHexString(StringUtil.joinStringFrom(splitted, 2));
            } catch (Exception e) {
                player.dropMessage("Invalid packet, please try again.");
                return true;
            }
            if (send) {
                player.getClient().getSession().write(MaplePacketCreator.getRelayPacket(packet));
            } else {
                try {
                    player.getClient().getSession().getHandler().messageReceived(player.getClient().getSession(), packet);
                } catch (Exception e) {
                }
            }
        } else if (splitted[0].equalsIgnoreCase("shutdown")) {
            int time = 60000;
            if (splitted.length > 1) {
                time *= Integer.parseInt(splitted[1]);
            }
            if (splitted[0].equalsIgnoreCase("shutdownnow")) {
                time = 1;
            }
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.shutdown(time);
            }
        } else if (splitted[0].equalsIgnoreCase("zakum")) {
            player.getMap().spawnFakeMonsterOnGroundBelow(MapleLifeFactory.getMonster(8800000), player.getPosition());
            for (int x = 8800003; x < 8800011; x++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(x), player.getPosition());
            }
        }
            } else {
            return false;
        }
        return true;
    }
}

