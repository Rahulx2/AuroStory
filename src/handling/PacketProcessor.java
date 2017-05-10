/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
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
package handling;

import handling.login.handler.PickCharHandler;
import handling.login.handler.RegisterPinHandler;
import handling.login.handler.LoginPasswordHandler;
import handling.login.handler.CharlistRequestHandler;
import handling.login.handler.GuestLoginHandler;
import handling.login.handler.CreateCharHandler;
import handling.login.handler.DeleteCharHandler;
import handling.login.handler.CheckCharNameHandler;
import handling.login.handler.AfterLoginHandler;
import handling.login.handler.ViewCharHandler;
import handling.login.handler.ClientStartHandler;
import handling.login.handler.RelogRequestHandler;
import handling.login.handler.CharSelectedHandler;
import handling.login.handler.ServerlistRequestHandler;
import handling.login.handler.ServerStatusRequestHandler;
import handling.channel.handler.RemoteGachaponHandler;
import handling.channel.handler.SkillEffectHandler;
import handling.channel.handler.DenyPartyRequestHandler;
import handling.channel.handler.DistributeSPHandler;
import handling.channel.handler.ChangeMapHandler;
import handling.channel.handler.PartyOperationHandler;
import handling.channel.handler.AutoAssignHandler;
import handling.channel.handler.NoteActionHandler;
import handling.channel.handler.SpecialMoveHandler;
import handling.channel.handler.CharInfoRequestHandler;
import handling.channel.handler.FishingHandler;
import handling.channel.handler.GiveFameHandler;
import handling.channel.handler.PetFoodHandler;
import handling.channel.handler.NPCMoreTalkHandler;
import handling.channel.handler.ChangeChannelHandler;
import handling.channel.handler.PetLootHandler;
import handling.channel.handler.UseItemHandler;
import handling.channel.handler.MobDamageMobFriendlyHandler;
import handling.channel.handler.AcceptFamilyHandler;
import handling.channel.handler.SummonDamageHandler;
import handling.channel.handler.InnerPortalHandler;
import handling.channel.handler.HealOvertimeHandler;
import handling.channel.handler.ItemIdSortHandler;
import handling.channel.handler.CancelChairHandler;
import handling.channel.handler.MoveLifeHandler;
import handling.channel.handler.QuestActionHandler;
import handling.channel.handler.BBSOperationHandler;
import handling.channel.handler.AllianceOperationHandler;
import handling.channel.handler.PartyChatHandler;
import handling.channel.handler.HiredMerchantRequest;
import handling.channel.handler.ItemPickupHandler;
import handling.channel.handler.RangedAttackHandler;
import handling.channel.handler.PartySearchStartHandler;
import handling.channel.handler.MovePetHandler;
import handling.channel.handler.MessengerHandler;
import handling.channel.handler.BuyCSItemHandler;
import handling.channel.handler.UseItemEffectHandler;
import handling.channel.handler.DueyHandler;
import handling.channel.handler.CloseChalkboardHandler;
import handling.channel.handler.PlayerLoggedinHandler;
import handling.channel.handler.CancelBuffHandler;
import handling.channel.handler.MoveSummonHandler;
import handling.channel.handler.PetCommandHandler;
import handling.channel.handler.SpawnPetHandler;
import handling.channel.handler.UseHammerHandler;
import handling.channel.handler.BuddylistModifyHandler;
import handling.channel.handler.AdminCommandHandler;
import handling.channel.handler.CloseRangeDamageHandler;
import handling.channel.handler.BeholderHandler;
import handling.channel.handler.GuildOperationHandler;
import handling.channel.handler.NPCTalkHandler;
import handling.channel.handler.SkillBookHandler;
import handling.channel.handler.TouchingCashShopHandler;
import handling.channel.handler.GeneralchatHandler;
import handling.channel.handler.PlayerInteractionHandler;
import handling.channel.handler.ChangeMapSpecialHandler;
import handling.channel.handler.UseCashItemHandler;
import handling.channel.handler.MonsterBookCoverHandler;
import handling.channel.handler.PartySearchRegisterHandler;
import handling.channel.handler.DenyGuildRequestHandler;
import handling.channel.handler.StorageHandler;
import handling.channel.handler.PlayerUpdateHandler;
import handling.channel.handler.EnterCashShopHandler;
import handling.channel.handler.FaceExpressionHandler;
import handling.channel.handler.PetChatHandler;
import handling.channel.handler.UseDeathItemHandler;
import handling.channel.handler.RingActionHandler;
import handling.channel.handler.SpouseChatHandler;
import handling.channel.handler.MakerSkillHandler;
import handling.channel.handler.WhisperHandler;
import handling.channel.handler.AutoAggroHandler;
import handling.channel.handler.UseMountFoodHandler;
import handling.channel.handler.ReportHandler;
import handling.channel.handler.DoorHandler;
import handling.channel.handler.UseSolomonHandler;
import handling.channel.handler.UseCatchItemHandler;
import handling.channel.handler.FamilyUseHandler;
import handling.channel.handler.ScriptedItemHandler;
import handling.channel.handler.ClientErrorLogHandler;
import handling.channel.handler.CancelDebuffHandler;
import handling.channel.handler.NPCAnimation;
import handling.channel.handler.MobDamageMobHandler;
import handling.channel.handler.UseMapleLifeHandler;
import handling.channel.handler.AdminLogHandler;
import handling.channel.handler.PetExcludeItemsHandler;
import handling.channel.handler.PetAutoPotHandler;
import handling.channel.handler.ReactorHitHandler;
import handling.channel.handler.UseChairHandler;
import handling.channel.handler.EnterMTSHandler;
import handling.channel.handler.NPCShopHandler;
import handling.channel.handler.MonsterBombHandler;
import handling.channel.handler.EnergyOrbDamageHandler;
import handling.channel.handler.KeymapChangeHandler;
import handling.channel.handler.MesoDropHandler;
import handling.channel.handler.UseSummonBag;
import handling.channel.handler.ScrollHandler;
import handling.channel.handler.SkillMacroHandler;
import handling.channel.handler.MovePlayerHandler;
import handling.channel.handler.TrockAddMapHandler;
import handling.channel.handler.DamageSummonHandler;
import handling.channel.handler.CancelItemEffectHandler;
import handling.channel.handler.CoconutHandler;
import handling.channel.handler.MTSHandler;
import handling.channel.handler.TouchReactorHandler;
import handling.channel.handler.TakeDamageHandler;
import handling.channel.handler.ItemMoveHandler;
import handling.channel.handler.CouponCodeHandler;
import handling.channel.handler.FamilyAddHandler;
import handling.channel.handler.DistributeAPHandler;
import handling.channel.handler.ItemSortHandler;
import handling.channel.handler.LeftKnockbackHandler;
import handling.channel.handler.MagicDamageHandler;
import handling.channel.handler.SnowballHandler;
import handling.handler.KeepAliveHandler;
import handling.handler.LoginRequiringNoOpHandler;

