// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class SmallShield extends Module
{
    public Setting<Float> offX;
    public Setting<Float> offY;
    public Setting<Float> mainX;
    public Setting<Float> mainY;
    private static SmallShield INSTANCE;
    
    public SmallShield() {
        super("SmallShield", "Makes you offhand lower.", Category.RENDER, false, false, false);
        this.offX = (Setting<Float>)this.register(new Setting("OffHandX", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.offY = (Setting<Float>)this.register(new Setting("OffHandY", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.mainX = (Setting<Float>)this.register(new Setting("MainHandX", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.mainY = (Setting<Float>)this.register(new Setting("MainHandY", (T)0.0f, (T)(-1.0f), (T)1.0f));
        this.setInstance();
    }
    
    private void setInstance() {
        SmallShield.INSTANCE = this;
    }
    
    public static SmallShield getINSTANCE() {
        if (SmallShield.INSTANCE == null) {
            SmallShield.INSTANCE = new SmallShield();
        }
        return SmallShield.INSTANCE;
    }
    
    static {
        SmallShield.INSTANCE = new SmallShield();
    }
}
