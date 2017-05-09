/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This prgogram is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation version 3 as published by
the Free Software Foundation. You may not use, modify or distribute
this program under any other version of the GNU Affero General Public
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scripting.npc;

import client.Equip;
import client.IItem;
import client.ISkill;
import client.ItemFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Config.Table.ExpTable;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleInventoryType;
import client.MapleJob;
import client.MapleOccupations;
import client.MaplePet;
import client.MapleQuestStatus;
import client.MapleSkinColor;
import client.MapleStat;
import client.SkillFactory;
import tools.Randomizer;
import java.io.File;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import handling.channel.ChannelServer;
import handling.mundo.MapleParty;
import handling.mundo.MaplePartyCharacter;
import tools.DatabaseConnection;
import handling.mundo.guild.MapleAlliance;
import handling.mundo.guild.MapleGuild;
import handling.mundo.remote.WorldChannelInterface;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import scripting.AbstractPlayerInteraction;
import scripting.event.EventManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleSquad;
import server.MapleSquadType;
import server.MapleStatEffect;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;

/**
 *
 * @author Matze
 */
public class NPCConversationManager extends AbstractPlayerInteraction {

    private int npc;
    private String getText;

    public NPCConversationManager(MapleClient c, int npc) {
        super(c);
        this.npc = npc;
    }

    public int getNpc() {
        return npc;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(this);
    }

    public void sendNext(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01"));
    }

    public void sendPrev(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00"));
    }

    public void sendNextPrev(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01"));
    }

    public void sendOk(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00"));
    }

