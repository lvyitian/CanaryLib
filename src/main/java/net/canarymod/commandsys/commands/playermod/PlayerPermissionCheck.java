package net.canarymod.commandsys.commands.playermod;

import net.canarymod.Canary;
import net.canarymod.Translator;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.Colors;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.permissionsystem.PermissionNode;

public class PlayerPermissionCheck {
    //groupmod permission add group value
    public void execute(MessageReceiver caller, String[] args) {
        Player player = Canary.getServer().matchPlayer(args[1]);
        PermissionNode node = PermissionNode.fromString(args[2]);
        if(player == null) {
            OfflinePlayer oplayer = Canary.getServer().getOfflinePlayer(args[1]);
            if(oplayer.getPermissionProvider().pathExists(node.getName())) {
                if(oplayer.hasPermission(node.getName())) {
                    caller.message(Colors.LIGHT_GREEN + node.getName() + ": true");
                }
                else {
                    caller.message(Colors.LIGHT_RED + node.getName() + ": false");
                }
            }
            else {
                caller.message(Colors.YELLOW + node.getName() + ": " + Translator.translate("no"));
            }
        }
        else {
            if(player.getPermissionProvider().pathExists(node.getName())) {
                if(player.hasPermission(node.getName())) {
                    caller.message(Colors.LIGHT_GREEN + node.getName() + ": true");
                }
                else {
                    caller.message(Colors.LIGHT_RED + node.getName() + ": false");
                }
            }
            else {
                caller.message(Colors.YELLOW + node.getName() + ": " + Translator.translate("no"));
            }
        }
    }
}