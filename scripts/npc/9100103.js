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
 * Gachapon
 * @NPC : Gachapon - Kerning City
 * @NPC ID : 9100103
 * @author Moogra
 * Item IDs by Sam
*/

var ids = [1032060,1032061,2049100,2049100,2049100,2049100,2049100,2049100,2049100,2049100,2049100,3994100,3994097,3994096,3994098,3994099,3010009,3010012,3010018,3011000,3010025,3010046,3010057,3010058,3010072,3010047,1092049,1102206,1102207,1102205,1072344,1052167,1022081,1122012,1032072,1032071,1032060,1022073,1122014,1082246,1082223,1112407,1112408,1002858,1002859,1002860,1002861,1302101,1302100,1302099,1302098,1382045,1382046,1382047,1382048,1382049,1382050,1382051,1382052,1382045,1452045,1452053,1472054,1492020,1492021,1492022,1482022,1482020,1472073,1482021,1492021,1412040,2070016,2049100,2340000,2070018,2000004,2020012,2000005,2030007,2022027,2040001,2041002, 2040805, 2040702, 2043802, 2040402, 2043702, 1302022, 1322021, 1322026, 1302026, 1442017, 1082147, 1102043, 1442016, 1402012, 1302027, 1322027, 1322025, 1312012, 1062000, 1332020, 1302028, 1372002, 1002033, 1092022, 1302021, 1102041, 1102042, 1322024, 1082148, 1002012, 1322012, 1322022, 1002020, 1302013, 1082146, 1442014, 1002096, 1302017, 1442012, 1322010, 1442011, 1442018, 1092011, 1092014, 1302003, 1432001, 1312011, 1002088, 1041020, 1322015, 1442004, 1422008, 1302056, 1432000, 1382001, 1041053, 1060014, 1050053, 1051032, 1050073, 1061036, 1002253, 1002034, 1051025, 1050067, 1051052, 1002072, 1002144, 1051054, 1050069, 1372007, 1050056, 1050074, 1002254, 1002274, 1002218, 1051055, 1382010, 1002246, 1050039, 1382007, 1372000, 1002013, 1050072, 1002036, 1002243, 1372008, 1382008, 1382011, 1092021, 1051034, 1050047, 1040019, 1041031, 1051033, 1002153, 1002252, 1051024, 1002153, 1050068, 1382003, 1382006, 1050055, 1051031, 1050025, 1002155, 1002245, 1452004, 1452023, 1060057, 1040071, 1002137, 1462009, 1452017, 1040025, 1041027, 1452005, 1452007, 1061057, 1472006, 1472019, 1060084, 1472028, 1002179, 1082074, 1332015, 1432001, 1060071, 1472007, 1472002, 1051009, 1061037, 1332016, 1332034, 1472020, 1102084, 1102086, 1102042, 1032026, 1082149];
var status = 0;

function start() {
    if (cm.haveItem(5220000, 3)) {
        cm.gainItem(5220000, -3);
        cm.processGachapon(ids, true);
        cm.dispose();
    } else if (cm.haveItem(5220000, 3))
        cm.sendYesNo("This is the elite Gachapon, You may get items that you will not find anywhere else, this will require 3 gachapon tickets.");
    else {
        cm.sendSimple("Welcome to the Elite Gachapon. How may I help you?\r\n\r\n#L0#What is Gachapon?#l\r\n#L1#Where can you buy Gachapon tickets?#l");
    }
}

function action(mode, type, selection){
    if (mode == 1 && cm.haveItem(5220000, 3)) {
        cm.processGachapon(ids, false);
        cm.dispose();
    } else {
        if (mode > 0) {
            status++;
            if (selection == 0) {
                cm.sendNext("Play Elite Gachapon to earn rare scrolls, equipment, chairs, mastery books, and other cool items! All you need is 3 #bGachapon Ticket#k to be the winner of a random mix of items.");
            } else if (selection == 1) {
                cm.sendNext("Gachapon Tickets are available in the #rCash Shop#k and can be purchased using NX or Maple Points. Click on the red SHOP at the lower right hand corner of the screen to visit the #rCash Shop #kwhere you can purchase tickets.");
                cm.dispose();
            } else if (status == 2) {
                cm.sendNext("You'll find Godly Shit here or I might just eat your Gachapons :)");
                cm.dispose();
            }
        }
    }
}