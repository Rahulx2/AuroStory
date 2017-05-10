/* 
 *@Author: iAkira 
 *@Map(s): 922240100 
 *@Function: Clock[anything else?] 
*/ 
importPackage(Packages.tools); 

function start(ms) { 
        ms.getPlayer().resetEnteredScript(); 
        ms.getClient().getSession().write(MaplePacketCreator.getClock(180)); 
}  