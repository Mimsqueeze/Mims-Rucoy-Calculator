# Mims-Rucoy-Calculator
A Public Discord Bot for the Rucoy Online Community on Discord! Utilizes the new Slash Command Interactions.
## Table of Contents
- [Getting Started](#Getting-Started)
- [Running the Project](#Running-the-Project)
- [Instructions of Use](#Instructions-of-Use)
- [Command List](#Command-List)
- [Credits and Acknowledgements](#Credits-and-Acknowledgements)

## Getting Started
To get started using this Discord Bot, you first need to be in a Discord Server with the Bot. If you are not in a Discord Server with the bot, simply create a new Discord server and invite the Bot by [**clicking this link**](https://discord.com/api/oauth2/authorize?client_id=758831061596635136&permissions=139586754624&scope=applications.commands%20bot).

Alternatively, if you want to run your own version of the Bot, you will need to clone the repository, create a new Application through the [**Discord Developer Portal**](https://discord.com/developers/applications), create a new Bot in the Bot tab, and copy paste the token into the Token.env file. Then, simply run the Main.java file in the src/bot folder your Discord Bot should now be running. Then, you can generate an invite link through the URL Generator in the OAuth2 tab to invite it to your server. 

## Instructions of Use
With the new [**Slash Command Update**](https://support.discord.com/hc/en-us/articles/1500000368501-Slash-Commands-FAQ) the prefix has been changed to`/`. Therefore, to use the bot, start by typing a slash `/` into the message box, and a list of comands should show up from the different bots your server. You can filter out the commands from this Bot by selecting the **Mims Rucoy Calculator** icon on the left. Then, simply select the desired command to execute and fill in the required parameters. (Filling in optional parameters may change the output of the command).  The command list is below, and can also be found in the `/help` command.

## Command List
Note: Parameters marked with an astericks **(\*)** are optional parameters, some of which are given default values. The rest are required parameters for the command. `buffs` default will always be 0.

#### /train [base] [stat] [buffs]\* [weapon atk]\*
Calculates the mob that you can train effectively on.
`weapon atk` default is 5.

#### /ptrain [class] [base] [stat] [buffs]\* [weapon atk]\* [ticks]\*
Calculates the mob that you can power-train effectively on.
`weapon atk` default is 5. `ticks` default is 3.

#### /moblist
Shows the list of mob IDs.

#### /oneshot [attacktype] [mobID] [base] [weaponatk] [stat]\* [buffs]\* [consistency]\*
Calculates whether you already one-shot a mob if a stat is given, or the stat level needed to one-shot a certain mob.
`consistency` default is 80%.
Do /moblist for the list of mob IDs.

#### /weapon [attacktype] [mobID] [base] [stat] [buffs]\*
Calculates the weapon needed to train on a certain mob.
Do /moblist for the list of mob IDs.

#### /dmg [attacktype] [mobID] [base] [stat] [buffs]\* [weapon atk]
Calculates the damage you do to certain mobs.
Do /moblist for the list of mob IDs.

#### /stat [trainingmethod] [stat1] [stat2] [statrate]\*
Calculates the time and amount of experience needed to reach a certain stat level.
`statrate` default is 3600 exp/hour.

#### /offline [stat1] [stat2]\* [hours]\*
Can only take **either** `stat2` or `hours`. If provided `stat2`, calculates the number of hours needed to reach the `stat2` stat from offline training. If provided `hours`, calculates the resulting stat level from `hours` hours of offline training. 

#### /exp [base]
Calculates the experience at a certain base level.

#### /grind [base1] [base2] [grindrate]\*
Calculates the time and amount of experience needed to reach a certain base level.
`grindrate` default is 2000000.

#### /skull [base]
Calculates the amount of gold needed to skull for a certain base level.

#### /changelog
Shows the changelog.

#### /info
Shows more information about the bot.

#### /invite
Shows the invite link to add the bot to your server.
*Make sure you give the bot ALL of the permissions requested.

#### /github
Displays the github link containing the source code for the bot. Oh, hi there!👋  

## Credits and Acknowledgements
**Special thanks to:**
- Tribrid#6932 for helping to provide some of the formulas
- potatoking#1441 for assisting the development of the bot and the migration to Slash Commands
- Hikari#2056 for being an awesome community organizer
- The Rucoy Community for offering encouragement and giving amazing suggestions that continue to motivate me to work on this project

***Note: If you would like to contact me for whatever reason (questions, suggestions, reporting a bug, etc.), send me a message on discord: *mims#6519.**
