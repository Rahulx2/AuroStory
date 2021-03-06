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
package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import handling.AbstractMaplePacketHandler;
import tools.DatabaseConnection;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.command.CommandProcessor;

/**
 *
 * @author Matze
 */
public final class WhisperHandler extends AbstractMaplePacketHandler {

    private static HashMap<String, Integer> gm = new HashMap<String, Integer>();

    private static void populateGMList() {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT name, gm FROM characters WHERE gm > 0");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                gm.put(rs.getString("name"), 1);
            }
            rs.close();
            ps.close();
            rs = null;
            ps = null;
        } catch (SQLException sql) {
        }
    }

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        byte mode = slea.readByte();
        if (mode == 6) { // whisper
            String recipient = slea.readMapleAsciiString();
            String text = slea.readMapleAsciiString();
            if (CommandProcessor.processCommand(c, text)) {
                return;
            }
            MapleCharacter player = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);
            if (player != null) {
                player.getClient().getSession().write(MaplePacketCreator.getWhisper(c.getPlayer().getName(), c.getChannel(), text));
                c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 1));
            } else {// not found
                try {
                    if (c.getChannelServer().getWorldInterface().isConnected(recipient)) {
                        c.getChannelServer().getWorldInterface().whisper(c.getPlayer().getName(), recipient, c.getChannel(), text);
                        c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 1));
                    } else {
                        c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                    }
                } catch (RemoteException e) {
                    c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                    c.getChannelServer().reconnectWorld();
                }
            }
        } else if (mode == 5) { // - /find
            String recipient = slea.readMapleAsciiString();
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);
            if (victim != null && c.getPlayer().gmLevel() >= victim.gmLevel()) {
                if (victim.inCS()) {
                    c.getSession().write(MaplePacketCreator.getFindReply(victim.getName(), -1, 2));
                } else if (victim.inMTS()) {
                    c.getSession().write(MaplePacketCreator.getFindReply(victim.getName(), -1, 0));
                } else {
                    c.getSession().write(MaplePacketCreator.getFindReply(victim.getName(), victim.getMap().getId(), 1));
                }
            } else if (c.getPlayer().gmLevel() < victim.gmLevel()) {
                c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
            } else { // not found
                if (gm.size() == 0) {
                    populateGMList();
                }
                try {
                    int channel = c.getChannelServer().getWorldInterface().find(recipient);
                    if (channel > -1 && !gm.containsKey(recipient)) {
                        c.getSession().write(MaplePacketCreator.getFindReply(recipient, channel, 3));
                    } else {
                        c.getSession().write(MaplePacketCreator.getWhisperReply(recipient, (byte) 0));
                    }
                } catch (RemoteException ex) {
                    c.getChannelServer().reconnectWorld();
                }
            }
        }
    }
}
