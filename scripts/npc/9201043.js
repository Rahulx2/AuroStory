var job;
var status = 0;
function start() 
{
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) { 
			cm.sendSimple("Hey there #b#h #.#k! Welcome to ImprovedStory! How would you like to proceed?\r\n\r\n#b#L0#What are the rates?#l\r\n#L1#Who are the current GMs?\#l\r\n#L2#How do I get NX?#l\r\n#L3#Get me out of here!#l\r\n#L4#I'm here to claim my Mark of the Beta!#l");
        } else if (status == 1) {
			if (selection == 0) {
				cm.sendOk("The rates are 83x EXP, 50x Meso and 5x Drop.");
				cm.dispose();
			} else if (selection == 1) {
				cm.sendOk("Currently our GMs are Chris, Will, Courtney, Lexy.");
				cm.dispose();
			} else if (selection == 2) {
				cm.sendOk("You gain NX by killing monsters, so kill some mobs and start earning! NX is also available through donating and voting; please check the main site at http://www.odinms.org for details.");
				cm.dispose();
			} else if (selection == 3) {
				cm.warp(100000000);
				cm.dispose();
			} else if (selection == 4) {
				{
					if((cm.getPlayer().isBeta()) && (!cm.getPlayer().hasreceivedMOTB()))
					{
						cm.sendOk("Here you go! Thank you for playing beta, and welcome back to ImprovedStory!");
						cm.gainItem(1002419);
						cm.getPlayer().setreceivedMOTB(true);
					}
					else if((cm.getPlayer().isBeta()) && (cm.getPlayer().hasreceivedMOTB()))
						cm.sendOk("You have already received your Mark of the Beta.");
					else
						cm.sendOk("Unfortunately, you aren't eligible for the Mark of the Beta.");
					cm.dispose();
				}
			}
	}
}
}