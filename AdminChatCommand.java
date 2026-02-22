package pl.wexfy.adminchat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;

public class AdminChatCommand {

    public static BrigadierCommand createBrigadierCommand(final ProxyServer proxy) {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder
                .<CommandSource>literal("adminchat")
                .requires(source -> source.hasPermission("adminchat.use"))
                .then(RequiredArgumentBuilder
                        .<CommandSource, String>argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            CommandSource source = context.getSource();
                            if (!(source instanceof Player)) {
                                source.sendMessage(Component.text("You must be a player to use this command."));
                                return Command.SINGLE_SUCCESS;
                            }
                            Player p = (Player) source;
                            String message = StringArgumentType.getString(context, "message");
                            String server = p.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("Unknown");
                            Component coloredMsg = LegacyComponentSerializer.legacyAmpersand()
                                    .deserialize("&8(&c&lAdminChat&8) &8(&e" + server + "&8) &6" + p.getUsername() + " &e" + message);
                            source.sendMessage(coloredMsg);
                            if (!(source.hasPermission("adminchat.use"))) {
                                source.sendMessage(Component.text("You don't have permission to use this command."));
                                return Command.SINGLE_SUCCESS;
                            }
                            proxy.getAllPlayers().forEach(player -> {
                                if (player.hasPermission("adminchat.use")) {
                                    player.sendMessage(coloredMsg);
                                }
                            });
                            return Command.SINGLE_SUCCESS;

                        }))
                .build();
        return new BrigadierCommand(node);
    }
    }
