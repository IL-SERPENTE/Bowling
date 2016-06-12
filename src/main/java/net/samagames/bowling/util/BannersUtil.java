/**
 *  Banners Util Class
 *  Copyright (C) 2015 Florian Cassayre
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package net.samagames.bowling.util;

import java.util.*;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public final class BannersUtil {

    /**
     * The map containing all the supported characters. White color represents the background and black color the font color.
     */
    private final static Map<Character, BannerMeta> chars = new HashMap<>();

    static {
        BannersUtil.chars.put('a', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_TOP, PatternType.STRIPE_LEFT, PatternType.STRIPE_RIGHT, PatternType.STRIPE_MIDDLE)));
        BannersUtil.chars.put('b', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_TOP, PatternType.STRIPE_LEFT, PatternType.STRIPE_RIGHT, PatternType.STRIPE_MIDDLE, PatternType.STRIPE_BOTTOM)));
        BannersUtil.chars.put('c', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_LEFT, PatternType.STRIPE_TOP, PatternType.STRIPE_BOTTOM)));
        BannersUtil.chars.put('d', getBannerMeta(DyeColor.BLACK, Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT))));
        BannersUtil.chars.put('e', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT))));
        BannersUtil.chars.put('f', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP))));
        BannersUtil.chars.put('g', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP))));
        BannersUtil.chars.put('h', getBannerMeta(DyeColor.BLACK, Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT))));
        BannersUtil.chars.put('i', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_BOTTOM, PatternType.STRIPE_TOP, PatternType.STRIPE_CENTER)));
        BannersUtil.chars.put('j', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT))));
        BannersUtil.chars.put('k', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT))));
        BannersUtil.chars.put('l', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_LEFT, PatternType.STRIPE_BOTTOM)));
        BannersUtil.chars.put('m', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP), new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_TOP), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT))));
        BannersUtil.chars.put('n', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_TOP), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT))));
        BannersUtil.chars.put('o', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_TOP, PatternType.STRIPE_RIGHT, PatternType.STRIPE_BOTTOM, PatternType.STRIPE_LEFT)));
        BannersUtil.chars.put('p', getBannerMeta(DyeColor.BLACK, Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT))));
        BannersUtil.chars.put('q', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP))));
        BannersUtil.chars.put('r', getBannerMeta(DyeColor.BLACK, Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE))));
        BannersUtil.chars.put('s', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP), new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_RIGHT), new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT), new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT))));
        BannersUtil.chars.put('t', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_CENTER, PatternType.STRIPE_TOP)));
        BannersUtil.chars.put('u', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_BOTTOM, PatternType.STRIPE_RIGHT, PatternType.STRIPE_LEFT)));
        BannersUtil.chars.put('v', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT))));
        BannersUtil.chars.put('w', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT))));
        BannersUtil.chars.put('x', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT, PatternType.STRIPE_DOWNRIGHT)));
        BannersUtil.chars.put('y', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL_MIRROR), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT))));
        BannersUtil.chars.put('z', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP), new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_LEFT), new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_RIGHT), new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT))));

        BannersUtil.chars.put('0', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_TOP, PatternType.STRIPE_RIGHT, PatternType.STRIPE_BOTTOM, PatternType.STRIPE_LEFT)));
        BannersUtil.chars.put('1', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_LEFT), new Pattern(DyeColor.WHITE, PatternType.BORDER), new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER))));
        BannersUtil.chars.put('2', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP), new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_LEFT), new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_RIGHT), new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT))));
        BannersUtil.chars.put('3', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP))));
        BannersUtil.chars.put('4', getBannerMeta(DyeColor.BLACK, Arrays.asList(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE))));
        BannersUtil.chars.put('5', getBannerMeta(DyeColor.BLACK, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR), new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT_MIRROR), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP))));
        BannersUtil.chars.put('6', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT), new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL), new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM), new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP))));
        BannersUtil.chars.put('7', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP), new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT), new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT))));
        BannersUtil.chars.put('8', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_TOP, PatternType.STRIPE_LEFT, PatternType.STRIPE_RIGHT, PatternType.STRIPE_MIDDLE, PatternType.STRIPE_BOTTOM)));
        BannersUtil.chars.put('9', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR), new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP), new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT))));

        BannersUtil.chars.put('.', getBannerMeta(DyeColor.WHITE, Collections.singletonList(new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT))));
        BannersUtil.chars.put(' ', getBannerMeta(DyeColor.WHITE, new ArrayList<>()));
        BannersUtil.chars.put('-', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE), new Pattern(DyeColor.WHITE, PatternType.BORDER))));
        BannersUtil.chars.put(':', getBannerMeta(DyeColor.BLACK, getPatterns(DyeColor.WHITE, PatternType.STRIPE_TOP, PatternType.STRIPE_LEFT, PatternType.STRIPE_RIGHT, PatternType.STRIPE_MIDDLE, PatternType.STRIPE_BOTTOM)));
        BannersUtil.chars.put(';', getBannerMeta(DyeColor.BLACK, getPatterns(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT, PatternType.STRIPE_LEFT, PatternType.STRIPE_TOP, PatternType.STRIPE_MIDDLE, PatternType.BORDER)));

        BannersUtil.chars.put('[', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.BORDER), new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT))));
        BannersUtil.chars.put(']', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.BORDER), new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_MIRROR), new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT), new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT))));
        BannersUtil.chars.put('/', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT)));
        BannersUtil.chars.put('\\', getBannerMeta(DyeColor.WHITE, getPatterns(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT)));
        BannersUtil.chars.put('*', getBannerMeta(DyeColor.WHITE, Arrays.asList(new Pattern(DyeColor.BLACK, PatternType.CROSS), new Pattern(DyeColor.BLACK, PatternType.STRAIGHT_CROSS), new Pattern(DyeColor.WHITE, PatternType.BORDER), new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER))));
    }

    /**
     * Return a list of patterns with the same color.
     * @param color The generic color that will be used
     * @param type The layers to be printed on the banner using the chosen color
     * @return A copy of this banner
     */
    public static List<Pattern> getPatterns(DyeColor color, PatternType... type) {
        List<Pattern> patterns = new ArrayList<>();
        for (PatternType patternType : type)
            patterns.add(new Pattern(color, patternType));
        return patterns;
    }

    /**
     * Returns a banner containing the patterns with the background color.
     * @param color The background color
     * @param patterns The layers to be printed on the banner
     * @return An {@link ItemStack} containing these parameters
     */
    public static ItemStack getBanner(DyeColor color, List<Pattern> patterns) {
        ItemStack banner = new ItemStack(Material.BANNER);
        banner.setItemMeta(getBannerMeta(color, patterns));
        return banner;
    }

    /**
     * Returns a banner meta corresponding to these parameters.
     * @param color The background color
     * @param patterns The layers to be printed on the banner
     * @return An ItemMeta containing these parameters
     */
    public static BannerMeta getBannerMeta(DyeColor color, List<Pattern> patterns) {
        BannerMeta meta = (BannerMeta) new ItemStack(Material.BANNER).getItemMeta();
        meta.setBaseColor(color);
        meta.setPatterns(patterns);
        return meta;
    }

    /**
     * Returns a banner item stack which represents the following character, with a specified background and the specified color.
     * Throws IllegalArgumentException if the character isn't registered.
     * @param c The character to be printed
     * @param background The background color
     * @param color The font color
     * @return A banner {@link ItemStack} with the corresponding character using a basic font system
     */
    public static ItemStack getCharBanner(char c, DyeColor background, DyeColor color) {
        return getCharBanner(c, background, color, false);
    }

    /**
     * Returns a banner item stack which represents the following character, with a specified background and the specified color, and possibly a border.
     * Throws IllegalArgumentException if the character isn't registered.
     * @param c The character to be printed
     * @param background The background color
     * @param color The font color
     * @param border If set to {@code true}, then a border will be applied to the banner
     * @return A banner {@link ItemStack} with the corresponding character using a basic font system
     */
    @SuppressWarnings("deprecation")
    public static ItemStack getCharBanner(char c, DyeColor background, DyeColor color, boolean border) {
        BannerMeta meta = BannersUtil.chars.get(Character.toLowerCase(c));

        if(meta == null)
            throw new IllegalArgumentException("This character can't be reproduced on a banner !");

        List<Pattern> patterns = meta.getPatterns();

        patterns = clonePatterns(patterns);

        for(int i = 0; i < patterns.size(); i++) {
            DyeColor patternColor = patterns.get(i).getColor().equals(DyeColor.BLACK) ? color : background;
            patterns.set(i, new Pattern(patternColor, patterns.get(i).getPattern()));
        }

        if(border)
            patterns.add(new Pattern(background, PatternType.BORDER));

        return getBanner(meta.getBaseColor().equals(DyeColor.WHITE) ? background : color, patterns);
    }

    /**
     * Copy the item's attributes to the banner block.
     * Throws IllegalArgumentException if the specified item is not a banner.
     * @param item The ItemStack to be copied
     * @param banner The banner block to copy
     */
    public static void editBanner(ItemStack item, Banner banner) {
        if(item.getType() != Material.BANNER) throw new IllegalArgumentException("The specified ItemStack isn't a banner !");
        BannerMeta meta = (BannerMeta) item.getItemMeta();
        banner.setBaseColor(meta.getBaseColor());
        banner.setPatterns(meta.getPatterns());
        banner.update();
    }

    @Deprecated
    private static List<Pattern> clonePatterns(List<Pattern> list) {
        List<Pattern> newList = new ArrayList<>();
        list.forEach(pattern -> newList.add(new Pattern(pattern.getColor(), pattern.getPattern())));
        return newList;
    }

    /**
     * Prevents the class to be instantiated.
     */
    private BannersUtil() {}
}