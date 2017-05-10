/* 
 * This file is part of the OdinMS Maple Story Server
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
	Boats Between Ellinia and Orbis
-- By ---------------------------------------------------------------------------------------------
	Information
-- Version Info -----------------------------------------------------------------------------------
	1.8 - Added haveBalrog back for the portal scripts
            - Made balrogs spawn 1 or 2 only
            - Fixed up mixing up of portalscript
        1.7 - Fix for CME
            - Changed invasion handler
            - Cleaned up code alittle
        1.6 - Fix for infinity looping [Information]
	1.5 - Ship/boat is now showed 
	    - Removed temp message[Information]
	    - Credit to Snow/superraz777 for old source
	    - Credit to Titan/Kool for the ship/boat packet
	1.4 - Fix typo [Information]
	1.3 - Removing some function since is not needed [Information]
	    - Remove register player menthod [Information]
	    - Remove map instance and use reset reactor function [Information]
	1.2 - Should be 2 ship not 1 [Information]
	1.1 - Add timer variable for easy edit [Information]
	1.0 - First Version by Information
---------------------------------------------------------------------------------------------------
**/

importPackage(Packages.tools);

var cleanTime = 60000; //The time to clean the boat - 1 min
var closeTime = 780000; //The time to close the entry - 13 mins
var beginTime = 60000; //The time to begin the ride - 1mins
var rideTime = 600000; //The time that require move to destination - 10mins
var invasionTime = 180000; //The time that spawn balrog - 3 mins
var Ellinia_Station;
var To_Ellinia_Takeoff;
var To_Ellinia_Boat;
var To_Ellinia_Cabin;
var To_Orbis_Takeoff;
var To_Orbis_Boat;
var To_Orbis_Cabin;
var Orbis_Station;
var Orbis_Dock;

function init() {
    //Define maps
    Ellinia_Station = em.getChannelServer().getMapFactory().getMap(101000300);

    To_Orbis_Takeoff = em.getChannelServer().getMapFactory().getMap(101000301);
    To_Orbis_Boat = em.getChannelServer().getMapFactory().getMap(200090010);
    To_Orbis_Cabin = em.getChannelServer().getMapFactory().getMap(200090011);

    To_Ellinia_Takeoff = em.getChannelServer().getMapFactory().getMap(200000112);
    To_Ellinia_Boat = em.getChannelServer().getMapFactory().getMap(200090000);
    To_Ellinia_Cabin = em.getChannelServer().getMapFactory().getMap(200090001);

    Orbis_Station = em.getChannelServer().getMapFactory().getMap(200000100);
    Orbis_Dock = em.getChannelServer().getMapFactory().getMap(200000111);

    //Open portals
    To_Orbis_Cabin.getPortal("out00").setScriptName("OBoat1");
    To_Orbis_Cabin.getPortal("out01").setScriptName("OBoat2");
    To_Ellinia_Cabin.getPortal("out00").setScriptName("EBoat1");
    To_Ellinia_Cabin.getPortal("out01").setScriptName("EBoat2");

    //Show Ship is docked at Ellinia
    Ellinia_Station.setDocked(true);
    Orbis_Station.setDocked(true);
    
    //Start
    scheduleNew();
}

function scheduleNew() {
    em.setProperty("haveBalrog", "false");
    em.setProperty("docked", "true");
    em.schedule("allowentry", closeTime);
    em.schedule("stopentry", cleanTime + closeTime);
    em.schedule("takeoff", cleanTime + closeTime + beginTime);
}

function allowentry() {
    em.setProperty("entry","true");
}

function stopentry() {
    //Spawn boxes
    To_Ellinia_Cabin.resetReactors();
    To_Orbis_Cabin.resetReactors();

    em.setProperty("entry","false");
}

function takeoff() {
    em.setProperty("docked","false");
    To_Orbis_Takeoff.warpMap(To_Orbis_Boat);
    Ellinia_Station.setDocked(false);
    To_Ellinia_Takeoff.warpMap(To_Ellinia_Boat);
    Orbis_Dock.setDocked(false);
    em.schedule("invasion", invasionTime);
    em.schedule("arrived", rideTime);
}

function arrived() {
    To_Orbis_Boat.warpMap(Orbis_Station);
    To_Orbis_Cabin.warpMap(Orbis_Station);
    Orbis_Dock.setDocked(true);
    To_Orbis_Boat.killAllMonsters();
    To_Ellinia_Boat.warpMap(Ellinia_Station);
    To_Ellinia_Cabin.warpMap(Ellinia_Station);
    Ellinia_Station.setDocked(true);
    To_Ellinia_Boat.killAllMonsters();
    scheduleNew();
}

function invasion() {
    var numspawn = 0;
    var chance = Math.floor(Math.random() * 10);
    if (chance >= 7) { // 30%
        numspawn = 2;
    } else if (chance >= 5 && chance <= 6) { // 20%
        numspawn = 1;
    } else { // 50%
        numspawn = 0;
    }
    if(numspawn > 0) {
        for(var i = 0; i < numspawn; i++) {
            To_Orbis_Boat.spawnMonsterOnGroundBelow(8150000, 485, -221);
            To_Ellinia_Boat.spawnMonsterOnGroundBelow(8150000, -590, -221);
        }
        em.setProperty("haveBalrog", "false");
        To_Orbis_Boat.broadcastMessage(MaplePacketCreator.musicChange("Bgm04/ArabPirate"));
        To_Ellinia_Boat.broadcastMessage(MaplePacketCreator.musicChange("Bgm04/ArabPirate"));
    }
}

function cancelSchedule() {
}