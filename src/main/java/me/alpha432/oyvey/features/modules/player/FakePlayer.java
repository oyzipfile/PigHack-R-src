// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import com.google.gson.JsonParser;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.alpha432.oyvey.features.modules.Module;

public class FakePlayer extends Module
{
    private String name;
    private EntityOtherPlayerMP _fakePlayer;
    
    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing", Category.PLAYER, false, false, false);
        this.name = "NiggaHack.me";
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        this._fakePlayer = null;
        if (FakePlayer.mc.player != null) {
            try {
                this._fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString(getUuid(this.name)), this.name));
            }
            catch (Exception e) {
                this._fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03"), this.name));
                Command.sendMessage("Failed to load uuid, setting another one.");
            }
            Command.sendMessage(String.format("%s has been spawned.", this.name));
            this._fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
            this._fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
            FakePlayer.mc.world.addEntityToWorld(-100, (Entity)this._fakePlayer);
        }
    }
    
    @Override
    public void onDisable() {
        if (FakePlayer.mc.world != null && FakePlayer.mc.player != null) {
            super.onDisable();
            FakePlayer.mc.world.removeEntity((Entity)this._fakePlayer);
        }
    }
    
    public static String getUuid(final String name) {
        final JsonParser parser = new JsonParser();
        final String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            final String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if (UUIDJson.isEmpty()) {
                return "invalid name";
            }
            final JsonObject UUIDObject = (JsonObject)parser.parse(UUIDJson);
            return reformatUuid(UUIDObject.get("id").toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    private static String reformatUuid(final String uuid) {
        String longUuid = "";
        longUuid = longUuid + uuid.substring(1, 9) + "-";
        longUuid = longUuid + uuid.substring(9, 13) + "-";
        longUuid = longUuid + uuid.substring(13, 17) + "-";
        longUuid = longUuid + uuid.substring(17, 21) + "-";
        longUuid += uuid.substring(21, 33);
        return longUuid;
    }
}
