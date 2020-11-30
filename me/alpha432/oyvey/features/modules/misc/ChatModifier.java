// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.alpha432.oyvey.OyVey;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.alpha432.oyvey.event.events.PacketEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ChatModifier extends Module
{
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    private static ChatModifier INSTANCE;
    public boolean check;
    
    public ChatModifier() {
        super("BetterChat", "Modifies your chat", Category.MISC, true, false, false);
        this.clean = (Setting<Boolean>)this.register(new Setting("NoChatBackground", (T)false, "Cleans your chat"));
        this.infinite = (Setting<Boolean>)this.register(new Setting("InfiniteChat", (T)false, "Makes your chat infinite."));
        this.setInstance();
    }
    
    private void setInstance() {
        ChatModifier.INSTANCE = this;
    }
    
    public static ChatModifier getInstance() {
        if (ChatModifier.INSTANCE == null) {
            ChatModifier.INSTANCE = new ChatModifier();
        }
        return ChatModifier.INSTANCE;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            final String s = event.getPacket().getMessage();
            if (s.startsWith(OyVey.commandManager.getPrefix())) {
                this.check = false;
            }
            else {
                this.check = true;
            }
        }
    }
    
    static {
        ChatModifier.INSTANCE = new ChatModifier();
    }
}
