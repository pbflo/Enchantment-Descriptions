package com.pbflo.potiondesc;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pbflo.potiondesc.client.TooltipHandler;
import com.pbflo.potiondesc.handler.ConfigurationHandler;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            for (final PotionType potion : /*PotionType.REGISTRY*/ForgeRegistries.POTION_TYPES) {
                for (final PotionEffect effect : potion.getEffects()) {
                    if (I18n.format(TooltipHandler.getTranslationKey(effect)).startsWith("potion.")) {
                        LOG.info(String.format("Undefined potion from %s %s", TooltipHandler.getModName(potion), TooltipHandler.getTranslationKey(effect)));
                    }
                }
            }
        }
    }
}
