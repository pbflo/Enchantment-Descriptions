package net.darkhax.enchdesc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.enchdesc.client.TooltipHandler;
import net.darkhax.enchdesc.handler.ConfigurationHandler;
import net.minecraft.client.resources.I18n;
//import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.init.PotionTypes;

@Mod(modid = "potiondesc", name = "Potion Descriptions", version = "@VERSION@", clientSideOnly = true, certificateFingerprint = "@FINGERPRINT@")
public class PotionDescriptions {

    public static final Logger LOG = LogManager.getLogger("Potion Descriptions");

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void preInit (FMLPreInitializationEvent event) {

        ConfigurationHandler.initConfig(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
    }

    @EventHandler
    @SideOnly(Side.CLIENT)
    public void postInit (FMLPostInitializationEvent event) {

        if (ConfigurationHandler.isExploreMode()) {

            for (final PotionType potion : PotionType.REGISTRY) {

                if (I18n.format(TooltipHandler.getTranslationKey(potion)).startsWith("potion.")) {

                    LOG.info(String.format("Undefined potion from %s %s", TooltipHandler.getModName(potion), TooltipHandler.getTranslationKey(potion)));
                }
            }
        }
    }
}
