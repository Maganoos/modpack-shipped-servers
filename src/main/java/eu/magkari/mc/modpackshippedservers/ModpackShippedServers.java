package eu.magkari.mc.modpackshippedservers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ModpackShippedServers implements ClientModInitializer {
    public static final String MOD_ID = "modpack-shipped-servers";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static SConfig CONFIG;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onInitializeClient() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("shipped-servers.json");

        try {
            if (Files.notExists(configPath)) {
                SConfig defaultConfig = new SConfig();
                String json = gson.toJson(defaultConfig);

                try (BufferedWriter out = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                    out.write(json);
                } catch (IOException e) {
                    LOGGER.error("Error writing file!", e);
                }

                CONFIG = defaultConfig;

            } else {
                String content = Files.readString(configPath, StandardCharsets.UTF_8);

                if (content.isBlank()) {
                    LOGGER.warn("Servers file was empty! Using default values.");
                    CONFIG = new SConfig();
                } else {
                    CONFIG = gson.fromJson(content, SConfig.class);

                    if (CONFIG == null) {
                        LOGGER.warn("Servers file had invalid JSON! Using default values.");
                        CONFIG = new SConfig();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("There was an error creating/loading the servers file!", e);
            CONFIG = new SConfig();
            LOGGER.warn("Using default values.");
        }


        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            ServerList serverList = new ServerList(client);
            serverList.load();
            if (serverList.size() == 0) {
                CONFIG.getServers().forEach(config -> {
                    //? >= 1.20.2 {
                    ServerData configuredServer = new ServerData(config.name(), config.address(), ServerData.Type.OTHER);
                    //?} else {
                    /*ServerData configuredServer = new ServerData(config.name(), config.address(), false);
                     *///?}
                    try {
                        configuredServer.setResourcePackStatus(ServerData.ServerPackStatus.valueOf(config.resourcePackPolicy().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("Invalid resource pack policy '{}' for server '{}'. Defaulting to 'PROMPT'.", config.resourcePackPolicy(), config.name());
                        configuredServer.setResourcePackStatus(ServerData.ServerPackStatus.PROMPT);
                    }
                    serverList.add(configuredServer, false);
                });
                serverList.save();
            }
        });
    }
}