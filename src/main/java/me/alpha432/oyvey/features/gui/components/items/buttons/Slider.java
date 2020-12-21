// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.gui.components.items.buttons;

import org.lwjgl.input.Mouse;
import java.util.Iterator;
import me.alpha432.oyvey.features.gui.components.Component;
import me.alpha432.oyvey.features.gui.OyVeyGui;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.util.RenderUtil;
import me.alpha432.oyvey.features.setting.Setting;

public class Slider extends Button
{
    public Setting setting;
    private Number min;
    private Number max;
    private int difference;

    public Slider(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.min = (Number) setting.getMin();
        this.max = (Number) setting.getMax();
        this.difference = this.max.intValue() - this.min.intValue();
        this.width = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.dragSetting(mouseX, mouseY);
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077);
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.getState() ? (this.isHovering(mouseX, mouseY) ? OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue()) : OyVey.colorManager.getColorWithAlpha(OyVey.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue())) : (this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077));
        OyVey.textManager.drawStringWithShadow(this.getName() + " " + ChatFormatting.GRAY + ((this.setting.getValue() instanceof Float) ? this.setting.getValue() : this.setting.getValue() instanceof Float), this.x + 2.3f, this.y - 1.7f - OyVeyGui.getClickGui().getTextOffset(), -1);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            this.setSettingFromX(mouseX);
        }
    }
    
    @Override
    public boolean isHovering(final int mouseX, final int mouseY) {
        for (final Component component : OyVeyGui.getClickGui().getComponents()) {
            if (component.drag) {
                return false;
            }
        }
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() + 8.0f && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
    }
    
    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    private void dragSetting(final int mouseX, final int mouseY) {
        if (this.isHovering(mouseX, mouseY) && Mouse.isButtonDown(0)) {
            this.setSettingFromX(mouseX);
        }
    }
    
    @Override
    public int getHeight() {
        return 14;
    }

    private void setSettingFromX(final int mouseX) {
        final float percent = (mouseX - this.x) / (this.width + 7.4f);
        if (this.setting.getValue() instanceof Double) {
            final double result = (double) this.setting.getMin() + this.difference * percent;
            this.setting.setValue(Math.round(10.0 * result) / 10.0);
        } else if (this.setting.getValue() instanceof Float) {
            final float result2 = (float) this.setting.getMin() + (int) (this.difference * percent);
            this.setting.setValue(Math.round(10.0f * result2) / 10.0f);
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((Integer) this.setting.getMin() + (int) (this.difference * percent));
        }
    }
    
    private float middle() {
    return this.max.floatValue() - this.min.floatValue();
}

    private float part() {
        return (Integer) this.setting.getValue() - this.min.intValue();
    }
    
    private float partialMultiplier() {
        return this.part() / this.middle();
    }
}
