var map = 240050101;
var minLvl = 9;
var maxLvl = 256;
var minAmt = 1;
var maxAmt = 6;

function start() {
    if (cm.getParty() == null) {
        cm.sendOk("If you want to enter the PQ, #bthe leader of your party must talk to me#k. Level range 10 ~ 200, 2~6 person party.");
        cm.dispose();
    } else if (!cm.isLeader()) {
        cm.sendOk("If you want to try the quest, please tell the #bleader of your party#k to talk to me.");
        cm.dispose();
    }else{
        var party = cm.getParty().getMembers();
        var inMap = cm.partyMembersInMap();
        var lvlOk = 0;
        for (var i = 0; i < party.size(); i++) {
        if (party.get(i).getLevel() >= minLvl && party.get(i).getLevel() <= maxLvl)
            lvlOk++;
        }
        if (inMap < minAmt || inMap > maxAmt) {
            cm.sendOk("You don't have enough people in your party. You need a party of #b"+minAmt+"#k - #r"+maxAmt+"#k members and they must be in the map with you. There are #b"+inMap+"#k members here.");
            cm.dispose();
        } else if (lvlOk != inMap) {
            cm.sendOk("Someone in your party isn't the proper level. Everyone needs to be Lvl. #b"+minLvl+"#k - #r"+maxLvl+"#k.");
            cm.dispose();
        } else if (cm.getPlayersInMap(240050101) >= 1 || cm.getPlayersInMap(240050102) >= 1 || cm.getPlayersInMap(240050103) >= 1 || cm.getPlayersInMap(240050310) >= 1 ) {
            cm.sendOk("Someone is inside the PQ.");
            cm.dispose();
        }else{
            cm.warpParty(map);
		cm.mapMessage(6,"[The Story]");
	cm.mapMessage(6,"Mocy's sister, Shumi, was kipnapped by the Master Guardian and thrown inside a black map.");
	cm.mapMessage(6,"The only way to enter the black map is to go over the 3 gate set up by the Master Guardian.");
	cm.mapMessage(6,"However, you must kill all the monster at the gates without having them touching you.");
	cm.mapMessage(1,"<Stage One>\r\nYou have to elimate all monster in the map without being damaged by the monster in the map.\r\nAfter elimating all the monster, the leader of the party are to talk to Mocy at the very top right of the map to go to Stage Two.");
	cm.dispose();
        }
    }
}  