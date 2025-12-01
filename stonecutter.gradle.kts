plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.13-SNAPSHOT" apply false
    id("me.modmuss50.mod-publish-plugin") version "1.0.+" apply false
}

stonecutter active "1.20.2"


// Make newer versions be published last
stonecutter tasks {
    order("publishModrinth")
}

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    swaps["minecraft"] = "\"" + node.metadata.version + "\";"
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String
}
