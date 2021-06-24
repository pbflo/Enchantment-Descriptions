package com.pbflo.potiondesc.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import com.pbflo.potiondesc.handler.ConfigurationHandler;

import net.minecraft.item.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import net.minecraft.potion.PotionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@SideOnly(Side.CLIENT)
public class TooltipHandler {

    private static final KeyBinding keyBindSneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;

    @SubscribeEvent
    public void onTooltipDisplayed (ItemTooltipEvent event) {

        if (event.getEntityPlayer() == null || event.getItemStack().isEmpty()) {
            return;
        }

        final List<String> tooltip = event.getToolTip();
        final Item i = event.getItemStack().getItem();

        if (i instanceof ItemPotion || i instanceof ItemTippedArrow || i instanceof ItemSpectralArrow) {
            if (GameSettings.isKeyDown(keyBindSneak)) {
                List<PotionEffect> potionEffects = this.getPotionEffects(event.getItemStack());

                for (final PotionEffect potion : potionEffects) {
                    tooltip.add(I18n.format("tooltip.potiondesc.name") + ": " + ChatFormatting.getByChar(ConfigurationHandler.getTitleColor()) + I18n.format(potion.getEffectName()));
                    tooltip.add(ChatFormatting.getByChar(ConfigurationHandler.getDescColor()) + this.getDescription(potion));
                    tooltip.add(I18n.format("tooltip.potiondesc.addedby") + ": " + ChatFormatting.BLUE + getModName(potion.getPotion()));
                }
            }
            else {
                tooltip.add(I18n.format("tooltip.potiondesc.activate", ChatFormatting.LIGHT_PURPLE, keyBindSneak.getDisplayName(), ChatFormatting.GRAY));
            }

        }
    }

    /**
     * Gets the description of the potion. Or the missing text, if no description exists.
     *
     * @param potion The potion type to get a description for.
     * @return The potion description.
     */
    private String getDescription (PotionEffect potion) {

        final String key = getTranslationKey(potion);
        String description = I18n.format(key);

        if (description.startsWith("potion.")) {
            description = I18n.format("tooltip.potiondesc.missing", getModName(potion.getPotion()), key);
        }

        return description;
    }

    /**
     * Reads the potion effects from an ItemStack.
     *
     * @param stack The stack to read the data from.
     * @return The list of potions stored on the stack.
     */
    private List<PotionEffect> getPotionEffects (ItemStack stack) { //ToDo: Rewrite
        List<PotionEffect> potionEffects = new ArrayList<>();

        if (stack.getItem() instanceof ItemSpectralArrow) {
            potionEffects.add(new PotionEffect(Potion.REGISTRY.getObjectById(24)));
        } else {
            potionEffects = PotionUtils.getEffectsFromStack(stack);
        }

        return potionEffects;
    }

    /**
     * Gets the name of the mod that registered the passed object. Works for anthing which uses
     * Forge's registry.
     *
     * @param registerable The object to get the mod name of.
     * @return The name of the mod which registered the object.
     */
    public static String getModName (IForgeRegistryEntry.Impl<?> registerable) {

        if (registerable != null && registerable.getRegistryName() != null) {
            final String modID = registerable.getRegistryName().getNamespace();
            final ModContainer mod = Loader.instance().getIndexedModList().get(modID);
            return mod != null ? mod.getName() : modID;
        }

        return "NULL";
    }

    public static String getTranslationKey (PotionEffect potion) {
        final Potion type = potion.getPotion();

        if (potion != null && type.getRegistryName() != null) {
            return String.format("potion.%s.%s.desc", type.getRegistryName().getNamespace(), potion.getEffectName());
        }

        return "NULL";
    }
}
