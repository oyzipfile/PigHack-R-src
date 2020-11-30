// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.player;

import me.alpha432.oyvey.util.InventoryUtil;
import net.minecraft.item.ItemExpBottle;
import me.alpha432.oyvey.features.modules.Module;

public class FastPlace extends Module
{
    public FastPlace() {
        super("FastPlace", "Fast everything.", Category.PLAYER, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class)) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
    }
}
