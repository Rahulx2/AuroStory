var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            cm.sendSimple("\r\n#L0#Quest complete!#l\r\n#L1#Bye.#l");
            } else if (status == 1) {
            if (selection == 0) {
                    if (cm.getPlayer().getMap().getMonsterCount() == 0 && cm.isLeader() && cm.haveItem(4032485)) {      
                     cm.warpParty(240050200);
            cm.gainItem(4032485, -1)
	cm.mapMessage(5,"Mocy: Look like it is too late to save her now....");
	cm.mapMessage(1,"Enter the portal to get your reward.");
	cm.dispose();
                     } else {
                   cm.sendOk("Sorry, it is either\r\nThere is still mob in the map,\r\nYou are not the party leader or\r\nYou do not have the #bLarge Model of a Coin#k.");
                   cm.dispose();
                   }
            } else if (selection == 1) {
                    cm.sendOk("Bye.");
                   cm.dispose();
                    
	
    }
}
}  
       
  
}        