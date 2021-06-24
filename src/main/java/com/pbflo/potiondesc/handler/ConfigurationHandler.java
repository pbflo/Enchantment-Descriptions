package com.pbflo.potiondesc.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public final class ConfigurationHandler {

    private ConfigurationHandler () {

        // Prevent people from constructing this, even though it's just me lol
    }

    /**
     * An instance of the Configuration object being used.
     */
    private static Configuration config = null;

    /**
     * Whether or not undocumented effects should be listed.
     */
    private static boolean exploreMode = false;

    /**
     * Determines in which color the title and description are drawn
     */
    private static String titleColor = "7";
    private static String descColor = "7";


    /**
     * Initializes the configuration file.
     *
     * @param file The file to read/write config stuff to.
     */
    public static void initConfig (File file) {

        config = new Configuration(file);
        syncConfig();
    }

    /**
     * Syncs all configuration properties.
     */
    public static void syncConfig () {
        setExploreMode(config.getBoolean("exploreMode", Configuration.CATEGORY_GENERAL, false, "Should the mod generate a list of potion effects from the instance that have no description? (only outputs the Effects that have a registered potion type)"));

        setTitleColor(config.getString("titleColor", Configuration.CATEGORY_CLIENT, "b", "The color of the effect name\n\n" +
                "possible values:\n" +
                "  \"0\" = Black (not advised)\n" +
                "  \"1\" = Dark Blue\n"+
                "  \"2\" = Dark Green\n"+
                "  \"3\" = Dark Aqua\n"+
                "  \"4\" = Dark Red\n"+
                "  \"5\" = Dark Purple\n" +
                "  \"6\" = Gold\n"+
                "  \"7\" = Gray\n"+
                "  \"8\" = Dark Gray\n"+
                "  \"9\" = Black\n"+
                "  \"a\" = Green\n"+
                "  \"b\" = Aqua\n"+
                "  \"c\" = Red\n"+
                "  \"d\" = Light Purple\n"+
                "  \"e\" = Yellow\n"+
                "  \"f\" = White\n", new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"}));
        setDescColor(config.getString("descColor", Configuration.CATEGORY_CLIENT, "f", "The color of the effect description\n\n"+
                "possible values:\n" +
                "  \"0\" = Black (not advised)\n" +
                "  \"1\" = Dark Blue\n"+
                "  \"2\" = Dark Green\n"+
                "  \"3\" = Dark Aqua\n"+
                "  \"4\" = Dark Red\n"+
                "  \"5\" = Dark Purple\n" +
                "  \"6\" = Gold\n"+
                "  \"7\" = Gray\n"+
                "  \"8\" = Dark Gray\n"+
                "  \"9\" = Black\n"+
                "  \"a\" = Green\n"+
                "  \"b\" = Aqua\n"+
                "  \"c\" = Red\n"+
                "  \"d\" = Light Purple\n"+
                "  \"e\" = Yellow\n"+
                "  \"f\" = White\n", new String[]{"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"}));

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static boolean isExploreMode () {
        return exploreMode;
    }

    public static void setExploreMode (boolean exploreMode) {
        ConfigurationHandler.exploreMode = exploreMode;
    }

    public static char getTitleColor () {
        return titleColor.charAt(0);
    }

    public static void setTitleColor (String titlecolor) {
        ConfigurationHandler.titleColor = titlecolor;
    }

    public static char getDescColor () {
        return descColor.charAt(0);
    }

    public static void setDescColor (String desccolor) {
        ConfigurationHandler.descColor = desccolor;
    }
}
