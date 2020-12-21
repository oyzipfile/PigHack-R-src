// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraft.entity.Entity;
import me.alpha432.oyvey.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import me.alpha432.oyvey.features.modules.Module;

public class PopCounter extends Module
{
    public static HashMap<String, Integer> TotemPopContainer;
    private static PopCounter INSTANCE;
    
    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Category.MISC, true, false, false);
        this.setInstance();
    }
    
    private void setInstance() {
        PopCounter.INSTANCE = this;
    }
    
    public static PopCounter getInstance() {
        if (PopCounter.INSTANCE == null) {
            PopCounter.INSTANCE = new PopCounter();
        }
        return PopCounter.INSTANCE;
    }
    
    @Override
    public void onEnable() {
        PopCounter.TotemPopContainer.clear();
    }
    
    public void onDeath(final EntityPlayer player) {
        if (PopCounter.TotemPopContainer.containsKey(player.getName())) {
            final int l_Count = PopCounter.TotemPopContainer.get(player.getName());
            PopCounter.TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                Command.sendMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totem!");
            }
            else {
                Command.sendMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totems!");
            }
        }
    }
    
    public void onTotemPop(final EntityPlayer player) {
        if (fullNullCheck()) {
            return;
        }
        if (PopCounter.mc.player.equals((Object)player)) {
            return;
        }
        int l_Count = 1;
        if (PopCounter.TotemPopContainer.containsKey(((Entity)player).getName())) {
            l_Count = PopCounter.TotemPopContainer.get(((Entity)player).getName());
            PopCounter.TotemPopContainer.put(((Entity)player).getName(), ++l_Count);
        }
        else {
            PopCounter.TotemPopContainer.put(((Entity)player).getName(), l_Count);
        }
        if (l_Count == 1) {
            Command.sendMessage(ChatFormatting.RED + ((Entity)player).getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totem.");
        }
        else {
            Command.sendMessage(ChatFormatting.RED + ((Entity)player).getName() + " popped " + ChatFormatting.GREEN + l_Count + ChatFormatting.RED + " Totems.");
        }
    }
    
    static {
        PopCounter.TotemPopContainer = new HashMap<String, Integer>();
        PopCounter.INSTANCE = new PopCounter();
    }
}
