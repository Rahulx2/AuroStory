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
 * Gachapon Script - Henesys, currently with Ellinia items
 * @author Moogra
 * @NPC : Gachapon - Henesys
 * @NPC ID : 9100101
 * Item IDs by Sam
 * TODO: FINISH REAL TEXT, use sendSimpleNext for text selection
*/
var ids = [5220000,5220000,5220000,5220000,5220000,5220000,5220000,5220000,5220000,5220000,1012076];
var status = 0;

function start() {
    if (cm.haveItem(5220000, 300)) {
        cm.gainItem(5220000, -300);
        cm.processGachapon(ids, true);
        cm.dispose();
    } else if (cm.haveItem(5220000, 300))
        cm.sendYesNo("Welcome to the God of Gachapons. It will cost you 300 gachapons to use this and you have 1 out of 10 chance of getting THE item");
    else {
        cm.sendSimple("Welcome I AM THE GOD OF Gachapon. How may I help you?\r\n\r\n#L0#What is  so great about this God of Gachapons?#l\r\n#L1#Where can you buy Gachapon tickets?#l");
    }
}

function action(mode, type, selection){
    if (mode == 1 && cm.haveItem(5220000, 300)) {
        cm.processGachapon(ids, false);
        cm.dispose();
    } else {
        if (mode > 0) {
            status++;
            if (selection == 0) {
				cm.gainItem(5220000, -100);
				cm.sendSimple("You dumb Cunt I'm a god don't be a fucktard, oh and i just took 100 gachapon tix thnx bitch");
				cm.dispose();
            } else if (selection == 1) {
                cm.sendNext("If you don't know don't talk to me.");
                cm.dispose();
            }
        }
    }
}