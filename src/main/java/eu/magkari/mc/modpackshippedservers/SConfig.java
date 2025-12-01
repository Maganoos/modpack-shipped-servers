package eu.magkari.mc.modpackshippedservers;

import java.util.List;

public class SConfig {
    public record ServerConfig(String name, String address, String resourcePackPolicy) {}

    private final List<ServerConfig> servers = List.of(new ServerConfig("Default Server", "127.0.0.1", "PROMPT"));

    public List<ServerConfig> getServers() {
        return servers;
    }
}