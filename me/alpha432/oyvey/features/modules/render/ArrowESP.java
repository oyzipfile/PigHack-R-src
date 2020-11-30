// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.render;

import com.google.common.collect.Maps;
import java.util.Iterator;
import me.alpha432.oyvey.util.Util;
import java.util.Map;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.Display;
import me.alpha432.oyvey.util.EntityUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.event.events.Render2DEvent;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class ArrowESP extends Module
{
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> radius;
    private final Setting<Float> size;
    private final Setting<Boolean> outline;
    private final Setting<Float> outlineWidth;
    private final Setting<Integer> fadeDistance;
    private final Setting<Boolean> invisibles;
    private final EntityListener entityListener;
    
    public ArrowESP() {
        super("ArrowESP", "Arrow tracers ", Category.RENDER, true, false, false);
        this.red = (Setting<Integer>)this.register(new Setting("Red", (T)255, (T)0, (T)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (T)0, (T)0, (T)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (T)255, (T)0, (T)255));
        this.radius = (Setting<Integer>)this.register(new Setting("Placement", (T)45, (T)10, (T)200));
        this.size = (Setting<Float>)this.register(new Setting("Size", (T)10.0f, (T)5.0f, (T)25.0f));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (T)true));
        this.outlineWidth = (Setting<Float>)this.register(new Setting("Outline-Width", (T)1.0f, (T)0.1f, (T)3.0f));
        this.fadeDistance = (Setting<Integer>)this.register(new Setting("Range", (T)100, (T)10, (T)200));
        this.invisibles = (Setting<Boolean>)this.register(new Setting("Invisibles", (T)false));
        this.entityListener = new EntityListener();
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        this.entityListener.render();
        EntityPlayer entity;
        Vec3d pos;
        Color color;
        int x;
        int y;
        float yaw;
        ArrowESP.mc.world.loadedEntityList.forEach(o -> {
            if (o instanceof EntityPlayer && this.isValid(o)) {
                entity = o;
                pos = this.entityListener.getEntityLowerBounds().get(entity);
                if (pos != null && !this.isOnScreen(pos) && !RenderUtil.isInViewFrustrum((Entity)entity)) {
                    color = EntityUtil.getColor((Entity)entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), (int)MathHelper.clamp(255.0f - 255.0f / this.fadeDistance.getValue() * ArrowESP.mc.player.getDistance((Entity)entity), 100.0f, 255.0f), true);
                    x = Display.getWidth() / 2 / ((ArrowESP.mc.gameSettings.guiScale == 0) ? 1 : ArrowESP.mc.gameSettings.guiScale);
                    y = Display.getHeight() / 2 / ((ArrowESP.mc.gameSettings.guiScale == 0) ? 1 : ArrowESP.mc.gameSettings.guiScale);
                    yaw = this.getRotations((EntityLivingBase)entity) - ArrowESP.mc.player.rotationYaw;
                    GL11.glTranslatef((float)x, (float)y, 0.0f);
                    GL11.glRotatef(yaw, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
                    RenderUtil.drawTracerPointer((float)x, (float)(y - this.radius.getValue()), this.size.getValue(), 2.0f, 1.0f, this.outline.getValue(), this.outlineWidth.getValue(), color.getRGB());
                    GL11.glTranslatef((float)x, (float)y, 0.0f);
                    GL11.glRotatef(-yaw, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
                }
            }
        });
    }
    
    private boolean isOnScreen(final Vec3d pos) {
        return pos.x > -1.0 && pos.y < 1.0 && pos.x > -1.0 && pos.z < 1.0 && pos.x / ((ArrowESP.mc.gameSettings.guiScale == 0) ? 1 : ArrowESP.mc.gameSettings.guiScale) >= 0.0 && pos.x / ((ArrowESP.mc.gameSettings.guiScale == 0) ? 1 : ArrowESP.mc.gameSettings.guiScale) <= Display.getWidth() && pos.y / ((ArrowESP.mc.gameSettings.guiScale == 0) ? 1 : ArrowESP.mc.gameSettings.guiScale) >= 0.0 && pos.y / ((ArrowESP.mc.gameSettings.guiScale == 0) ? 1 : ArrowESP.mc.gameSettings.guiScale) <= Display.getHeight();
    }
    
    private boolean isValid(final EntityPlayer entity) {
        return entity != ArrowESP.mc.player && (!entity.isInvisible() || this.invisibles.getValue()) && entity.isEntityAlive();
    }
    
    private float getRotations(final EntityLivingBase ent) {
        final double x = ent.posX - ArrowESP.mc.player.posX;
        final double z = ent.posZ - ArrowESP.mc.player.posZ;
        return (float)(-(Math.atan2(x, z) * 57.29577951308232));
    }
    
    private static class EntityListener
    {
        private final Map<Entity, Vec3d> entityUpperBounds;
        private final Map<Entity, Vec3d> entityLowerBounds;
        
        private void render() {
            if (!this.entityUpperBounds.isEmpty()) {
                this.entityUpperBounds.clear();
            }
            if (!this.entityLowerBounds.isEmpty()) {
                this.entityLowerBounds.clear();
            }
            for (final Entity e : Util.mc.world.loadedEntityList) {
                final Vec3d bound = this.getEntityRenderPosition(e);
                bound.add(new Vec3d(0.0, e.height + 0.2, 0.0));
                final Vec3d upperBounds = RenderUtil.to2D(bound.x, bound.y, bound.z);
                final Vec3d lowerBounds = RenderUtil.to2D(bound.x, bound.y - 2.0, bound.z);
                if (upperBounds != null && lowerBounds != null) {
                    this.entityUpperBounds.put(e, upperBounds);
                    this.entityLowerBounds.put(e, lowerBounds);
                }
            }
        }
        
        private Vec3d getEntityRenderPosition(final Entity entity) {
            final double partial = Util.mc.timer.renderPartialTicks;
            final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial - Util.mc.getRenderManager().viewerPosX;
            final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial - Util.mc.getRenderManager().viewerPosY;
            final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial - Util.mc.getRenderManager().viewerPosZ;
            return new Vec3d(x, y, z);
        }
        
        public Map<Entity, Vec3d> getEntityLowerBounds() {
            return this.entityLowerBounds;
        }
        
        private EntityListener() {
            this.entityUpperBounds = (Map<Entity, Vec3d>)Maps.newHashMap();
            this.entityLowerBounds = (Map<Entity, Vec3d>)Maps.newHashMap();
        }
    }
}