public final class PacketProcessor {

    public enum Mode {

        LOGINSERVER, CHANNELSERVER
    };
    private static PacketProcessor instance;
    private MaplePacketHandler[] handlers;

    private PacketProcessor() {
        int maxRecvOp = 0;
        for (RecvPacketOpcode op : RecvPacketOpcode.values()) {
            if (op.getValue() > maxRecvOp) {
                maxRecvOp = op.getValue();
            }
        }
        handlers = new MaplePacketHandler[maxRecvOp + 1];
    }

    MaplePacketHandler getHandler(short packetId) {
        if (packetId > handlers.length) {
            return null;
        }
        MaplePacketHandler handler = handlers[packetId];
        if (handler != null) {
            return handler;
        }
        return null;
    }

    public void registerHandler(RecvPacketOpcode code, MaplePacketHandler handler) {
        try {
            handlers[code.getValue()] = handler;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error registering handler - " + code.name());
        }
    }

    public synchronized static PacketProcessor getProcessor(Mode mode) {
        if (instance == null) {
            instance = new PacketProcessor();
            instance.reset(mode);
        }
        return instance;
    }

    public void reset(Mode mode) {
        handlers = new MaplePacketHandler[handlers.length];
        registerHandler(RecvPacketOpcode.PONG, new KeepAliveHandler());
        if (mode == Mode.LOGINSERVER) {
            registerHandler(RecvPacketOpcode.AFTER_LOGIN, new AfterLoginHandler());
            registerHandler(RecvPacketOpcode.SERVERLIST_REREQUEST, new ServerlistRequestHandler());
            registerHandler(RecvPacketOpcode.CHARLIST_REQUEST, new CharlistRequestHandler());
            registerHandler(RecvPacketOpcode.CHAR_SELECT, new CharSelectedHandler());
            registerHandler(RecvPacketOpcode.LOGIN_PASSWORD, new LoginPasswordHandler());
            registerHandler(RecvPacketOpcode.RELOG, new RelogRequestHandler());
            registerHandler(RecvPacketOpcode.SERVERLIST_REQUEST, new ServerlistRequestHandler());
            registerHandler(RecvPacketOpcode.SERVERSTATUS_REQUEST, new ServerStatusRequestHandler());
            registerHandler(RecvPacketOpcode.CHECK_CHAR_NAME, new CheckCharNameHandler());
            registerHandler(RecvPacketOpcode.CREATE_CHAR, new CreateCharHandler());
            registerHandler(RecvPacketOpcode.DELETE_CHAR, new DeleteCharHandler());
            registerHandler(RecvPacketOpcode.VIEW_ALL_CHAR, new ViewCharHandler());
            registerHandler(RecvPacketOpcode.PICK_ALL_CHAR, new PickCharHandler());
            registerHandler(RecvPacketOpcode.COCONUT, new CoconutHandler());  
            registerHandler(RecvPacketOpcode.LEFT_KNOCKBACK, new LeftKnockbackHandler());
            registerHandler(RecvPacketOpcode.SNOWBALL, new SnowballHandler());
            registerHandler(RecvPacketOpcode.REGISTER_PIN, new RegisterPinHandler());
            registerHandler(RecvPacketOpcode.GUEST_LOGIN, new GuestLoginHandler());
            registerHandler(RecvPacketOpcode.CLIENT_START, new ClientStartHandler());
        } else if (mode == Mode.CHANNELSERVER) {
            registerHandler(RecvPacketOpcode.ERROR, new ClientErrorLogHandler());
            registerHandler(RecvPacketOpcode.CHANGE_CHANNEL, new ChangeChannelHandler());
            registerHandler(RecvPacketOpcode.STRANGE_DATA, LoginRequiringNoOpHandler.getInstance());
            registerHandler(RecvPacketOpcode.GENERAL_CHAT, new GeneralchatHandler());
            registerHandler(RecvPacketOpcode.WHISPER, new WhisperHandler());
            registerHandler(RecvPacketOpcode.NPC_TALK, new NPCTalkHandler());
            registerHandler(RecvPacketOpcode.NPC_TALK_MORE, new NPCMoreTalkHandler());
            registerHandler(RecvPacketOpcode.QUEST_ACTION, new QuestActionHandler());
            registerHandler(RecvPacketOpcode.NPC_SHOP, new NPCShopHandler());
            registerHandler(RecvPacketOpcode.ITEM_SORT, new ItemSortHandler());
            registerHandler(RecvPacketOpcode.ITEM_MOVE, new ItemMoveHandler());
            registerHandler(RecvPacketOpcode.MESO_DROP, new MesoDropHandler());
            registerHandler(RecvPacketOpcode.PLAYER_LOGGEDIN, new PlayerLoggedinHandler());
            registerHandler(RecvPacketOpcode.CHANGE_MAP, new ChangeMapHandler());
            registerHandler(RecvPacketOpcode.MOVE_LIFE, new MoveLifeHandler());
            registerHandler(RecvPacketOpcode.CLOSE_RANGE_ATTACK, new CloseRangeDamageHandler());
            registerHandler(RecvPacketOpcode.RANGED_ATTACK, new RangedAttackHandler());
            registerHandler(RecvPacketOpcode.MAGIC_ATTACK, new MagicDamageHandler());
            registerHandler(RecvPacketOpcode.TAKE_DAMAGE, new TakeDamageHandler());
            registerHandler(RecvPacketOpcode.MOVE_PLAYER, new MovePlayerHandler());
            registerHandler(RecvPacketOpcode.USE_CASH_ITEM, new UseCashItemHandler());
            registerHandler(RecvPacketOpcode.USE_ITEM, new UseItemHandler());
            registerHandler(RecvPacketOpcode.USE_RETURN_SCROLL, new UseItemHandler());
            registerHandler(RecvPacketOpcode.USE_UPGRADE_SCROLL, new ScrollHandler());
            registerHandler(RecvPacketOpcode.USE_SUMMON_BAG, new UseSummonBag());
            registerHandler(RecvPacketOpcode.FACE_EXPRESSION, new FaceExpressionHandler());
            registerHandler(RecvPacketOpcode.HEAL_OVER_TIME, new HealOvertimeHandler());
            registerHandler(RecvPacketOpcode.ITEM_PICKUP, new ItemPickupHandler());
            registerHandler(RecvPacketOpcode.CHAR_INFO_REQUEST, new CharInfoRequestHandler());
            registerHandler(RecvPacketOpcode.SPECIAL_MOVE, new SpecialMoveHandler());
            registerHandler(RecvPacketOpcode.USE_INNER_PORTAL, new InnerPortalHandler());
            registerHandler(RecvPacketOpcode.CANCEL_BUFF, new CancelBuffHandler());
            registerHandler(RecvPacketOpcode.CANCEL_ITEM_EFFECT, new CancelItemEffectHandler());
            registerHandler(RecvPacketOpcode.PLAYER_INTERACTION, new PlayerInteractionHandler());
            registerHandler(RecvPacketOpcode.DISTRIBUTE_AP, new DistributeAPHandler());
            registerHandler(RecvPacketOpcode.DISTRIBUTE_SP, new DistributeSPHandler());
            registerHandler(RecvPacketOpcode.CHANGE_KEYMAP, new KeymapChangeHandler());
            registerHandler(RecvPacketOpcode.CHANGE_MAP_SPECIAL, new ChangeMapSpecialHandler());
            registerHandler(RecvPacketOpcode.STORAGE, new StorageHandler());
            registerHandler(RecvPacketOpcode.GIVE_FAME, new GiveFameHandler());
            registerHandler(RecvPacketOpcode.PARTY_OPERATION, new PartyOperationHandler());
            registerHandler(RecvPacketOpcode.DENY_PARTY_REQUEST, new DenyPartyRequestHandler());
            registerHandler(RecvPacketOpcode.PARTYCHAT, new PartyChatHandler());
            registerHandler(RecvPacketOpcode.USE_DOOR, new DoorHandler());
            registerHandler(RecvPacketOpcode.ENTER_MTS, new EnterMTSHandler());
            registerHandler(RecvPacketOpcode.ENTER_CASH_SHOP, new EnterCashShopHandler());
            registerHandler(RecvPacketOpcode.DAMAGE_SUMMON, new DamageSummonHandler());
            registerHandler(RecvPacketOpcode.MOVE_SUMMON, new MoveSummonHandler());
            registerHandler(RecvPacketOpcode.SUMMON_ATTACK, new SummonDamageHandler());
            registerHandler(RecvPacketOpcode.BUDDYLIST_MODIFY, new BuddylistModifyHandler());
            registerHandler(RecvPacketOpcode.USE_ITEMEFFECT, new UseItemEffectHandler());
            registerHandler(RecvPacketOpcode.USE_CHAIR, new UseChairHandler());
            registerHandler(RecvPacketOpcode.CANCEL_CHAIR, new CancelChairHandler());
            registerHandler(RecvPacketOpcode.DAMAGE_REACTOR, new ReactorHitHandler());
            registerHandler(RecvPacketOpcode.GUILD_OPERATION, new GuildOperationHandler());
            registerHandler(RecvPacketOpcode.DENY_GUILD_REQUEST, new DenyGuildRequestHandler());
            registerHandler(RecvPacketOpcode.BBS_OPERATION, new BBSOperationHandler());
            registerHandler(RecvPacketOpcode.SKILL_EFFECT, new SkillEffectHandler());
            registerHandler(RecvPacketOpcode.MESSENGER, new MessengerHandler());
            registerHandler(RecvPacketOpcode.NPC_ACTION, new NPCAnimation());
            registerHandler(RecvPacketOpcode.TOUCHING_CS, new TouchingCashShopHandler());
            registerHandler(RecvPacketOpcode.BUY_CS_ITEM, new BuyCSItemHandler());
            registerHandler(RecvPacketOpcode.COUPON_CODE, new CouponCodeHandler());
            registerHandler(RecvPacketOpcode.SPAWN_PET, new SpawnPetHandler());
            registerHandler(RecvPacketOpcode.MOVE_PET, new MovePetHandler());
            registerHandler(RecvPacketOpcode.PET_CHAT, new PetChatHandler());
            registerHandler(RecvPacketOpcode.PET_COMMAND, new PetCommandHandler());
            registerHandler(RecvPacketOpcode.PET_FOOD, new PetFoodHandler());
            registerHandler(RecvPacketOpcode.PET_LOOT, new PetLootHandler());
            registerHandler(RecvPacketOpcode.AUTO_AGGRO, new AutoAggroHandler());
            registerHandler(RecvPacketOpcode.MONSTER_BOMB, new MonsterBombHandler());
            registerHandler(RecvPacketOpcode.CANCEL_DEBUFF, new CancelDebuffHandler());
            registerHandler(RecvPacketOpcode.USE_SKILL_BOOK, new SkillBookHandler());
            registerHandler(RecvPacketOpcode.SKILL_MACRO, new SkillMacroHandler());
            registerHandler(RecvPacketOpcode.NOTE_ACTION, new NoteActionHandler());
            registerHandler(RecvPacketOpcode.CLOSE_CHALKBOARD, new CloseChalkboardHandler());
            registerHandler(RecvPacketOpcode.USE_MOUNT_FOOD, new UseMountFoodHandler());
            registerHandler(RecvPacketOpcode.MTS_OP, new MTSHandler());
            registerHandler(RecvPacketOpcode.RING_ACTION, new RingActionHandler());
            registerHandler(RecvPacketOpcode.SPOUSE_CHAT, new SpouseChatHandler());
            registerHandler(RecvPacketOpcode.PET_AUTO_POT, new PetAutoPotHandler());
            registerHandler(RecvPacketOpcode.PET_EXCLUDE_ITEMS, new PetExcludeItemsHandler());
            registerHandler(RecvPacketOpcode.ENERGY_ORB_ATTACK, new EnergyOrbDamageHandler());
            registerHandler(RecvPacketOpcode.TROCK_ADD_MAP, new TrockAddMapHandler());
            registerHandler(RecvPacketOpcode.HIRED_MERCHANT_REQUEST, new HiredMerchantRequest());
            registerHandler(RecvPacketOpcode.MOB_DAMAGE_MOB, new MobDamageMobHandler());
            registerHandler(RecvPacketOpcode.REPORT, new ReportHandler());
            registerHandler(RecvPacketOpcode.MONSTER_BOOK_COVER, new MonsterBookCoverHandler());
            registerHandler(RecvPacketOpcode.AUTO_DISTRIBUTE_AP, new AutoAssignHandler());
            registerHandler(RecvPacketOpcode.MAKER_SKILL, new MakerSkillHandler());
            registerHandler(RecvPacketOpcode.ADD_FAMILY, new FamilyAddHandler());
            registerHandler(RecvPacketOpcode.USE_FAMILY, new FamilyUseHandler());
            registerHandler(RecvPacketOpcode.USE_HAMMER, new UseHammerHandler());
            registerHandler(RecvPacketOpcode.SCRIPTED_ITEM, new ScriptedItemHandler());
            registerHandler(RecvPacketOpcode.TOUCHING_REACTOR, new TouchReactorHandler());
            registerHandler(RecvPacketOpcode.BEHOLDER, new BeholderHandler());
            registerHandler(RecvPacketOpcode.ADMIN_COMMAND, new AdminCommandHandler());
            registerHandler(RecvPacketOpcode.ADMIN_LOG, new AdminLogHandler());
            registerHandler(RecvPacketOpcode.ALLIANCE_OPERATION, new AllianceOperationHandler());
            registerHandler(RecvPacketOpcode.USE_SOLOMON_ITEM, new UseSolomonHandler());
            registerHandler(RecvPacketOpcode.USE_FISHING_ITEM, new FishingHandler());
            registerHandler(RecvPacketOpcode.USE_REMOTE, new RemoteGachaponHandler());
            registerHandler(RecvPacketOpcode.ACCEPT_FAMILY, new AcceptFamilyHandler());
            registerHandler(RecvPacketOpcode.DUEY_ACTION, new DueyHandler());
            registerHandler(RecvPacketOpcode.USE_DEATHITEM, new UseDeathItemHandler());
            registerHandler(RecvPacketOpcode.PLAYER_UPDATE, new PlayerUpdateHandler());
            registerHandler(RecvPacketOpcode.USE_MAPLELIFE, new UseMapleLifeHandler());
            registerHandler(RecvPacketOpcode.USE_CATCH_ITEM, new UseCatchItemHandler());
            registerHandler(RecvPacketOpcode.MOB_DAMAGE_MOB_FRIENDLY, new MobDamageMobFriendlyHandler());
            registerHandler(RecvPacketOpcode.PARTY_SEARCH_REGISTER, new PartySearchRegisterHandler());
            registerHandler(RecvPacketOpcode.PARTY_SEARCH_START, new PartySearchStartHandler());
            registerHandler(RecvPacketOpcode.ITEM_SORT2, new ItemIdSortHandler());
            //    registerHandler(RecvPacketOpcode.POISON_BOMB, new PoisonBombHandler());
        }
    }
}
