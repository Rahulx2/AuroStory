/* 
 *@Author: iAkira 
 *@Map(s): 922240000 
 *@Function: Handles all map-related effects 
*/ 
importPackage(Packages.tools); 

function start(ms) { 
    try { 
        ms.getPlayer().resetEnteredScript(); 
        ms.getClient().getSession().write(MaplePacketCreator.showEffect("event/space/start")); 
        ms.getClient().getSession().write(MaplePacketCreator.getClock(180));        
        ms.getPlayer().startMapEffect("Please rescue Gaga within the time limit.", 5120027); 
    } catch(err) { 
        ms.getPlayer().dropMessage(err); 
    } 
}  