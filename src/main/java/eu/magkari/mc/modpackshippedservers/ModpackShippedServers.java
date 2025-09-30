package eu.magkari.mc.modpackshippedservers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

public class ModpackShippedServers implements ClientModInitializer {
    public static final String MOD_ID = "modpack-shipped-servers";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static SConfig CONFIG;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onInitializeClient() {
        File configFile = FabricLoader.getInstance().getConfigDir().resolve("shipped-servers.json").toFile();

        try {
            if (configFile.createNewFile()) {
                String json = gson.toJson(JsonParser.parseString(gson.toJson(new SConfig())));
                try (PrintWriter out = new PrintWriter(configFile)) {
                    out.println(json);
                }
                CONFIG = new SConfig();
            } else {
                CONFIG = gson.fromJson(new String(Files.readAllBytes(configFile.toPath())), SConfig.class);
                if (CONFIG == null) {
                    throw new NullPointerException("The servers file was empty.");
                }
            }
        } catch (Exception exception) {
            LOGGER.error("There was an error creating/loading the servers file!", exception);
            CONFIG = new SConfig();
            LOGGER.warn("Defaulting to original servers file.");
        }

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            ServerList serverList = new ServerList(client);
            serverList.loadFile();
            if (serverList.size() == 0) {
                CONFIG.getServers().forEach(config -> {
                    ServerInfo configuredServer = Utils.createServerInfo(config.name(), config.address());
                    try {
                        configuredServer.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.valueOf(config.resourcePackPolicy().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("Invalid resource pack policy '{}' for configuredServer '{}'. Defaulting to 'PROMPT'.", config.resourcePackPolicy(), config.name());
                        configuredServer.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.PROMPT);
                        serverList.add(configuredServer, false);
                        return;
                    }
                    serverList.add(configuredServer, false);
                });
                serverList.saveFile();
            }
        });
    }
}