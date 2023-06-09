import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import javax.security.auth.login.LoginException;
import javax.sound.midi.SysexMessage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;   

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class Bot extends ListenerAdapter {
    private final Color embedColor = new Color(255,215,0);

    // Filler emojis
    private final String slime_lord_emoji = "<:slime_lord:781391458103590932>";
    private final String slime_emoji = "<:slime:781391443226394626>";

    // Potion emojis
    private final String health_potion_emoji = "<:health_potion:1116836561418461246>"; // cost is 50
    private final String mana_potion_emoji = "<:mana_potion:1116836562366365706>"; // avg mana is 100 cost is 50

    private final String greater_health_potion_emoji = "<:greater_health_potion:1116836558595694813>"; // cost is 150
    private final String greater_mana_potion_emoji = "<:greater_mana_potion:1116836560038543581>"; // avg mana is 250 cost is 150

    private final String super_health_potion_emoji = "<:super_health_potion:787594987276861460>"; // cost is 350
    private final String super_mana_potion_emoji = "<:super_mana_potion:787594974375313418>"; // avg mana is 500 cost is 350
    
    private final String ultimate_health_potion_emoji = "<:ultimate_health_potion:1116812748546920479>"; // cost is 650
    private final String ultimate_mana_potion_emoji = "<:ultimate_mana_potion:1116812747661910037>"; // avg mana is 750 cost is 650

    // List of Mobs
    // Comments denote the index of the Mob in the Array, and whether /*P*/ if it is powertrainable
    public static final Mob[] mobs = {
            /*00*/  /*P*/   new Mob("Rat Lv.1", "<:1_rat:781390912248348683>", 4, 25),
            /*01*/  /*P*/   new Mob("Rat Lv.3", "<:3_rat:781390923753717781>", 7, 35),
            /*02*/  /*P*/   new Mob("Crow Lv.6", "<:6_crow:781390951191937024>", 13, 40),
            /*03*/  /*P*/   new Mob("Wolf Lv.9", "<:9_wolf:781390959735996436>", 17, 50),
            /*04*/  /*P*/   new Mob("Scorpion Lv.12", "<:12_scorpion:781390969824215052>", 18, 50),
            /*05*/  /*P*/   new Mob("Cobra Lv.13", "<:14_cobra:781390978988507158>", 18, 50),
            /*06*/  /*P*/   new Mob("Worm Lv.14", "<:14_worm:781390986614014004>", 19, 55),
            /*07*/  /*P*/   new Mob("Goblin Lv.15", "<:15_goblin:781390994376622140>", 21, 60),
            /*08*/  /*P*/   new Mob("Mummy Lv.25", "<:25_mummy:781391003780513813>", 36, 80),
            /*09*/  /*P*/   new Mob("Pharaoh Lv.35", "<:35_pharaoh:781391013591253063>", 51, 100),
            /*10*/  /*P*/   new Mob("Assassin Lv.45", "<:45_assassin:781391026727682051>", 71, 120),
            /*11*/  /*P*/   new Mob("Assassin Lv.50", "<:50_assassin:781391034990460958>", 81, 140),
            /*12*/  /*P*/   new Mob("Assassin Ninja Lv.55", "<:55_ninja_assassin:781391057128390696>", 91, 160),
            /*13*/          new Mob("Skeleton Archer Lv.80", "<:80_skeleton_archer:781391133023928359>", 101, 300),
            /*14*/  /*P*/   new Mob("Zombie Lv.65", "<:65_zombie:781391115181228069>", 106, 200),
            /*15*/  /*P*/   new Mob("Skeleton Lv.75", "<:75_skeleton:781391124929314827>", 121, 300),
            /*16*/  /*P*/   new Mob("Skeleton Warrior Lv.90", "<:90_skeleton_warrior:781391140691771393>", 146, 375),
            /*17*/  /*P*/   new Mob("Vampire Lv.100", "<:100_vampire:781391148982730763>", 171, 450),
            /*18*/  /*P*/   new Mob("Vampire Lv.110", "<:110_vampire:781391156671807508>", 186, 530),
            /*19*/          new Mob("Drow Ranger Lv.125", "<:120_drow_ranger:781391163743010867>", 191, 600),
            /*20*/          new Mob("Drow Mage Lv. 130", "<:130_drow_mage:781391184470867968>", 191, 600),
            /*21*/  /*P*/   new Mob("Drow Assassin Lv.120", "<:125_drow_assassin:781391173088575488>", 221, 620),
            /*22*/          new Mob("Drow Sorceress Lv.140", "<:140_drow_sorceress:781391206755336222>", 221, 600),
            /*23*/  /*P*/   new Mob("Drow Fighter Lv.135", "<:135_drow_fighter:781391196534472705>", 246, 680),
            /*24*/          new Mob("Lizard Archer Lv.160", "<:160_lizard_archer:781391233582497793>", 271, 650),
            /*25*/          new Mob("Lizard Shaman Lv.170", "<:170_lizard_shaman:781391275177672715>", 276, 600),
            /*26*/          new Mob("Dead Eyes Lv.170", "<:170_dead_eyes:781391266923020309>", 276, 600),
            /*27*/  /*P*/   new Mob("Lizard Warrior Lv.150", "<:150_lizard_warrior:781391223604117534>", 301, 680),
            /*28*/  /*P*/   new Mob("Djinn Lv.150", "<:150_djinn:781391214833303562>", 301, 640),
            /*29*/          new Mob("Lizard High Shaman Lv.190", "<:190_lizard_high_shaman:781391314947538944>", 326, 740),
            /*30*/  /*P*/   new Mob("Gargoyle Lv.190", "<:190_gargoyle:781391298598010892>", 326, 740),
            /*31*/          new Mob("Dragon Hatchling Lv. 240", "<:240_dragon_hatchling:781391338758471690>", 331, 10000),
            /*32*/  /*P*/   new Mob("Lizard Captain lv.180", "<:180_lizard_captain:781391284002488370>", 361, 815),
            /*33*/          new Mob("Dragon Lv.250", "<:250_dragon:781391347922894849>", 501, 20000),
            /*34*/  /*P*/   new Mob("Minotaur Lv.225", "<:225_minotaur:781391328763314187>", 511, 4250),
            /*35*/  /*P*/   new Mob("Minotaur Lv.250", "<:250_minotaur:781391355561246752>", 591, 5000),
            /*36*/          new Mob("Dragon Warden Lv.280", "<:280_dragon_warden:781391390575165450>", 626, 30000),
            /*37*/          new Mob("Ice Elemental Lv.300", "<:300_ice_elemental:781391399709835267>", 676, 40000),
            /*38*/  /*P*/   new Mob("Minotaur Lv.275", "<:275_minotaur:781391363476291597>", 681, 5750),
            /*39*/          new Mob("Ice Dragon Lv.320", "<:320_ice_dragon:781391412217249823>", 726, 50000),
            /*40*/          new Mob("Yeti Lv.350", "<:350_yeti:781391428030431262>", 826, 60000),

            // new Mob("Lava Golem Lv.375",
            // new Mob("Orthrus Lv.425",
            // new Mob("Demon Lv.450",
    };

    // List of Weapons
    // Comments denote the index of the Weapon in the Array
    private final Weapon[] weapons = {
            /*00*/          new Weapon("Training Weapon(4)", "<:4_7_9_11_13_golden_dagger:802411966684987422>", "<:4_7_9_11_13_golden_bow:802411945369927711>", "<:4_7_9_11_13_golden_wand:802411976001191946>", 4, 0),
            /*01*/          new Weapon("Training Weapon(5)", "<:4_5_15_17_19_dagger:781573394603966504>", "<:4_5_15_17_19_bow:781573383217348658>", "<:4_5_15_17_19_wand:781573410525413376>", 5, 0),
            /*02*/          new Weapon("Training Weapon(7)", "<:4_7_9_11_13_golden_dagger:802411966684987422>", "<:4_7_9_11_13_golden_bow:802411945369927711>", "<:4_7_9_11_13_golden_wand:802411976001191946>", 7, 0),
            /*03*/          new Weapon("Training Weapon(9)", "<:4_7_9_11_13_golden_dagger:802411966684987422>", "<:4_7_9_11_13_golden_bow:802411945369927711>", "<:4_7_9_11_13_golden_wand:802411976001191946>", 9, 0),
            /*04*/          new Weapon("Training Weapon(11)", "<:4_7_9_11_13_golden_dagger:802411966684987422>", "<:4_7_9_11_13_golden_bow:802411945369927711>", "<:4_7_9_11_13_golden_wand:802411976001191946>", 11, 0),
            /*05*/          new Weapon("Training Weapon(13)","<:4_7_9_11_13_golden_dagger:802411966684987422>", "<:4_7_9_11_13_golden_bow:802411945369927711>", "<:4_7_9_11_13_golden_wand:802411976001191946>", 13, 0),
            /*06*/          new Weapon("Weapon(15)", "<:4_5_15_17_19_dagger:781573394603966504>", "<:4_5_15_17_19_bow:781573383217348658>", "<:4_5_15_17_19_wand:781573410525413376>", 15, 0),
            /*07*/          new Weapon("Weapon(17)", "<:4_5_15_17_19_dagger:781573394603966504>", "<:4_5_15_17_19_bow:781573383217348658>", "<:4_5_15_17_19_wand:781573410525413376>", 17, 0),
            /*08*/          new Weapon("Weapon(19)", "<:4_5_15_17_19_dagger:781573394603966504>", "<:4_5_15_17_19_bow:781573383217348658>", "<:4_5_15_17_19_wand:781573410525413376>", 19, 0),
            /*09*/          new Weapon("Weapon(20)", "<:20_22_24_short_sword:781573430142566460>", "<:20_22_24_studded_bow:781573439143673922>", "<:20_22_24_novice_wand:781573421523402753>", 20, 0),
            /*10*/          new Weapon("Weapon(22)", "<:20_22_24_short_sword:781573430142566460>", "<:20_22_24_studded_bow:781573439143673922>", "<:20_22_24_novice_wand:781573421523402753>", 22, 0),
            /*11*/          new Weapon("Weapon(24)", "<:20_22_24_short_sword:781573430142566460>", "<:20_22_24_studded_bow:781573439143673922>", "<:20_22_24_novice_wand:781573421523402753>", 24, 0),
            /*12*/          new Weapon("Weapon(25)", "<:25_27_29_sword:781573471007146035>", "<:25_27_29_iron_bow:781573448425275392>", "<:25_27_29_priest_wand:781573463285956668>", 25, 0),
            /*13*/          new Weapon("Weapon(27)", "<:25_27_29_sword:781573471007146035>", "<:25_27_29_iron_bow:781573448425275392>", "<:25_27_29_priest_wand:781573463285956668>", 27, 0),
            /*14*/          new Weapon("Weapon(29)", "<:25_27_29_sword:781573471007146035>", "<:25_27_29_iron_bow:781573448425275392>", "<:25_27_29_priest_wand:781573463285956668>", 29, 0),
            /*15*/          new Weapon("Drow Weapon(30)", "<:30_32_34_341_broadsword:781573477776883723>", "<:30_32_34_341_drow_bow:781573484400607243>", "<:30_32_34_341_royal_priest_wand:781573492524449794>", 30, 0),
            /*16*/          new Weapon("Drow Weapon(32)", "<:30_32_34_341_broadsword:781573477776883723>", "<:30_32_34_341_drow_bow:781573484400607243>", "<:30_32_34_341_royal_priest_wand:781573492524449794>", 32, 0),
            /*17*/          new Weapon("Drow Weapon(34)", "<:30_32_34_341_broadsword:781573477776883723>", "<:30_32_34_341_drow_bow:781573484400607243>", "<:30_32_34_341_royal_priest_wand:781573492524449794>", 34, 0),
            /*18*/          new Weapon("Drow Weapon(34+1)", "<:30_32_34_341_broadsword:781573477776883723>", "<:30_32_34_341_drow_bow:781573484400607243>", "<:30_32_34_341_royal_priest_wand:781573492524449794>", 34, 1),
            /*19*/          new Weapon("Lizard/Gargoyle Weapon(35)", "<:35_37_39_391_lizard_slayer:781573548241059950><:35_37_39_391_gargoyle_slayer:781573514564993034>", "<:35_37_39_391_lizard_bow:781573535676104736><:35_37_39_391_gargoyle_bow:781573500762193921>", "<:35_37_39_391_shaman_wand:781573555975749663><:35_37_39_391_gargoyle_wand:781573524255014942>", 35, 0),
            /*20*/          new Weapon("Lizard/Gargoyle Weapon(37)", "<:35_37_39_391_lizard_slayer:781573548241059950><:35_37_39_391_gargoyle_slayer:781573514564993034>", "<:35_37_39_391_lizard_bow:781573535676104736><:35_37_39_391_gargoyle_bow:781573500762193921>", "<:35_37_39_391_shaman_wand:781573555975749663><:35_37_39_391_gargoyle_wand:781573524255014942>", 37, 0),
            /*21*/          new Weapon("Lizard/Gargoyle Weapon(39)", "<:35_37_39_391_lizard_slayer:781573548241059950><:35_37_39_391_gargoyle_slayer:781573514564993034>", "<:35_37_39_391_lizard_bow:781573535676104736><:35_37_39_391_gargoyle_bow:781573500762193921>", "<:35_37_39_391_shaman_wand:781573555975749663><:35_37_39_391_gargoyle_wand:781573524255014942>", 39, 0),
            /*22*/          new Weapon("Lizard/Gargoyle Weapon(39+1)", "<:35_37_39_391_lizard_slayer:781573548241059950><:35_37_39_391_gargoyle_slayer:781573514564993034>", "<:35_37_39_391_lizard_bow:781573535676104736><:35_37_39_391_gargoyle_bow:781573500762193921>", "<:35_37_39_391_shaman_wand:781573555975749663><:35_37_39_391_gargoyle_wand:781573524255014942>", 39, 1),
            /*23*/          new Weapon("Dragon/Minotaur Weapon(40+1)", "<:401_422_443_dragon_slayer:781573572421484555><:401_422_443_minotaur_slayer:781573599219286066>", "<:401_422_443_dragon_bow:781573565287891024><:401_422_443_minotaur_bow:781573590403121172>", "<:401_422_443_dragon_wand:781573581246955601><:401_422_443_minotaur_wand:781573607322681384>", 40, 1),
            /*24*/          new Weapon("Dragon/Minotaur Weapon(42+2)", "<:401_422_443_dragon_slayer:781573572421484555><:401_422_443_minotaur_slayer:781573599219286066>", "<:401_422_443_dragon_bow:781573565287891024><:401_422_443_minotaur_bow:781573590403121172>", "<:401_422_443_dragon_wand:781573581246955601><:401_422_443_minotaur_wand:781573607322681384>", 42, 2),
            /*25*/          new Weapon("Dragon/Minotaur Weapon(44+3)", "<:401_422_443_dragon_slayer:781573572421484555><:401_422_443_minotaur_slayer:781573599219286066>", "<:401_422_443_dragon_bow:781573565287891024><:401_422_443_minotaur_bow:781573590403121172>", "<:401_422_443_dragon_wand:781573581246955601><:401_422_443_minotaur_wand:781573607322681384>", 44, 3),
            /*26*/          new Weapon("Icy Weapon(45+1)", "<:451_472_493_icy_broadsword:781573626281328690>", "<:451_472_493_icy_bow:781573616629841951>", "<:451_472_493_icy_wand:781573633792540692>", 45, 1),
            /*27*/          new Weapon("Icy Weapon(47+2)", "<:451_472_493_icy_broadsword:781573626281328690>", "<:451_472_493_icy_bow:781573616629841951>", "<:451_472_493_icy_wand:781573633792540692>", 47, 2),
            /*28*/          new Weapon("Icy Weapon(49+3)", "<:451_472_493_icy_broadsword:781573626281328690>", "<:451_472_493_icy_bow:781573616629841951>", "<:451_472_493_icy_wand:781573633792540692>", 49, 3), 
            /*29*/          new Weapon("Golden Weapon(50+1)", "<:501_522_543_golden_broadsword:802412010616520716>", "<:501_522_543_golden_bow:802412021806792755>", "<:501_522_543_golden_wand:802411996715679794>", 50, 1),
            /*30*/          new Weapon("Golden Weapon(52+2)", "<:501_522_543_golden_broadsword:802412010616520716>", "<:501_522_543_golden_bow:802412021806792755>", "<:501_522_543_golden_wand:802411996715679794>", 52, 2),
            /*31*/          new Weapon("Golden Weapon(54+3)", "<:501_522_543_golden_broadsword:802412010616520716>", "<:501_522_543_golden_bow:802412021806792755>", "<:501_522_543_golden_wand:802411996715679794>", 54, 3),
            /*32*/          new Weapon("Golden Weapon(56+4)", "<:501_522_543_golden_broadsword:802412010616520716>", "<:501_522_543_golden_bow:802412021806792755>", "<:501_522_543_golden_wand:802411996715679794>", 54, 3),
            /*32*/          new Weapon("Golden Weapon(58+5)", "<:501_522_543_golden_broadsword:802412010616520716>", "<:501_522_543_golden_bow:802412021806792755>", "<:501_522_543_golden_wand:802411996715679794>", 54, 3),
    };

    public static void main(String[] args) throws LoginException {
        // Local variable to store the token String
        String token = ""; 

        // Retrieve the token from the .env file
        try {
            BufferedReader readToken = new BufferedReader(new FileReader("Token.env"));
            token = readToken.readLine();
            readToken.close();
        } catch (Exception e) {
            // Typically goes here if file is not found
            e.printStackTrace();
            System.exit(0);
        }
        
        // Deployment for bot in less than 100 servers
        JDA jda = JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class)).addEventListeners(new Bot()).build();
        jda.getPresence().setActivity(Activity.playing("v4.0 is out! /changelog for more details!"));


        // Deployment for bot in over 100 servers (requires sharding)
        /* 
        JDA jda = JDABuilder.createDefault(token).build();
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.addEventListeners(new Bot());
        builder.setActivity(Activity.playing("v4.0 is out! /changelog for more details!"));
        builder.build();
        */

        // Updating the commands
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(Commands.slash("help", "Returns a list of commands"));
        commands.addCommands(Commands.slash("github", "Displays the github link for the bot code"));
        commands.addCommands(Commands.slash("invite", "Shows the invite link to add the bot to your server"));
        commands.addCommands(Commands.slash("info", "Shows more information about the bot"));
        commands.addCommands(Commands.slash("changelog", "Shows the changelog"));
        commands.addCommands(Commands.slash("exp", "Calculates the experience at the current base level")
                        .addOptions(new OptionData(INTEGER, "level", "enter current exp level").setRequiredRange(0, 1000).setRequired(true))
        );
        commands.addCommands(Commands.slash("skull", "Calculates the amount of gold needed for skulling")
                        .addOptions(new OptionData(INTEGER, "base", "enter current base level").setRequiredRange(0, 1000).setRequired(true))
        );
        commands.addCommands(Commands.slash("potioncost", "Calculates the amount of gold needed for potions")
                        .addOptions(new OptionData(INTEGER, "numpotions", "enter number of desired potions").setRequiredRange(1, 9007199254740991L).setRequired(true))
        );
        commands.addCommands(
                Commands.slash("grind", "Calculates the experience needed to reach a certain base level")
                        .addOptions(new OptionData(INTEGER, "base1", "enter current base level").setRequiredRange(0, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "base2", "enter goal base level").setRequiredRange(1, 1001).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "grindrate", "grind rate in exp/hour").setRequiredRange(1, 1000000000).setRequired(false))
        );
        commands.addCommands(
                Commands.slash("train", "Calculates the mob that you can train effectively on")
                        .addOptions(new OptionData(INTEGER, "base", "enter base level").setRequiredRange(1, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "stat", "enter stat level").setRequiredRange(5, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "buff", "enter buffs").setRequiredRange(0, 100).setRequired(false))
                        .addOptions(new OptionData(INTEGER, "weaponatk", "weapon attack").setRequiredRange(4, 100).setRequired(false))
        );
        commands.addCommands(
                Commands.slash("stat", "Calculates the amount of ticks and time needed to reach a certain stat level")
                        .addOptions(new OptionData(INTEGER, "stat1", "enter current stat level").setRequiredRange(5, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "stat2", "enter goal stat level").setRequiredRange(5, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "statrate", "enter exp per hour").setRequiredRange(1, 36000).setRequired(false))
        );
        commands.addCommands(
                Commands.slash("ptrain", "Calculates the mob that you can power train effectively on")
                        .addOptions(new OptionData(INTEGER, "class", "enter class").addChoice("Melee", 0).addChoice("Distance", 1).addChoice("Magic", 2).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "base", "enter base level").setRequiredRange(1, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "stat", "enter stat level").setRequiredRange(5, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "buff", "enter buffs").setRequiredRange(0, 100).setRequired(false))
                        .addOptions(new OptionData(INTEGER, "weaponatk", "enter weapon attack").setRequiredRange(4, 100).setRequired(false))
                        .addOptions(new OptionData(INTEGER, "tick", "enter ticks per second").setRequiredRange(3, 14).setRequired(false))
        );
        commands.addCommands(
                Commands.slash("dmg", "Calculates the auto-damage you do to a certain group of mobs")
                        .addOptions(new OptionData(INTEGER, "attacktype", "enter attacktype").addChoice("Auto", 0).addChoice("Special (Melee)", 1).addChoice("Special (Distance)", 2).addChoice("Special (Magic)", 3).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "mob", "enter mob ID (Do /moblist for the list of mob IDs)").setRequiredRange(1, 40).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "weaponatk", "enter weapon attack").setRequiredRange(4, 100).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "base", "enter base level").setRequiredRange(0, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "stat", "enter stat level").setRequiredRange(5, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "buff", "enter buffs").setRequiredRange(0, 100).setRequired(false))
        );
        commands.addCommands(
                Commands.slash("weapon", "Calculates the weapon needed to train on a certain mob")
                .addOptions(new OptionData(INTEGER, "attacktype", "enter attacktype").addChoice("Afk Train (Auto)", 0).addChoice("Power Train (Melee)", 1).addChoice("Power Train (Distance)", 2).addChoice("Power Train (Magic)", 3).setRequired(true))
                .addOptions(new OptionData(INTEGER, "mob", "enter mob ID (Do /moblist for the list of mob IDs)").setRequiredRange(1, 40).setRequired(true))
                .addOptions(new OptionData(INTEGER, "base", "enter base level").setRequiredRange(0, 1000).setRequired(true))
                .addOptions(new OptionData(INTEGER, "stat", "enter stat level").setRequiredRange(5, 1000).setRequired(true))
                .addOptions(new OptionData(INTEGER, "buff", "enter buffs").setRequiredRange(0, 100).setRequired(false))
        );
        commands.addCommands(
                Commands.slash("offline", "Calculates the time needed between stat1 and stat2, or the stat gain from a specified hours")
                        .addOptions(new OptionData(INTEGER, "stat1", "enter current stat level").setRequiredRange(5, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "stat2", "enter goal stat level").setRequiredRange(5, 1000).setRequired(false))
                        .addOptions(new OptionData(INTEGER, "hours", "enter hours of offline training").setRequiredRange(0, 1000000).setRequired(false))
        );
        commands.addCommands(
                Commands.slash("oneshot", "Calculates the stat needed to one-shot a certain mob")
                        .addOptions(new OptionData(INTEGER, "attacktype", "enter attacktype").addChoice("Auto", 0).addChoice("Special (Melee)", 1).addChoice("Special (Distance)", 2).addChoice("Special (Magic)", 3).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "mob", "enter mob ID (Do /moblist for the list of mob IDs)").setRequiredRange(1, 40).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "base", "enter base level").setRequiredRange(0, 1000).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "weaponatk", "enter weapon attack").setRequiredRange(4, 100).setRequired(true))
                        .addOptions(new OptionData(INTEGER, "stat", "enter stat level").setRequiredRange(5, 1000).setRequired(false))
                        .addOptions(new OptionData(INTEGER, "buff", "enter buffs").setRequiredRange(0, 100).setRequired(false))
                        .addOptions(new OptionData(INTEGER, "consistency", "enter desired percentage % chance to one-shot").setRequiredRange(1, 100).setRequired(false))
        );
        commands.addCommands(Commands.slash("moblist", "Returns a list of mob IDs"));
        commands.queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null)
            return;
        
        updateLogs(event.getName());
        
        // Handles events by calling the appropriate method
        switch (event.getName()) {
            case "help" -> help(event);
            case "github" -> github(event);
            case "invite" -> invite(event);
            case "info" -> info(event);
            case "changelog" -> changelog(event);
            case "moblist" -> moblist(event);
            case "exp" -> {
                int currLevel = event.getOption("level").getAsInt(); // never null
                exp(event, currLevel);
            }
            case "skull" -> {
                int currlevel = event.getOption("base").getAsInt(); // never null
                skull(event, currlevel);
            }
            case "potioncost" -> {
                long numpots = event.getOption("numpotions").getAsLong();
                potions(event, numpots);
            }
            case "grind" -> {
                int currlevel1 = event.getOption("base1").getAsInt(); // never null
                int currleve2 = event.getOption("base2").getAsInt(); // never null
                int grindrate = event.getOption("grindrate", 2000000, OptionMapping::getAsInt); // default: 2000000
                grind(event, currlevel1, currleve2, grindrate);
            }
            case "train" -> {
                int baselevel = event.getOption("base").getAsInt();
                int statlevel = event.getOption("stat").getAsInt();
                int bufflevel = event.getOption("buff", 0, OptionMapping::getAsInt);
                int weaponlevel = event.getOption("weaponatk", 5, OptionMapping::getAsInt);
                train(event, baselevel, statlevel, bufflevel, weaponlevel);
            }
            case "stat" -> {
                int stat1 = event.getOption("stat1").getAsInt();
                int stat2 = event.getOption("stat2").getAsInt();
                int statrate = event.getOption("statrate", -1, OptionMapping::getAsInt);
                stat(event, stat1, stat2, statrate);
            }
            case "ptrain" -> {
                int classtype = event.getOption("class").getAsInt();
                int baselevel = event.getOption("base").getAsInt();
                int statlevel = event.getOption("stat").getAsInt();
                int bufflevel = event.getOption("buff", 0, OptionMapping::getAsInt);
                int weaponlevel = event.getOption("weaponatk", 5, OptionMapping::getAsInt);
                int tick = event.getOption("tick", 4, OptionMapping::getAsInt);
                ptrain(event, classtype, baselevel, statlevel, bufflevel, weaponlevel, tick);
            }
            case "dmg" -> {
                int attacktype = event.getOption("attacktype").getAsInt();
                int mob = event.getOption("mob").getAsInt();
                int baselevel = event.getOption("base").getAsInt();
                int statlevel = event.getOption("stat").getAsInt();
                int bufflevel = event.getOption("buff", 0, OptionMapping::getAsInt);
                int weaponlevel = event.getOption("weaponatk").getAsInt();
                dmg(event, attacktype, mob, baselevel, statlevel, bufflevel, weaponlevel);
            }
            case "weapon" -> {
                int attacktype = event.getOption("attacktype").getAsInt();
                int mob = event.getOption("mob").getAsInt();
                int baselevel = event.getOption("base").getAsInt();
                int statlevel = event.getOption("stat").getAsInt();
                int bufflevel = event.getOption("buff", 0, OptionMapping::getAsInt);
                weapon(event, attacktype, mob, baselevel, statlevel, bufflevel);
            }
            case "offline" -> {
                int stat1 = event.getOption("stat1").getAsInt();
                int stat2 = event.getOption("stat2", 0, OptionMapping::getAsInt);
                int hours = event.getOption("hours", 0, OptionMapping::getAsInt);
                offline(event, stat1, stat2, hours);
            }
            case "oneshot" -> {
                int attacktype = event.getOption("attacktype").getAsInt();
                int mob = event.getOption("mob").getAsInt();
                int baselevel = event.getOption("base").getAsInt();
                int statlevel = event.getOption("stat", 0, OptionMapping::getAsInt);
                int bufflevel = event.getOption("buff", 0, OptionMapping::getAsInt);
                int weaponlevel = event.getOption("weaponatk").getAsInt();
                int consistency = event.getOption("consistency", 80, OptionMapping::getAsInt);
                oneshot(event, attacktype, mob, baselevel, statlevel, bufflevel, weaponlevel, consistency);
            }
            // Command does not exist
            default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    public void help(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder(); // Building an embed
        embed.setTitle("Commands " + slime_lord_emoji + slime_emoji); // Sets title
        embed.setColor(embedColor); // Sets color
        String message = (
            "**/train [base] [stat] [buffs] [weapon atk]**\n" +
            "Calculates the mob that you can train effectively on.\n" +
            "Buff default is 0. Weapon atk default is 5.\n" +
            "\n" +

            "**/ptrain [class] [base] [stat] [buffs] [weapon atk] [ticks]**\n" +
            "Calculates the mob that you can power-train effectively on.\n" +
            "Buff default is 0. Weapon atk default is 5. Ticks default is 3.\n" +
            "\n" +

            "**/moblist**\n" +
            "Shows the list of mob IDs.\n" +
            "\n" +

            "**/oneshot [attacktype] [mobID] [base] [weaponatk] [stat] [buffs] [consistency]**\n" +
            "Calculates whether you already one-shot a mob, or the stat level needed to one-shot a certain mob.\n" +
            "Buff default is 0. Consistency default is 80%.\n" +
            "Do /moblist for the list of mob IDs.\n" +
            "\n" +

            "**/weapon [attacktype] [mobID] [base] [stat] [buffs]**\n" +
            "Calculates the weapon needed to train on a certain mob.\n" +
            "Buff default is 0. \n" +
            "Do /moblist for the list of mob IDs.\n" +
            "\n" +

            "**/dmg [attacktype] [mobID] [base] [stat] [buffs] [weapon atk]**\n" +
            "Calculates the damage you do to certain mobs.\n" +
            "Do /moblist for the list of mob IDs.\n" +
            "\n" +

            "**/stat [stat1] [stat2] [statrate]**\n" +
            "Calculates the time, amount of experience, and potions needed to reach a certain stat level.\n" +
            "Statrate default is 3600.\n" +
            "\n" +

            "**/offline [stat1] [stat2] [hours]**\n" + 
            "Calculates the offline training time needed for stat2, or stat gain from hours of offline training.\n" +
            "\n" +

            "**/exp [base]**\n" + 
            "Calculates the experience at a certain base level.\n" +
            "\n" +

            "**/grind [base1] [base2] [grindrate]**\n" +
            "Calculates the time and amount of experience needed to reach a certain base level.\n" +
            "Grindrate default is 2000000.\n" +
            "\n" +
            
            "**/skull [base]**\n" +
            "Calculates the amount of gold needed to skull for a certain base level.\n" +
            "\n" +

            "**/potioncost [numpotions]**\n" +
            "Calculates the amount of gold needed for a certain number potions.\n" + 
            "\n" + 

            "**/help**\n" + 
            "Displays the command list, but it looks like you already know how to use this!\n" +
            "\n" +

            "**/changelog**\n" + 
            "Shows the changelog.\n" +
            "\n" +

            "**/info**\n" + 
            "Shows more information about the bot.\n" +
            "\n" +

            "**/invite**\n" + 
            "Shows the invite link to add the bot to your server.\n" +
            "*Make sure you give the bot **ALL** of the permissions requested.\n" +
            "\n" +

            "**/github**\n" +
            "Displays the github link containing the source code for the bot!\n" +
            "\n" +

            "\n" +
            "*If you have suggestions or bugs to report, message me!*: ***mims#6519***\n"
        );
        embed.appendDescription(message);
        event.replyEmbeds(embed.build()).queue();
    }

    public void github(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("GitHub Link " + slime_lord_emoji + slime_emoji);
        embed.setColor(embedColor);
        embed.appendDescription("https://github.com/Mimsqueeze/Mims-Rucoy-Calculator :heart:");
        event.replyEmbeds(embed.build()).queue();
    }

    public void invite(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Invite Link " + slime_lord_emoji + slime_emoji);
        embed.setColor(embedColor);
        embed.appendDescription("*Make sure you give the bot **ALL** of the permissions requested!\n https://discord.com/api/oauth2/authorize?client_id=758831061596635136&permissions=139586754624&scope=applications.commands%20bot");
        event.replyEmbeds(embed.build()).queue();
    }

    public void exp(SlashCommandInteractionEvent event, int currLevel) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Experience Calculation");
        embed.setColor(embedColor);
        embed.appendDescription("Level **" + String.format("%,d", currLevel) + "** is at **" + String.format("%,.0f", Formulas.exp_Calc(currLevel)) + "** exp!\n" +
                "You need **" + String.format("%,.0f", (Formulas.exp_Calc(currLevel + 1) - Formulas.exp_Calc(currLevel))) + "** experience to reach Level **" + String.format("%,d", currLevel + 1) + "**!");
        event.replyEmbeds(embed.build()).queue();
    }

    public void skull(SlashCommandInteractionEvent event, int currLevel) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Skulling Gold Calculation");
        embed.setColor(embedColor);
        embed.appendDescription(
            "Base Level: **" + String.format("%,d", currLevel) + "**\n" +
            "Gold Needed for **Yellow Skull**<:skull_yellow:781416899975053352>: **" + String.format("%,d", 150 * currLevel) + "** <:gold:781421094979633183>\n" +
            "Gold Needed for **Orange Skull**<:skull_orange:781416870170984448>: **" + String.format("%,d", 150 * 4 * currLevel) + "** <:gold:781421094979633183>\n" +
            "Gold Needed for **Red Skull**<:skull_red:781416880664477697>: **" + String.format("%,d", 150 * 13 * currLevel) + "** <:gold:781421094979633183>\n" +
            "Gold Needed for **Black Skull**<:skull_black:781416856564006925>: **" + String.format("%,d", 150 * 40 * currLevel) + "** <:gold:781421094979633183>"
        );
        event.replyEmbeds(embed.build()).queue();
    }

    public void potions(SlashCommandInteractionEvent event, long numpots) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Potion Gold Calculation");
        embed.setColor(embedColor);
        embed.appendDescription("Potion Number: **" + String.format("%,d", numpots) + "**\n" +
            "Gold Needed for **Potion**" + mana_potion_emoji + health_potion_emoji + ": **" + String.format("%,d", numpots*50) + "** <:gold:781421094979633183>\n" +
            "Gold Needed for **Greater Potion**" + greater_mana_potion_emoji + greater_health_potion_emoji + ": **" + String.format("%,d", numpots*150) + "** <:gold:781421094979633183>\n" +
            "Gold Needed for **Super Potion**" + super_mana_potion_emoji + super_health_potion_emoji + ": **" + String.format("%,d", numpots*350) + "** <:gold:781421094979633183>\n" +
            "Gold Needed for **Ultimate Potion**" + ultimate_mana_potion_emoji + ultimate_health_potion_emoji + ": **" + String.format("%,d", numpots*650) + "** <:gold:781421094979633183>"
        );
        event.replyEmbeds(embed.build()).queue();
    }

    public void info(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Info and Credits "+ slime_lord_emoji + slime_emoji);
        embed.setColor(embedColor);
        String message = (
            "**Mims' Rucoy Calculator v4.1** - Updated on **06/09/23**\n" +
            "Made by **Mims** (ign: Mimsqueeze)\n" +
            "\n" +
            "Heya! :) I'm a Rucoy Calculator with a variety of features/commands such as:\n" +
            slime_emoji + "/train and /ptrain commands, which tells you the best mob for effective training!\n" +
            slime_emoji + "/dmg command, which tells you the damage you do to a certain mob!\n" +
            slime_emoji + "/weapon command, which tells you the best weapon to train with!\n" +
            slime_emoji + "/oneshot command, which tells you the stat level needed to one-shot a certain mob!\n" +
            slime_emoji + "/offline command, which tells you the amount of offline training you need to reach the next level\n" +
            slime_emoji + "+ much more!\n" +
            "*If you have any suggestions, tips, or if there are any bugs, please let me know on discord: **mims#6519***\n" +
            "***Also, if you know the formulas for Player damage, Player defense, or PVE Damage, and would like to share them with me, add me! It would be a huge help, and I would really appreciate it!***\n" + 
            "\n" +
            "**+** Special thanks to **Tribrid** and **PotatoKing** for helping me make this discord bot possible!"
        );
        embed.appendDescription(message);
        event.replyEmbeds(embed.build()).queue();
    }

    public void changelog(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Changelog " + slime_lord_emoji + slime_emoji);
        embed.setColor(embedColor);
        String message = (
            slime_emoji + "**09/28/20 v1.3** - Added **!invite**, **!stat**, and **!changelog** command. Testing out !grind command combining outputs into all one message.\n" +
            slime_emoji + "**09/29/20 v1.4** - Combined !train and !help command output into one output message.\n" +
            slime_emoji + "**09/30/20 v1.5** - Changed !dam and !specdam commands to !dmg and !specdmg. Also **Added Ice Mob data** to !train, !dmg, and !specdmg Commands, check them out! :D\n" +
            slime_emoji + "**10/02/20 v1.6** - Added **!grind** and **!mino** commands, check them out! If there are any bugs, lmk on discord :) - Also working on combining outputs today.\n" +
            slime_emoji + "**10/03/20 v1.7** - Combined all command outputs into one message, improving bot speed and it looks more clean now lol.\n" +
            slime_emoji + "**10/11/20 v1.8** - Fixed bug where people entered invalid numbers, causing the program to crash.\n" +
            slime_emoji + "**11/07/20 v1.9** - Fixed yet another crashing bug (Hopefully it goes down less now)! And applied the bot for varification! (yes, finally.)\n" +
            slime_emoji + "**11/12/20 v1.10** - Added **!exp** and **!skull** commands, and remade the **!help** command! Check them out!\n" +
            slime_emoji + "**11/26/20 v2.0** - Changed output format, and added the !vote command.\n" +
            slime_emoji + "**11/27/20 v2.1** - Fixed more crashing bugs, lol.\n" +
            slime_emoji + "**12/04/20 v2.2** - Added more features to the !ptrain and !mptrain commands, including tick inputs as well as the tickrate output. Check it out!\n" +
            slime_emoji + "**12/07/20 v2.3** - Fixed even more crashing bugs! You guys like inputting a lot of big numbers, huh? \n" +
            slime_emoji + "**12/13/20 v2.4** - Added [statrate] to !stat command, !stat command now tells you the number of potions for power-training, !train now tells you the stats to deal 1 max damage. and !specdmg and !dmg now include critical damage! Keep the good suggestions coming!\n" +
            slime_emoji + "**12/21/20 v2.5** - Fixed more crashing bugs - I'm now spam proof! Woo! More updates coming next week.\n" +
            slime_emoji + "**01/09/21 v2.6** - Happy new year! Added the time it takes to kill the mob you can train in the !train command, working on adding it to the other commands as well!\n" +
            slime_emoji + "**01/23/21 v2.7** - Added new weapons to the !mino command, and added time to !ptrain, !mptrain, and !mino commands.\n" +
            slime_emoji + "**09/19/21 v2.8** - Added slash commands.\n" +
            slime_emoji + "**04/25/22 v3.0** - Completely migrated to slash commands. Bug fixes coming soon.\n" +
            slime_emoji + "**04/26/22 v3.1** - Fixed some bugs, optimized code, /weapon and /offline commands coming soon.\n" +
            slime_emoji + "**05/20/22 v3.2** - Fixed a bug that would skip certain mobs in the /train command, thanks to Forger#9792\n" +
            slime_emoji + "**08/01/22 v3.3** - Finished /weapon command. Try it out! (Report bugs to mims#6519)\n" +
            slime_emoji + "**09/17/22 v3.4** - Finished /offline command. Try it with either stat2 or hours! /potion, /simulatetrain, and /simulategrind commands coming soon!\n" +
            slime_emoji + "**12/31/22 v4.0** - Fixed a bug where buffs and stat were not consistent, fixed bug where gold emoji did not show, added the new rarity golden weapons, fixed drow mob levels, modified /weapon to work with new golden weapons, fixed output bug in /weapon, revised /stat and /offline command formula to be more accurate, bot now uses java i/o to get token, removed the /vote command, and added the new /oneshot command (thanks Cubels#0084 for the suggestion!)\n" +
            "However perhaps most importantly, the source code to the bot is now **public** on GitHub! Do /github for the link! Thanks for everyones support and Happy New Year~! :heart:\n"
        );
        embed.appendDescription(message);

        EmbedBuilder embed2 = new EmbedBuilder();
        embed2.setColor(embedColor);
        String message2 = (
            slime_emoji + "**06/09/23 v4.1** - /stat now tells you how many potions you need for powertraining! Also fixed /weapon and /ptrain formulas to account for the update to powertraining. Also added the new /potioncost command that tells you the cost of a certain number of potions! Also added tracking command usage frequency. Happy power training everyone!" + slime_lord_emoji + "\n"
        );
        embed2.appendDescription(message2);
        event.replyEmbeds(embed.build(), embed2.build()).queue();
                
    }

    public void grind(SlashCommandInteractionEvent event, int base1, int base2, int grindRate) {
        if (base1 > base2) {
            event.reply("Goal level must be greater than current level").setEphemeral(true).queue();
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Grinding Calculation");
        embed.setColor(embedColor);
        embed.appendDescription("Initial Level: **" + String.format("%,d", base1) + "** " + slime_lord_emoji + " Goal Level: **" + String.format("%,d", base2) + "** " + slime_lord_emoji + " Grindrate: **" + String.format("%,d", grindRate) + "** Exp/Hour\n" +
                "You need **" + String.format("%,.0f", (Formulas.exp_Calc(base2) - Formulas.exp_Calc(base1))) + "** experience until you reach base level **" + String.format("%,d", base2) + "**!\n" +
                "This is around **" + String.format("%,.1f", 60 * (Formulas.exp_Calc(base2) - Formulas.exp_Calc(base1)) / grindRate) + "** minutes, or **" + String.format("%,.1f", (Formulas.exp_Calc(base2) - Formulas.exp_Calc(base1)) / grindRate) + "** hours of grinding at a rate of **" + String.format("%,d", grindRate) + "** exp/hr!");
        event.replyEmbeds(embed.build()).queue();
    }

    public void stat(SlashCommandInteractionEvent event, int stat1, int stat2, int statRate) {
        if (stat1 > stat2) {
            event.reply("stat2 must be greater than stat1").setEphemeral(true).queue();
        }
        double ticks1;
        double ticks2;
        if (stat1 <= 54) {
            ticks1 = Formulas.stat0to54_Calc(stat1);
        } else {
            ticks1 = Formulas.stat55to99_Calc(stat1);
        } 
        
        if (stat2 <= 54) {
            ticks2 = Formulas.stat0to54_Calc(stat2);
        } else {
            ticks2 = Formulas.stat55to99_Calc(stat2);
        }

        double totalTicks = ticks2 - ticks1;
        

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Stat Calculation");
        embed.setColor(embedColor);
        if (statRate != -1) {
            embed.appendDescription(
                "Initial Stat: ** " + String.format("%,d", stat1) + " **" + slime_lord_emoji +" Goal Stat: **" + String.format("%,d", stat2) + "**\n" +
                "You need approximately** " + String.format("%,.0f", totalTicks) + "** ticks until you reach stat level **" + String.format("%,d", stat2) + "**!\n" +
                "This is around **" + String.format("%,.1f", totalTicks*60/statRate) + "** minutes, or **" + String.format("%,.1f", totalTicks/statRate) + "** hours of training at a rate of **" + String.format("%,d", statRate) + "** exp/hr!");
        } else {
            long mana_1 = 50*(long)totalTicks/4; // Standard 4-tick powertrain
            long mana_1_5 = 50*(long)totalTicks/6; // 1.5x exp 4-tick powertrain
            long mana_2 = 50*(long)totalTicks/8; // 2x exp 4-tick powertrain
            long mana_2_5 = 50*(long)totalTicks/10; // 2.5x exp 4-tick powertrain

            embed.appendDescription(
                "Initial Stat: ** " + String.format("%,d", stat1) + " **" + slime_lord_emoji +" Goal Stat: **" + String.format("%,d", stat2) + "**\n" +
                "You need approximately** " + String.format("%,.0f", totalTicks) + "** ticks until you reach stat level **" + String.format("%,d", stat2) + "**!\n" +
                "\n" + 
                "This is around **" + String.format("%,.1f", totalTicks*60/3600) + "** minutes, or **" + String.format("%,.1f", totalTicks/3600) + "** hours of afk training without bonuses!\n" +
                "This is around **" + String.format("%,.1f", totalTicks*60/14400) + "** minutes, or **" + String.format("%,.1f", totalTicks/14400) + "** hours of **4-tick** power training without bonuses!\n" +
                "Which will cost you around **" + String.format("%,d",mana_1/100) + mana_potion_emoji + "**, **" + String.format("%,d",mana_1/250) + greater_mana_potion_emoji + "**, **" + String.format("%,d",mana_1/500) + super_mana_potion_emoji + "**, or **" + String.format("%,d",mana_1/750) + ultimate_mana_potion_emoji + "**\n" +
                "\n" + 
                "This is around **" + String.format("%,.1f", totalTicks*60/(3600*1.5)) + "** minutes, or **" + String.format("%,.1f", totalTicks/(3600*1.5)) + "** hours of afk training with a **1.5x** bonus!\n" +
                "This is around **" + String.format("%,.1f", totalTicks*60/(14400*1.5)) + "** minutes, or **" + String.format("%,.1f", totalTicks/(14400*1.5)) + "** hours of **4-tick** power training with a **1.5x** bonus!\n" +
                "Which will cost you around **" + String.format("%,d",mana_1_5/100) + mana_potion_emoji + "**, **" + String.format("%,d",mana_1_5/250) + greater_mana_potion_emoji + "**, **" + String.format("%,d",mana_1_5/500) + super_mana_potion_emoji + "**, or **" + String.format("%,d",mana_1_5/750) + ultimate_mana_potion_emoji + "**\n" +
                "\n" + 
                "This is around **" + String.format("%,.1f", totalTicks*60/(3600*2)) + "** minutes, or **" + String.format("%,.1f", totalTicks/(3600*2)) + "** hours of afk training with a **2x** bonus!\n" +
                "This is around **" + String.format("%,.1f", totalTicks*60/(14400*2)) + "** minutes, or **" + String.format("%,.1f", totalTicks/(14400*2)) + "** hours of **4-tick** power training with a **2x** bonus!\n" +
                "Which will cost you around **" + String.format("%,d",mana_2/100) + mana_potion_emoji + "**, **" + String.format("%,d",mana_2/250) + greater_mana_potion_emoji + "**, **" + String.format("%,d",mana_2/500) + super_mana_potion_emoji + "**, or **" + String.format("%,d",mana_2/750) + ultimate_mana_potion_emoji + "**\n" +
                "\n" + 
                "This is around **" + String.format("%,.1f", totalTicks*60/(3600*2.5)) + "** minutes, or **" + String.format("%,.1f", totalTicks/(3600*2.5)) + "** hours of afk training with a **2.5x** bonus!\n" +
                "This is around **" + String.format("%,.1f", totalTicks*60/(14400*2.5)) + "** minutes, or **" + String.format("%,.1f", totalTicks/(14400*2.5)) + "** hours of **4-tick** power training with a **2.5x** bonus!\n" +
                "Which will cost you around **" + String.format("%,d",mana_2_5/100) + mana_potion_emoji + "**, **" + String.format("%,d",mana_2_5/250) + greater_mana_potion_emoji + "**, **" + String.format("%,d",mana_2_5/500) + super_mana_potion_emoji + "**, or **" + String.format("%,d",mana_2_5/750) + ultimate_mana_potion_emoji + "**\n"
            );
        }
        event.replyEmbeds(embed.build()).queue();
    }

    public void moblist(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Moblist");
        embed.setColor(embedColor);
        StringBuilder string = new StringBuilder();
        for (int x = 0; x < mobs.length; x++) {
            string.append("Mob ID: **").append(x).append("** - **").append(mobs[x].getMob_name()).append(" ").append(mobs[x].getEmoji_code()).append("**\n");
        }
        event.replyEmbeds(embed.appendDescription(string).build()).queue();
    }

    public void train(SlashCommandInteractionEvent event, int base, int stat, int buff, int weaponatk) {
        String header = "Base: **" + base + "** " + slime_lord_emoji + " Stat: **" + stat + "** " + slime_lord_emoji + " Buffs: **+" + buff + "** " + slime_lord_emoji + " Weapon: **" + weaponatk + " Atk**\n";
        String str0 = ""; //You can train effectively on...
        String str1 = ""; //Max Damage...
        String str2 = ""; //Average time to kill...
        String str3 = ""; //Average time to kill...
        String str4 = ""; //You need...
        String str5 = ""; //You deal...

        int stat1 = stat + buff;
        double min_raw_damage = Formulas.auto_min_raw_damage_Calc(stat1,weaponatk,base);
        double max_raw_damage = Formulas.auto_max_raw_damage_Calc(stat1,weaponatk,base);
        double max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
        double accuracy = 0;

        //An index variable for mobs[]
        int pos = 0;

        //Iterate through loop until you find a mob you can train on with greater than .1749 accuracy
        for (int x = mobs.length-1; x >= 0; x--) {
            if (x == 26|| x == 31) {continue;}
            accuracy = Formulas.accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, x);
            if (accuracy >= 0.1749) {
                pos = x;
                break;
            }
        }

        //Calculate average damage which you need for average time to kill
        double min_damage = Formulas.min_damage_Calc(min_raw_damage, pos);
        double max_damage = Formulas.max_damage_Calc(max_raw_damage, pos);
        double max_crit_damage = Formulas.max_crit_damage_Calc(max_raw_crit_damage, pos);
        double avgdmg = Formulas.average_damage_Calc(accuracy, max_damage, min_damage, max_crit_damage);
        double tickrate = Formulas.tickrate_Calc(accuracy, 3600);

        //In certain cases you can effective train on two mobs
        boolean onemob = true;
        boolean checknextmob = true;
        int newpos = pos + 1;
        if (pos == 5 || pos == 20 || pos == 22 || pos == 28 || pos == 30) {
            pos--;
            onemob = false;
        }
        if (newpos > 40) {
            checknextmob = false;
        }
        if (newpos == 26||newpos == 31){
            newpos++;
        }

        double time = Formulas.time_to_kill_Calc(avgdmg, pos);
        str0 = "You can train effectively on **" + mobs[pos].getMob_name() + mobs[pos].getEmoji_code() + "**!\n";
        if (!onemob) {
            double time2 = Formulas.time_to_kill_Calc(avgdmg, pos+1);
            str0 = "You can train effectively on **" + mobs[pos].getMob_name() + mobs[pos].getEmoji_code() + " & " + mobs[pos + 1].getMob_name() + mobs[pos + 1].getEmoji_code() + "**!\n";
            str3 = "Average time to kill **" + mobs[pos + 1].getMob_name() + mobs[pos + 1].getEmoji_code() + "**: **" + (int)time2/60 + "** min. **" + (int)time2%60 + "** sec.\n"; //part of output embed
        }

        //calculating stats you need to train on the next mob
        int statadd = 0;
        boolean checked = false;
        boolean alrdealdamage = false;
        boolean dealdamage = false;
        double new_max_damage;
        double newaccuracy = 0;
        while (newaccuracy < 0.1749 && checknextmob) {
            int statneeded = stat1 + statadd;

            double new_min_raw_damage = Formulas.auto_min_raw_damage_Calc(statneeded, weaponatk, base);
            double new_max_raw_damage = Formulas.auto_max_raw_damage_Calc(statneeded, weaponatk, base);

            double new_max_raw_critdamage = Formulas.max_raw_crit_damage_Calc(new_max_raw_damage);

            new_max_damage = Formulas.max_damage_Calc(new_max_raw_damage, newpos);
            newaccuracy = Formulas.accuracy_Calc(new_max_raw_critdamage, new_max_raw_damage, new_min_raw_damage, newpos);
            if (new_max_damage >= 1 && !checked) { //if you can already deal damage to the next mob
                str5 = ("You can deal **" + (int)(new_max_damage) + "** max damage to **" + mobs[newpos].getMob_name() + mobs[newpos].getEmoji_code() + "**!"); //part of output
                alrdealdamage = true;
            }
            else if (new_max_damage > 1 && !alrdealdamage && !dealdamage) { //if you cant deal damage to the next mob yet, you can deal damage in a certain amount of stats!
                str5 = ("You can deal **" + (int)(new_max_damage) + "** max damage to **" + mobs[newpos].getMob_name() + mobs[newpos].getEmoji_code() +"** in **" + statadd + "** stats!"); //part of output
                dealdamage = true;
            }
            checked = true;
            statadd++;
        }
        if (checknextmob) {
            str1 = "Max. Damage: **" + (int)max_damage + "** " + slime_lord_emoji + " Tickrate: **" + (int)tickrate + " / 3600**\n";
            str2 = "Average time to kill **" + mobs[pos].getMob_name() + mobs[pos].getEmoji_code() + "**: **" + (int)time/60 + "** min. **" + (int)time%60 + "** sec.\n";
            str4 = "You need **" + statadd + "** stats to train effectively on **" + mobs[newpos].getMob_name() + mobs[newpos].getEmoji_code() + "**!\n";
        }
        else {
            str1 = "Min. Damage (Auto): **" + (int)min_damage + "** " + slime_lord_emoji + " Max. Damage (Auto): **" + (int)max_damage + "**\n";
            str2 = "Average time to kill **" + mobs[pos].getMob_name() + mobs[pos].getEmoji_code() + "**: **" + (int)time/60 + "** min. **" + (int)time%60 + "** sec.\n";
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Training Calculation");
        embed.setColor(embedColor);
        embed.appendDescription(header + str0 + str1 + str2 + str3 + str4 + str5);
        event.replyEmbeds(embed.build()).queue();
    }

    public void ptrain(SlashCommandInteractionEvent event, int classtype, int base, int stat, int buff, int weaponatk, int tick) {
        String str0; // You can power train effectively on...
        String str1; // Max Damage... Tickrate...
        String str2; // Average time to kill...
        String str3 = ""; // You need... stats to train effectively on...
        String str4 = ""; // You can deal... max damage to...
        String classEmoji = "";

        int stat1 = stat + buff;
        double max_raw_damage;
        double min_raw_damage;

        if (classtype == 2) {
            min_raw_damage = Formulas.special_magic_min_raw_damage_Calc(stat1, weaponatk, base);
            max_raw_damage = Formulas.special_magic_max_raw_damage_Calc(stat1, weaponatk, base);
            classEmoji = "Magic :fire:";
        } else {
            min_raw_damage = Formulas.special_meldist_min_raw_damage_Calc(stat1, weaponatk, base);
            max_raw_damage = Formulas.special_meldist_max_raw_damage_Calc(stat1, weaponatk, base);
            if (classtype == 0){
                classEmoji = "Melee :crossed_swords:";
            } else {
                classEmoji = "Distance :bow_and_arrow:";}
        }
        String header = "Base: **" + base + "** " + slime_lord_emoji + " Stat: **" + stat + "** " + slime_lord_emoji + " Buffs: **+" + buff + "** " + slime_lord_emoji + " Weapon: **" + weaponatk + " Atk ** " + slime_lord_emoji + " Tick: **" + tick + "**\n";

        double max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
        double accuracy = 0;
        int pos = 0;
        double threshold = Formulas.threshold_Calc(tick);

        //Iterate through loop until you find a mob you can power train on with greater than .1749 accuracy
        for (int x = mobs.length-1; x >= 0; x--) {
            if (x == 13 || x == 19 || x == 20 || x == 22 || x == 24 || x == 25 || x == 26 || x == 29 || x == 31 || x == 33 || x == 36 || x == 37 || x >= 39) {continue;}
            accuracy = Formulas.accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, x);
            if (accuracy >= threshold) {
                pos = x;
                break;
            }
        }

        //Calculate average damage which you need for average time to kill
        double min_damage = Formulas.min_damage_Calc(min_raw_damage, pos);
        double max_damage = Formulas.max_damage_Calc(max_raw_damage, pos);
        double max_crit_damage = Formulas.max_crit_damage_Calc(max_raw_crit_damage, pos);
        double avgdmg = Formulas.average_damage_Calc(accuracy, max_damage, min_damage, max_crit_damage);
        double totalaccuracy = Formulas.total_accuracy_Calc(accuracy, tick);
        double maxtickrate = Formulas.max_tickrate_Calc(tick);
        double powertickrate = Formulas.powertickrate_Calc(totalaccuracy, maxtickrate);
        double time = Formulas.time_to_kill_Calc(avgdmg, pos);

        str0 = "You can power train ** " + classEmoji + "** on  **" + mobs[pos].getMob_name() + mobs[pos].getEmoji_code() + "**!\n";

        //calculating stats you need to train on the next mob

        int statadd = 0;
        boolean checked = false;
        boolean alrdealdamage = false;
        boolean dealdamage = false;
        double new_max_damage;
        double newaccuracy = 0;
        int newpos = pos + 1;
        while (true) {
            if (newpos == 13 || newpos == 19 || newpos == 20 || newpos == 22 || newpos == 24 || newpos == 25 || newpos == 26 || newpos == 29 || newpos == 31 || newpos == 33 || newpos == 36 || newpos == 37) {
                newpos++;
            }
            else if (newpos >= 38) {
                newpos = 38;
                break;
            }
            else {
                break;
            }
        }
        boolean checknextmob = pos != 38;

        while (newaccuracy < threshold && checknextmob) {
            int statneeded = stat1 + statadd;

            double new_max_raw_damage;
            double new_min_raw_damage;

            if (classtype == 2) {
                new_min_raw_damage = Formulas.special_magic_min_raw_damage_Calc(statneeded, weaponatk, base);
                new_max_raw_damage = Formulas.special_magic_max_raw_damage_Calc(statneeded, weaponatk, base);
            } else {
                new_min_raw_damage = Formulas.special_meldist_min_raw_damage_Calc(statneeded, weaponatk, base);
                new_max_raw_damage = Formulas.special_meldist_max_raw_damage_Calc(statneeded, weaponatk, base);
            }

            double new_max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(new_max_raw_damage);

            new_max_damage = Formulas.max_damage_Calc(new_max_raw_damage, newpos);

            newaccuracy = Formulas.accuracy_Calc(new_max_raw_crit_damage, new_max_raw_damage, new_min_raw_damage, newpos);
            if (new_max_damage >= 1 && !checked) { //if you can already deal damage to the next mob
                str4 = ("You can deal **" + (int)(new_max_damage) + "** max damage to **" + mobs[newpos].getMob_name() + mobs[newpos].getEmoji_code() + "**!"); //part of output
                alrdealdamage = true;
            }
            else if (new_max_damage > 1 && !alrdealdamage && !dealdamage) { //if you cant deal damage to the next mob yet, you can deal damage in a certain amount of stats!
                str4 = ("You can deal **" + (int)(new_max_damage) + "** max damage to **" + mobs[newpos].getMob_name() + mobs[newpos].getEmoji_code() +"** in **" + statadd + "** stats!"); //part of output
                dealdamage = true;
            }
            checked = true;
            statadd++;
        }

        //Building remaining Strings
        if (checknextmob) {
            str1 = "Max. Damage: **" + (int)max_damage + "** " + slime_lord_emoji + " Tickrate: **" + (int)powertickrate + " / " + (int)maxtickrate + "**\n";
            str2 = "Average time to kill **" + mobs[pos].getMob_name() + mobs[pos].getEmoji_code() + "**: **" + (int)time/60 + "** min. **" + (int)time%60 + "** sec.\n";
            str3 = "You need **" + statadd + "** stats to power train effectively on **" + mobs[newpos].getMob_name() + mobs[newpos].getEmoji_code() + "**!\n";
        }
        else {
            str1 = "Min. Damage (Auto): **" + (int)min_damage + "** " + slime_lord_emoji + " Max. Damage (Auto): **" + (int)max_damage + "**\n";
            str2 = "Average time to kill **" + mobs[pos].getMob_name() + mobs[pos].getEmoji_code() + "**: **" + (int)time/60 + "** min. **" + (int)time%60 + "** sec.\n";
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Power Training Calculation");
        embed.setColor(embedColor);
        embed.appendDescription(header + str0 + str1 + str2 + str3 + str4);
        event.replyEmbeds(embed.build()).queue();
    }

    public void dmg(SlashCommandInteractionEvent event, int attacktype, int mob, int base, int stat, int buff, int weaponatk) {
        String header = "Base: **" + base + "** " + slime_lord_emoji + " Stat: **" + stat + "** " + slime_lord_emoji + " Buffs: **+" + buff + "** " + slime_lord_emoji + " Weapon: **" + weaponatk + " Atk **\n";
        String str0;
        String str1;
        String str2;

        String attacktypestring = "";

        int stat1 = stat + buff;
        double max_raw_damage;
        double min_raw_damage;

        if (attacktype == 0) {
            min_raw_damage = Formulas.auto_min_raw_damage_Calc(stat1, weaponatk, base);
            max_raw_damage = Formulas.auto_max_raw_damage_Calc(stat1, weaponatk, base);
            attacktypestring = "Auto";
        } else if (attacktype == 3) {
            min_raw_damage = Formulas.special_magic_min_raw_damage_Calc(stat1, weaponatk, base);
            max_raw_damage = Formulas.special_magic_max_raw_damage_Calc(stat1, weaponatk, base);
            attacktypestring = "Special :fire:";
        } else {
            min_raw_damage = Formulas.special_meldist_min_raw_damage_Calc(stat1, weaponatk, base);
            max_raw_damage = Formulas.special_meldist_max_raw_damage_Calc(stat1, weaponatk, base);
            if (attacktype == 1){
                attacktypestring = "Special :crossed_swords:";
            } else {
                attacktypestring = "Special :bow_and_arrow:";
            }
        }

        double max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
        double min_damage = Formulas.min_damage_Calc(min_raw_damage, mob);
        double max_damage = Formulas.max_damage_Calc(max_raw_damage, mob);
        double max_crit_damage = Formulas.max_crit_damage_Calc(max_raw_crit_damage, mob);

        double normalaccuracy = Formulas.normal_accuracy_Calc(max_raw_damage, min_raw_damage, mob);
        double critaccuracy = Formulas.crit_accuracy_Calc(max_raw_crit_damage, max_raw_damage, mob);

        str0 = "Mob: **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "**\n";

        if (normalaccuracy == 1.00) {
            str1 = "Min. Damage (" + attacktypestring + "): **" + (int)min_damage + "** " + slime_lord_emoji + " Max. Damage (" + attacktypestring + "): **" + (int)max_damage + "**\n";
        } else if (normalaccuracy > 0) {
            str1 = "Max. Damage (" + attacktypestring + "): **" + (int)max_damage + "**\n";
        } else {
            str1 = "You aren't strong enough to deal normal damage to this mob.";
        }
        if (critaccuracy > 0) {
            str2 = "Maximum Critical Damage (" + attacktypestring + "): **" + (int)max_crit_damage + "**\n";
        } else {
            str2 = "You aren't strong enough to deal critical damage to this mob.";
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(attacktypestring + " Damage Calculation");
        embed.setColor(embedColor);
        embed.appendDescription(header + str0 + str1 + str2);
        event.replyEmbeds(embed.build()).queue();
    }
    
    public void weapon(SlashCommandInteractionEvent event, int attacktype, int mob, int base, int stat, int buff) {
        String header = "Mob: **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code()  + slime_lord_emoji + "** Base: **" + base + "** " + slime_lord_emoji + " Stat: **" + stat + "** " + slime_lord_emoji + " Buffs: **+" + buff + "** \n";
        String[] attackstrings;
        String str0;
        String str1;
        String str2;
        String str3 = "";

        int tick = 1;
        double min_raw_damage = 0;
        double max_raw_damage = 0; 
        double max_raw_crit_damage = 0;
        double accuracy = 0;

        double threshold;
        if (attacktype == 0){ //Auto
            threshold = 0.1749;
            attackstrings = new String[]{"(Auto)", "train"};
        } else if (attacktype == 1){ //Melee 
            tick = 4;
            threshold = Formulas.threshold_Calc(tick);
            attackstrings = new String[]{"(Spec)", "power train **Melee :crossed_swords:**"};
        } else if (attacktype == 2){ //Distance
            tick = 4;
            threshold = Formulas.threshold_Calc(tick);
            attackstrings = new String[]{"(Spec)", "power train **Distance :bow_and_arrow:**"};
        } else { //Magic
            tick = 4;
            threshold = Formulas.threshold_Calc(tick);
            attackstrings = new String[]{"(Spec)", "power train **Magic :fire:**"};
        }
        //An index variable for weapons[]
        int pos = 0;

        //Iterate through loop until you find a mob you can train on with greater than .1749 accuracy
        for (int x = 0; x < weapons.length; x++) {
            int stat1 = stat + buff + weapons[x].getWeapon_buffs();
            if (attacktype == 0){ //Auto
                min_raw_damage = Formulas.auto_min_raw_damage_Calc(stat1,weapons[x].getWeapon_attack(),base);
                max_raw_damage = Formulas.auto_max_raw_damage_Calc(stat1,weapons[x].getWeapon_attack(),base);
                max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
                accuracy = Formulas.accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, mob);
            } else if (attacktype == 1){ //Melee 
                min_raw_damage = Formulas.special_meldist_min_raw_damage_Calc(stat1,weapons[x].getWeapon_attack(),base);
                max_raw_damage = Formulas.special_meldist_max_raw_damage_Calc(stat1,weapons[x].getWeapon_attack(),base);
                max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
                accuracy = Formulas.accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, mob);
            } else if (attacktype == 2){ //Distance
                min_raw_damage = Formulas.special_meldist_min_raw_damage_Calc(stat1,weapons[x].getWeapon_attack(),base);
                max_raw_damage = Formulas.special_meldist_max_raw_damage_Calc(stat1,weapons[x].getWeapon_attack(),base);
                max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
                accuracy = Formulas.accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, mob);
            } else { //Magic
                min_raw_damage = Formulas.special_magic_min_raw_damage_Calc(stat1,weapons[x].getWeapon_attack(),base);
                max_raw_damage = Formulas.special_magic_max_raw_damage_Calc(stat1,weapons[x].getWeapon_attack(),base);
                max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
                accuracy = Formulas.accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, mob);
            }
            if (accuracy >= threshold) {
                pos = x;
                break;
            }
        }
        int stat2 = stat + buff + weapons[pos].getWeapon_buffs();
        
        double min_damage = Formulas.min_damage_Calc(min_raw_damage, mob);
        double max_damage = Formulas.max_damage_Calc(max_raw_damage, mob);
        double max_crit_damage = Formulas.max_crit_damage_Calc(max_raw_crit_damage, mob);
        double avgdmg = Formulas.average_damage_Calc(accuracy, max_damage, min_damage, max_crit_damage);
        double maxtickrate = Formulas.max_tickrate_Calc(tick);

        double alltickrate;
        if (attacktype == 0){
            alltickrate = Formulas.tickrate_Calc(accuracy, 3600);
        } else {
            double totalaccuracy = Formulas.total_accuracy_Calc(accuracy, tick);
            alltickrate = Formulas.powertickrate_Calc(totalaccuracy, maxtickrate);
        }

        double time = Formulas.time_to_kill_Calc(avgdmg, mob);
        str0 = "You can " + attackstrings[1] + " effectively on **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "** with a **" + weapons[pos].getWeapon_name() + weapons[pos].getEmoji_code() + "**!\n";
        str2 = "Average time to kill **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "**: **" + (int)time/60 + "** min. **" + (int)time%60 + "** sec.\n";
        
        int statadd = 0;
        double newaccuracy = 0;
        boolean checknextweapon = true;
        if (pos >= 33 || pos <= 0){
            checknextweapon = false;
        }
        while (newaccuracy < threshold && checknextweapon) {
            int statneeded = stat2 + statadd;

            double new_min_raw_damage = Formulas.auto_min_raw_damage_Calc(statneeded, weapons[pos-1].getWeapon_attack(), base);
            double new_max_raw_damage = Formulas.auto_max_raw_damage_Calc(statneeded, weapons[pos-1].getWeapon_attack(), base);

            double new_max_raw_critdamage = Formulas.max_raw_crit_damage_Calc(new_max_raw_damage);

            newaccuracy = Formulas.accuracy_Calc(new_max_raw_critdamage, new_max_raw_damage, new_min_raw_damage, mob);
            statadd++;
        }
        if (checknextweapon) {
            str1 = "Max. Damage " + attackstrings[0] + ": **" + (int)max_damage + "** " + slime_lord_emoji + " Tickrate: **" + (int)alltickrate + " / " + (int)maxtickrate + "**\n";
            str3 = "You need **" + statadd + "** stats to " + attackstrings[1] + " effectively on **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "** with a **" + weapons[pos-1].getWeapon_name() + weapons[pos].getEmoji_code() + "**!\n";
        }
        else {
            str1 = "Min. Damage " + attackstrings[0] + ": **" + (int)min_damage + "** " + slime_lord_emoji + " Max. Damage " + attackstrings[0] + ": **" + (int)max_damage + "**\n";
        }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Weapon Calculation");
        embed.setColor(embedColor);
        embed.appendDescription(header + str0 + str1 + str2 + str3);
        event.replyEmbeds(embed.build()).queue();
    }

    public void offline(SlashCommandInteractionEvent event, int stat1, int stat2, int hours) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Stat Calculation");
        embed.setColor(embedColor);
        if (stat2 > 0 && hours <= 0) {
            if (stat1 > stat2) {
                event.reply("Stat2 must be greater than Stat1").setEphemeral(true).queue();
            }
            double ticks1;
            double ticks2;
            if (stat1 <= 54) {
                ticks1 = Formulas.stat0to54_Calc(stat1);
            } else { 
                ticks1 = Formulas.stat55to99_Calc(stat1);
            } 

            if (stat2 <= 54) {
                ticks2 = Formulas.stat0to54_Calc(stat2);
            } else {
                ticks2 = Formulas.stat55to99_Calc(stat2);
            }
    
            double totalticks = ticks2 - ticks1;
            embed.appendDescription("Initial Stat: ** " + String.format("%,d", stat1) + " **" + slime_lord_emoji +" Goal Stat: **" + String.format("%,d", stat2) + "**\n" +
                    "You need approximately** " + String.format("%,.0f", totalticks) + "** ticks until you reach stat level **" + String.format("%,d", stat2) + "**!\n" +
                    "This is around **" + String.format("%,.1f", totalticks*60/600) + "** minutes, or **" + String.format("%,.1f", totalticks/600) + "** hours of offline training at **600** exp/hr");
        } else if (hours > 0 && stat2 <= 0) {
            int tickstrained = 600 * hours;
            double ticks1;
            double ticks2;
            if (stat1 <= 54) {
                ticks1 = Formulas.stat0to54_Calc(stat1);
            } else {
                ticks1 = Formulas.stat55to99_Calc(stat1);
            } 

            ticks2 = tickstrained + ticks1;
            double newStat = Math.round(100.0*Formulas.findStatLevel_Calc(ticks2))/100.0;
            if (newStat < 5){
                event.reply("Something went wrong: Could not find new stat").setEphemeral(true).queue();
            }
            embed.appendDescription("Initial Stat: ** " + String.format("%,d", stat1) + " **" + slime_lord_emoji +" Hours: **" + String.format("%,d", hours) + "**\n" +
                    "Your new stat will be approximately: **" + newStat + "** with **" + hours + "** hours of offline training"
            );
        } else {
            embed.appendDescription("Something went wrong: Please enter either hours OR stat2");
        }
        
        event.replyEmbeds(embed.build()).queue();
    }

    public void oneshot(SlashCommandInteractionEvent event, int attacktype, int mob, int base, int stat, int buff, int weaponatk, double consistency) {
        String header;
        if (stat >= 5) {
            header = "Base: **" + base + "** " + slime_lord_emoji + " Stat: **" + stat + "** " + slime_lord_emoji + " Buffs: **+" + buff + "** " + slime_lord_emoji + " Weapon: **" + weaponatk + " Atk** " + slime_lord_emoji + " Consistency: **" + (int)consistency + "%** \n";
        } else {
            header = "Base: **" + base + "** " + slime_lord_emoji + " Weapon: **" + weaponatk + " Atk** " + slime_lord_emoji + " Consistency: **" + (int)consistency + "%** \n";
        }

        String mobInfo = "Mob: **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "** Health: **" + mobs[mob].getMob_health() + "**\n";
        String str0 = ""; // You already oneshot this mob with ***% consistency, or You cannot oneshot this mob yet, DOES NOT CALCULATE IF STAT IS 0
        String str1 = ""; // If the % consistency you can oneshot is already over consistency provided, string blank, else, say You need *** stats to oneshot this mob with ***% consistency
        String str2 = ""; // Min Damage and Max Damage
        String str3 = ""; // Critical Damage
        double currentConsistency = 0; // will be 0 if stat not provided

        String[] attackstrings;

        double min_raw_damage = 0;
        double max_raw_damage = 0; 
        double max_raw_crit_damage = 0;

        if (attacktype == 0){ // Auto
            attackstrings = new String[]{"(Auto)", "**Auto Attack**"};
        } else if (attacktype == 1){ // Melee 
            attackstrings = new String[]{"(Special :crossed_swords:)", "**Melee Special :crossed_swords:**"};
        } else if (attacktype == 2){ // Distance
            attackstrings = new String[]{"(Special :bow_and_arrow:)", "**Distance Special :bow_and_arrow:**"};
        } else { // Magic
            attackstrings = new String[]{"(Special :fire:)", "**Magic Special :fire:**"};
        }

        if (stat >= 5) { // stat inputted
            int stat1 = stat + buff;
            if (attacktype == 0){ // Normal
                min_raw_damage = Formulas.auto_min_raw_damage_Calc(stat1, weaponatk, base);
                max_raw_damage = Formulas.auto_max_raw_damage_Calc(stat1, weaponatk, base);
                max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
            } else if (attacktype == 3){ // Magic
                min_raw_damage = Formulas.special_magic_min_raw_damage_Calc(stat1, weaponatk, base);
                max_raw_damage = Formulas.special_magic_max_raw_damage_Calc(stat1, weaponatk, base);
                max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
            } else { // Melee and Distance
                min_raw_damage = Formulas.special_meldist_min_raw_damage_Calc(stat1, weaponatk, base);
                max_raw_damage = Formulas.special_meldist_max_raw_damage_Calc(stat1, weaponatk, base);
                max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(max_raw_damage);
            }

            //Calculate current consistency
            currentConsistency = Formulas.consistency_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, mob);
            if (currentConsistency > 0) {
                str0 = "You **can** already one-shot a **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "** with " + attackstrings[1] + " at **" + (int)(currentConsistency*100) + "%** consistency!\n";
            } else {
                str0 = "You **cannot** one-shot a **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "** with " + attackstrings[1] + " yet!\n";
            }

            double min_damage = Formulas.min_damage_Calc(min_raw_damage, mob);
            double max_damage = Formulas.max_damage_Calc(max_raw_damage, mob);
            double max_crit_damage = Formulas.max_crit_damage_Calc(max_raw_crit_damage, mob);
    
            double normalaccuracy = Formulas.normal_accuracy_Calc(max_raw_damage, min_raw_damage, mob);
            double critaccuracy = Formulas.crit_accuracy_Calc(max_raw_crit_damage, max_raw_damage, mob);
    
            if (normalaccuracy == 1.00) {
                str1 = "Min. Damage " + attackstrings[0] + ": **" + (int)min_damage + "** " + slime_lord_emoji + " Max. Damage " + attackstrings[0] + ": **" + (int)max_damage + "**\n";
            } else if (normalaccuracy > 0) {
                str1 = "Max. Damage " + attackstrings[0] + ": **" + (int)max_damage + "**\n";
            } else {
                str1 = "You aren't strong enough to deal normal damage to this mob!\n";
            }
            if (critaccuracy > 0) {
                str2 = "Maximum Critical Damage " + attackstrings[0] + ": **" + (int)max_crit_damage + "**\n";
            } else {
                str2 = "You aren't strong enough to deal critical damage to this mob!\n";
            }
        }

        int statneeded = 5;
        boolean statFound = false;
        if (currentConsistency*100 < consistency) {
            while (!statFound && statneeded < 1000) {
                double new_min_raw_damage;
                double new_max_raw_damage;
                double new_max_raw_crit_damage;
                if (attacktype == 0){ // Normal
                    new_min_raw_damage = Formulas.auto_min_raw_damage_Calc(statneeded, weaponatk, base);
                    new_max_raw_damage = Formulas.auto_max_raw_damage_Calc(statneeded, weaponatk, base);
                    new_max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(new_max_raw_damage);
                } else if (attacktype == 3){ // Magic
                    new_min_raw_damage = Formulas.special_magic_min_raw_damage_Calc(statneeded, weaponatk, base);
                    new_max_raw_damage = Formulas.special_magic_max_raw_damage_Calc(statneeded, weaponatk, base);
                    new_max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(new_max_raw_damage);
                } else { // Melee and Distance
                    new_min_raw_damage = Formulas.special_meldist_min_raw_damage_Calc(statneeded, weaponatk, base);
                    new_max_raw_damage = Formulas.special_meldist_max_raw_damage_Calc(statneeded, weaponatk, base);
                    new_max_raw_crit_damage = Formulas.max_raw_crit_damage_Calc(new_max_raw_damage);
                }
                if (Formulas.consistency_Calc(new_max_raw_crit_damage, new_max_raw_damage, new_min_raw_damage, mob)*100 >= consistency) {
                    statFound = true;
                } else {
                    statneeded++;
                }
            }
            if (!statFound) {
                str3 = "We could not find the necessary stat level for you to one-shot a **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "** because it is too high!\n"; //could find appropriate stat
            } else {
                str3 = "You need stat **level " + statneeded + "** to one-shot a **" + mobs[mob].getMob_name() + mobs[mob].getEmoji_code() + "** with **" + (int)consistency + "%** consistency\n";
            }
        }
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(attackstrings[1] + " One-shot Calculation");
        embed.setColor(embedColor);
        embed.appendDescription(header + mobInfo + str0 + str1 + str2 + str3);
        event.replyEmbeds(embed.build()).queue();
    }

    private void updateLogs(String command) {
        try {
            // input the (modified) file content to the StringBuffer "input"
            BufferedReader file = new BufferedReader(new FileReader("./commands.log"));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            boolean found = false;
            while ((line = file.readLine()) != null) {
                if (line.contains(command) && !found) {
                    int number= Integer.parseInt(line.substring(command.length() + 2));
                    line= command + ": " + (number + 1);
                    found = true;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream("./commands.log");
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        } catch(Exception e) {  
            e.printStackTrace();  
        }  
    }
}
