# Modpack Shipped Servers
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/IRJW4Ojq?style=for-the-badge&logo=modrinth&color=%2300AF5C)](https://modrinth.com/mod/IRJW4Ojq) [![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/maganoos/modpack-shipped-servers/total?style=for-the-badge&logo=github&color=whitesmoke)](https://github.com/maganoos/modpack-shipped-servers) ![Modrinth Version](https://img.shields.io/modrinth/v/IRJW4Ojq?style=for-the-badge&logo=semver)<br>
A simple mod that allows modpack developers to define a default set of servers that the player can then choose to do with as they please.
This is useful for modpacks made for servers (or advertisement), so that they can easily provide a list of recommended servers for players to join.
When the mod first runs, it will check if there is already server data present. If any are detected, it simply does nothing. If not, it adds your servers.

## Configuration
The mod uses a simple JSON file to define the servers. The file is located at `config/shipped-servers.json` in the Minecraft directory.
Below is an example:
```json
{
  "servers": [
    {
      "name": "Example",
      "address": "play.example.com",
      "resourcePackPolicy": "prompt"
    },
    {
      "name": "Another Example",
      "address": "eu.example.com",
      "resourcePackPolicy": "enabled"
    },
    {
      "name": "Yet Another Example",
      "address": "na.example.com",
      "resourcePackPolicy": "disabled"
    }
  ]
}
```
The `servers` array can contain any number of server objects, each with the following properties:
- `name`: The name of the server as it will appear in the server list.
- `address`: The address of the server (IP or domain).
- `resourcePackPolicy`: The resource pack policy for the server. Can be `enabled`, `disabled`, or `prompt`. Case-insensitive.
