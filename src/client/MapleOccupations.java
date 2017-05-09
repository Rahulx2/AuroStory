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
/*
 *
 * @author blawgwarmzz
 * MapleOccupations.java
 *
 */
package client;

public enum MapleOccupations {

    NOJOB(0),
    HOBO(1),
    Specialist(100),
    BlackOp(200),
    Monk(300),
    Sinner(400),
    Businessman(500),
    GM(600),
    Sacrificer(700),
    Grim(800);
    final int jobid;

    private MapleOccupations(int id) {
        jobid = id;
    }

    public int getId() {
        return jobid;
    }

    public static MapleOccupations getById(int id) {
        for (MapleOccupations l : MapleOccupations.values()) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    public boolean isA(MapleOccupations basejob) {
        return getId() >= basejob.getId() && getId() / 100 == basejob.getId() / 100;
    }
}
