// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.OyVey;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ExtraTab extends Module
{
    public Setting<Integer> size;
    private static ExtraTab INSTANCE;
    
    public ExtraTab() {
        super("ExtraTab", "Extends Tab.", Category.MISC, false, false, false);
        this.size = (Setting<Integer>)this.register(new Setting("Size", (T)250, (T)1, (T)1000));
        this.setInstance();
    }
    
    private void setInstance() {
        ExtraTab.INSTANCE = this;
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String name = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (OyVey.friendManager.isFriend(name)) {
            return ChatFormatting.AQUA + name;
        }
        return name;
    }
    
    public static ExtraTab getINSTANCE() {
        if (ExtraTab.INSTANCE == null) {
            ExtraTab.INSTANCE = new ExtraTab();
        }
        return ExtraTab.INSTANCE;
    }
    
    static {
        ExtraTab.INSTANCE = new ExtraTab();
    }
}
