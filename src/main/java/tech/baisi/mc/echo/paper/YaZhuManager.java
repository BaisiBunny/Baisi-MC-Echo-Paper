package tech.baisi.mc.echo.paper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class YaZhuManager implements CommandExecutor {
    final private MySQLManager mySQLManager;
    private boolean running = false;
    private Component helpText;
    private Component hostHelpText;
    private Component errorText;
    private Component startText;
    private Component endText;
    private ArrayList<HashMap<String,Integer>> teams = new ArrayList<>();
    private String[] names = new String[9];
    private HashSet<String> numbers = new HashSet<>();
    private int amount = 2;

    public YaZhuManager(MySQLManager mySQLManager) {
        this.mySQLManager = mySQLManager;

        for(int i = 0; i <= 8; i++){
            teams.add(new HashMap<>());
        }

        helpText = Component.text("使用帮助：").color(TextColor.color(0, 255, 0))
                .appendNewline()
                .append(Component.text("规则：赢家通吃，按下注比例均分奖励。")).color(TextColor.color(0, 255, 0))
                .appendNewline()
                .append(Component.text("/yz [队伍序号(1-8)] [硬币数(>100)] ").color(TextColor.color(255,255,0)))
                .append(Component.text("在某队下注一些硬币，可在不同队伍下注。")).color(TextColor.color(0, 255, 0))
                .appendNewline()
                .append(Component.text("/yz qx ").color(TextColor.color(255,255,0)))
                .append(Component.text("取消所有下注（比赛开始后不可取消）")).color(TextColor.color(0, 255, 0))
                .appendNewline()
                .append(Component.text("/yz host ").color(TextColor.color(255, 93, 105)))
                .append(Component.text("查看主持人的使用帮助")).color(TextColor.color(0, 255, 0));

        hostHelpText = Component.text("主持人 使用帮助 【请勿滥用】").color(TextColor.color(0, 255, 0))
                .appendNewline()
                .append(Component.text("/yz ks ").color(TextColor.color(255, 93, 105)))
                .append(Component.text("【主持人使用】开始比赛，买定离手")).color(TextColor.color(0, 255, 0))
                .appendNewline()
                .append(Component.text("/yz amount [队伍数量(2-8)] ").color(TextColor.color(255, 93, 105)))
                .append(Component.text("【主持人使用】设置总队伍的数量")).color(TextColor.color(0, 255, 0))
                .appendNewline()
                .append(Component.text("/yz name [队伍序号(1-8)] [队伍名称] ").color(TextColor.color(255, 93, 105)))
                .append(Component.text("【主持人使用】设置某队伍的名称")).color(TextColor.color(0, 255, 0))
                .appendNewline()
                .append(Component.text("/yz win [队伍序号(1-8)] ").color(TextColor.color(255, 93, 105)))
                .append(Component.text("【主持人使用】宣布某队伍获胜，结算奖励。")).color(TextColor.color(0, 255, 0));

        errorText = Component.text("指令错误，请输入").append(Component.text(" /yz ").color(TextColor.color(255,255,0))).append(Component.text("查看用法。"));

        startText = Component.text("比赛开始！下注停止").color(TextColor.color(255, 0, 221));

        endText = Component.text("比赛结束！").color(TextColor.color(255, 0, 221));

        numbers.add("1");
        numbers.add("2");
        numbers.add("3");
        numbers.add("4");
        numbers.add("5");
        numbers.add("6");
        numbers.add("7");
        numbers.add("8");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 0){
            commandSender.sendMessage(helpText);
            return true;
        }
        if(args[0].equals("qx")){
            if(running){
                commandSender.sendMessage("比赛已开始，不允许取消。");
                return true;
            }
            String name = commandSender.getName();
            for(int i = 1; i <= 8; i++){
                if(teams.get(i).containsKey(name)){
                    mySQLManager.addMoney(name,teams.get(i).remove(name));
                }
            }
            showInfo();
            return true;
        }
        if(args[0].equals("host")){
            commandSender.sendMessage(hostHelpText);
            return true;
        }
        if(args[0].equals("ks")){
            running = true;
            List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
            if(!players.isEmpty()){
                for(Player p : players){
                    p.sendMessage(startText);
                }
            }
            return true;
        }
        if(args[0].equals("amount")){
            if(args.length != 2){
                commandSender.sendMessage(errorText);
                return true;
            }else {
                int x;
                try {
                    x = Integer.parseInt(args[1]);
                }catch (Exception e){
                    commandSender.sendMessage(errorText);
                    return true;
                }
                if(x < 2 || x > 8){
                    commandSender.sendMessage("队伍数量只能是2-8");
                    return true;
                }else {
                    amount = x;
                    commandSender.sendMessage("队伍数量设置为 "+x);
                    return true;
                }
            }
        }
        if(args[0].equals("name")){
            if (args.length != 3){
                commandSender.sendMessage(errorText);
                return true;
            }else {
                int x;
                try {
                    x = Integer.parseInt(args[1]);
                }catch (Exception e){
                    commandSender.sendMessage(errorText);
                    return true;
                }
                if(x < 1 || x > 8){
                    commandSender.sendMessage("队伍序号只能是1-8");
                    return true;
                }else {
                    names[x] = args[2];
                    commandSender.sendMessage("设置队伍 "+x+" 的名称为 "+names[x]);
                    return true;
                }
            }
        }
        if(args[0].equals("win")){
            if(args.length != 2){
                commandSender.sendMessage(errorText);
                return true;
            }else {
                int x;
                try {
                    x = Integer.parseInt(args[1]);
                }catch (Exception e){
                    commandSender.sendMessage(errorText);
                    return true;
                }
                if(x < 1 || x > 8){
                    commandSender.sendMessage("队伍序号只能是1-8");
                    return true;
                }else {
                    // x win
                    end(x);
                    running = false;
                    return true;
                }
            }
        }
        if(numbers.contains(args[0])){
            if(running){
                commandSender.sendMessage("比赛已开始，不允许下注。");
                return true;
            }
            if(args.length != 2){
                commandSender.sendMessage(errorText);
                return true;
            }else {
                int x;
                int team;
                try {
                    x = Integer.parseInt(args[1]);
                    team = Integer.parseInt(args[0]);
                }catch (Exception e){
                    commandSender.sendMessage(errorText);
                    return true;
                }
                String name = commandSender.getName();
                int money = mySQLManager.getMoney(name);

                HashMap<String,Integer> target = teams.get(team);
                if(x < 100){
                    commandSender.sendMessage("最小下注 100 个硬币");
                    return true;
                }else {
                    if(target.containsKey(name)){
                        if(money+target.get(name)-x >= 0){
                            mySQLManager.addMoney(name,target.get(name)-x);
                            target.replace(name,x);
                            showInfo();
                            return true;
                        }else {
                            commandSender.sendMessage("你只有 "+money+" 个硬币");
                            return true;
                        }
                    }else {
                        if(mySQLManager.getMoney(name)-x >= 0){
                            mySQLManager.addMoney(name,-x);
                            target.put(name,x);
                            showInfo();
                            return true;
                        }
                    }
                }
            }
        }


        commandSender.sendMessage(errorText);
        return true;
    }

    void showInfo(){
        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        if(!players.isEmpty()){
            Component info = Component.text("当前下注情况：");
            for(int i = 1; i <= amount; i++){
                info = info.appendNewline().append(Component.text("队伍"+i+" "+names[i]+" :").color(TextColor.color(255,255,0)));
                for(String s : teams.get(i).keySet()){
                    info = info.append(Component.text(" "+s+":"+teams.get(i).get(s)));
                }
            }
            for(Player p : players){
                p.sendMessage(info);
            }
        }
    }

    void end(int x){
        int money = 0;
        HashMap<String,Integer> winner = (HashMap<String, Integer>) teams.get(x).clone();
        int winnerTotal = 0;
        for(int m : winner.values()){
            winnerTotal = winnerTotal + m;
        }
        for(int i = 1; i <= 8; i++){
            for(int m : teams.get(i).values()){
                money = money + m;
            }
            teams.get(i).clear();
        }
        Component toShow = endText;
        for(String s : winner.keySet()){
            int origin = winner.get(s);
            int award = money*origin/winnerTotal;
            mySQLManager.addMoney(s,award);
            toShow = toShow.appendNewline().append(Component.text(s+" 赢得了 "+origin+" + "+(award-origin)+" 硬币！").color(TextColor.color(255,255,0)));
        }
        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        if(!players.isEmpty()){
            for(Player p : players){
                p.sendMessage(toShow);
            }
        }
    }
}
