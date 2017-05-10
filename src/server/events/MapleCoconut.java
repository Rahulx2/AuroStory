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

package server.events;

import client.MapleCharacter;
import server.TimerManager;
import server.maps.MapleMap;
import tools.MaplePacketCreator;

/**
 *
 * @author kevintjuh93
 */
public class MapleCoconut {
       private MapleMap map = null;

       public MapleCoconut(MapleMap map) {
           this.map = map;
       }

       public void startEvent() {
           map.startEvent();
           map.broadcastMessage(MaplePacketCreator.hitCoconut(true, 0, 0));
           map.setCoconutsHittable(true);
           map.broadcastMessage(MaplePacketCreator.getClock(300));

        TimerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (map.getId() == 109080000) {
                    if (map.getMapleScore() == map.getStoryScore()) {
                    bonusTime();
                    } else if (map.getMapleScore() > map.getStoryScore()) {
                        for (MapleCharacter chr : map.getCharacters()) {
                            if (chr.getTeam() == 0) {
                                chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/victory"));
                                chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Victory"));
                            } else {
                                chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/lose"));
                                chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Failed"));
                            }
                        }
                        warpOut(0);
                    } else {
                        for (MapleCharacter chr : map.getCharacters()) {
                            if (chr.getTeam() == 1) {
                                chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/victory"));
                                chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Victory"));
                            } else {
                                chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/lose"));
                                chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Failed"));
                            }
                        }
                        warpOut(1);
                    }
                }
            }
        }, 300000);
       }

       public void bonusTime() {
           map.broadcastMessage(MaplePacketCreator.getClock(120));
        TimerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (map.getMapleScore() == map.getStoryScore()) {
                    for (MapleCharacter chr : map.getCharacters()) {
                        chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/lose"));
                        chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Failed"));
                    }
                    warpOut(-1);
                } else if (map.getMapleScore() > map.getStoryScore()) {
                        for (MapleCharacter chr : map.getCharacters()) {
                            if (chr.getTeam() == 0) {
                                chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/victory"));
                                chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Victory"));
                            } else {
                                chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/lose"));
                                chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Failed"));
                            }
                        }
                        warpOut(0);
                    } else {
                        for (MapleCharacter chr : map.getCharacters()) {
                            if (chr.getTeam() == 1) {
                                chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/victory"));
                                chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Victory"));
                            } else {
                                chr.getClient().getSession().write(MaplePacketCreator.showEffect("event/coconut/lose"));
                                chr.getClient().getSession().write(MaplePacketCreator.playSound("Coconut/Failed"));
                            }
                        }
                        warpOut(1);
                    }            
            }
        }, 120000);

       }

       public void warpOut(final int winteam) {
          map.setCoconutsHittable(false);
        TimerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
            for (MapleCharacter chr : map.getCharacters()) {
                if (chr.getTeam() == winteam) {
                chr.changeMap(109050000);
                } else {
                chr.changeMap(109050001);
                }
            }
            map.resetCoconutScore();
            map.setCoconut(null);
            }
        }, 12000);
       }
}  