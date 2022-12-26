package bot;

public class Weapon {
    private final String weapon_name;
    private final String[] emoji_codes;
    private final int weapon_atk;
    private final int weapon_buffs;

    public Weapon(String name, String melee_emoji, String distance_emoji, String magic_emoji, int attack, int buff){
        this.weapon_name = name;
        this.weapon_atk = attack;
        this.weapon_buffs = buff;

        emoji_codes = new String[3];
        this.emoji_codes[0] = melee_emoji;
        this.emoji_codes[1] = distance_emoji;
        this.emoji_codes[2] = magic_emoji;
    }

    public String getWeapon_name(){
        return weapon_name;
    }

    public String getEmoji_code(int classtype){
        return emoji_codes[classtype];
    }

    public String getEmoji_code(){
        return emoji_codes[0] + emoji_codes[1] + emoji_codes[2];
    }

    public int getWeapon_attack(){
        return weapon_atk;
    }

    public int getWeapon_buffs(){
        return weapon_buffs;
    }
}
