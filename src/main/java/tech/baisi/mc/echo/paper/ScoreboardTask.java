package tech.baisi.mc.echo.paper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

public class ScoreboardTask {
    private Timer timer;
    private Scoreboard scoreboard;
    private Objective objective;
    public void init(){
        timer = new Timer();
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        objective = scoreboard.getObjective("alive");
        if(objective == null){
            objective = scoreboard.registerNewObjective("alive", Criteria.DUMMY,"今日在线(分钟)");
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void start(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                addTime();
                resetObjective();
            }
        },60000,60000);
    }

    public void stop(){
        timer.cancel();
    }

    private void addTime(){
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if(!players.isEmpty()){
            for(Player p : players){
                Score score = objective.getScore(p.getName());
                score.setScore(score.getScore()+1);
            }
        }

        Score score = objective.getScore("GDP(Coin)");
        score.setScore(Entrance.gdp);
    }

    private void resetObjective(){
        if((System.currentTimeMillis()/(1000*60))%1440 == 1200){
            Entrance.gdp = 0;

            objective.unregister();
            objective = scoreboard.registerNewObjective("alive", Criteria.DUMMY,"今日在线(分钟)");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
    }
}
