package com.salvaging;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("salvaging")
public interface SalvagingConfig extends Config
{
    // Shipwreck Highlight Section
    @ConfigSection(
            name = "Shipwreck Highlights",
            description = "Settings for shipwreck highlighting",
            position = 0
    )
    String shipwreckHighlightSection = "shipwreckHighlight";

    @ConfigItem(
            keyName = "highlightActiveShipwrecks",
            name = "Highlight Active Shipwrecks",
            description = "Highlights active (salvageable) shipwrecks in green",
            section = shipwreckHighlightSection,
            position = 0
    )
    default boolean highlightActiveShipwrecks()
    {
        return true;
    }

    @Alpha
    @ConfigItem(
            keyName = "activeShipwreckColor",
            name = "Active Shipwreck Color",
            description = "Color to highlight active shipwrecks",
            section = shipwreckHighlightSection,
            position = 1
    )
    default Color activeShipwreckColor()
    {
        return new Color(0, 255, 0, 255);
    }

    @ConfigItem(
            keyName = "highlightDepletedShipwrecks",
            name = "Highlight Depleted Shipwrecks",
            description = "Highlights depleted (inactive) shipwrecks in red",
            section = shipwreckHighlightSection,
            position = 2
    )
    default boolean highlightDepletedShipwrecks()
    {
        return true;
    }

    @Alpha
    @ConfigItem(
            keyName = "depletedShipwreckColor",
            name = "Depleted Shipwreck Color",
            description = "Color to highlight depleted shipwrecks",
            section = shipwreckHighlightSection,
            position = 3
    )
    default Color depletedShipwreckColor()
    {
        return new Color(255, 0, 0, 255);
    }

    // Salvage Range Section
    @ConfigSection(
            name = "Salvage Range",
            description = "Settings for the salvage range overlay",
            position = 1
    )
    String salvageRangeSection = "salvageRange";

    @ConfigItem(
            keyName = "showSalvageRange",
            name = "Show Salvage Range",
            description = "Highlights the tiles within salvage range of active shipwrecks",
            section = salvageRangeSection,
            position = 0
    )
    default boolean showSalvageRange()
    {
        return true;
    }

    @Alpha
    @ConfigItem(
            keyName = "tileFillColor",
            name = "Fill Color",
            description = "Color to fill the salvage range tiles",
            section = salvageRangeSection,
            position = 1
    )
    default Color tileFillColor()
    {
        return new Color(0, 255, 255, 0);
    }

    @Alpha
    @ConfigItem(
            keyName = "tileBorderColor",
            name = "Border Color",
            description = "Color of the tile borders",
            section = salvageRangeSection,
            position = 2
    )
    default Color tileBorderColor()
    {
        return new Color(0, 255, 255, 255);
    }

    @Range(min = 1, max = 5)
    @ConfigItem(
            keyName = "tileBorderWidth",
            name = "Border Width",
            description = "Width of the tile borders in pixels",
            section = salvageRangeSection,
            position = 3
    )
    default int tileBorderWidth()
    {
        return 2;
    }

    @Range(min = 0, max = 255)
    @ConfigItem(
            keyName = "fillOpacity",
            name = "Fill Opacity",
            description = "Opacity of the tile fill (0-255)",
            section = salvageRangeSection,
            position = 4
    )
    default int fillOpacity()
    {
        return 50;
    }

    // Salvage Overlap Section
    @ConfigSection(
            name = "Salvage Overlap (Double Spots)",
            description = "Settings for overlapping salvage ranges",
            position = 2
    )
    String salvageOverlapSection = "salvageOverlap";

    @ConfigItem(
            keyName = "showOverlap",
            name = "Show Overlap",
            description = "Highlights tiles where multiple salvage ranges overlap (double spots)",
            section = salvageOverlapSection,
            position = 0
    )
    default boolean showOverlap()
    {
        return true;
    }

    @Alpha
    @ConfigItem(
            keyName = "overlapFillColor",
            name = "Overlap Fill Color",
            description = "Color to fill overlapping salvage range tiles",
            section = salvageOverlapSection,
            position = 1
    )
    default Color overlapFillColor()
    {
        return new Color(255, 215, 0, 100);
    }

    @Alpha
    @ConfigItem(
            keyName = "overlapBorderColor",
            name = "Overlap Border Color",
            description = "Color of the overlap tile borders",
            section = salvageOverlapSection,
            position = 2
    )
    default Color overlapBorderColor()
    {
        return new Color(255, 215, 0, 255);
    }

    // Toggle Shipwrecks Section
    @ConfigSection(
            name = "Toggle Shipwrecks",
            description = "Enable or disable specific shipwreck types",
            position = 3,
            closedByDefault = true
    )
    String toggleShipwrecksSection = "toggleShipwrecks";

    @ConfigItem(
            keyName = "showSmallShipwreck",
            name = "Small Shipwreck",
            description = "Show highlights for Small Shipwrecks",
            section = toggleShipwrecksSection,
            position = 0
    )
    default boolean showSmallShipwreck()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showFishermanShipwreck",
            name = "Fisherman's Shipwreck",
            description = "Show highlights for Fisherman's Shipwrecks",
            section = toggleShipwrecksSection,
            position = 1
    )
    default boolean showFishermanShipwreck()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showBarracudaShipwreck",
            name = "Barracuda Shipwreck",
            description = "Show highlights for Barracuda Shipwrecks",
            section = toggleShipwrecksSection,
            position = 2
    )
    default boolean showBarracudaShipwreck()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showLargeShipwreck",
            name = "Large Shipwreck",
            description = "Show highlights for Large Shipwrecks",
            section = toggleShipwrecksSection,
            position = 3
    )
    default boolean showLargeShipwreck()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showPirateShipwreck",
            name = "Pirate Shipwreck",
            description = "Show highlights for Pirate Shipwrecks",
            section = toggleShipwrecksSection,
            position = 4
    )
    default boolean showPirateShipwreck()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showMercenaryShipwreck",
            name = "Mercenary Shipwreck",
            description = "Show highlights for Mercenary Shipwrecks",
            section = toggleShipwrecksSection,
            position = 5
    )
    default boolean showMercenaryShipwreck()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showFremennikShipwreck",
            name = "Fremennik Shipwreck",
            description = "Show highlights for Fremennik Shipwrecks",
            section = toggleShipwrecksSection,
            position = 6
    )
    default boolean showFremennikShipwreck()
    {
        return true;
    }

    @ConfigItem(
            keyName = "showMerchantShipwreck",
            name = "Merchant Shipwreck",
            description = "Show highlights for Merchant Shipwrecks",
            section = toggleShipwrecksSection,
            position = 7
    )
    default boolean showMerchantShipwreck()
    {
        return true;
    }
}
