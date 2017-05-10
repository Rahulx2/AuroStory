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
import handling.channel.ChannelServer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import tools.DatabaseConnection;
import server.life.MapleMonster;
import server.MapleOxQuiz;
import server.MaplePortal;
import server.events.MapleEvent;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import server.maps.MapleMap;
import tools.MaplePacketCreator;
import tools.HexTool;
import tools.StringUtil;

class Admin {

    static boolean execute(MapleClient c, String[] splitted, char heading) {
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
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
        } else if (splitted[0].equals("gmperson")) {
            if (splitted.length == 3) {
                MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim != null) {
                    int level = 0;
                    try {
                        level = Integer.parseInt(splitted[2]);
                    } catch (NumberFormatException blackness) {
                    }
                    victim.setGM(level);
                    if (victim.isGM()) {
                        victim.dropMessage(6, "You now have level " + level + " GM powers.");
                    }
                } else {
                    player.dropMessage("The player " + splitted[1] + " is either offline or not in this channel");
                }
            }
            
        } else if (splitted[0].equals("worldtrip")) {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                    for (int i = 1; i <= 10; i++) {
                        MapleMap target = cserv.getMapFactory().getMap(200000000);
                        MaplePortal targetPortal = target.getPortal(0);
                        victim.changeMap(target, targetPortal);
                        MapleMap target1 = cserv.getMapFactory().getMap(102000000);
                        MaplePortal targetPortal1 = target.getPortal(0);
                        victim.changeMap(target1, targetPortal1);
                        MapleMap target2 = cserv.getMapFactory().getMap(103000000);
                        MaplePortal targetPortal2 = target.getPortal(0);
                        victim.changeMap(target2, targetPortal2);
                        MapleMap target3 = cserv.getMapFactory().getMap(100000000);
                        MaplePortal targetPortal3 = target.getPortal(0);
                        victim.changeMap(target3, targetPortal3);
                        MapleMap target4 = cserv.getMapFactory().getMap(200000000);
                        MaplePortal targetPortal4 = target.getPortal(0);
                        victim.changeMap(target4, targetPortal4);
                        MapleMap target5 = cserv.getMapFactory().getMap(211000000);
                        MaplePortal targetPortal5 = target.getPortal(0);
                        victim.changeMap(target5, targetPortal5);
                        MapleMap target6 = cserv.getMapFactory().getMap(230000000);
                        MaplePortal targetPortal6 = target.getPortal(0);
                        victim.changeMap(target6, targetPortal6);
                        MapleMap target7 = cserv.getMapFactory().getMap(222000000);
                        MaplePortal targetPortal7 = target.getPortal(0);
                        victim.changeMap(target7, targetPortal7);
                        MapleMap target8 = cserv.getMapFactory().getMap(251000000);
                        MaplePortal targetPortal8 = target.getPortal(0);
                        victim.changeMap(target8, targetPortal8);
                        MapleMap target9 = cserv.getMapFactory().getMap(220000000);
                        MaplePortal targetPortal9 = target.getPortal(0);
                        victim.changeMap(target9, targetPortal9);
                        MapleMap target10 = cserv.getMapFactory().getMap(221000000);
                        MaplePortal targetPortal10 = target.getPortal(0);
                        victim.changeMap(target10, targetPortal10);
                        MapleMap target11 = cserv.getMapFactory().getMap(240000000);
                        MaplePortal targetPortal11 = target.getPortal(0);
                        victim.changeMap(target11, targetPortal11);
                        MapleMap target12 = cserv.getMapFactory().getMap(600000000);
                        MaplePortal targetPortal12 = target.getPortal(0);
                        victim.changeMap(target12, targetPortal12);
                        MapleMap target13 = cserv.getMapFactory().getMap(800000000);
                        MaplePortal targetPortal13 = target.getPortal(0);
                        victim.changeMap(target13, targetPortal13);
                        MapleMap target14 = cserv.getMapFactory().getMap(680000000);
                        MaplePortal targetPortal14 = target.getPortal(0);
                        victim.changeMap(target14, targetPortal14);
                        MapleMap target15 = cserv.getMapFactory().getMap(105040300);
                        MaplePortal targetPortal15 = target.getPortal(0);
                        victim.changeMap(target15, targetPortal15);
                        MapleMap target16 = cserv.getMapFactory().getMap(990000000);
                        MaplePortal targetPortal16 = target.getPortal(0);
                        victim.changeMap(target16, targetPortal16);
                        MapleMap target17 = cserv.getMapFactory().getMap(100000001);
                        MaplePortal targetPortal17 = target.getPortal(0);
                        victim.changeMap(target17, targetPortal17);
                    }
                    victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestSpawnpoint(
                            c.getPlayer().getPosition()));            
            } else if (splitted[0].equalsIgnoreCase("saveall")) {
            for (ChannelServer chan : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : chan.getPlayerStorage().getAllCharacters()) {
                    chr.saveToDB(true);
                }
            }
            player.dropMessage("Save Complete.");
        } else if (splitted[0].equalsIgnoreCase("setgmlevel")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setGM(Integer.parseInt(splitted[2]));
            player.message("Done.");
            victim.getClient().disconnect();
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
        } else if (splitted[0].equalsIgnoreCase("eventpoints")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            victim.gainEventPoints(Integer.parseInt(splitted[2]));
            player.dropMessage("You have given " + splitted[1] + " " + splitted[2] + " Event Points.");
           victim.dropMessage(6, player.getName() + " has given you " + splitted[2] + " Event Points.");  
            } else if (splitted[0].equalsIgnoreCase("pinkbean")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8820001), player.getPosition());
        } else if (splitted[0].equalsIgnoreCase("startevent")) {
            for (MapleCharacter chr : c.getPlayer().getMap().getCharacters()) {
                 c.getPlayer().getMap().startEvent(chr);
            }
            c.getChannelServer().setEvent(null);
        } else if (splitted[0].equalsIgnoreCase("scheduleevent")) {
           if (c.getPlayer().getMap().hasEventNPC()) {
            if (splitted[1].equals("treasure")) {
                c.getChannelServer().setEvent(new MapleEvent(109010000, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (splitted[1].equals("ox")) {
                c.getChannelServer().setEvent(new MapleEvent(109020001, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (splitted[1].equals("ola")) {
                c.getChannelServer().setEvent(new MapleEvent(109030101, 50)); // Wrong map but still Ola Ola
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (splitted[1].equals("fitness")) {
                c.getChannelServer().setEvent(new MapleEvent(109040000, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (splitted[1].equals("snowball")) {
                c.getChannelServer().setEvent(new MapleEvent(109060001, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else if (splitted[1].equals("coconut")) {
                c.getChannelServer().setEvent(new MapleEvent(109080000, 50));
            try {
                cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
            } else {
                player.message("Wrong Syntax: /scheduleevent treasure, ox, ola, fitness, snowball or coconut");
            }
           } else {
               player.message("You can only use this command in the following maps: 60000, 104000000, 200000000, 220000000");
           }
        } else if (splitted[0].equalsIgnoreCase("warpsnowball")) {
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                 chr.changeMap(109060000, chr.getTeam());
            }
            } else if (splitted[0].equalsIgnoreCase("news")) {
                  String title = (splitted[1]);
                  String message = StringUtil.joinStringFrom(splitted, 2);
                try {
                    java.sql.Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO recronews ( title, message, date ) VALUES ( ?, ?, ? )");
                    ps.setString(1, title);
                    ps.setString(2, message);
                    ps.setString(3, now("dd/MM/yy"));
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    player.dropMessage("[Error] - Cannot save Recro news!");
                }
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

        public static String now(String dateFormat) {
                      Calendar cal = Calendar.getInstance();
                      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
               return sdf.format(cal.getTime());

     }
}

