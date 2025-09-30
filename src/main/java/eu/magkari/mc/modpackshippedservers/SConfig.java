package eu.magkari.mc.modpackshippedservers;

import java.util.List;

public class SConfig {
    public record ServerConfig(String name, String address, String resourcePackPolicy) {
        @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                ServerConfig that = (ServerConfig) o;

                if (!resourcePackPolicy.equals(that.resourcePackPolicy)) return false;
                if (!name.equals(that.name)) return false;
                return address.equals(that.address);
            }

    }

    private final List<ServerConfig> servers = List.of(new ServerConfig("Default Server", "127.0.0.1", "PROMPT"));

    public List<ServerConfig> getServers() {
        return servers;
    }
}