package de.langomatisch.skyblock.ranksync.listener;

import de.langomatisch.skyblock.ranksync.RankSyncModule;
import de.langomatisch.skyblock.ranksync.rank.HypixelRank;
import net.hypixel.api.reply.PlayerReply;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.concurrent.ExecutionException;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws ExecutionException, InterruptedException {
        Player player = event.getPlayer();
        PlayerReply playerReply = RankSyncModule.getInstance().getHypixelAPI().getPlayerByUuid(player.getUniqueId()).get();
        if (playerReply.getPlayer().has("newPackageRank")) {
            String hypixelRank = playerReply.getPlayer().get("newPackageRank").getAsString();
            setScoreboard(player, HypixelRank.getRank(hypixelRank));
        }else {
            setScoreboard(player, HypixelRank.DEFAULT);
        }
    }

    private void setScoreboard(Player player, HypixelRank rank) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Scoreboard scoreboard = onlinePlayer.getScoreboard();
            if (scoreboard == null) scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Team team = scoreboard.getTeam(rank.ordinal() + player.getUniqueId().toString().substring(0, 11));
            if (team == null)
                team = scoreboard.registerNewTeam(rank.ordinal() + player.getUniqueId().toString().substring(0, 11));
            team.setPrefix(rank.getPrefix());
            team.addEntry(player.getName());
            onlinePlayer.setScoreboard(scoreboard);
        }
    }

}
