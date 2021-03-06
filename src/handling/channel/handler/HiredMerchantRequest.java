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

import java.util.Arrays;
import client.MapleClient;
import handling.AbstractMaplePacketHandler;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import java.rmi.RemoteException;

/**
 *
 * @author XoticStory
 */
public final class HiredMerchantRequest extends AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        try {
            if (c.getChannelServer().getWorldRegistry().getProperty("merchants").equals(Boolean.FALSE)) {
                c.getPlayer().dropMessage("Merchants are currently disabled. Please check back soon!");
                return;
            }
        } catch (RemoteException re) {
            c.getChannelServer().reconnectWorld();
            return;
        }

        if (c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 23000, Arrays.asList(MapleMapObjectType.HIRED_MERCHANT)).size() == 0 && c.getPlayer().getMapId() > 910000000 && c.getPlayer().getMapId() < 910000023) {
            if (!c.getPlayer().hasMerchant()) {
                c.getSession().write(MaplePacketCreator.hiredMerchantBox());
            } else {
                c.getPlayer().dropMessage(1, "You already have a store open.");
            }
        } else {
            c.getPlayer().dropMessage(1, "You cannot open your hired merchant here.");
        }
    }
}
