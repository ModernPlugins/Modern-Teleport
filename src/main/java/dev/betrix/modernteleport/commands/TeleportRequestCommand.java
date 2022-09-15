package dev.betrix.modernteleport.commands;

import dev.betrix.modernteleport.ModernTeleport;
import dev.betrix.modernteleport.TeleportHandler;
import dev.betrix.modernteleport.TeleportResult;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TeleportRequestCommand implements CommandExecutor {

    private final ModernTeleport modernTeleport;
    private final TeleportHandler teleportHandler;

    public TeleportRequestCommand(ModernTeleport modernTeleport, TeleportHandler teleportHandler) {
        this.modernTeleport = modernTeleport;
        this.teleportHandler = teleportHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (sender instanceof Player player) {
            Player target = modernTeleport.getServer().getPlayer(args[0]);
            TeleportResult result = teleportHandler.canTeleport(player, target);

            if (result.result()) {
                teleportHandler.sendRequest(player, Objects.requireNonNull(target));

                modernTeleport.playSound(player, Key.key("entity.experience_orb.pickup"));
            } else {
                modernTeleport.playSound(player, Key.key("entity.villager.no"));
            }

            ArrayList<TagResolver> resolvers = new ArrayList<>();
            resolvers.add(Placeholder.unparsed("sender_name", sender.getName()));
            resolvers.add(Placeholder.unparsed("target_name", target.getName()));
            if (result.tagResolvers() != null) {
                resolvers.addAll(Arrays.asList(result.tagResolvers()));
            }

            modernTeleport.messagePlayer(player, result.messageKey(), resolvers);

        }

        return true;
    }
}
