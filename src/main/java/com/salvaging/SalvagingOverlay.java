package com.salvaging;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Singleton
public class SalvagingOverlay extends Overlay
{
    private static final int SALVAGE_RANGE = 7; // 7 tiles from the shipwreck edge
    private static final int SHIPWRECK_SIZE = 2; // Shipwrecks are 2x2

    private final Client client;
    private final SalvagingPlugin plugin;
    private final SalvagingConfig config;

    @Inject
    private SalvagingOverlay(Client client, SalvagingPlugin plugin, SalvagingConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        // First, calculate which tiles are in overlap zones (only for active shipwrecks)
        Map<WorldPoint, Integer> tileOverlapCount = new HashMap<>();

        for (GameObject shipwreck : plugin.getActiveShipwrecks())
        {
            if (!plugin.isShipwreckEnabled(shipwreck))
            {
                continue;
            }

            // Only count salvageable (active) shipwrecks for overlap calculation
            if (plugin.isShipwreckDepleted(shipwreck))
            {
                continue;
            }

            if (!config.showSalvageRange())
            {
                continue;
            }

            WorldPoint shipwreckLocation = shipwreck.getWorldLocation();
            int minX = shipwreckLocation.getX() - SALVAGE_RANGE;
            int maxX = shipwreckLocation.getX() + SHIPWRECK_SIZE - 1 + SALVAGE_RANGE;
            int minY = shipwreckLocation.getY() - SALVAGE_RANGE;
            int maxY = shipwreckLocation.getY() + SHIPWRECK_SIZE - 1 + SALVAGE_RANGE;
            int plane = shipwreckLocation.getPlane();

            for (int x = minX; x <= maxX; x++)
            {
                for (int y = minY; y <= maxY; y++)
                {
                    WorldPoint tilePoint = new WorldPoint(x, y, plane);
                    tileOverlapCount.put(tilePoint, tileOverlapCount.getOrDefault(tilePoint, 0) + 1);
                }
            }
        }

        // Now render the shipwrecks
        for (GameObject shipwreck : plugin.getActiveShipwrecks())
        {
            if (!plugin.isShipwreckEnabled(shipwreck))
            {
                continue;
            }

            WorldPoint shipwreckLocation = shipwreck.getWorldLocation();
            boolean isDepleted = plugin.isShipwreckDepleted(shipwreck);

            // Highlight the shipwreck itself
            if (isDepleted)
            {
                // Depleted/inactive shipwrecks - RED
                if (config.highlightDepletedShipwrecks())
                {
                    renderShipwreckHighlight(graphics, shipwreck, config.depletedShipwreckColor());
                }
            }
            else
            {
                // Active/salvageable shipwrecks - GREEN
                if (config.highlightActiveShipwrecks())
                {
                    renderShipwreckHighlight(graphics, shipwreck, config.activeShipwreckColor());
                }

                // Render salvage range for active shipwrecks
                if (config.showSalvageRange())
                {
                    renderSalvageRange(graphics, shipwreckLocation, tileOverlapCount);
                }
            }
        }

        return null;
    }

    private void renderShipwreckHighlight(Graphics2D graphics, GameObject shipwreck, Color color)
    {
        LocalPoint localPoint = shipwreck.getLocalLocation();
        if (localPoint != null)
        {
            Polygon polygon = Perspective.getCanvasTileAreaPoly(client, localPoint, SHIPWRECK_SIZE);
            if (polygon != null)
            {
                // Fill with semi-transparent color
                graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
                graphics.fillPolygon(polygon);
                // Border with full color
                graphics.setColor(color);
                graphics.setStroke(new BasicStroke(2));
                graphics.drawPolygon(polygon);
            }
        }
    }

    private void renderSalvageRange(Graphics2D graphics, WorldPoint shipwreckLocation, Map<WorldPoint, Integer> tileOverlapCount)
    {
        int minX = shipwreckLocation.getX() - SALVAGE_RANGE;
        int maxX = shipwreckLocation.getX() + SHIPWRECK_SIZE - 1 + SALVAGE_RANGE;
        int minY = shipwreckLocation.getY() - SALVAGE_RANGE;
        int maxY = shipwreckLocation.getY() + SHIPWRECK_SIZE - 1 + SALVAGE_RANGE;
        int plane = shipwreckLocation.getPlane();

        Color fillColor = new Color(
                config.tileFillColor().getRed(),
                config.tileFillColor().getGreen(),
                config.tileFillColor().getBlue(),
                config.fillOpacity()
        );
        Color borderColor = config.tileBorderColor();
        int borderWidth = config.tileBorderWidth();

        Color overlapFillColor = new Color(
                config.overlapFillColor().getRed(),
                config.overlapFillColor().getGreen(),
                config.overlapFillColor().getBlue(),
                config.overlapFillColor().getAlpha()
        );
        Color overlapBorderColor = config.overlapBorderColor();

        // Render individual tiles
        for (int x = minX; x <= maxX; x++)
        {
            for (int y = minY; y <= maxY; y++)
            {
                WorldPoint tilePoint = new WorldPoint(x, y, plane);

                // Check if this tile is in an overlap zone (double spot)
                boolean isOverlap = config.showOverlap() && tileOverlapCount.getOrDefault(tilePoint, 0) > 1;

                // Determine which edges should have borders (only outer edges)
                boolean isWestEdge = (x == minX);
                boolean isEastEdge = (x == maxX);
                boolean isSouthEdge = (y == minY);
                boolean isNorthEdge = (y == maxY);

                // Use overlap colors if this tile overlaps
                Color tileFill = isOverlap ? overlapFillColor : fillColor;
                Color tileBorder = isOverlap ? overlapBorderColor : borderColor;

                renderTileWithSelectiveBorder(graphics, tilePoint, tileFill, tileBorder, borderWidth,
                        isWestEdge, isEastEdge, isSouthEdge, isNorthEdge);
            }
        }
    }

    private void renderTileWithSelectiveBorder(Graphics2D graphics, WorldPoint worldPoint,
                                               Color fillColor, Color borderColor, int borderWidth,
                                               boolean drawWest, boolean drawEast, boolean drawSouth, boolean drawNorth)
    {
        LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
        if (localPoint == null)
        {
            return;
        }

        Polygon tilePoly = Perspective.getCanvasTilePoly(client, localPoint);
        if (tilePoly == null)
        {
            return;
        }

        // Draw fill
        graphics.setColor(fillColor);
        graphics.fillPolygon(tilePoly);

        // Draw only the outer edges
        // Tile polygon points are ordered: 0=SW, 1=SE, 2=NE, 3=NW
        graphics.setColor(borderColor);
        graphics.setStroke(new BasicStroke(borderWidth));

        if (drawSouth)
        {
            graphics.drawLine(tilePoly.xpoints[0], tilePoly.ypoints[0],
                    tilePoly.xpoints[1], tilePoly.ypoints[1]);
        }
        if (drawEast)
        {
            graphics.drawLine(tilePoly.xpoints[1], tilePoly.ypoints[1],
                    tilePoly.xpoints[2], tilePoly.ypoints[2]);
        }
        if (drawNorth)
        {
            graphics.drawLine(tilePoly.xpoints[2], tilePoly.ypoints[2],
                    tilePoly.xpoints[3], tilePoly.ypoints[3]);
        }
        if (drawWest)
        {
            graphics.drawLine(tilePoly.xpoints[3], tilePoly.ypoints[3],
                    tilePoly.xpoints[0], tilePoly.ypoints[0]);
        }
    }
}
