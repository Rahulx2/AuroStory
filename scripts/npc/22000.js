/*Shanks - Class Medal giver o.o
@author fhrisbriner
*/
var status = 0;
var ejob = array(100, 200, 300, 400, 500);
var ejob2 = array(110, 120, 130, 210, 220, 230, 310, 320, 410, 420, 510, 520);
var ejob3 = array(111, 121, 131, 211, 221, 231, 311, 321, 411, 421, 511, 521);
var ejob4 = array(112, 122, 132, 212, 222, 232, 312, 322, 412, 422, 512, 522);
var cjob = array(1100, 1200, 1300, 1400, 1500);
var cjob2 = array(1110, 1210, 1310, 1410, 1510);
var cjob3 = array(1111, 1211, 1311, 1411, 1511);
var hjob = 2100;
var hjob2 = 2110;
var hjob3 = 2111;
var hjob4 = 2112;

function start() {
    cm.sendYesNo("Hi I'm The class medal giver of ImprovedStoryv83... Want your class medal now?");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0 && type != 1)
            status -= 2;
        else if(type == 1 || (mode == -1 && type != 1)){
            if(mode == 0)
                cm.sendOk("Hmm... I guess your not Ready yet...");
            cm.dispose();
            return;
        }
    }
    if (status == 1) {
        if (cm.getJobId() =  ejob);
            cm.gainItem(1142107);
			cm.dispose();
		} else if (status == 2) {
			(cm.getJobId() = ejob2)
            cm.gainItem(1142108);
			cm.dispose();
		} else if (status == 3) {
			(cm.getJobId() = ejob3)
            cm.gainItem(1142109);
			cm.dispose();
		} else if (status == 4) {		
			(cm.getJobId() = ejob4)
            cm.gainItem(1142110);
			cm.dispose();
		} else if (status == 5) {	
			(cm.getJobId() = cjob)
            cm.gainItem(1142066);
			cm.dispose();
		} else if (status == 6) {	
			(cm.getJobId() = cjob2)
            cm.gainItem(1142067);
			cm.dispose();
		} else if (status == 7) {
			(cm.getJobId() = cjob3)
            cm.gainItem(1142068);
			cm.dispose();
		} else if (status == 8) {	
			(cm.getJobId() = hjob)
            cm.gainItem(1142130);
			cm.dispose();
		} else if (status == 9) {	
			(cm.getJobId() = hjob2)
            cm.gainItem(1142131);
			cm.dispose();
		} else if (status == 10) {	
			(cm.getJobId() = hjob3)
            cm.gainItem(1142132);
			cm.dispose();
		} else if (status == 11) {	
			(cm.getJobId() = hjob4)
            cm.gainItem(1142133);
			cm.dispose();
			}
		}
    }
}