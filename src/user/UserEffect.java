/* 
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor. 
 */ 

package user; 

/** 
 * UserEffect 
 *  
 * @author Eric 
 */ 
public enum UserEffect { 
    LevelUp(0x0), 
    SkillUse(0x01), 
    SkillAffected(0x02), 
    Quest(0x03), 
    Pet(0x04), 
    SkillSpecial(0x05), 
    ProtectOnDieItemUse(0x06), 
    PlayPortalSE(0x07), 
    JobChanged(0x08), 
    QuestComplete(0x09), 
    IncDecHPEffect(0x0A), 
    BuffItemEffect(0x0B), 
    SquibEffect(0x0C), 
    MonsterBookCardGet(0x0D), 
    LotteryUse(0x0E), 
    ItemLevelUp(0x0F), 
    ItemMaker(0x10), 
    ExpItemConsumed(0x11), 
    ReservedEffect(0x12),  
    Buff(0x13), // 0x13 is nowhere to be found in the v83 client o_O 
    ConsumeEffect(0x14), 
    UpgradeTombItemUse(0x15), 
    BattlefieldItemUse(0x16), 
    AvatarOriented(0x17), 
    IncubatorUse(0x18), 
    PlaySoundWithMuteBGM(0x19), 
    SoulStoneUse(0x1A), 
    IncDecHPEffect_EX(0x1B), // the rest of these are not in v83. rip evan hp effect :( 
    DeliveryQuestItemUse(0x1C), 
    RepeatEffectRemove(0x1D), 
    EvolRing(0x1E); 
    private final int effect; 
     
    private UserEffect(int effect) { 
        this.effect = effect; 
    } 
     
    public int getEffect() { 
        return effect; 
    } 
}  