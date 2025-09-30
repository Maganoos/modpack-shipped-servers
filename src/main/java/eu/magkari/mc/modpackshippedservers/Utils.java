package eu.magkari.mc.modpackshippedservers;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.client.network.ServerInfo;

import java.lang.reflect.Constructor;

public class Utils {

    public static ServerInfo createServerInfo(String name, String address) {
        try {
            MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
            Class<?> serverTypeClass = Class.forName(resolver.mapClassName("intermediary", "net.minecraft.class_642$class_8678"));
            Object otherType = Enum.valueOf((Class<Enum>) serverTypeClass, "OTHER");

            Constructor<ServerInfo> enumConstructor = ServerInfo.class.getConstructor(
                    String.class,
                    String.class,
                    serverTypeClass
            );
            return enumConstructor.newInstance(name, address, otherType);

        } catch (ClassNotFoundException e) {
            try {
                Constructor<ServerInfo> booleanConstructor = ServerInfo.class.getDeclaredConstructor(
                        String.class,
                        String.class,
                        boolean.class
                );
                return booleanConstructor.newInstance(name, address, false);
            } catch (NoSuchMethodException booleanMissing) {
                throw new RuntimeException("no such method!", booleanMissing);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to create ServerInfo using Boolean constructor", ex);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create ServerInfo using enum constructor", ex);
        }
    }
}
