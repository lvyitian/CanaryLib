package net.canarymod;

import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.UnknownWorldException;
import net.canarymod.api.world.World;
import net.canarymod.config.Configuration;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Set of miscellaneous tools
 *
 * @author Chris (damagefilter)
 * @author Jason (darkdiplomat)
 */
public class ToolBox {

    private static TimeZone tz_GMT = TimeZone.getTimeZone("GMT");

    /**
     * Merge 2 arrays. This will just merge two arrays.
     *
     * @param first
     *         the first array to be merged
     * @param second
     *         the second array to be merged
     *
     * @return array containing all elements of the 2 given ones
     */
    public static <T> T[] arrayMerge(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * Merge 2 integer arrays. This will just merge two arrays.
     *
     * @param first
     *         the first array to be merged
     * @param second
     *         the second array to be merged
     *
     * @return array containing all elements of the 2 given ones
     */
    public static int[] arrayMerge(int[] first, int[] second) {
        int[] result = Arrays.copyOf(first, first.length + second.length);

        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * Merge 2 arrays. This will remove duplicates.
     *
     * @param first
     *         the first array to be merged
     * @param second
     *         the second array to be merged
     * @param template
     *         the array to use as a template for merging the arrays into one
     *
     * @return array containing all elements of the given 2 arrays, minus duplicate entries
     */
    public static <T> T[] safeArrayMerge(T[] first, T[] second, T[] template) {
        HashSet<T> res = new HashSet<T>();

        Collections.addAll(res, first);
        Collections.addAll(res, second);
        return res.toArray(template);
    }

    /**
     * If the given string is "null" or null, this method returns null,
     * otherwise it will return the string as it was passed
     *
     * @param str
     *         the {@link String} to parse
     *
     * @return {@code null} if the {@link String} is {@code null} or is equal to {@code "null"}; the {@link String} value otherwise
     */
    public static String stringToNull(String str) {
        if (str == null) {
            return null;
        }
        if (str.equalsIgnoreCase("null")) {
            return null;
        }
        else {
            return str;
        }
    }

    /**
     * Round a entity ordinate to a valid block location component
     * This takes into account the rounding issues in negative x/z direction
     *
     * @param num
     *         the {@code double} value to round to the nearest lower integer
     *
     * @return the rounded number
     */
    public static int floorToBlock(double num) {
        int i = (int) num;

        return num < i ? i - 1 : i;
    }

    /**
     * Parses a fully qualified world name as generated by World.getFqName() to a {@link World} object.
     * If only the world group name is given (that's "default" by default),
     * then the NORMAL dimension will be assumed.
     *
     * @param world
     *         the fully qualified world name or a world group name.
     *
     * @return the world that was parsed or null if the world didn't exist or wasn't loaded.
     */
    public static World parseWorld(String world) {
        world = world.replace(":", "_"); // some inputs come with a : as separator!
        DimensionType t = DimensionType.fromName(world.substring(Math.max(0, world.lastIndexOf("_"))));
        String nameOnly = world.substring(0, Math.max(0, world.lastIndexOf("_")));
        try {
            if (t != null) {
                return Canary.getServer().getWorldManager().getWorld(nameOnly, t, false);
            }
            else {
                return Canary.getServer().getWorldManager().getWorld(nameOnly, DimensionType.fromId(0), false);
            }
        }
        catch (UnknownWorldException e) {
            return null;
        }
    }

    /**
     * Get the Unix timestamp for the current time
     *
     * @return {@code long} timestamp
     */
    public static long getUnixTimestamp() {
        return (System.currentTimeMillis() / 1000L);
    }

    /**
     * Parse number of seconds for the given time and TimeUnit String<br>
     * Example: long 1 String HOUR will give you number of seconds in 1 hour.<br>
     * This is used to work with Unix timestamps.
     *
     * @param time
     *         the {@code long} time
     * @param timeUnit
     *         MINUTES, HOURS, DAYS, WEEKS, MONTHS
     *
     * @return {@code long} parsed time
     */
    public static long parseTime(long time, String timeUnit) {

        if (timeUnit.toLowerCase().startsWith("min")) {
            time *= 60;
        }
        else if (timeUnit.toLowerCase().startsWith("h")) { //hours
            time *= 3600;
        }
        else if (timeUnit.toLowerCase().startsWith("d")) { //days
            time *= 86400;
        }
        else if (timeUnit.toLowerCase().startsWith("w")) { //weeks
            time *= 604800;
        }
        else if (timeUnit.toLowerCase().startsWith("mo")) { //months
            time *= 2629743;
        }
        else {
            throw new NumberFormatException(timeUnit + " is not a valid time unit!");
        }
        return time;
    }

    /**
     * Parse number of milliseconds for the given time and TimeUnit String<br>
     * Example: long 1 String HOUR will give you number of seconds in 1 hour.<br>
     * This is used to work with Unix timestamps.
     *
     * @param time
     *         the {@code long} time
     * @param timeUnit
     *         MINUTES, HOURS, DAYS, WEEKS, MONTHS
     *
     * @return {@code long} parsed time
     */
    public static long parseTimeInMillis(long time, String timeUnit) {
        if (timeUnit.toLowerCase().startsWith("minute")) {
            time *= 60;
        }
        else if (timeUnit.toLowerCase().startsWith("hour")) {
            time *= 3600;
        }
        else if (timeUnit.toLowerCase().startsWith("day")) {
            time *= 86400;
        }
        else if (timeUnit.toLowerCase().startsWith("week")) {
            time *= 604800;
        }
        else if (timeUnit.toLowerCase().startsWith("month")) {
            time *= 2629743;
        }
        else {
            throw new NumberFormatException(timeUnit + " is not a valid time unit!");
        }
        return time * 1000;
    }

    /**
     * Parse number of seconds for the given time and TimeUnit<br>
     * Example: long 1 String {@link TimeUnit#HOURS} will give you number of
     * seconds in 1 hour.<br>
     * This is used to work with unix timestamps.
     *
     * @param time
     *         the {@code long} time
     * @param unit
     *         the {@link TimeUnit} to use for conversion
     *
     * @return {@code long} parsed time
     */
    public static long parseTime(long time, TimeUnit unit) {
        return unit.convert(time, TimeUnit.SECONDS);
    }

    /**
     * Formats a Unix timestamp into the date format defined in server.cfg
     *
     * @param timestamp
     *         the {@code long} time
     *
     * @return {@link String} formatted TimeStamp
     */
    public static String formatTimestamp(long timestamp) {
        return new SimpleDateFormat(Configuration.getServerConfig().getDateFormat()).format(timestamp * 1000);
    }

    /**
     * Form ats a Unix timestamp into the date format specified by {@code format}
     *
     * @param timestamp
     * @param format
     *
     * @return {@link String} formatted TimeStamp
     */
    public static String formatTimestamp(long timestamp, String format) {
        return new SimpleDateFormat(format).format(timestamp * 1000);
    }

    /**
     * Gets a readable string for the days/hours/minutes/seconds until a period of time
     *
     * @param time
     *         the Unix-TimeStamp to start from
     * @param delay
     *         the delay from the start point until expiration
     *
     * @return the String representation of the time until
     */
    public static String getTimeUntil(long time, long delay) {
        long correctedTime = (time + delay) - getUnixTimestamp();
        if (correctedTime <= 0) {
            return "ERR Time";
        }
        return getTimeUntil(correctedTime);
    }

    /**
     * Gets a readable string for the days/hours/minutes/seconds until a period of time
     *
     * @param time
     *         the Unix-TimeStamp of (or amount of seconds until) the future time expiration
     *
     * @return the String representation of the time until
     */
    public static String getTimeUntil(long time) {
        if (time <= 0) {
            return "ERR Time";
        }
        // How many days left?
        StringBuilder stringTimeLeft = new StringBuilder();
        if (time >= 60 * 60 * 24) {
            int days = (int) Math.floor(time / (60 * 60 * 24));
            time -= 60 * 60 * 24 * days;
            if (days == 1) {
                stringTimeLeft.append(Integer.toString(days)).append(" day, ");
            }
            else {
                stringTimeLeft.append(Integer.toString(days)).append(" days, ");
            }
        }
        if (time >= 60 * 60) {
            int hours = (int) Math.floor(time / (60 * 60));
            time -= 60 * 60 * hours;
            if (hours == 1) {
                stringTimeLeft.append(Integer.toString(hours)).append(" hour, ");
            }
            else {
                stringTimeLeft.append(Integer.toString(hours)).append(" hours, ");
            }
        }
        if (time >= 60) {
            int minutes = (int) Math.floor(time / (60));
            time -= 60 * minutes;
            if (minutes == 1) {
                stringTimeLeft.append(Integer.toString(minutes)).append(" minute ");
            }
            else {
                stringTimeLeft.append(Integer.toString(minutes)).append(" minutes ");
            }
        }
        else {
            // Lets remove the last comma, since it will look bad with 2 days, 3 hours, and 14 seconds.
            if (stringTimeLeft.length() != 0) {
                stringTimeLeft.deleteCharAt(stringTimeLeft.length() - 1);
            }
        }
        int secs = (int) time;
        if (stringTimeLeft.length() != 0) {
            stringTimeLeft.append("and ");
        }
        if (secs == 1) {
            stringTimeLeft.append(secs).append(" second.");
        }
        else {
            stringTimeLeft.append(secs).append(" seconds.");
        }
        return stringTimeLeft.toString();
    }

    /**
     * Generates a Calendar(GMT, English) object representing the current time of day from the time of a world.
     * This time can be retrieved from World.getRelativeTime().
     * Year, month and day values may be odd as those are not contained within the range
     * of the world tick times in Minecraft.
     *
     * @param ticks
     *         the relative time of a world
     *
     * @return Calendar object representing the world time as real date
     */
    public static Calendar worldTicksToCalendar(long ticks) {
        long tickMillis = TimeUnit.HOURS.toMillis(6) + (ticks * 3600); // Add 6 hours and assume each tick is 3600 mcmillis
        Calendar calendar = Calendar.getInstance(tz_GMT, Locale.ENGLISH);
        calendar.setTimeInMillis(tickMillis);
        return calendar;
    }

    /**
     * Converts World relative time into a 24 hour clock format
     *
     * @param ticks
     *         the current time of a world, retrieved by World.getRelativeTime()
     *
     * @return 24 hour clock formatted string of the time
     */
    public static String worldTimeTo24hClock(long ticks) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(tz_GMT);
        return dateFormat.format(worldTicksToCalendar(ticks).getTime());
    }

    /**
     * Converts World relative time into a 12 hour clock format
     *
     * @param ticks
     *         the current time of a world, retrieved by World.getRelativeTime()
     *
     * @return 12 hour clock formatted string of the time
     */
    public static String worldTimeTo12hClock(long ticks) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        dateFormat.setTimeZone(tz_GMT);
        return dateFormat.format(worldTicksToCalendar(ticks).getTime());
    }


    /**
     * Calculate experience points from the given level,
     * The returned value can be passed to Player.set/remove/addExperience.
     *
     * @param level
     *         the level you want to get the Experience points for
     *
     * @return the amount of experience points for the given level
     */
    public static int levelToExperience(int level) {
        //source: http://minecraft.gamepedia.com/Experience#Formulas_and_Tables
        int mid = Math.max(0, level - 15);
        int high = Math.max(0, level - 30);
        return level * 17 + (mid * (mid - 1) / 2) * 3 + (high * (high - 1) / 2) * 7;
    }
}
