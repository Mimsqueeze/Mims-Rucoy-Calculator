package bot;

public class Formulas {
    public static double auto_min_raw_damage_Calc(double stat, double weaponatk, double base){
        return(stat * weaponatk)/20 + (base)/4;
    }
    public static double auto_max_raw_damage_Calc(double stat, double weaponatk, double base){
        return(stat * weaponatk)/10 + (base)/4;
    }
    public static double special_meldist_min_raw_damage_Calc(double stat, double weaponatk, double base){
        return 1.5*((stat * weaponatk)/20 +(base)/4);
    }
    public static double special_meldist_max_raw_damage_Calc(double stat, double weaponatk, double base){
        return 1.5*((stat * weaponatk)/10 +(base)/4);
    }
    public static double special_magic_min_raw_damage_Calc(double stat, double weaponatk, double base){
        return 1.5*(((1.05*(stat) * weaponatk)/20) + 9*(base)/32);
    }
    public static double special_magic_max_raw_damage_Calc(double stat, double weaponatk, double base){
        return 1.5*(((1.05*(stat) * weaponatk)/10) + 9*(base)/32);
    }
    public static double min_damage_Calc(double min_raw_damage, int pos){
        double min_damage = min_raw_damage - Bot.mobs[pos].getMob_defense();
        if (min_damage < 0){
            min_damage = 0;
        }
        return min_damage;
    }
    public static double max_damage_Calc(double max_raw_damage, int pos){
        return max_raw_damage - Bot.mobs[pos].getMob_defense();
    }
    public static double max_raw_crit_damage_Calc(double max_raw_damage){
        return max_raw_damage*1.05;
    }
    public static double max_crit_damage_Calc(double max_raw_crit_damage, int pos){
        return max_raw_crit_damage - Bot.mobs[pos].getMob_defense();
    }
    public static double normal_accuracy_Calc(double max_raw_damage, double min_raw_damage, int x){
        double normalaccuracy = (max_raw_damage-Bot.mobs[x].getMob_defense())/(max_raw_damage - min_raw_damage);
        if (normalaccuracy > 1.00){
            normalaccuracy = 1.00;
        }
        return normalaccuracy;
    }
    public static double crit_accuracy_Calc(double max_raw_crit_damage, double max_raw_damage, int x){
        double critaccuracy = (max_raw_crit_damage-Bot.mobs[x].getMob_defense())/(max_raw_crit_damage - max_raw_damage);
        if (critaccuracy > 1.00){
            critaccuracy = 1.00;
        }
        return critaccuracy;
    }
    public static double accuracy_Calc(double max_raw_crit_damage, double max_raw_damage, double min_raw_damage, int x){
        return (normal_accuracy_Calc(max_raw_damage, min_raw_damage, x)*0.99) + (crit_accuracy_Calc(max_raw_crit_damage, max_raw_damage, x)*0.01);
    }
    public static double total_accuracy_Calc(double accuracy, double tick){
        return 1.0 - Math.pow(Math.pow(1.0-accuracy,tick),10);
    }
    public static double time_to_kill_Calc(double avgdmg, int pos){
        return Bot.mobs[pos].getMob_health() / avgdmg;
    }
    public static double average_damage_Calc(double accuracy, double max_damage, double min_damage, double max_crit_damage){
        return (accuracy)*(.99*((max_damage + min_damage)/2)) + 0.01*((max_crit_damage + max_damage)/2);
    }
    public static double tickrate_Calc(double accuracy, double maxtickrate){
        return maxtickrate*(1.0 - Math.pow(1.0-accuracy,10.0));
    }
    public static double powertickrate_Calc(double totalaccuracy, double maxtickrate){
        return maxtickrate*totalaccuracy;
    }
    public static double max_tickrate_Calc(double tick){
        if (tick <= 5) {
            return tick * 3600;
        } else {
            return 18000;
        }
    }
    public static double exp_Calc(double base) {
        return Math.pow(base, (base / 1000) + 3);
    }
    public static double stat0to54_Calc(double stat){
        return Math.pow(stat, (stat/1000) + 2.373);
    }
    public static double stat55to99_Calc(double stat){
        // final double offset = 4692.687;
        // double adjustedStat = stat + 15.68952;
        // return Math.pow(adjustedStat, (adjustedStat/1000) + 2.272) - offset;
        return Math.pow(stat, (stat/1000) + 2.171);
    }
    /*
    public static double stat100to599_Calc(double stat){
        final double offset = 22516.303;
        double adjustedStat = stat + 45.43406;
        // return Math.pow(adjustedStat, (adjustedStat/1000) + 2.171) - offset;
        return Math.pow(stat, (stat/1000) + 2.373);
    }
    public static double stat600plus_Calc(double stat){
        final double offset = 2766484;
        double adjustedStat = stat + 110.34322;
        return Math.pow(adjustedStat, (adjustedStat/1000) + 2.070) - offset;
    }
    */
    public static double findStatLevel_Calc(double ticks2){
        if (ticks2 <= stat0to54_Calc(54)){
            for (int stat = 5; stat <= 54; stat++){
                if (ticks2 <= stat0to54_Calc(stat)){
                    double fract = (ticks2 - stat0to54_Calc(stat-1))/(stat0to54_Calc(stat) - stat0to54_Calc(stat-1));
                    return (stat-1) + fract;
                }
            }
        } else { // if (ticks2 <= stat55to99_Calc(99)){
            for (int stat = 55; stat <= 1000; stat++){ // 99; stat++){
                if (ticks2 <= stat55to99_Calc(stat)){
                    double fract = (ticks2 - stat55to99_Calc(stat-1))/(stat55to99_Calc(stat) - stat55to99_Calc(stat-1));
                    return (stat-1) + fract;
                }
            }
        }
        /*
        else if (ticks2 <= stat100to599_Calc(599)) {
            for (int stat = 100; stat <= 599; stat++){
                if (ticks2 <= stat100to599_Calc(stat)){
                    double fract = (ticks2 - stat100to599_Calc(stat-1))/(stat100to599_Calc(stat) - stat100to599_Calc(stat-1));
                    return (stat-1) + fract;
                }
            }
        } else {
            for (int stat = 600; stat <= 1000; stat++){
                if (ticks2 <= stat600plus_Calc(stat)){
                    double fract = (ticks2 - stat600plus_Calc(stat-1))/(stat600plus_Calc(stat) - stat600plus_Calc(stat-1));
                    return (stat-1) + fract;
                }
            }
        }
        */
        return -1;
    }
    public static double threshold_Calc(double tick){
        return 1.0 - Math.pow(.8251,(1.0/tick));
    }
    public static double consistency_Calc(double max_raw_crit_damage, double max_raw_damage, double min_raw_damage, int mob) {
        int health = Bot.mobs[mob].getMob_health();
        int defense = Bot.mobs[mob].getMob_defense();
        int totaldefense = health + defense;

        if (totaldefense - max_raw_crit_damage > 0) {
            return 0;
        }

        double range = max_raw_damage - min_raw_damage;
        double normaloneshots = max_raw_damage - totaldefense;
        if (normaloneshots > 0) {
            double normalconsistency = (normaloneshots/range);
            return normalconsistency*0.99 + 0.01;
        } else {
            double critrange = max_raw_crit_damage - max_raw_damage;
            double criticaloneshots = max_raw_crit_damage - totaldefense;
            return (criticaloneshots/critrange)*0.01;
        }
    }
}
