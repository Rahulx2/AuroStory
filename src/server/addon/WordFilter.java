/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package server.addon;
import java.lang.StringBuilder;
import java.util.Locale;
import tools.MaplePacketCreator;
import client.MapleCharacter;

/**
 *
 * @author FateJiki
 */
public class WordFilter {
        private final static String[] blocked = { // all curse's right away from WZ! [Credits to Super_Sonic]
        //numbers
        "1esb1an","455h0Ie","455h0le","455m45ter","455much","455munch","4n4I5ex","4n4Isex","4n4l5ex","4n4lsex","4ssh0Ie","4ssh0le","4ssm4ster","4ssmuch","4ssmunch",        "5chI0ng","5chl0ng","5h1b4I","5h1b4l","5h1baI","5h1bal","5h1t","5h1z","5hibaI","5hibal","5hit","5hiz","5hlbal","5hlt","5hlz",
        // letter a
        "a55h0Ie","a55h0le","a55ma5ter","a55much","a55munch","anaI5ex","anaIsex","anal","anal5ex","analsex","assh0Ie","assh0le","asshole","asslover","ass-lover","assmaster","assmuch","assmunch",
        // letter b
        "b14tch","b1atch","b1tch","b1tch455","b1tcha55","b1tchass","b1y0tch","b1z4tch","b1zatch","b1zn4tch","b1znatch","b45t4rd","b4IIz","b4llz","b4st4rd","balls","ballz","bastard","beeeech","beeotch","beeyotch","beyotch","bI0wj0b","bI0wme","bi4tch","biaatch","biatch","biiiiitch","biiiitch","biiitch","biitch","biotch","bitch","bitch4ss","bitcha55","bitchass","bittch","biy0tch","biyaaatch","biyatch","biyotch","biz4tch","bizatch","bizn4tch","biznatch","bl0wj0b","bl0wme","bl4tch",        "blatch","bllltch","blotch","blowjob","blowme","bltch","bltch4ss","bltcha55","bltchass","bly0tch","blyotch","blz4tch","blzatch","blzn4tch","blznatch","buttmunch","bytch",
        // letter c
        "c8","ch00ch1e","ch00chie","ch00chle","ch1nk","chink","choochie","cI1t0r1s","cIit","cIit0ri5","cIit0ris","cl1t0r1s","clit","clit0ri5","clit0ris","clitoris","cllt","cllt0rl5","cllt0rls","cock","condom","cottonpick","cum","cunnt","cunt",
        // letter d
        "d0ggy5tyIe","d0ggy-5tyIe","d0ggy5tyle","d0ggy-5tyle","d0ggystyIe","d0ggystyle","d1ck","damn","deepthr04t","deepthroat","dick","dildo","dilhole","dlldo","doggystyle","doggy-style","dumbfuck","dyke",
        // letter e
        "eatme",
        // letter f
        "fag","fagg0t","faggot","fetish","fucker","fuck","fuc","fuker","fukkin","fuk","fuuk",
        // letter g
        "g00k","g4y","gaaay","gaay","gay","gizay","goddamn","goddmamn","gook",
        // letter h
        "havesex","homo","hong","hoochie","hooters",
        // letter i
        "Iesb0",
        // letter j
        "j4ck0ff","jackoff","jack-off","jap5","japs","jerkme","jerk-off","jiz","jizm",
        // letter k
        "kike",
        // letter l
        "lesb0","lesbian","lesbo","lezbo",
        // letter m
        "m1ss10nary","mastabate","mastarbate","masterbate","masturbate","missi0n4ry","missi0nary","missionary","mlssl0n4ry","mlssl0nary","mofucc","mothafuc","mutha","mytit",
        // letter n
        "n1gger","negro","niga","nigar","niger","nigga","niggar","nigger","nipple","nlgger","nutsack", "Nido", "Story",
        // letter o
        "orgasm","orgy","p0rn","pen15","penis","phuck","porn","porno","pr0n","pussie","pussy",
        // letter r
        "retard","rubmy",
        // letter s
        "schlong","sexfreak","sexmachine","sexual","sexwith","sh1t","shibal","shiiiiiit","shit","shiz","shlt","spank","sperm","spum","ssh1t","sshit","sshlt","suckme","suckmy",
        // letter t
        "titty","tltty","tw4t","twat",
        // letter v
        "v4g1n4","vagina",
        // letter w
        "w4nker","wackoff","wack-off","wanker","whatthefukk","whore",
        // letter y
        "y0urt1t","y0urtit","y0urtlt","yourtit",
        // others ;D
        "server",
        "this server sucks", "how to hack", "bitch", "nigger", "nig", "hoe", "ho3s", "hooe", ".org", "moograms", "moongra", "julien sucks", "quit this", "selling sources", "join my server",
        "coon","Poontang","Poon","Coon","Koon","Porchmonkey","Porch","Spic","Yellowboy","Slant","nazzi","Nazi","Adolph","fuck'","Gestapo","Fuhrer","F?rer","Himmler","Goebbles","Crip","Pedobear","Pedo","Slut","fuker","Skank","Skeezer","Whore","drivebitch","fuckgm","fuckugm","fukugm","fucugm","motherfuckin'","movebitch","asshat","hitler","splooge","spooge",
        "skeet","blow","asshole'","dick'","titties","assface","douche","doosh","dooshe","kraut","kyke","wop","fuckjoo","pollock","jigaboo","pecker","peckerwood","fck'","ragheaD","wetback","masturbate","fck","sht","rape","brogamecom","freewebscomnxking","gamekoocom","lolsalecom","maplestorygoldscom","mesorichcom","mmogresourcecom","mmovpcom","msvgoldscom","msgodmodecom","msgoldscom","myigskycom","nexonmesospiczocom",
        "power4gamecom","rpgbuckscom","togexcom","zedgamecom","maplegold","msgold","c0m","maplestorygoldscom","lolsalecom","mapleaidcom","power4gamecom","zedgamenet","thsalecom","msmesoseu","brosalecom","thvendcom","igscom","gamekoocom","gosalecom","gameimcom","sellmscom","mmogamesalecom","igamegardencom","swagvaultcom","thepowerlevelcom","gamekoo","gamek00","gamek0o","gameko0","power4game","p0wer4game","xxcity","mmogamesale","mm0gamesale","mmogamesaie","guygame","pkxman","igs",
        "lgs","oosale","00sale","o0sale","0osale","lolsale","l0lsale","ioisale","ioisaie","i0isale","i0isaie","zedgame","brosale","br0sale","msmesos","msmes0s","thepowerlevel","thep0werlevel","thepowerievei","thep0werievei","mapleaid","maplestorygolds","atmgame","yangyuqiang","ugamegold","ugamegoid","ugameg0ld","ugameg0id","swagvault","igamegarden","lgamegarden","sellms","gameim","thvend",
        "mysupersales","igecom","peons4hire","gmworker","xxc1ty","mmogamesa1e","mm0gamesa1e","1gs","oosa1e","00sa1e","o0sa1e","0osa1e","1o1sa1e","101sa1e","brosa1e","br0sa1e","thepower1eve1","thep0wer1eve1","map1ea1d","map1estorygo1ds","yangyuq1ang","ugamego1d","ugameg01d","swagvau1t","1gamegard","se11ms","game1m","mysupersa1es","1ge","peons4h1re","lgecom","1gecom1","ig3com","1g3com","nexon3xwus","n3xon3xwus","n3x0n3xwus","nex0n3xwus","prizerebelnettc",
        "prizerebe1nettc","prizerebeinettc","pr1zerebelnettc","pr1zerebe1nettc","pr1zerebeinettc","prlzerebelnettc","prlzerebeinettc","prlzerebe1nettc","priz3r3b3ln3ttc","priz3r3b3in3ttc","priz3r3b31n3ttc","pr1z3r3b3ln3ttc","pr1z3r3b3in3ttc","pr1z3r3b31n3ttc","prlz3r3b3ln3ttc","prlz3r3b3in3ttc","prlz3r3b31n3ttc","mesodealcom","mes0dealcom","mesodealco0m","m3sodealcom","friskgamecom","fr1skgamecom",
        "frlskgamecom","m3so","mes0","m3s0","n3xon","nex0n","n3x0n","gam3","n3t","gamecorn","garnecom","lolgame","1olgame","10lgame","1oigame","10igame","1o1game","101game","lo1game","l01game","loigame","l0igame","iolgame","i0lgame","io1game","i01game","ioigame","i0igame","g4me","g4m3","itemratecom","ratecom","r4tecom","rat3com","r4t3com","gamecom","salecom","s4lecom","gamekoo",
        "guygamecom","gamegoldcom","goldcom","38dugameus","garnecorn","xxclty","xxc1ty","Thsalecom","fvck","thefirststorycom","wow4scom","mapleftptk","mapieftptk","thsaiecom","fuuck","Shiit","saiecom","sa1ecom","5alecom","Garnekoocom","Garnekoo","Oosalecom","Oosaiecom","wow4snet","mmodocom","gm963com",
        "vi4scom","maplestoryio","maplestorysh","itemshopscom","itemshopsnet","game8thcom","mmobe","maplestorymesos","fastmesos","goldceo","mygamebuy","sale2k","mapleftp","sellaccount","ogpal","msogpal","gamecuu","thepowerlevel","thepowerIeveI","thepowerleveI","thepowerIevel", "geempl0x851"
        };
        public final static String[] blockedSites = {".com", ".net", "no-ip", ".org", ".biz", ".tk", ".com", ".org", ".biz", ".info", ".us", ".cc", ".bz", ".tv", ".vg", ".ws", ".co.uk", ".org.uk", ".me.uk", ".de", ".eu", ".be", ".cn", ".com.cn", ".net.cn", ".org.cn", ".tw", ".com.tw", ".org.tw", ".at", ".com.mx", ".co.nz", ".nz", ".net.nz", ".org.nz", ".gs", ".tc", ".ms"};