    public void sendYesNo(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, ""));
    }

    public void sendAcceptDecline(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, ""));
    }

    public void sendSimple(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, ""));
    }

    public void teachSkill(int id, int level, int masterlevel) {
        getPlayer().changeSkillLevel(SkillFactory.getSkill(id), level, masterlevel);
    }

    public void maxaran() {
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21121000), getPlayer().getSkillLevel(21121000), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120001), getPlayer().getSkillLevel(21120001), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120003), getPlayer().getSkillLevel(21120003), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120004), getPlayer().getSkillLevel(21120004), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120006), getPlayer().getSkillLevel(21120006), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120007), getPlayer().getSkillLevel(21120007), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120008), getPlayer().getSkillLevel(21120008), 5);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120009), getPlayer().getSkillLevel(21120009), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120010), getPlayer().getSkillLevel(21120010), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120005), getPlayer().getSkillLevel(21120005), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21120002), getPlayer().getSkillLevel(21120002), 30);
        // End of 4th Job + Start of 3rd
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21111001), getPlayer().getSkillLevel(21111001), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21111004), getPlayer().getSkillLevel(21111004), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21111005), getPlayer().getSkillLevel(21111005), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21111006), getPlayer().getSkillLevel(21111006), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21111003), getPlayer().getSkillLevel(21111003), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21111001), getPlayer().getSkillLevel(21111001), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21111002), getPlayer().getSkillLevel(21111002), 20);
        // End of 3rd job + Start of 2nd
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100000), getPlayer().getSkillLevel(21100000), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100002), getPlayer().getSkillLevel(21100002), 30);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100003), getPlayer().getSkillLevel(21100003), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100001), getPlayer().getSkillLevel(21100001), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100004), getPlayer().getSkillLevel(21100004), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100005), getPlayer().getSkillLevel(21100005), 20);
        // End of 2nd job + Start of 1st
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21000000), getPlayer().getSkillLevel(21000000), 10);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100002), getPlayer().getSkillLevel(21100002), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100003), getPlayer().getSkillLevel(21100003), 20);
        getPlayer().changeSkillLevel(SkillFactory.getSkill(21100001), getPlayer().getSkillLevel(21100001), 15);
        getPlayer().dropMessage("Your Aran Mastery Has Been Maxed, Enjoy.");
    }

    public void sendStyle(String text, int styles[]) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalkStyle(npc, text, styles));
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
    }

    public void sendGetText(String text) {
        getClient().getSession().write(MaplePacketCreator.getNPCTalkText(npc, text));
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return this.getText;
    }

    public int getJobId() {
        return getPlayer().getJob().getId();
    }

    public void startQuest(int id) {
        try {
            MapleQuest.getInstance(id).forceStart(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public void completeQuest(int id) {
        try {
            MapleQuest.getInstance(id).forceComplete(getPlayer(), npc);
        } catch (NullPointerException ex) {
        }
    }

    public int getMeso() {
        return getPlayer().getMeso();
    }

    public void gainMeso(int gain) {
        getPlayer().gainMeso(gain, true, false, true);
    }

    public void gainExp(int gain) {
        getPlayer().gainExp(gain, true, true);
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public EventManager getEventManager(String event) {
        return getClient().getChannelServer().getEventSM().getEventManager(event);
    }

    public void changeOccupationById(int occ) {
        getPlayer().changeOccupation(MapleOccupations.getById(occ));
    }

    public boolean HasOccupation() {
        return (getPlayer().getOccupation().getId() % 100 == 0);
    }

    public boolean HasOccupationV2() {
        int job = getPlayer().getOccupation().getId();
        switch (job) {
            case 100:
                return true;
            case 200:
                return true;
            case 300:
                return true;
            case 400:
                return true;
            case 500:
                return true;
            case 600:
                return true;
            case 700:
                return true;
            default:
                return false;
        }
    }

    public void showEffect(String effect) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(effect, 3));
    }

    public void playSound(String sound) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(sound, 4));
    }

    public void setHair(int hair) {
        getPlayer().setHair(hair);
        getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        getPlayer().equipChanged();
    }

    public void setFace(int face) {
        getPlayer().setFace(face);
        getPlayer().updateSingleStat(MapleStat.FACE, face);
        getPlayer().equipChanged();
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor(MapleSkinColor.getById(color));
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public void setLevelz(int level) {
        getPlayer().setLevel(10);
    }

    public void setLevelx(int level) {
        getPlayer().setLevel(8);
    }

    public int itemQuantity(int itemid) {
        return getPlayer().getInventory(MapleItemInformationProvider.getInstance().getInventoryType(itemid)).countById(itemid);
    }

    public void displayGuildRanks() {
        MapleGuild.displayGuildRanks(getClient(), npc);
    }

    public void environmentChange(String env, int mode) {
        getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(env, mode));
    }

    public void gainCloseness(int closeness) {
        for (MaplePet pet : getPlayer().getPets()) {
            if (pet.getCloseness() > 30000) {
                pet.setCloseness(30000);
                return;
            }
            pet.gainCloseness(closeness);
            while (pet.getCloseness() > ExpTable.getClosenessNeededForLevel(pet.getLevel())) {
                pet.setLevel(pet.getLevel() + 1);
                getClient().getSession().write(MaplePacketCreator.showOwnPetLevelUp(getPlayer().getPetIndex(pet)));
            }
            getPlayer().getClient().getSession().write(MaplePacketCreator.updatePet(pet));
        }
    }

    public String getName() {
        return getPlayer().getName();
    }

    public int getGender() {
        return getPlayer().getGender();
    }

    public int getHiredMerchantMesos(boolean zero) {
        int mesos = 0;
        PreparedStatement ps = null;
        Connection con = DatabaseConnection.getConnection();
        try {
            ps = con.prepareStatement("SELECT MerchantMesos FROM characters WHERE id = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                mesos = rs.getInt("MerchantMesos");
            }
            rs.close();
            ps.close();
            if (zero) {
                ps = con.prepareStatement("UPDATE characters SET MerchantMesos = 0 WHERE id = ?");
                ps.setInt(1, getPlayer().getId());
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException e) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
        return mesos;
    }

    public void setHiredMerchantMesos(int mesos) {
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE characters SET MerchantMesos = ? WHERE id = ?");
            ps.setInt(1, mesos);
            ps.setInt(2, getPlayer().getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
        }
    }

    public void changeJobById(int a) {
        getPlayer().changeJob(MapleJob.getById(a));
    }

    public void addRandomItem(int id) {
        MapleItemInformationProvider i = MapleItemInformationProvider.getInstance();
        MapleInventoryManipulator.addFromDrop(getClient(), i.randomizeStats((Equip) i.getEquipById(id)), true);
    }

    public MapleJob getJobName(int id) {
        return MapleJob.getById(id);
    }

    public boolean isQuestCompleted(int quest) {
        try {
            return getQuestStatus(quest) == MapleQuestStatus.Status.COMPLETED;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean isQuestStarted(int quest) {
        try {
            return getQuestStatus(quest) == MapleQuestStatus.Status.STARTED;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void sendCygnusCharCreate() {
        c.getSession().write(MaplePacketCreator.sendCygnusCreateChar());
    }

    public MapleStatEffect getItemEffect(int itemId) {
        return MapleItemInformationProvider.getInstance().getItemEffect(itemId);
    }

    public void resetStats() {
        int totAp = getPlayer().getStr() + getPlayer().getDex() + getPlayer().getLuk() + getPlayer().getInt() + getPlayer().getRemainingAp();
        getPlayer().setStr(4);
        getPlayer().setDex(4);
        getPlayer().setLuk(4);
        getPlayer().setInt(4);
        getPlayer().setRemainingAp(totAp - 16);
        getPlayer().updateSingleStat(MapleStat.STR, 4);
        getPlayer().updateSingleStat(MapleStat.DEX, 4);
        getPlayer().updateSingleStat(MapleStat.LUK, 4);
        getPlayer().updateSingleStat(MapleStat.INT, 4);
        getPlayer().updateSingleStat(MapleStat.AVAILABLEAP, totAp);
    }

    public void maxMastery() {
        for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
            try {
                ISkill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                if ((skill.getId() / 10000 % 10 == 2 || (getClient().getPlayer().isCygnus() && skill.getId() / 10000 % 10 == 1)) && getPlayer().getSkillLevel(skill) < 1) {
                    getPlayer().changeSkillLevel(skill, 0, skill.getMaxLevel());
                }
            } catch (NumberFormatException nfe) {
                break;
            } catch (NullPointerException npe) {
                continue;
            }
        }
    }

    public void processGachapon(int[] id, boolean remote) {
        int[] gacMap = {100000000, 101000000, 102000000, 103000000, 105040300, 800000000, 809000101, 809000201, 600000000, 120000000};
        int itemid = id[Randomizer.getInstance().nextInt(id.length)];
        addRandomItem(itemid);
        if (!remote) {
            gainItem(5220000, (short) -1);
        }
        sendNext("You have obtained a #b#t" + itemid + "##k.");
        //getClient().getChannelServer().broadcastPacket(MaplePacketCreator.gachaponMessage(getPlayer().getInventory(MapleInventoryType.getByType((byte) (itemid / 1000000))).findById(itemid), c.getPlayer().getMapName(gacMap[(getNpc() != 9100117 && getNpc() != 9100109) ? (getNpc() - 9100100) : getNpc() == 9100109 ? 8 : 9]), getPlayer()));
    }

    public void disbandAlliance(MapleClient c, int allianceId) {
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM `alliance` WHERE id = ?");
            ps.setInt(1, allianceId);
            ps.executeUpdate();
            ps.close();
            c.getChannelServer().getWorldInterface().allianceMessage(c.getPlayer().getGuild().getAllianceId(), MaplePacketCreator.disbandAlliance(allianceId), -1, -1);
            c.getChannelServer().getWorldInterface().disbandAlliance(allianceId);
        } catch (RemoteException r) {
            c.getChannelServer().reconnectWorld();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public boolean canBeUsedAllianceName(String name) {
        if (name.contains(" ") || name.length() > 12) {
            return false;
        }
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT name FROM alliance WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ps.close();
                rs.close();
                return false;
            }
            ps.close();
            rs.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static MapleAlliance createAlliance(MapleCharacter chr1, MapleCharacter chr2, String name) {
        int id = 0;
        int guild1 = chr1.getGuildId();
        int guild2 = chr2.getGuildId();
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `alliance` (`name`, `guild1`, `guild2`) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, guild1);
            ps.setInt(3, guild2);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        MapleAlliance alliance = new MapleAlliance(name, id, guild1, guild2);
        try {
            WorldChannelInterface wci = chr1.getClient().getChannelServer().getWorldInterface();
            wci.setGuildAllianceId(guild1, id);
            wci.setGuildAllianceId(guild2, id);
            chr1.setAllianceRank(1);
            chr1.saveGuildStatus();
            chr2.setAllianceRank(2);
            chr2.saveGuildStatus();
            wci.addAlliance(id, alliance);
            wci.allianceMessage(id, MaplePacketCreator.makeNewAlliance(alliance, chr1.getClient()), -1, -1);
        } catch (RemoteException e) {
            chr1.getClient().getChannelServer().reconnectWorld();
            chr2.getClient().getChannelServer().reconnectWorld();
            return null;
        }
        return alliance;
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<MapleCharacter>();
        for (ChannelServer channel : ChannelServer.getAllInstances()) {
            for (MapleCharacter chr : channel.getPartyMembers(getPlayer().getParty(), -1)) {
                if (chr != null) {
                    chars.add(chr);
                }
            }
        }
        return chars;
    }

    public void warpParty(int id) {
        for (MapleCharacter mc : getPartyMembers()) {
            if (id == 925020100) {
                mc.setDojoParty(true);
            }
            mc.changeMap(getWarpMap(id));
        }
    }

    public void removeHiredMerchantItem(int id) {
        try {
            List<Pair<IItem, MapleInventoryType>> workingList = getHiredMerchantItems();
            for (Pair<IItem, MapleInventoryType> p : workingList) {
                if (p.getLeft().getDBID() == id) {
                    workingList.remove(p);
                    break;
                }
            }
            ItemFactory.MERCHANT.saveItems(workingList, c.getPlayer().getId());
            c.getPlayer().saveToDB(true);
        } catch (Exception e) {
        }
    }

    public List<Pair<IItem, MapleInventoryType>> getHiredMerchantItems() {
        try {
            return ItemFactory.MERCHANT.loadItems(c.getPlayer().getId(), false);
        } catch (Exception e) {
            System.out.println("Error loading merchant items:");
            e.printStackTrace();
        }
        return null;
    }

    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharacters()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public MapleCharacter getSquadMember(MapleSquadType type, int index) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        MapleCharacter ret = null;
        if (squad != null) {
            ret = squad.getMembers().get(index);
        }
        return ret;
    }

    public void addSquadMember(MapleSquadType type) {
        MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.addMember(getPlayer());
        }
    }

    public int getAverageLevel(MapleParty mp) {
        int total = 0;
        for (MaplePartyCharacter mpc : mp.getMembers()) {
            total += mpc.getLevel();
        }
        return total / mp.getMembers().size();
    }
}
