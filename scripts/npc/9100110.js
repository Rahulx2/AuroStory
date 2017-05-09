var ids = [2340000];
var status = 0;

function start() {
    if (cm.haveItem(5451000, 1000)) {
        cm.gainItem(5451000, -1000);
        cm.processGachapon(ids, true);
        cm.dispose();
    } else if (cm.haveItem(5220000, 10))
        cm.sendYesNo("You may use Gachapon. Would you like to use your Gachapon ticket?");
    else {
        cm.sendSimple("Welcome to the " + cm.getPlayer().getMap().getMapName() + " Gachapon. How may I help you?\r\n\r\n#L0#What is Gachapon?#l\r\n#L1#Where can you buy Gachapon tickets?#l");
    }
}

function action(mode, type, selection){
    if (mode == 1 && cm.haveItem(5220000, 10)) {
        cm.processGachapon(ids, false);
        cm.dispose();
    } else {
        if (mode > 0) {
            status++;
            if (selection == 0) {
                cm.sendNext("Play Gachapon to earn rare scrolls, equipment, chairs, mastery books, and other cool items! All you need is a #bGachapon Ticket#k to be the winner of a random mix of items.");
            } else if (selection == 1) {
                cm.sendNext("Gachapon Tickets are available in the #rCash Shop#k and can be purchased using NX or Maple Points. Click on the red SHOP at the lower right hand corner of the screen to visit the #rCash Shop #kwhere you can purchase tickets.");
                cm.dispose();
            } else if (status == 2) {
                cm.sendNext("You'll find a variety of items from the " + cm.getPlayer().getMap().getMapName() + " Gachapon, but you'll most likely find several related items and scrolls since " + cm.getPlayer().getMap().getMapName() + " is known as the town.");
                cm.dispose();
            }
        }
    }
}