        public static String illegalArrayCheck(String text, MapleCharacter player){
            StringBuilder sb = new StringBuilder(text);
            String subString = text.toLowerCase();
            if(!containsWebsite(text, player)){
            for(int i = 0; i < blocked.length; i++){
                if(subString.contains(blocked[i].toLowerCase())){
                    player.dropMessage(5, "Our swear filter picked up this word : '" + blocked[i].toLowerCase() + "'.");
                    sb.replace(sb.indexOf(blocked[i].toLowerCase()), sb.lastIndexOf(blocked[i].toLowerCase()) + blocked[i].length(), "porn");
                }
            }
            }
            return sb.toString();
        }

        public static boolean containsWebsite(String text, MapleCharacter player){
            boolean yes = false;
            String subString = text.toLowerCase();
            for(int i = 0; i < blockedSites.length; i++){
                if(subString.contains(blockedSites[i].toLowerCase())){
                    yes = true;
                }
            }
            if(yes){
             player.getClient().getChannelServer().broadcastGMPacket(MaplePacketCreator.serverNotice(0, "[Attention to ALL GMs] - " + player.getName() + " has possibly given an advertising link to another server. Ban him if it leads to another server and if his intention is bad. (Though often it is."));
                player.getClient().getChannelServer().broadcastGMPacket(MaplePacketCreator.serverNotice(0, "Here's the entire dialogue : " + player.getName() + " :" + subString.toString()));
               // LoggerManager.logToDB(" Player(" + player.getName() + ") has mentioned a website. Here's the entire sentence : '" + text + "'", player.getId(), true);
            // put your logger software here
            }
            return true;
        }

}  