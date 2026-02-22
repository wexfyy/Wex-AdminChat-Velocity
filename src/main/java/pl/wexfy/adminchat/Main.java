package pl.wexfy.adminchat;

import com.google.inject.Inject;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import pl.wexfy.adminchat.commands.AdminChatCommand;

@Plugin(id = "wex-adminchat-velocity", name = "Wex-AdminChat-Velocity", version = BuildConstants.VERSION)
public class Main {

    @Inject
    private Logger logger;

    private ProxyServer proxy;

    @Inject
    public Main(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Loading plugin...");
        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("adminchat")
                .aliases("ac")
                .plugin(this)
                .build();
        BrigadierCommand commandToRegister = AdminChatCommand.createBrigadierCommand(proxy);
        commandManager.register(commandMeta, commandToRegister);
        logger.info("Thank you for downloading my plugin! If you have any questions or suggestions, feel free to contact me on Discord: wexfyy");
    }

}
