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
            cm.sendSimple("\r\n#L0#Proceed us to next stage#l\r\n#L1#Bye.#l");
            } else if (status == 1) {
            if (selection == 0) {
                    if (cm.getPlayer().getMap().getMonsterCount() == 0) {      
                     cm.warpParty(240050310);
            cm.mapMessage(6,"[FINAL STAGE] Please elimate all the mob here.");
	cm.mapMessage(6,"[FINAL STAGE] The leader is to loot the item dropped by the boss after it died.");
	cm.mapMessage(1,"<BOSS STAGE>\r\n Please elimate all the mob here.\r\nThe leader is to loot the item dropped by the boss to clear quest.");
	cm.mapMessage(5,"Master Guardian: Haha it is too late to save Shumi! I have already eaten her!");
	cm.mapMessage(5,"Master Guardian: You people should be my meal too! Hahahahaaa");
	cm.mapMessage(5,"Master Guardian have casted a HP Seal on you. It will set your HP to 50 when ever you got hit.");
	cm.dispose();
                     } else {
                   cm.sendOk("Sorry, there is still mob in the map.");
                   cm.dispose();
                   }
            } else if (selection == 1) {
                    cm.sendOk("Bye.");
                   cm.dispose();
                    
	
    }
}
}  
       
  
}        