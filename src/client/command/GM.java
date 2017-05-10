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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.PreparedStatement;
import client.IItem;
import client.ISkill;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleJob;
import client.MaplePet;
import client.MapleStat;
import client.SkillFactory;
import Config.Table.ExpTable;
import java.awt.Point;
import java.io.File;
import tools.DatabaseConnection;
import tools.StringUtil;
import handling.channel.ChannelServer;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import scripting.npc.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.life.MapleLifeFactory;
import tools.MaplePacketCreator;
import scripting.portal.PortalScriptManager;
import scripting.reactor.ReactorScriptManager;
import server.MapleShopFactory;
import server.life.MapleMonsterInformationProvider;
import handling.ExternalCodeTableGetter;
import handling.PacketProcessor;
import handling.SendPacketOpcode;
import handling.RecvPacketOpcode;
import client.Item;
import client.MapleDisease;
import client.MapleOccupations;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import static tools.StringUtil.getOptionalIntArg;

class GM {

    static boolean execute(MapleClient c, String[] splitted, char heading) {
        MapleCharacter player = c.getPlayer();
        ChannelServer cserv = c.getChannelServer();
        if (splitted[0].equals("ap")) {
            player.setRemainingAp(Integer.parseInt(splitted[1]));
            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
        } else if (splitted[0].equalsIgnoreCase("clock")) {
            player.getMap().setClock(true);
            player.getMap().setTimeLimit(Integer.parseInt(splitted[1]));
        } else if (splitted[0].equalsIgnoreCase("clockd")) {
            player.getMap().setClock(false);
        } else if (splitted[0].equals("buffme")) {
            final int[] array = {9001000, 9101002, 9101003, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 1111002, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (int i : array) {
                SkillFactory.getSkill(i).getEffect(SkillFactory.getSkill(i).getMaxLevel()).applyTo(player);
            }
        } else if (splitted[0].equals("chattype")) {
            player.toggleGMChat();
            player.message("You now chat in " + (player.getGMChat() ? "white." : "black."));
        } else if (splitted[0].equals("cody")) {
            NPCScriptManager.getInstance().start(c, 9200000, null, null);
        } else if (splitted[0].equals("dispoNPCse")) {
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(MaplePacketCreator.enableActions());
            player.message("Done.");
        } else if (splitted[0].equals("bombmap")) {

            for (MapleCharacter chr : player.getMap().getCharacters()) {
            for (int i = 0; i < 250; i+=50) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), new Point(chr.getPosition().x - i, chr.getPosition().y));
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), new Point(chr.getPosition().x + i, chr.getPosition().y));
            }
            }
            player.dropMessage("Planted " + splitted[1] + " bombs.");
            } else if (splitted[0].equals("bomb")) {
                if (splitted.length > 1) {
                    for (int i = 0; i < Integer.parseInt(splitted[1]); i++) {
                        player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), player.getPosition());
                    }
            player.dropMessage("Planted " + splitted[1] + " bombs.");
                } else {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), player.getPosition());
            player.dropMessage("Planted a bomb.");
            }
        } else if (splitted[0].equals("speakall")) {
            String text = StringUtil.joinStringFrom(splitted, 1);
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                mch.getMap().broadcastMessage(MaplePacketCreator.getChatText(mch.getId(), text, false, 0));
            }
            } else if (splitted[0].equals("diseasemap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int type = 0;
            if (splitted[2].equalsIgnoreCase("SEAL")) {
                type = 120;
            } else if (splitted[2].equalsIgnoreCase("DARKNESS")) {
                type = 121;
            } else if (splitted[2].equalsIgnoreCase("WEAKEN")) {
                type = 122;
            } else if (splitted[2].equalsIgnoreCase("STUN")) {
                type = 123;
            } else if (splitted[2].equalsIgnoreCase("POISON")) {
                type = 125;
            } else if (splitted[2].equalsIgnoreCase("SEDUCE")) {
                type = 128;
            } else {
                player.dropMessage("ERROR.");
            }
            victim.giveDebuff(MapleDisease.getType(type), MobSkillFactory.getMobSkill(type, 1));
            } 
                    } else if (splitted[0].equalsIgnoreCase("seduce")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.SEDUCE, MobSkillFactory.getMobSkill(128, level));
            } else {
                player.dropMessage("Player is not on.");
            }
        } else if (splitted[0].equalsIgnoreCase("stun")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.STUN, MobSkillFactory.getMobSkill(123, level));
            } else {
                player.dropMessage("Player is not on.");
            }
        } else if (splitted[0].equalsIgnoreCase("seal")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.SEAL, MobSkillFactory.getMobSkill(120, level));
            } else {
                player.dropMessage("Player is not on.");
            }
         } else if (splitted[0].equalsIgnoreCase("seducemap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.SEDUCE, MobSkillFactory.getMobSkill(128, level));
            } else {
                player.dropMessage("Player is not on.");
            }
            }
        } else if (splitted[0].equalsIgnoreCase("stunmap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.STUN, MobSkillFactory.getMobSkill(123, level));
            } else {
                player.dropMessage("Player is not on.");
            }
            }
             } else if (splitted[0].equalsIgnoreCase("sealmap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.SEAL, MobSkillFactory.getMobSkill(120, level));
            } else {
                player.dropMessage("Player is not on.");
            }
            }
            } else if (splitted[0].equalsIgnoreCase("weaken")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.WEAKEN, MobSkillFactory.getMobSkill(122, level));
            } else {
                player.dropMessage("Player is not on.");
            }
                                } else if (splitted[0].equalsIgnoreCase("weakenmap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int level = Integer.parseInt(splitted[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.WEAKEN, MobSkillFactory.getMobSkill(122, level));
            } else {
                player.dropMessage("Player is not on.");
            } 
            }            
            } else if (splitted[0].equals("speak")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                String text = StringUtil.joinStringFrom(splitted, 2);
                victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), text, false, 0));
            } else {
                player.dropMessage("Player not found");
            }
            }  else if (splitted[0].equals("startrace")) {
                 if (!cserv.getRace() && !cserv.getWaiting()){
            cserv.setWaiting(true);
            cserv.setWaitingTime(2); //Replace with time in minutes you want to wait.
            cserv.raceCountdown();
            try {
           cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, "[Event]: The Great Victoria Island Race will begin soon! Please head to Henesys!").getBytes());
             } catch (RemoteException e) {
               cserv.reconnectWorld();
                 }
                        try {
           cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, "Use @joinrace to join and @rules to see the rules and regulations of this event.").getBytes());
             } catch (RemoteException e) {
               cserv.reconnectWorld();
                 }
                 }else{
                    player.dropMessage("[Notice]: A race is still in progress.");
                 }
                 } else if (splitted[0].equals("clock")) {
            player.getMap().broadcastMessage(MaplePacketCreator.getClock(getOptionalIntArg(splitted, 1, 60)));
        } else if (splitted[0].equalsIgnoreCase("clockd")) {
            player.getMap().setClock(false);
            } else if (splitted[0].equals("warpoxtop") || splitted[0].equals("warpoxleft") || splitted[0].equals("warpoxright") || splitted[0].equals("warpoxmiddle")) {
            if (player.getMap().getId() == 109020001) {
                if (splitted[0].equals("warpoxtop")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y <= -206 && !person.isGM())
                            person.changeMap(person.getMap().getReturnMap(),person.getMap().getReturnMap().getPortal(0));
                    }
                    player.dropMessage("Top Warpped Out.");
                } else if (splitted[0].equals("warpoxleft")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 334 && person.getPosition().x >= -952 && person.getPosition().x <= -308 && !person.isGM())
                            person.changeMap(person.getMap().getReturnMap(),person.getMap().getReturnMap().getPortal(0));
                    }
                    player.dropMessage("Left Warpped Out.");
                } else if (splitted[0].equals("warpoxright")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 334 && person.getPosition().x >= -142 && person.getPosition().x <= 502 && !person.isGM())
                            person.changeMap(person.getMap().getReturnMap(),person.getMap().getReturnMap().getPortal(0));
                    }
                    player.dropMessage("Right Warpped Out.");
                } else if (splitted[0].equals("warpoxmiddle")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 274 && person.getPosition().x >= -308 && person.getPosition().x <= -142 && !person.isGM())
                            person.changeMap(person.getMap().getReturnMap(),person.getMap().getReturnMap().getPortal(0));
                    }
                    player.dropMessage("Middle Warpped Out.");
                }
            } else {
                player.dropMessage("These commands can only be used in the OX Map.");
            }
        } else if (splitted[0].equalsIgnoreCase("healplayer")) {
                                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
                                    victim.setHp(victim.getCurrentMaxHp());
                                    victim.updateSingleStat(MapleStat.HP, victim.getHp());
                                    victim.setHp(victim.getCurrentMaxMp());
                                    victim.updateSingleStat(MapleStat.MP, victim.getMp());
        } else if (splitted[0].equals("event")) {
            if (player.getClient().getChannelServer().eventOn == false) {
                int mapid = getOptionalIntArg(splitted, 1, c.getPlayer().getMapId());
                player.getClient().getChannelServer().eventOn = true;
                player.getClient().getChannelServer().eventMap = mapid;
                try {
                    cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, c.getChannel(), "[Event] A GM is hosting an event in Channel " + c.getChannel() + "! Use @joinevent to join it!").getBytes());
                } catch (RemoteException e) {
                    cserv.reconnectWorld();
                }
            } else {
                player.getClient().getChannelServer().eventOn = false;
                try {
                    cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, c.getChannel(), "[Event] The event has ended. Thanks to all of those who participated.").getBytes());
                } catch (RemoteException e) {
                    cserv.reconnectWorld();
                }
            }
        } else if (splitted[0].equals("killnear")) {
            MapleMap map = player.getMap();
            List<MapleMapObject> players = map.getMapObjectsInRange(player.getPosition(), (double) 50000, Arrays.asList(MapleMapObjectType.PLAYER));
            for (MapleMapObject closeplayers : players) {
                MapleCharacter playernear = (MapleCharacter) closeplayers;
            if (playernear.isAlive() && playernear != player);
                playernear.setHp(0);
                playernear.updateSingleStat(MapleStat.HP, 0);
                playernear.dropMessage(6, "You were too close to a GM.");
            }                    
        } else if (splitted[0].equals("oxmap")) {
             player.changeMap(109020001);
        } else if (splitted[0].equalsIgnoreCase("write")) {
            if (splitted.length > 1) { // Will drop syntax if you use the command by itself
                boolean left = player.isFacingLeft(); // Getting the direction that the player is facing
                String txt = StringUtil.joinStringFrom(splitted, 1); // Getting desired text, from splitted[1] -> splitted[infinite]
                if (left) { // if the player is facing left
                    int i, len = txt.length(); // start of reversing string method
                    StringBuffer reverse = new StringBuffer(len);
                    for (i = (len - 1); i >= 0; i--) {
                        reverse.append(txt.charAt(i));
                    }
                    txt = reverse.toString(); // end of reversing string method
                }
                Point pos = player.getPosition();
                Map<String, Integer> letters = new HashMap<String, Integer>(); // start of defining letter item ids
                letters.put("a", 0);
                letters.put("b", 1);
                letters.put("c", 2);
                letters.put("d", 3);
                letters.put("e", 4);
                letters.put("f", 5);
                letters.put("g", 6);
                letters.put("h", 7);
                letters.put("i", 8);
                letters.put("j", 9);
                letters.put("k", 10);
                letters.put("l", 11);
                letters.put("m", 12);
                letters.put("n", 13);
                letters.put("o", 14);
                letters.put("p", 15);
                letters.put("q", 16);
                letters.put("r", 17);
                letters.put("s", 18);
                letters.put("t", 19);
                letters.put("u", 20);
                letters.put("v", 21);
                letters.put("w", 22);
                letters.put("x", 23);
                letters.put("y", 24);
                letters.put("z", 25);
                letters.put("1", -990);
                letters.put("2", -989);
                letters.put("3", -988);
                letters.put("4", -987);
                letters.put("5", -986);
                letters.put("6", -985);
                letters.put("7", -984);
                letters.put("8", -983);
                letters.put("9", -982);
                letters.put("0", -981);
                letters.put("+", -978);
                letters.put("-", -977);
                for (char ch : txt.toCharArray()) { // for each character in the string
                    if (left) { // if player is facing left then the direction of the letters is negative x (<)
                        pos.x -= 30;
                    } else { // if player is facing right then the direction of the letters is positive x (>)
                        pos.x += 30;
                    }
                    if (letters.containsKey(Character.toString(ch).toLowerCase())) { // matching the current iteration of the char list with a letter item id
                        Item toDrop = new Item((letters.get(Character.toString(ch).toLowerCase()) + 3991000), (byte) 0, (short) 1); //letter item id + 3991000 = real item id (faster to type the seven digit number only once)
                        player.getMap().spawnItemDrop(player.getId(), pos, player, toDrop, pos, true, true);
                    }
                }
                letters.clear();
            } else {
                player.dropMessage("Syntax: !letters <message>"); // will only run if splitted < 2 (i.e if you use the command by itself)
            } 
           
        } else if (splitted[0].equalsIgnoreCase("giveeventpoints")) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            victim.gainEventPoints(Integer.parseInt(splitted[2]));
            player.dropMessage("You have given " + splitted[1] + " " + splitted[2] + " Event Points.");
           victim.dropMessage(6, player.getName() + " has given you " + splitted[2] + " Event Points.");  
                                        } else if (splitted[0].equals("mynpcpos")) {
            Point pos = c.getPlayer().getPosition();
            player.message("CY: " + pos.y + " | RX0: " + (pos.x + 50) + " | R: " + pos.x + " | RX1: " + (pos.x - 50) + " | FH: " + c.getPlayer().getMap().getFootholds().findBelow(pos).getId());
        } else if (splitted[0].equals("fame")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setFame(Integer.parseInt(splitted[2]));
            victim.updateSingleStat(MapleStat.FAME, victim.getFame());
        } else if (splitted[0].equals("giftnx")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).modifyCSPoints(1, Integer.parseInt(splitted[2]));
            player.message("Done");
        } else if (splitted[0].equals("gmshop")) {
            MapleShopFactory.getInstance().getShop(1337).sendShop(c);
        } else if (splitted[0].equals("heal")) {
            player.setHpMp(30000);
        } else if (splitted[0].equals("id")) {
            try {
                BufferedReader dis = new BufferedReader(new InputStreamReader(new URL("http://www.mapletip.com/search_java.php?search_value=" + splitted[1] + "&check=true").openConnection().getInputStream()));
                String s;
                while ((s = dis.readLine()) != null) {
                    player.dropMessage(s);
                }
                dis.close();
            } catch (Exception e) {
            }
        } else if (splitted[0].equals("item")) {
            int itemId = Integer.parseInt(splitted[1]);
            short quantity = 1;
            try {
                quantity = Short.parseShort(splitted[2]);
            } catch (Exception e) {
            }
            if (itemId >= 5000000 && itemId < 5000065) {
                MaplePet.createPet(itemId);
            } else {
                MapleInventoryManipulator.addById(c, itemId, quantity, player.getName(), -1);
                IItem item3 = player.getInventory(MapleInventoryType.getByType((byte) (itemId / 1000000))).findById(itemId);
            }

        } else if (splitted[0].equals("drop")) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int itemId = Integer.parseInt(splitted[1]);
            short quantity = (short) StringUtil.getOptionalIntArg(splitted, 2, 1);
            IItem toDrop;
            if (ii.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                toDrop = ii.getEquipById(itemId);
            } else {
                toDrop = new Item(itemId, (byte) 0, (short) quantity);
            }
            StringBuilder logMsg = new StringBuilder("Created by ");
            logMsg.append(c.getPlayer().getName());
            logMsg.append(" using !drop. Quantity: ");
            logMsg.append(quantity);
            //toDrop.log(logMsg.toString(), false);
            toDrop.setOwner(player.getName());
            c.getPlayer().getMap().spawnItemDrop(c.getPlayer().getObjectId(), c.getPlayer().getPosition(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
        } else if (splitted[0].equals("job")) {
            player.changeJob(MapleJob.getById(Integer.parseInt(splitted[1])));
        } else if (splitted[0].equals("occupation")) {
            player.changeOccupation(MapleOccupations.getById(Integer.parseInt(splitted[1])));
            player.dropMessage("You have changed your job to " + MapleOccupations.getById(Integer.parseInt(splitted[1])));
        } else if (splitted[0].equals("kill")) {
            cserv.getPlayerStorage().getCharacterByName(splitted[1]).setHpMp(0);
        } else if (splitted[0].equals("level")) {
            player.setLevel(Integer.parseInt(splitted[1]));
            player.gainExp(-player.getExp(), false, false);
            player.updateSingleStat(MapleStat.LEVEL, player.getLevel());
            player.setExp(0);
            player.updateSingleStat(MapleStat.EXP, 0);
        } else if (splitted[0].equals("levelup")) {
            player.gainExp(ExpTable.getExpNeededForLevel(player.getLevel()) - player.getExp(), false, false);
        } else if (splitted[0].equals("maxstat")) {
            final String[] s = {"setall", String.valueOf(Short.MAX_VALUE)};
            execute(c, s, heading);
            player.setLevel(255);
            player.setFame(13337);
            player.setMaxHp(30000);
            player.setMaxMp(30000);
            player.updateSingleStat(MapleStat.LEVEL, 255);
            player.updateSingleStat(MapleStat.FAME, 13337);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);
        } else if (splitted[0].equals("maxskills")) {
            for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
                try {
                    ISkill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                    if (skill.getId() < 1009 || skill.getId() > 1011) {
                        player.changeSkillLevel(skill, skill.getMaxLevel(), skill.getMaxLevel());
                    }
                } catch (NumberFormatException nfe) {
                    break;
                } catch (NullPointerException npe) {
                    continue;
                }
            }
        } else if (splitted[0].equals("mesos")) {
            player.gainMeso(Integer.parseInt(splitted[1]), true);
        } /* else if (splitted[0].equals("notice"))
        try {
        cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, "[Notice] " + joinStringFrom(splitted, 1)).getBytes());
        } catch (Exception e) {
        cserv.reconnectWorld();
        }*/ else if (splitted[0].equals("onlinechan")) {
            String s = "Characters online (" + cserv.getPlayerStorage().getAllCharacters().size() + ") : ";
            for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                s += MapleCharacter.makeMapleReadable(chr.getName()) + ", ";
            }
            player.dropMessage(s.substring(0, s.length() - 2));
        } else if (splitted[0].equals("pap")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8500001), player.getPosition());
        } else if (splitted[0].equals("pianus")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8510000), player.getPosition());
        } else if (splitted[0].equals("servermessage")) {
            for (int i = 1; i <= ChannelServer.getAllInstances().size(); i++) {
                ChannelServer.getInstance(i).setServerMessage(joinStringFrom(splitted, 1));
            }
        } else if (splitted[0].equals("setall")) {
            final int x = Short.parseShort(splitted[1]);
            player.setStr(x);
            player.setDex(x);
            player.setInt(x);
            player.setLuk(x);
            player.updateSingleStat(MapleStat.STR, x);
            player.updateSingleStat(MapleStat.DEX, x);
            player.updateSingleStat(MapleStat.INT, x);
            player.updateSingleStat(MapleStat.LUK, x);
        } else if (splitted[0].equals("sp")) {
            player.setRemainingSp(Integer.parseInt(splitted[1]));
            player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
		} else if (splitted[0].equals("ban")) {
            String originalReason = StringUtil.joinStringFrom(splitted, 2);
            String reason = player.getName() + " banned " + splitted[1] + ": " + originalReason;
            MapleCharacter target = cserv.getPlayerStorage().getCharacterByName(splitted[1]);
            if (target != null) {
            } else {
                if (MapleCharacter.ban(splitted[1], reason, false)) {
                    String readableTargetName = player.getName();
                    String ip = target.getClient().getSession().getRemoteAddress().toString().split(":")[0];
                    reason += " (IP: " + ip + ")";
                    try {
                        cserv.getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(6, readableTargetName + " has been banned for " + originalReason).getBytes());
                    } catch (RemoteException e) {
                        cserv.reconnectWorld();
                    }
                } else {
                    player.dropMessage("Failed to ban " + splitted[1]);
                }
            }
        } else if (splitted[0].equals("unban")) {
            try {
                PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET banned = -1, norankupdate = 0 WHERE id = " + MapleCharacter.getIdByName(splitted[1]) + "");
                p.executeUpdate();
                p.close();
            } catch (Exception e) {
                player.message("Failed to unban " + splitted[1]);
                return true;
            }
            player.message("Unbanned " + splitted[1]);
        } else if (splitted[0].equals("clearportalscripts")) {
            PortalScriptManager.getInstance().clearScripts();
        } else if (splitted[0].equals("clearmonsterdrops")) {
            MapleMonsterInformationProvider.getInstance().clearDrops();
        } else if (splitted[0].equals("clearreactordrops")) {
            ReactorScriptManager.getInstance().clearDrops();
        } else if (splitted[0].equals("clearshops")) {
            MapleShopFactory.getInstance().clear();
        } else if (splitted[0].equals("clearevents")) {
            for (ChannelServer instance : ChannelServer.getAllInstances()) {
                instance.reloadEvents();
            }
        } else if (splitted[0].equals("!reloadops")) {
            try {
                ExternalCodeTableGetter.populateValues(SendPacketOpcode.getDefaultProperties(), SendPacketOpcode.values(), true);
                ExternalCodeTableGetter.populateValues(RecvPacketOpcode.getDefaultProperties(), RecvPacketOpcode.values(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER).reset(PacketProcessor.Mode.CHANNELSERVER);
            PacketProcessor.getProcessor(PacketProcessor.Mode.CHANNELSERVER).reset(PacketProcessor.Mode.CHANNELSERVER);
        } else if (splitted[0].equalsIgnoreCase("horntail")) {
            for (int i = 8810002; i < 8810010; i++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(i), player.getPosition());
            }
        } else if (splitted[0].equals("say")) {
            try {
                cserv.getWorldInterface().broadcastMessage(player.getName(), MaplePacketCreator.serverNotice(6, player.getName() + ": " + joinStringFrom(splitted, 1)).getBytes());
            } catch (Exception e) {
                cserv.reconnectWorld();
            }
        } else {
            return false;
        }
        return true;
    }

    static String joinStringFrom(String arr[], int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i != arr.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    private static int getNoticeType(String typestring) {
        if (typestring.equals("n")) {
            return 0;
        } else if (typestring.equals("p")) {
            return 1;
        } else if (typestring.equals("l")) {
            return 2;
        } else if (typestring.equals("nv")) {
            return 5;
        } else if (typestring.equals("v")) {
            return 5;
        } else if (typestring.equals("b")) {
            return 6;
        }
        return -1;
    }
}
