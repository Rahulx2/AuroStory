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
package handling.login.handler;

import client.IItem;
import client.Item;
import client.Equip;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventory;
import client.MapleInventoryType;
import client.MapleSkinColor;
import client.MapleJob;
import handling.AbstractMaplePacketHandler;
import server.MapleItemInformationProvider;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import java.util.Arrays;
import java.util.List;

public final class CreateCharHandler extends AbstractMaplePacketHandler {

    private final int[] allowedEquips = {1040006, 1040010, 1040002, 1060002, 1060006,
        1072005, 1072001, 1072037, 1072038, 1322005, 1312004, 1042167, 1062115, 1072383,
        1442079, 1302000, 1041002, 1041006, 1041010, 1041011, 1061002, 1061008}; //sauch meinen verdammte schwanz.

    /*  public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
    String name = slea.readMapleAsciiString();
    if (!MapleCharacter.canCreateChar(name)) {
    return;
    }
    MapleCharacter newchar = MapleCharacter.getDefault(c);
    newchar.setWorld(c.getWorld());
    int face = slea.readInt();
    newchar.setFace(face);
    newchar.setHair(slea.readInt() + slea.readInt());
    newchar.setSkinColor(MapleSkinColor.getById(slea.readInt()));
    int top = slea.readInt();
    int bottom = slea.readInt();
    int shoes = slea.readInt();
    int weapon = slea.readInt();
    newchar.setGender(slea.readByte());
    newchar.setName(name);
    MapleInventory equip = newchar.getInventory(MapleInventoryType.EQUIPPED);
    IItem eq_top = MapleItemInformationProvider.getInstance().getEquipById(top);
    eq_top.setPosition((byte) -5);
    equip.addFromDB(eq_top);
    IItem eq_bottom = MapleItemInformationProvider.getInstance().getEquipById(bottom);
    eq_bottom.setPosition((byte) -6);
    equip.addFromDB(eq_bottom);
    IItem eq_shoes = MapleItemInformationProvider.getInstance().getEquipById(shoes);
    eq_shoes.setPosition((byte) -7);
    equip.addFromDB(eq_shoes);
    IItem eq_weapon = MapleItemInformationProvider.getInstance().getEquipById(weapon);
    eq_weapon.setPosition((byte) -11);
    equip.addFromDB(eq_weapon);
    newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161001, (byte) 0, (short) 1));
    newchar.saveToDB(false);
    c.getSession().write(MaplePacketCreator.addNewCharEntry(newchar));
    }*/
    //   public final class CreateCharHandler extends AbstractMaplePacketHandler {
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        String name = slea.readMapleAsciiString();
        if (!MapleCharacter.canCreateChar(name)) {
            return;
        }
        MapleCharacter newchar = MapleCharacter.getDefault(c);
        newchar.setWorld(c.getWorld());
        int job = slea.readInt();
        int face = slea.readInt();
        newchar.setFace(face);
        newchar.setHair(slea.readInt() + slea.readInt());
        newchar.setSkinColor(MapleSkinColor.getById(slea.readInt()));
        int top = slea.readInt();
        int bottom = slea.readInt();
        int shoes = slea.readInt();
        int weapon = slea.readInt();

        if (!((containsInt(allowedEquips, top) && containsInt(allowedEquips, bottom) && containsInt(allowedEquips, shoes) && containsInt(allowedEquips, weapon)))) {
            //well now.
            c.banMacs(); //*shrugs* maybe they'll have logged in before
            c.getSession().close(true);
            return;
        }

        newchar.setGender(slea.readByte());
        newchar.setName(name);
        if (job == 0) { // Knights of Cygnus
            newchar.setJob(MapleJob.NOBLESSE);
            newchar.setMap(130000000);
            newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161047, (byte) 0, (short) 1));
        } else if (job == 1) { // Adventurer
            newchar.setMap(1000000);
            newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161001, (byte) 0, (short) 1));
        } else if (job == 2) { // Aran
            newchar.setJob(MapleJob.LEGEND);
            newchar.setMap(140000000);
            newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(4161048, (byte) 0, (short) 1));
        } else {
            System.out.println("[CHAR CREATION] A new job ID has been found: " + job);
        }
        MapleInventory equip = newchar.getInventory(MapleInventoryType.EQUIPPED);
        Equip eq_top = new Equip(top, (byte) -5, -1);
        eq_top.setWdef((short) 3);
        equip.addFromDB(eq_top.copy());
        Equip eq_bottom = new Equip(bottom, (byte) -6, -1);
        eq_bottom.setWdef((short) 2);
        equip.addFromDB(eq_bottom.copy());
        Equip eq_shoes = new Equip(shoes, (byte) -7, -1);
        eq_shoes.setWdef((short) 2); //rite? o_O
        equip.addFromDB(eq_shoes.copy());
        Equip eq_weapon = new Equip(weapon, (byte) -11, -1);
        eq_weapon.setWatk((short) 15);
        equip.addFromDB(eq_weapon.copy());
        newchar.saveToDB(false);
        c.getSession().write(MaplePacketCreator.addNewCharEntry(newchar));
    }

    private boolean containsInt(int[] array, int toCompare) {
        for (int i : array) {
            if (i == toCompare) {
                return true;
            }
        }
        return false;
    }
}
