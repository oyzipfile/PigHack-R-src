// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.misc;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.util.NonNullList;
import net.minecraft.client.renderer.RenderHelper;
import me.alpha432.oyvey.util.ColorUtil;
import java.awt.Color;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import me.alpha432.oyvey.event.events.Render2DEvent;
import java.util.Iterator;
import net.minecraft.item.ItemShulkerBox;
import java.util.concurrent.ConcurrentHashMap;
import me.alpha432.oyvey.util.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import me.alpha432.oyvey.features.modules.Module;

public class ToolTips extends Module
{
    private static final ResourceLocation SHULKER_GUI_TEXTURE;
    private static ToolTips INSTANCE;
    public Map<EntityPlayer, ItemStack> spiedPlayers;
    public Map<EntityPlayer, Timer> playerTimers;
    private int textRadarY;
    
    public ToolTips() {
        super("ShulkerViewer", "Several tweaks for tooltips.", Category.MISC, true, false, false);
        this.spiedPlayers = new ConcurrentHashMap<EntityPlayer, ItemStack>();
        this.playerTimers = new ConcurrentHashMap<EntityPlayer, Timer>();
        this.textRadarY = 0;
        this.setInstance();
    }
    
    private void setInstance() {
        ToolTips.INSTANCE = this;
    }
    
    public static ToolTips getInstance() {
        if (ToolTips.INSTANCE == null) {
            ToolTips.INSTANCE = new ToolTips();
        }
        return ToolTips.INSTANCE;
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        for (final EntityPlayer player : ToolTips.mc.world.playerEntities) {
            if (player != null && player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox && ToolTips.mc.player != player) {
                final ItemStack stack = player.getHeldItemMainhand();
                this.spiedPlayers.put(player, stack);
            }
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        final int x = -3;
        int y = 124;
        this.textRadarY = 0;
        for (final EntityPlayer player : ToolTips.mc.world.playerEntities) {
            if (this.spiedPlayers.get(player) != null) {
                if (player.getHeldItemMainhand() == null || !(player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox)) {
                    final Timer playerTimer = this.playerTimers.get(player);
                    if (playerTimer == null) {
                        final Timer timer = new Timer();
                        timer.reset();
                        this.playerTimers.put(player, timer);
                    }
                    else if (playerTimer.passedS(3.0)) {
                        continue;
                    }
                }
                else if (player.getHeldItemMainhand().getItem() instanceof ItemShulkerBox) {
                    final Timer playerTimer = this.playerTimers.get(player);
                    if (playerTimer != null) {
                        playerTimer.reset();
                        this.playerTimers.put(player, playerTimer);
                    }
                }
                final ItemStack stack = this.spiedPlayers.get(player);
                this.renderShulkerToolTip(stack, x, y, player.getName());
                y += 78;
                this.textRadarY = y - 10 - 114 + 2;
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void makeTooltip(final ItemTooltipEvent event) {
    }
    
    public void renderShulkerToolTip(final ItemStack stack, final int x, final int y, final String name) {
        final NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10)) {
            final NBTTagCompound blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag");
            if (blockEntityTag.hasKey("Items", 9)) {
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                ToolTips.mc.getTextureManager().bindTexture(ToolTips.SHULKER_GUI_TEXTURE);
                RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
                RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 57, 500);
                RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
                GlStateManager.disableDepth();
                final Color color = new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), 200);
                this.renderer.drawStringWithShadow((name == null) ? stack.getDisplayName() : name, (float)(x + 8), (float)(y + 6), ColorUtil.toRGBA(color));
                GlStateManager.enableDepth();
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableColorMaterial();
                GlStateManager.enableLighting();
                final NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(blockEntityTag, (NonNullList)nonnulllist);
                for (int i = 0; i < nonnulllist.size(); ++i) {
                    final int iX = x + i % 9 * 18 + 8;
                    final int iY = y + i / 9 * 18 + 18;
                    final ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                    ToolTips.mc.getItemRenderer().itemRenderer.zLevel = 501.0f;
                    RenderUtil.itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                    RenderUtil.itemRender.renderItemOverlayIntoGUI(ToolTips.mc.fontRenderer, itemStack, iX, iY, (String)null);
                    ToolTips.mc.getItemRenderer().itemRenderer.zLevel = 0.0f;
                }
                GlStateManager.disableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    public static void displayInv(ItemStack stack, String name) {
        try {
            Item item = stack.getItem();
            TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            ItemShulkerBox shulker = (ItemShulkerBox)item;
            entityBox.blockType = shulker.getBlock();
            entityBox.setWorld(mc.world);
            ItemStackHelper.loadAllItems(stack.getTagCompound().getCompoundTag("BlockEntityTag"), entityBox.items);
            entityBox.readFromNBT(stack.getTagCompound().getCompoundTag("BlockEntityTag"));
            entityBox.setCustomName(name == null ? stack.getDisplayName() : name);
            (new Thread(() -> {
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException var2) {
                }

                mc.player.displayGUIChest(entityBox);
            })).start();
        } catch (Exception var5) {
        }

    }
    
    static {
        SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
        ToolTips.INSTANCE = new ToolTips();
    }
}
