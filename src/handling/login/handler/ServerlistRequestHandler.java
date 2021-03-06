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

import client.MapleClient;
import Config.Server;
import java.rmi.RemoteException;
import handling.AbstractMaplePacketHandler;
import handling.login.LoginServer;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public final class ServerlistRequestHandler extends AbstractMaplePacketHandler {

    private static final String[] names = {"Scania", "Bera", "Broa", "Windia", "Khaini", "Bellocan", "Mardia", "Kradia", "Yellonde", "Demethos", "Elnido", "Kastia", "Judis", "Arkenia", "Plana", "Galicia", "Kalluna", "Stius", "Croa", "Zenith", "Medere"};

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        for (int i = 0; i < Server.NUM_WORLDS; i++) {//input world numbers here change 1 to number of worlds
            try {
                c.getSession().write(MaplePacketCreator.getServerList(i, names[i], LoginServer.getInstance().getWorldInterface().getChannelLoad(i)));
            } catch (RemoteException e) {
            }
        }
        c.getSession().write(MaplePacketCreator.getEndOfServerList());
    }
}
