/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Bowman Job Instructor - Ant Tunnel For Bowman (108000100)
-- By ---------------------------------------------------------------------------------------------
	Unknown
-- Version Info -----------------------------------------------------------------------------------
        1.3 - Recoded by XxOsirisxX (BubblesDev v75)
        1.2 - Clean up by Moogra
	1.1 - Statement fix [Information]
	1.0 - First Version by Unknown
---------------------------------------------------------------------------------------------------
**/

function start() {
    if (cm.haveItem(4031013,30)) {
        if (cm.canHold(4031012))
            cm.sendNext("Ohhhhh... you collected all 30 Dark Marbles!! Wasn't it difficult?? Alright. You've passed the test and for that I'll give you #bThe Proff of a Hero#k. Take that item and go back to Henesys");
        else
            cm.sendNext("Ohhhhh... you collected all 30 Dark Marbles!! But your inventory seems to be full, please make some room and talk to me again."); //Not GMS like.
    } else {
        cm.sendOk("You will have to collect me #b30 #t4031013##k. Good luck."); //Not GMS like
        cm.dispose();
    }
}

function action(mode, type, selection) {
    if (mode > 0){
        cm.removeAll(4031013);
		if(cm.haveItem(4031010))
			cm.gainItem(4031010, -1);
        cm.gainItem(4031012, 1);
        cm.warp(106010000, 1);
    }
    cm.dispose();
}