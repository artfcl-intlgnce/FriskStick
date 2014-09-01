package friskstick.commands;

import friskstick.main.FriskStick;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    private FriskStick plugin;

    public ReportCommand(FriskStick plugin) {

        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // If the config allows reporting...
        if (plugin.getConfig().getBoolean("enable-reporting")) {

            // If the player entered the command correctly...
            if (label.equalsIgnoreCase("report") && args.length == 1) {

                // If the sender is a player and has proper perms...
                if (sender instanceof Player && (sender.isOp() || sender.hasPermission("friskstick.report.send"))) {

                    // If the player entered in the command is a valid player...
                    if (plugin.getServer().getPlayer(args[0]) != null) {

                        // Write a new report to reports.txt
                        plugin.reportFileInstance.writeReportToFile((Player) sender, plugin.getServer().getPlayer(args[0]));

                        sender.sendMessage(ChatColor.GREEN + "Report submitted successfully!");

                        // Send a notification to all players with proper perms
                        for (Player p : plugin.getServer().getOnlinePlayers()) {

                            if (p.isOp() || p.hasPermission("friskstick.report.receive")) {

                                p.sendMessage(plugin.getConfig().getString("player-report-msg").replaceAll("&", "§").replaceAll("%reported%", args[0]).replaceAll("%snitch%", sender.getName()));

                            }
                        }

                    } else {

                        sender.sendMessage(ChatColor.RED + "The player you entered either does not exist or is currently offline.");

                    }

                } else if (!(sender instanceof Player)) {

                    sender.sendMessage(ChatColor.RED + "This command cannot be run from the console.");

                } else if (!sender.hasPermission("friskstick.report.send")) {

                    sender.sendMessage(ChatColor.RED + "You do not have permission to do that!");

                }

            } else {

                sender.sendMessage(ChatColor.GOLD + "Usage: /report <player>");

            }

        } else {

            sender.sendMessage(ChatColor.RED + "Reporting is not enabled on this server.");

        }

        return true;

    }

}
