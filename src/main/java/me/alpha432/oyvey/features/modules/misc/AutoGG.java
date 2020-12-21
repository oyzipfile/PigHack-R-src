// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import java.util.Objects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.setting.Setting;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.features.modules.Module;

public class AutoGG extends Module
{
    private ConcurrentHashMap<String, Integer> targetedPlayers;
    public Setting<String> custom;
    public Setting<String> test;
    private static AutoGG INSTANCE;
    
    public AutoGG() {
        super("AutoGG", "Sends msg after you kill someone", Category.MISC, true, false, false);
        this.targetedPlayers = null;
        this.custom = (Setting<String>)this.register(new Setting("Custom", "Nigga-Hack.me"));
        this.test = (Setting<String>)this.register(new Setting("Test", "null"));
        this.setInstance();
    }
    
    private void setInstance() {
        AutoGG.INSTANCE = this;
    }
    
    public static AutoGG getINSTANCE() {
        if (AutoGG.INSTANCE == null) {
            AutoGG.INSTANCE = new AutoGG();
        }
        return AutoGG.INSTANCE;
    }
    
    @Override
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
    }
    
    @Override
    public void onDisable() {
        this.targetedPlayers = null;
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        for (final Entity entity : AutoGG.mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }
            final EntityPlayer player = (EntityPlayer)entity;
            if (player.getHealth() > 0.0f) {
                continue;
            }
            final String name2 = player.getName();
            if (this.shouldAnnounce(name2)) {
                this.doAnnounce(name2);
                break;
            }
        }
        this.targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(name);
            }
            else {
                this.targetedPlayers.put(name, timeout - 1);
            }
        });
    }
    
    @SubscribeEvent
    public void onLeavingDeathEvent(final LivingDeathEvent event) {
        if (AutoGG.mc.player == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        final EntityLivingBase entity;
        if ((entity = event.getEntityLiving()) == null) {
            return;
        }
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer)entity;
        if (player.getHealth() > 0.0f) {
            return;
        }
        final String name = player.getName();
        if (this.shouldAnnounce(name)) {
            this.doAnnounce(name);
        }
    }
    
    private boolean shouldAnnounce(final String name) {
        return this.targetedPlayers.containsKey(name);
    }
    
    private void doAnnounce(final String name) {
        this.targetedPlayers.remove(name);
        AutoGG.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((String)this.custom.getValue()));
        int u = 0;
        for (int i = 0; i < 10; ++i) {
            ++u;
        }
        if (!this.test.getValue().equalsIgnoreCase("null")) {
            AutoGG.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((String)this.test.getValue()));
        }
    }
    
    public void addTargetedPlayer(final String name) {
        if (Objects.equals(name, AutoGG.mc.player.getName())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        this.targetedPlayers.put(name, 20);
    }
    
    static {
        AutoGG.INSTANCE = new AutoGG();
    }
}
