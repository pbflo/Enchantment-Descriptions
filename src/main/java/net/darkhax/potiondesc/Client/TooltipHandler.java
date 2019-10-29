package net.darkhax.enchdesc.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
//import net.minecraft.enchantment.Enchantment;
//import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import net.minecraft.potion.PotionType;
import net.minecraft.item.ItemPotion;

@SideOnly(Side.CLIENT)
public class TooltipHandler {

    private static final KeyBinding keyBindSneak = Minecraft.getMinecraft().gameSettings.keyBindSneak;

    @SubscribeEvent
    public void onTooltipDisplayed (ItemTooltipEvent event) {

        if (event.getEntityPlayer() == null) {

            return;
        }

        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemPotion) {

            final List<String> tooltip = event.getToolTip();

            if (GameSettings.isKeyDown(keyBindSneak)) {

                final List<PotionType> potiontypes = this.getPotionType(event.getItemStack());

                for (final PotionType potion : potiontypes) {

                    tooltip.add(I18n.format("tooltip.potiondesc.name") + ": " + I18n.format(potion.getName()));
                    tooltip.add(this.getDescription(potion));
                    tooltip.add(I18n.format("tooltip.potiondesc.addedby") + ": " + ChatFormatting.BLUE + getModName(potion));
                }
            }
            else {
                tooltip.add(I18n.format("tooltip.potiondesc.activate", ChatFormatting.LIGHT_PURPLE, keyBindSneak.getDisplayName(), ChatFormatting.GRAY));
            }
        }
    }

    /**
     * Gets the description of the enchantment. Or the missing text, if no description exists.
     *
     * @param enchantment The enchantment to get a description for.
     * @return The enchantment description.
     */
    private String getDescription (PotionType potion) {

        final String key = getTranslationKey(potion);
        String description = I18n.format(key);

        if (description.startsWith("potion.")) {
            description = I18n.format("tooltip.potiondesc.missing", getModName(potion), key);
        }

        return description;
    }

    /**
     * Reads a List of enchantments from an ItemEnchantedBook stack.
     *
     * @param book Instance of ItemEnchantedBook, as it uses non-static methods for some
     *        reason.
     * @param stack The stack to read the data from.
     * @return The list of enchantments stored on the stack.
     */
    private List<PotionType> getPotionType (ItemStack stack) { //ToDo: Rewrite

        final NBTTagList potionTags = ItemPotion.getPotionType(stack);
        final List<PotionType> potiontypes = new ArrayList<>();

        if (potionTags != null) {

            for (int index = 0; index < potionTags.tagCount(); ++index) {

                final int id = potionTags.getCompoundTagAt(index).getShort("id"); 
                final PotionType potion = PotionType.getEnchantmentByID(id);

                if (potion != null) {
                    potiontypes.add(potion);
                }
            }
        }

        return potiontypes;
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

    public static String getTranslationKey (PotionType potion) {

        if (potion != null && potion.getRegistryName() != null) {

            return String.format("potion.%s.%s.desc", potion.getRegistryName().getNamespace(), potion.getRegistryName().getPath());
        }

        return "NULL";
    }
}
