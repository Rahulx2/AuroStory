/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.command;

import client.IItem;
import client.ISkill;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleStat;
import client.SkillFactory;
import java.io.File;
import handling.channel.ChannelServer;
import scripting.npc.NPCScriptManager;
import server.MapleShopFactory;
import tools.MaplePacketCreator;
import tools.StringUtil;
import java.util.ArrayList;
import server.MapleItemInformationProvider;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import tools.Pair;
import tools.DatabaseConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import server.MapleInventoryManipulator;

/**
 *
 * @author Administrator
 */
public class Player {

    static void execute(MapleClient c, String[] splitted, char heading) {
        ChannelServer cserv = c.getChannelServer();
        MapleCharacter player = c.getPlayer();
        if (splitted[0].equals("woof")) {
            player.message("meow");
        } else if (splitted[0].equals("str") || splitted[0].equals("int") || splitted[0].equals("luk") || splitted[0].equals("dex")) {
            int amount = Integer.parseInt(splitted[1]);
            boolean str = splitted[0].equals("str");
            boolean Int = splitted[0].equals("int");
            boolean luk = splitted[0].equals("luk");
            boolean dex = splitted[0].equals("dex");
            if (amount > 0 && amount <= player.getRemainingAp() && amount <= 32763 || amount < 0 && amount >= -32763 && Math.abs(amount) + player.getRemainingAp() <= 32767) {
                if (str && amount + player.getStr() <= 32767 && amount + player.getStr() >= 4) {
                    player.setStr(player.getStr() + amount);
                    player.updateSingleStat(MapleStat.STR, player.getStr());
                    player.setRemainingAp(player.getRemainingAp() - amount);
                    player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                } else if (Int && amount + player.getInt() <= 32767 && amount + player.getInt() >= 4) {
                    player.setInt(player.getInt() + amount);
                    player.updateSingleStat(MapleStat.INT, player.getInt());
                    player.setRemainingAp(player.getRemainingAp() - amount);
                    player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                } else if (luk && amount + player.getLuk() <= 32767 && amount + player.getLuk() >= 4) {
                    player.setLuk(player.getLuk() + amount);
                    player.updateSingleStat(MapleStat.LUK, player.getLuk());
                    player.setRemainingAp(player.getRemainingAp() - amount);
                    player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                } else if (dex && amount + player.getDex() <= 32767 && amount + player.getDex() >= 4) {
                    player.setDex(player.getDex() + amount);
                    player.updateSingleStat(MapleStat.DEX, player.getDex());
                    player.setRemainingAp(player.getRemainingAp() - amount);
                    player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
                } else {
                    player.dropMessage("Please make sure the stat you are trying to raise is not over 32,767 or under 4.");
                }
            } else {
                player.dropMessage("Please make sure your AP is not over 32,767 and you have enough to distribute.");
            }
        }  else if (splitted[0].equalsIgnoreCase("clearinv")) {
                if (splitted.length == 2) {
                    if (splitted[1].equalsIgnoreCase("all")) {
                        clearSlot(c, 1);
                        clearSlot(c, 2);
                        clearSlot(c, 3);
                        clearSlot(c, 4);
                        clearSlot(c, 5);
                    } else if (splitted[1].equalsIgnoreCase("equip")) {
                        clearSlot(c, 1);
                    } else if (splitted[1].equalsIgnoreCase("use")) {
                        clearSlot(c, 2);
                    } else if (splitted[1].equalsIgnoreCase("etc")) {
                        clearSlot(c, 3);
                    } else if (splitted[1].equalsIgnoreCase("setup")) {
                        clearSlot(c, 4);
                    } else if (splitted[1].equalsIgnoreCase("cash")) {
                        clearSlot(c, 5);
                    } else {
                        player.dropMessage("@clearslot " + splitted[1] + " does not exist!");
                        player.dropMessage("@clearslot <all, equip, use, etc, setup, cash>");
                    }
                }
                    } else if (splitted[0].equals("expfix")) {
            player.setExp(0);
            player.updateSingleStat(MapleStat.EXP, player.getExp());                 
                } else if (splitted[0].equals("maxskills")) {
            if (!player.getCheatTracker().Spam(1200000, 0)) {
                for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
                    try {
                        ISkill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                        player.changeSkillLevel(skill, skill.getMaxLevel(), skill.getMaxLevel());
                    } catch (NumberFormatException nfe) {
                        break;
                    } catch (NullPointerException npe) {
                        continue;
                    }
                    player.saveToDB(true);
                }
            } else {
                player.dropMessage("Please do not spam maxing skills");
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        } else if (splitted[0].equals("gm")) {
            if (splitted.length < 2) {
                return;
            }
            if (!player.getCheatTracker().Spam(300000, 1)) { // 5 minutes.
                try {
                    c.getChannelServer().getWorldInterface().broadcastGMMessage(null, MaplePacketCreator.serverNotice(6, "Channel: " + c.getChannel() + "  " + player.getName() + ": " + StringUtil.joinStringFrom(splitted, 1)).getBytes());
                } catch (Exception ex) {
                    c.getChannelServer().reconnectWorld();
                }
                player.dropMessage("Message sent.");
            } else {
                player.dropMessage(1, "Please don't flood GMs with your messages.");
            } 
            } else if (splitted[0].equals("dispose")) {
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(MaplePacketCreator.enableActions());
            player.message("Done.");
        } else if (splitted[0].equals("save")) {
            player.saveToDB(true);
            player.dropMessage("Save complete!");
        } else if (splitted[0].equals("spinel")) {
            NPCScriptManager.getInstance().start(c, 9000020, null, null);
        } else if (splitted[0].equals("anhero")) {
            player.setHp(0);
            player.updateSingleStat(MapleStat.HP, 0);
            player.dropMessage("FAIL :D");
        } else if (splitted[0].equals("shop")) {
            MapleShopFactory.getInstance().getShop(6969).sendShop(c);
        } else if (splitted[0].equals("joinrace")) {
                   if (c.getPlayer().getEntryNumber() < 1) {
                   if (c.getPlayer().getMapId() == 100000000){
                   if (cserv.getWaiting()){
                       c.getPlayer().setEntryNumber(cserv.getCompetitors() + 1);
                       cserv.setCompetitors(cserv.getCompetitors() + 1);
		player.dropMessage("[Notice]: You have successfully joined the race! Your entry number is " + c.getPlayer().getEntryNumber() + ".");
                   } else {
                       player.dropMessage("There is no event currently taking place.");
                   }
                   } else {
                        player.dropMessage("You are not at Henesys.");
                   }
                   }else{
                         player.dropMessage("You have already joined this race.");
                   }
                 } else if (splitted[0].equals("rules")) {
                   if (cserv.getWaiting() || cserv.getRace()) {
		player.message("The Official Rules and Regulations of the Great Victoria Island Race:");
                player.message("-------------------------------------------------------------------------------------------");
                player.message("To win you must race from Henesys all the way to Henesys going Eastward.");
                player.message("Rule #1: No cheating. You can't use any warping commands, or you'll be disqualified.");
                player.message("Rule #2: You may use any form of transportation. This includes Teleport, Flash Jump and Mounts.");
                player.message("Rule #3: You are NOT allowed to kill any monsters in your way. They are obstacles.");
                player.message("Rule #4: You may start from anywhere in Henesys, but moving on to the next map before the start won't work.");
                   } else {
                       player.message("There is no event currently taking place.");
                   }
                   } else if (splitted[0].equals("potshop")) {
            MapleShopFactory.getInstance().getShop(1336).sendShop(c);
        } else if (splitted[0].equals("storage")) {
            player.getStorage().sendStorage(player.getClient(), 1032006);
        } else if (splitted[0].equals("whatdrops") || splitted[0].equals("droplist")) {
            String searchString = StringUtil.joinStringFrom(splitted, 1);
            boolean itemSearch = splitted[0].equals("whatdrops");
            int limit = 5;
            ArrayList<Pair<Integer, String>> searchList;
            if (itemSearch) {
                searchList = MapleItemInformationProvider.getInstance().getItemDataByName(searchString);
            } else {
                searchList = CommandProcessor.getMobsIDsFromName(searchString);
            }
            Iterator<Pair<Integer, String>> listIterator = searchList.iterator();
            for (int i = 0; i < limit; i++) {
                if (listIterator.hasNext()) {
                    Pair<Integer, String> data = listIterator.next();
                    if (itemSearch) {
                        player.dropMessage("Item " + data.getRight() + " dropped by:");
                    } else {
                        player.dropMessage("Mob " + data.getRight() + " drops:");
                    }
                    try {
                        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM monsterdrops WHERE " + (itemSearch ? "itemid" : "monsterid") + " = ? LIMIT 50");
                        ps.setInt(1, data.getLeft());
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            String resultName;
                            if (itemSearch) {
                                resultName = CommandProcessor.getMobNameFromID(rs.getInt("monsterid"));
                            } else {
                                resultName = MapleItemInformationProvider.getInstance().getName(rs.getInt("itemid"));
                            }
                            if (resultName != null) {
                                player.dropMessage(resultName);
                            }
                        }
                        rs.close();
                        ps.close();
                    } catch (Exception e) {
                        player.dropMessage("There was a problem retreiving the required data. Please try again.");
                        e.printStackTrace();
                        return;
                    }
                } else {
                    break;
                }
            }

        } else if (splitted[0].equals("commands") || splitted[0].equals("help") || splitted[0].equals("ayuda")) {
            player.dropMessage("Commands of AuroStory");
    //        player.dropMessage("@shop - Opens up the basic shop.");
      //      player.dropMessage("@potshop - Opens up the potion shop.");
        //    player.dropMessage("@spinel - Opens up Spinel from any location.");
          //  player.dropMessage("@dispose - Solves various problems with NPCs.");
        //    player.dropMessage("@anhero  - Causes your character to die.");
        //    player.dropMessage("@storage - Opens up your storage.");
         //   player.dropMessage("@whatdrops - Tells you what drops a particular item.");
         //   player.dropMessage("@droplist - Tells you what items drop from a particular mob.");
            player.dropMessage("@str/@dex/@int/@luk - Adds stats faster.");
            player.dropMessage("@joinrace - Enjoy Race of Event GM");
            player.dropMessage("@rules - Rules of Island Race");
        } else {
            player.message("Command " + heading + splitted[0] + " does not exist.: do @commands - @help - @ayuda");
        }
    }

    private static void clearSlot(MapleClient c, int type) {
        MapleInventoryType invent;
        if (type == 1) {
            invent = MapleInventoryType.EQUIP;
        } else if (type == 2) {
            invent = MapleInventoryType.USE;
        } else if (type == 3) {
            invent = MapleInventoryType.ETC;
        } else if (type == 4) {
            invent = MapleInventoryType.SETUP;
        } else {
            invent = MapleInventoryType.CASH;
        }
        List<Integer> itemMap = new LinkedList<Integer>();
        for (IItem item : c.getPlayer().getInventory(invent).list()) {
            itemMap.add(item.getItemId());
        }
        for (int itemid : itemMap) {
            MapleInventoryManipulator.removeAllById(c, itemid, false);
        }
    }
}
