package bot;

public class Mob {
    private final String mob_name;
    private final String emoji_code;
    private final int mob_defense;
    private final int mob_health;

    public Mob(String name, String emojicode, int defense, int health){
        this.mob_name = name;
        this.emoji_code = emojicode;
        this.mob_defense = defense;
        this.mob_health = health;
    }

    public String getMob_name(){
        return mob_name;
    }

    public String getEmoji_code(){
        return emoji_code;
    }

    public int getMob_defense(){
        return mob_defense;
    }

    public int getMob_health(){
        return mob_health;
    }
}
