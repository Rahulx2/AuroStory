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
package net.channel.handler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import constants.ExpTable;
import client.IEquip;
import client.IItem;
import client.ISkill;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleJob;
import client.MaplePet;
import client.MapleStat;
import client.SkillFactory;
import constants.InventoryConstants;
import java.rmi.RemoteException;
import java.sql.SQLException;
import net.AbstractMaplePacketHandler;
import scripting.npc.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.maps.FieldLimit;
import server.maps.MapleMap;
import server.maps.MapleTVEffect;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.Randomizer;
import tools.data.input.SeekableLittleEndianAccessor;
import java.util.Calendar;
import net.channel.ChannelServer;

public final class UseCashItemHandler extends AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleCharacter player = c.getPlayer();
        if (System.currentTimeMillis() - player.getLastUsedCashItem() < 3000) {
            return;
        }
        player.setLastUsedCashItem(System.currentTimeMillis());
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        slea.skip(2);
        int itemId = slea.readInt();
        int itemType = itemId / 10000;
        IItem toUse = player.getInventory(MapleInventoryType.CASH).getItem(player.getInventory(MapleInventoryType.CASH).findById(itemId).getPosition());
        if (toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        String medal = "";
        IItem medalItem = player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -49);
        if (medalItem != null) {
            medal = "<" + ii.getName(medalItem.getItemId()) + "> ";
        }
        try {
            if (itemType == 505) { // AP/SP reset
                if (itemId > 5050000) {
                    int SPTo = slea.readInt();
                    int SPFrom = slea.readInt();
                    ISkill skillSPTo = SkillFactory.getSkill(SPTo);
                    ISkill skillSPFrom = SkillFactory.getSkill(SPFrom);
                    int curLevel = player.getSkillLevel(skillSPTo);
                    int curLevelSPFrom = player.getSkillLevel(skillSPFrom);
                    if ((curLevel < skillSPTo.getMaxLevel()) && curLevelSPFrom > 0) {
                        player.changeSkillLevel(skillSPFrom, curLevelSPFrom - 1, player.getMasterLevel(skillSPFrom));
                        player.changeSkillLevel(skillSPTo, curLevel + 1, player.getMasterLevel(skillSPTo));
                    }
                } else {
                    List<Pair<MapleStat, Integer>> statupdate = new ArrayList<Pair<MapleStat, Integer>>(2);
                    int APTo = slea.readInt();
                    int APFrom = slea.readInt();
                    switch (APFrom) {
                        case 64: // str
                            if (player.getStr() < 5) {
                                return;
                            }
                            player.addStat(1, -1);
                            break;
                        case 128: // dex
                            if (player.getDex() < 5) {
                                return;
                            }
                            player.addStat(2, -1);
                            break;
                        case 256: // int
                            if (player.getInt() < 5) {
                                return;
                            }
                            player.addStat(3, -1);
                            break;
                        case 512: // luk
                            if (player.getLuk() < 5) {
                                return;
                            }
                            player.addStat(4, -1);
                            break;
                        case 2048: // HP
                            if (player.getHpMpApUsed() < 1 || player.getHpMpApUsed() >= 10000) {
                                return;
                            }
                            break;
                        case 8192: // MP
                            if (player.getHpMpApUsed() <= 0 || player.getHpMpApUsed() >= 10000) {
                                return;
                            }
                            int mp = player.getMp();
                            int level = player.getLevel();
                            boolean canWash = true;
                            if (player.getJob().isA(MapleJob.SPEARMAN) && mp < 4 * level + 156) {
                                canWash = false;
                            } else if (player.getJob().isA(MapleJob.FIGHTER) && mp < 4 * level + 56) {
                                canWash = false;
                            } else if (player.getJob().isA(MapleJob.THIEF) && player.getJob().getId() % 100 > 0 && mp < level * 14 - 4) {
                                canWash = false;
                            } else if (mp < level * 14 + 148) {
                                canWash = false;
                            }
                            if (canWash) {
                                player.setMp(player.getMp() - Randomizer.getInstance().nextInt(4) - 10);
                            }
                            break;
                        default:
                            c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, true));
                            return;
                    }
                    DistributeAPHandler.addStat(c, APTo);
                    c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, true));
                }
                remove(c, itemId);
            } else if (itemType == 506) {
                int tagType = itemId % 10;
                IItem eq = null;
                if (tagType == 0) { // Item tag.
                    int equipSlot = slea.readShort();
                    if (equipSlot == 0) {
                        return;
                    }
                    eq = player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) equipSlot);
                    eq.setOwner(player.getName());
                } else if (tagType == 1) { // Sealing lock
                    MapleInventoryType type = MapleInventoryType.getByType((byte) slea.readInt());
                    IItem item = player.getInventory(type).getItem((byte) slea.readInt());
                    if (item == null) {
                        return;
                    }
                    byte flag = item.getFlag();
                    flag |= InventoryConstants.LOCK;
                    item.setFlag(flag);
                    c.getSession().write(MaplePacketCreator.updateItemInSlot(item));
                    remove(c, itemId);
                } else if (tagType == 2) { // Incubator
                    byte inventory2 = (byte) slea.readInt();
                    byte slot2 = (byte) slea.readInt();
                    IItem item2 = player.getInventory(MapleInventoryType.getByType(inventory2)).getItem(slot2);
                    if (item2 == null) // hacking
                    {
                        return;
                    }
                    if (getIncubatedItem(c, itemId)) {
                        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.getByType(inventory2), slot2, (short) 1, false);
                        remove(c, itemId);
                    }
                    return;
                }
                slea.readInt(); // time stamp
                c.getSession().write(MaplePacketCreator.updateEquipSlot(eq));
                remove(c, itemId);
            } else if (itemType == 507) {
                boolean whisper;
                long curTime = Calendar.getInstance().getTimeInMillis();
                boolean allowed = false;
                try {
                    allowed = c.getChannelServer().getWorldRegistry().getProperty("smega").equals(Boolean.TRUE);
                } catch (RemoteException re) {
                    c.getChannelServer().reconnectWorld();
                }
                if (c.getPlayer().getMegaLimit() > curTime) {
                    c.getPlayer().dropMessage("You may not megaphone yet. Please wait " + Math.floor((c.getPlayer().getMegaLimit() - curTime) / 1000) + " seconds.");
                    return;
                } else if (!allowed) {
                    c.getPlayer().dropMessage("Megaphones are currently disallowed, please wait for a GM to enable them.");
                    return;
                } else {
                    Calendar futureCal = Calendar.getInstance();
                    futureCal.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND) + 30);
                    c.getPlayer().setMegaLimit(futureCal.getTimeInMillis());
                }
                switch (itemId / 1000 % 10) {
                    case 1: // Megaphone
                        if (player.getLevel() > 9) {
                            player.getClient().getChannelServer().broadcastPacket(MaplePacketCreator.serverNotice(2, medal + player.getName() + " : " + slea.readMapleAsciiString()));
                        } else {
                            player.dropMessage(1, "You may not use this until you're level 10.");
                        }
                        break;
                    case 2: // Super megaphone
                        c.getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(3, c.getChannel(), medal + player.getName() + " : " + slea.readMapleAsciiString(), (slea.readByte() != 0)).getBytes());
                        break;
                    case 5: // Maple TV
                        int tvType = itemId % 10;
                        boolean megassenger = false;
                        boolean ear = false;
                        MapleCharacter victim = null;
                        if (tvType != 1) {
                            if (tvType >= 3) {
                                megassenger = true;
                                if (tvType == 3) {
                                    slea.readByte();
                                }
                                ear = 1 == slea.readByte();
                            } else if (tvType != 2) {
                                slea.readByte();
                            }
                            if (tvType != 4) {
                                victim = c.getChannelServer().getPlayerStorage().getCharacterByName(slea.readMapleAsciiString());
                            }
                        }
                        List<String> messages = new LinkedList<String>();
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < 5; i++) {
                            String message = slea.readMapleAsciiString();
                            if (megassenger) {
                                builder.append(" " + message);
                            }
                            messages.add(message);
                        }
                        slea.readInt();
                        if (megassenger) {
                            c.getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.serverNotice(3, c.getChannel(), medal + player.getName() + " : " + builder.toString(), ear).getBytes());
                        }
                        if (!MapleTVEffect.isActive()) {
                            new MapleTVEffect(player, victim, messages, tvType);
                            remove(c, itemId);
                        } else {
                            player.dropMessage(1, "MapleTV is already in use.");
                            return;
                        }
                        break;
                    case 6: //item megaphone
                        String msg = medal + player.getName() + " : " + slea.readMapleAsciiString();
                        whisper = slea.readByte() == 1;
                        IItem item = null;
                        if (slea.readByte() == 1) { //item
                            item = player.getInventory(MapleInventoryType.getByType((byte) slea.readInt())).getItem((byte) slea.readInt());
                            if (item == null) //hack
                            {
                                return;
                            } else if (ii.isDropRestricted(item.getItemId())) {
                                player.dropMessage(1, "You cannot trade this item.");
                                c.getSession().write(MaplePacketCreator.enableActions());
                                return;
                            }
                        }
                        c.getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.itemMegaphone(msg, whisper, c.getChannel(), item).getBytes());
                        break;
                    case 7: //triple megaphone
                        int lines = slea.readByte();
                        if (lines < 1 || lines > 3) {//hack
                            return;
                        }
                        String[] msg2 = new String[lines];
                        for (int i = 0; i < lines; i++) {
                            msg2[i] = medal + player.getName() + " : " + slea.readMapleAsciiString();
                        }
                        whisper = slea.readByte() == 1;
                        c.getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.getMultiMegaphone(msg2, c.getChannel(), whisper).getBytes());
                        break;
                }
                remove(c, itemId);
            } else if (itemType == 508) { //graduation banner
                slea.readMapleAsciiString(); // message, sepearated by 0A for lines
                c.getSession().write(MaplePacketCreator.enableActions());
            } else if (itemType == 509) {
                String sendTo = slea.readMapleAsciiString();
                String msg = slea.readMapleAsciiString();
                try {
                    player.sendNote(sendTo, msg);
                } catch (SQLException e) {
                }
                remove(c, itemId);
            } else if (itemType == 510) {
                player.getMap().broadcastMessage(MaplePacketCreator.musicChange("Jukebox/Congratulation"));
                remove(c, itemId);
            } else if (itemType == 512) {
                if (ii.getStateChangeItem(itemId) != 0) {
                    for (MapleCharacter mChar : player.getMap().getCharacters()) {
                        ii.getItemEffect(ii.getStateChangeItem(itemId)).applyTo(mChar);
                    }
                }
                player.getMap().startMapEffect(ii.getMsg(itemId).replaceFirst("%s", player.getName()).replaceFirst("%s", slea.readMapleAsciiString()), itemId);
                remove(c, itemId);
            } else if (itemType == 517) {
                MaplePet pet = player.getPet(0);
                String newName = slea.readMapleAsciiString();
                if (pet == null || newName.length() > 13 || newName.length() < 0) {
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                }
                pet.setName(newName);
                c.getSession().write(MaplePacketCreator.updatePet(pet));
                c.getSession().write(MaplePacketCreator.enableActions());
                player.getMap().broadcastMessage(player, MaplePacketCreator.changePetName(player, newName, 1), true);
                remove(c, itemId);
            } else if (itemType == 504) { // vip teleport rock
                String error1 = "Either the player could not be found or you were trying to teleport to an illegal location.";
                byte rocktype = slea.readByte();
                remove(c, itemId);
                c.getSession().write(MaplePacketCreator.refreshTeleportRockMapList(player, rocktype));
                if (rocktype == 0) {
                    int mapId = slea.readInt();
                    MapleMap map = c.getChannelServer().getMapFactory().getMap(mapId);
                    if (map.getForcedReturnId() == 999999999 && FieldLimit.CANNOTVIPROCK.check(mapId)) {
                        player.changeMap(c.getChannelServer().getMapFactory().getMap(mapId));
                    } else {
                        MapleInventoryManipulator.addById(c, itemId, (short) 1);
                        player.dropMessage(1, error1);
                        c.getSession().write(MaplePacketCreator.enableActions());
                    }
                } else {
                    String name = slea.readMapleAsciiString();
                    MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
                    boolean success = false;
                    if (victim != null) {
                        MapleMap target = victim.getMap();
                        if (c.getChannelServer().getMapFactory().getMap(c.getChannelServer().getWorldInterface().getLocation(name).map).getForcedReturnId() == 999999999 || victim.getMapId() < 100000000) {
                            if (!victim.isGM()) {
                                if (itemId == 5041000 || victim.getMapId() / player.getMapId() == 1) { //viprock & same continent
                                    player.changeMap(target, target.findClosestSpawnpoint(victim.getPosition()));
                                    success = true;
                                } else {
                                    player.dropMessage(1, error1);
                                }
                            } else {
                                player.dropMessage(1, error1);
                            }
                        } else {
                            player.dropMessage(1, "You cannot teleport to this map.");
                        }
                    } else {
                        player.dropMessage(1, "Player could not be found in this channel.");
                    }
                    if (!success) {
                        MapleInventoryManipulator.addById(c, itemId, (short) 1);
                        c.getSession().write(MaplePacketCreator.enableActions());
                    }
                }
            } else if (itemType == 520) {
                player.gainMeso(ii.getMeso(itemId), true, false, true);
                remove(c, itemId);
                c.getSession().write(MaplePacketCreator.enableActions());
            } else if (itemType == 524) {
                for (int i = 0; i < 3; i++) {
                    MaplePet pet = player.getPet(i);
                    if (pet != null) {
                        if (pet.canConsume(itemId)) {
                            pet.setFullness(100);
                            if (pet.getCloseness() + 100 > 30000) {
                                pet.setCloseness(30000);
                            } else {
                                pet.gainCloseness(100);
                            }
                            while (pet.getCloseness() >= ExpTable.getClosenessNeededForLevel(pet.getLevel())) {
                                pet.setLevel(pet.getLevel() + 1);
                                c.getSession().write(MaplePacketCreator.showOwnPetLevelUp(player.getPetIndex(pet)));
                                player.getMap().broadcastMessage(MaplePacketCreator.showPetLevelUp(player, player.getPetIndex(pet)));
                            }
                            c.getSession().write(MaplePacketCreator.updatePet(pet));
                            player.getMap().broadcastMessage(player, MaplePacketCreator.commandResponse(player.getId(), 0, 1, true), true);
                            remove(c, itemId);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } else if (itemType == 530) {
                ii.getItemEffect(itemId).applyTo(player);
                remove(c, itemId);
            } else if (itemType == 533) {
                NPCScriptManager.getInstance().start(c, 9010009, null, null);
            } else if (itemType == 537) {
                if (!FieldLimit.CANNOTCHALKBOARD.check(c.getPlayer().getMapId())) {
                    return;
                }
                player.setChalkboard(slea.readMapleAsciiString());
                player.getMap().broadcastMessage(MaplePacketCreator.useChalkboard(player, false));
                player.getClient().getSession().write(MaplePacketCreator.enableActions());
            } else if (itemType == 539) {
                List<String> lines = new LinkedList<String>();
                for (int i = 0; i < 4; i++) {
                    lines.add(slea.readMapleAsciiString());
                }
                c.getChannelServer().getWorldInterface().broadcastMessage(null, MaplePacketCreator.getAvatarMega(player, medal, c.getChannel(), itemId, lines, (slea.readByte() != 0)).getBytes());
                remove(c, itemId);
            } else if (itemType == 545) {// MiuMiu's travel store
                c.getSession().write(MaplePacketCreator.enableActions());
            } else if (itemType == 552) {
                MapleInventoryType type = MapleInventoryType.getByType((byte) slea.readInt());
                byte slot = (byte) slea.readInt();
                IItem item = player.getInventory(type).getItem(slot);
                if (item == null || item.getQuantity() <= 0 || (item.getFlag() & InventoryConstants.KARMA) > 0 && ii.isKarmaAble(item.getItemId())) {
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                }
                item.setFlag((byte) InventoryConstants.KARMA);
                c.getSession().write(MaplePacketCreator.clearInventoryItem(type, item.getPosition(), false));
                c.getSession().write(MaplePacketCreator.addInventorySlot(type, item, false));
                remove(c, itemId);
                c.getSession().write(MaplePacketCreator.enableActions());
            } else if (itemType == 557) {
                slea.readInt();
                int itemSlot = slea.readInt();
                slea.readInt();
                final IEquip equip = (IEquip) player.getInventory(MapleInventoryType.EQUIP).getItem((byte) itemSlot);
                if (equip.getVicious() == 2 || player.getInventory(MapleInventoryType.CASH).findById(5570000) == null) {
                    return;
                }
                equip.setVicious(equip.getVicious() + 1);
                equip.setUpgradeSlots(equip.getUpgradeSlots() + 1);
                remove(c, itemId);
                c.getSession().write(MaplePacketCreator.enableActions());
                c.getSession().write(MaplePacketCreator.sendHammerData(equip.getVicious()));
                c.getSession().write(MaplePacketCreator.hammerItem(equip));
            } else {
                System.out.println("NEW CASH ITEM: " + itemType + "\n" + slea.toString());
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        } catch (RemoteException re) {
            c.getChannelServer().reconnectWorld();
        }
    }

    private static final void remove(MapleClient c, int itemId) {
        MapleInventoryManipulator.removeById(c, MapleInventoryType.CASH, itemId, 1, true, false);
    }

    private static final boolean getIncubatedItem(MapleClient c, int id) {
        final int[] ids = {1012070, 1302049, 1302063, 1322027, 2000004, 2000005, 2020013, 2020015, 2040307, 2040509, 2040519, 2040521, 2040533, 2040715, 2040717, 2040810, 2040811, 2070005, 2070006, 4020009};
        final int[] quantitys = {1, 1, 1, 1, 240, 200, 200, 200, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3};
        int amount = 0;
        for (int i = 0; i < ids.length; i++) {
            if (i == id) {
                amount = quantitys[i];
                break;
            }
        }
        if (c.getPlayer().getInventory(MapleInventoryType.getByType((byte) (id / 1000000))).isFull()) {
            return false;
        }
        MapleInventoryManipulator.addById(c, id, (short) amount);
        return true;
    }
}
