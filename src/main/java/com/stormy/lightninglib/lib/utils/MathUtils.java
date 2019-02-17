package com.stormy.lightninglib.lib.utils;

import java.util.Random;
import java.util.UUID;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MathUtils {

    public static final double phi = 1.618033988749894;
    public static final double pi = Math.PI;
    public static final double todeg = 57.29577951308232;
    public static final double torad = 0.017453292519943;
    public static final double sqrt2 = 1.414213562373095;

    public static final double lengthSq(double x, double y, double z) { return x * x + y * y + z * z; }

    public static final double lengthSq(double x, double z) { return x * x + z * z; }

    public static float getPercent(double number, double total) {
        return (float) (number * 100.0D / total);
    }

    public static double[] SIN_TABLE = new double[65536];

    static {
        for (int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = Math.sin(i / 65536D * 2 * Math.PI); }

        SIN_TABLE[0] = 0;
        SIN_TABLE[16384] = 1;
        SIN_TABLE[32768] = 0;
        SIN_TABLE[49152] = 1; }

    public static double sin(double d) {
        return SIN_TABLE[(int) ((float) d * 10430.378F) & 65535];
    }
    public static double cos(double d) {
        return SIN_TABLE[(int) ((float) d * 10430.378F + 16384.0F) & 65535];
    }

    public static int[] longToIntArray(long value) {
        int[] digits = Long.toString(value).chars().map(c -> c -= '0').toArray();
        return digits;
    }

    public static int getSphericalDistance(CoordinateUtils startPos, CoordinateUtils endPos) {
        final int dx = endPos.x - startPos.x;
        final int dy = endPos.z - startPos.z;
        final int dz = endPos.y - startPos.y;
        return (int)Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz));
    }

    public static float approachLinear(float a, float b, float max) {
        return (a > b) ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max); }

    public static double approachLinear(double a, double b, double max) {
        return (a > b) ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max); }

    public static float interpolate(float a, float b, float d) {
        return a + (b - a) * d;
    }

    public static double interpolate(double a, double b, double d) {
        return a + (b - a) * d;
    }

    public static double approachExp(double a, double b, double ratio) {
        return a + (b - a) * ratio;
    }

    public static double approachExp(double a, double b, double ratio, double cap) {
        double d = (b - a) * ratio;
        if (Math.abs(d) > cap) {
            d = Math.signum(d) * cap;
        }
        return a + d;
    }

    public static double retreatExp(double a, double b, double c, double ratio, double kick) {
        double d = (Math.abs(c - a) + kick) * ratio;
        if (d > Math.abs(b - a)) {
            return b;
        }
        return a + Math.signum(b - a) * d;
    }


    public static int getCubicDistance(CoordinateUtils startPos, CoordinateUtils endPos) {
        return Math.abs(endPos.x - startPos.x) + Math.abs(endPos.y - startPos.y) + Math.abs(endPos.z - startPos.z);
    }

    public static int getHorSquaredDistance(CoordinateUtils startPos, CoordinateUtils endPos) {
        return Math.abs(endPos.x - startPos.x) + Math.abs(endPos.z - startPos.z);
    }

    public static int getVerDistance(CoordinateUtils startPos, CoordinateUtils endPos) {
        return Math.abs(endPos.y - startPos.y);
    }

    public static double getDistanceRatioToCenter(int point1, int point2, int pos) {
        double radius = Math.abs(point2 - point1) / 2D;
        double dar = Math.abs(Math.abs(pos - point1) - radius);
        return radius != 0.0D? dar / radius : 0.0D;
    }

    public static int parseInt(String string) {
        return parseInt(string, 0);
    }

    public static int parseInt(String string, int defaultValue) {
        try {
            return Integer.parseInt(string.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static long intArrayToLong(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int element : array) {
            sb.append(element);
        }
        return Long.parseLong(sb.toString());
    }

    public static int getRandom(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static double clip(double value, double min, double max) {
        if (value > max) {
            value = max;
        }
        if (value < min) {
            value = min;
        }
        return value;
    }

    public static float clip(float value, float min, float max) {
        if (value > max) {
            value = max;
        }
        if (value < min) {
            value = min;
        }
        return value;
    }

    public static boolean between(double min, double value, double max) {
        return min <= value && value <= max;
    }

    public static final float SQRT_2 = sqrt(2.0F);
    /** A table of sin values computed from 0 (inclusive) to 2*pi (exclusive), with steps of 2*PI / 65536. */
    private static final Random RANDOM = new Random();
    /**
     * Though it looks like an array, this is really more like a mapping.  Key (index of this array) is the upper 5 bits
     * of the result of multiplying a 32-bit unsigned integer by the B(2, 5) De Bruijn sequence 0x077CB531.  Value
     * (value stored in the array) is the unique index (from the right) of the leftmost one-bit in a 32-bit unsigned
     * integer that can cause the upper 5 bits to get that value.  Used for highly optimized "find the log-base-2 of
     * this number" calculations.
     */
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    private static final double FRAC_BIAS;
    private static final double[] ASINE_TAB;
    private static final double[] COS_TAB;

    /**
     * sin looked up in a table
     */


    /**
     * cos looked up in the sin table with the appropriate offset
     */
    public static double cos(float value) {
        return SIN_TABLE[(int) (value * 10430.378F + 16384.0F) & 65535];
    }

    public static float sqrt(float value) {
        return (float) Math.sqrt(value);
    }

    public static float sqrt(double value) {
        return (float) Math.sqrt(value);
    }

    /**
     * Returns the greatest integer less than or equal to the float argument
     */
    public static int floor(float value) {
        int i = (int) value;
        return value < i ? i - 1 : i;
    }

    /**
     * returns par0 cast as an int, and no greater than Integer.MAX_VALUE-1024
     */
    @SideOnly(Side.CLIENT)
    public static int fastFloor(double value) {
        return (int) (value + 1024.0D) - 1024;
    }

    /**
     * Returns the greatest integer less than or equal to the double argument
     */
    public static int floor(double value) {
        int i = (int) value;
        return value < i ? i - 1 : i;
    }

    public static int clip(int value, int min, int max) {
        if (value > max) {
            value = max;
        }
        if (value < min) {
            value = min;
        }
        return value;
    }

    public static double map(double valueIn, double inMin, double inMax, double outMin, double outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static float map(float valueIn, float inMin, float inMax, float outMin, float outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static double round(double number, double multiplier) {
        return Math.round(number * multiplier) / multiplier;
    }

    /**
     * Rounds the number of decimal places based on the given multiplier.<br>
     * e.g.<br>
     * Input: 17.5245743<br>
     * multiplier: 1000<br>
     * Output: 17.534<br>
     * multiplier: 10<br>
     * Output 17.5<br><br>
     *
     * @param number     The input value.
     * @param multiplier The multiplier.
     * @return The input rounded to a number of decimal places based on the multiplier.
     */
    public static float round(float number, float multiplier) {
        return Math.round(number * multiplier) / multiplier;
    }

    /**
     * @return min <= value <= max
     */
    public static int approachExpI(int a, int b, double ratio) {
        int r = (int) Math.round(approachExp(a, b, ratio));
        return r == a ? b : r;
    }

    public static int retreatExpI(int a, int b, int c, double ratio, int kick) {
        int r = (int) Math.round(retreatExp(a, b, c, ratio, kick));
        return r == a ? b : r;
    }

    public static int roundAway(double d) {
        return (int) (d < 0 ? Math.floor(d) : Math.ceil(d));
    }

    public static int compare(int a, int b) {
        return a == b ? 0 : a < b ? -1 : 1;
    }

    public static int compare(double a, double b) {
        return a == b ? 0 : a < b ? -1 : 1;
    }

    public static BlockPos min(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
    }

    public static BlockPos max(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
    }

    public static int absSum(BlockPos pos) {
        return (pos.getX() < 0 ? -pos.getX() : pos.getX()) + (pos.getY() < 0 ? -pos.getY() : pos.getY()) + (pos.getZ() < 0 ? -pos.getZ() : pos.getZ());
    }

    public static boolean isAxial(BlockPos pos) {
        return pos.getX() == 0 ? (pos.getY() == 0 || pos.getZ() == 0) : (pos.getY() == 0 && pos.getZ() == 0);
    }

    public static int toSide(BlockPos pos) {
        if (!isAxial(pos)) {
            return -1;
        }
        if (pos.getY() < 0) {
            return 0;
        }
        if (pos.getY() > 0) {
            return 1;
        }
        if (pos.getZ() < 0) {
            return 2;
        }
        if (pos.getZ() > 0) {
            return 3;
        }
        if (pos.getX() < 0) {
            return 4;
        }
        if (pos.getX() > 0) {
            return 5;
        }

        return -1;
    }

    /**
     * Long version of floor()
     */
    public static long lfloor(double value) {
        long i = (long) value;
        return value < i ? i - 1L : i;
    }

    @SideOnly(Side.CLIENT)
    public static int absFloor(double value) {
        return (int) (value >= 0.0D ? value : -value + 1.0D);
    }

    public static float abs(float value) {
        return value >= 0.0F ? value : -value;
    }

    /**
     * Returns the unsigned value of an int.
     */
    public static int abs(int value) {
        return value >= 0 ? value : -value;
    }

    public static int ceil(float value) {
        int i = (int) value;
        return value > i ? i + 1 : i;
    }

    public static int ceil(double value) {
        int i = (int) value;
        return value > i ? i + 1 : i;
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters.
     */
    public static int clamp(int num, int min, int max) {
        return num < min ? min : (num > max ? max : num);
    }

    /**
     * Returns the value of the first parameter, clamped to be within the lower and upper limits given by the second and
     * third parameters
     */
    public static float clamp(float num, float min, float max) {
        return num < min ? min : (num > max ? max : num);
    }

    public static double clamp(double num, double min, double max) {
        return num < min ? min : (num > max ? max : num);
    }

    public static double clampedLerp(double lowerBnd, double upperBnd, double slide) {
        return slide < 0.0D ? lowerBnd : (slide > 1.0D ? upperBnd : lowerBnd + (upperBnd - lowerBnd) * slide);
    }

    /**
     * Maximum of the absolute value of two numbers.
     */
    public static double absMax(double p_76132_0_, double p_76132_2_) {
        if (p_76132_0_ < 0.0D) {
            p_76132_0_ = -p_76132_0_;
        }

        if (p_76132_2_ < 0.0D) {
            p_76132_2_ = -p_76132_2_;
        }

        return p_76132_0_ > p_76132_2_ ? p_76132_0_ : p_76132_2_;
    }

    /**
     * Buckets an integer with specifed bucket sizes.
     */
    @SideOnly(Side.CLIENT)
    public static int intFloorDiv(int p_76137_0_, int p_76137_1_) {
        return p_76137_0_ < 0 ? -((-p_76137_0_ - 1) / p_76137_1_) - 1 : p_76137_0_ / p_76137_1_;
    }

    public static int getInt(Random random, int minimum, int maximum) {
        return minimum >= maximum ? minimum : random.nextInt(maximum - minimum + 1) + minimum;
    }

    public static float nextFloat(Random random, float minimum, float maximum) {
        return minimum >= maximum ? minimum : random.nextFloat() * (maximum - minimum) + minimum;
    }

    public static double nextDouble(Random random, double minimum, double maximum) {
        return minimum >= maximum ? minimum : random.nextDouble() * (maximum - minimum) + minimum;
    }

    public static double average(long[] values) {
        long i = 0L;

        for (long j : values) {
            i += j;
        }

        return (double) i / (double) values.length;
    }

    @SideOnly(Side.CLIENT)
    public static boolean epsilonEquals(float p_180185_0_, float p_180185_1_) {
        return abs(p_180185_1_ - p_180185_0_) < 1.0E-5F;
    }

    @SideOnly(Side.CLIENT)
    public static int normalizeAngle(int p_180184_0_, int p_180184_1_) {
        return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
    }

    @SideOnly(Side.CLIENT)
    public static float positiveModulo(float numerator, float denominator) {
        return (numerator % denominator + denominator) % denominator;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static float wrapDegrees(float value) {
        value = value % 360.0F;

        if (value >= 180.0F) {
            value -= 360.0F;
        }

        if (value < -180.0F) {
            value += 360.0F;
        }

        return value;
    }

    /**
     * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
     */
    public static double wrapDegrees(double value) {
        value = value % 360.0D;

        if (value >= 180.0D) {
            value -= 360.0D;
        }

        if (value < -180.0D) {
            value += 360.0D;
        }

        return value;
    }

    /**
     * Adjust the angle so that his value is in range [-180;180[
     */
    public static int clampAngle(int angle) {
        angle = angle % 360;

        if (angle >= 180) {
            angle -= 360;
        }

        if (angle < -180) {
            angle += 360;
        }

        return angle;
    }

    /**
     * parses the string as integer or returns the second parameter if it fails
     */
    public static int getInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        }
        catch (Throwable var3) {
            return defaultValue;
        }
    }

    /**
     * parses the string as integer or returns the second parameter if it fails. this value is capped to par2
     */
    public static int getInt(String value, int defaultValue, int max) {
        return Math.max(max, getInt(value, defaultValue));
    }

    /**
     * parses the string as double or returns the second parameter if it fails.
     */
    public static double getDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        }
        catch (Throwable var4) {
            return defaultValue;
        }
    }

    public static double getDouble(String value, double defaultValue, double max) {
        return Math.max(max, getDouble(value, defaultValue));
    }

    /**
     * Returns the input value rounded up to the next highest power of two.
     */
    public static int smallestEncompassingPowerOfTwo(int value) {
        int i = value - 1;
        i = i | i >> 1;
        i = i | i >> 2;
        i = i | i >> 4;
        i = i | i >> 8;
        i = i | i >> 16;
        return i + 1;
    }

    /**
     * Is the given value a power of two?  (1, 2, 4, 8, 16, ...)
     */
    private static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    /**
     * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
     * value. Optimized for cases where the input value is a power-of-two. If the input value is not a power-of-two,
     * then subtract 1 from the return value.
     */
    public static int log2DeBruijn(int value) {
        value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) (value * 125613361L >> 27) & 31];
    }

    /**
     * Efficiently calculates the floor of the base-2 log of an integer value.  This is effectively the index of the
     * highest bit that is set.  For example, if the number in binary is 0...100101, this will return 5.
     */
    public static int log2(int value) {
        /**
         * Uses a B(2, 5) De Bruijn sequence and a lookup table to efficiently calculate the log-base-two of the given
         * value. Optimized for cases where the input value is a power-of-two. If the input value is not a power-of-two,
         * then subtract 1 from the return value.
         */
        return log2DeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    /**
     * Rounds the first parameter up to the next interval of the second parameter.
     *
     * For instance, {@code roundUp(1, 4)} returns 4; {@code roundUp(0, 4)} returns 0; and {@code roundUp(4, 4)} returns
     * 4.
     */
    public static int roundUp(int number, int interval) {
        if (interval == 0) {
            return 0;
        }
        else if (number == 0) {
            return interval;
        }
        else {
            if (number < 0) {
                interval *= -1;
            }

            int i = number % interval;
            return i == 0 ? number : number + interval - i;
        }
    }

    /**
     * Makes an integer color from the given red, green, and blue float values
     */
    @SideOnly(Side.CLIENT)
    public static int rgb(float rIn, float gIn, float bIn) {
        /**
         * Makes a single int color with the given red, green, and blue values.
         */
        return rgb(floor(rIn * 255.0F), floor(gIn * 255.0F), floor(bIn * 255.0F));
    }

    /**
     * Makes a single int color with the given red, green, and blue values.
     */
    @SideOnly(Side.CLIENT)
    public static int rgb(int rIn, int gIn, int bIn) {
        int lvt_3_1_ = (rIn << 8) + gIn;
        lvt_3_1_ = (lvt_3_1_ << 8) + bIn;
        return lvt_3_1_;
    }

    @SideOnly(Side.CLIENT)
    public static int multiplyColor(int p_180188_0_, int p_180188_1_) {
        int i = (p_180188_0_ & 16711680) >> 16;
        int j = (p_180188_1_ & 16711680) >> 16;
        int k = (p_180188_0_ & 65280) >> 8;
        int l = (p_180188_1_ & 65280) >> 8;
        int i1 = (p_180188_0_ & 255) >> 0;
        int j1 = (p_180188_1_ & 255) >> 0;
        int k1 = (int) ((float) i * (float) j / 255.0F);
        int l1 = (int) ((float) k * (float) l / 255.0F);
        int i2 = (int) ((float) i1 * (float) j1 / 255.0F);
        return p_180188_0_ & -16777216 | k1 << 16 | l1 << 8 | i2;
    }

    /**
     * Gets the decimal portion of the given double. For instance, {@code frac(5.5)} returns {@code .5}.
     */
    @SideOnly(Side.CLIENT)
    public static double frac(double number) {
        return number - Math.floor(number);
    }

    @SideOnly(Side.CLIENT)
    public static long getPositionRandom(Vec3i pos) {
        return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
    }

    public static UUID getRandomUUID(Random rand) {
        long i = rand.nextLong() & -61441L | 16384L;
        long j = rand.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
        return new UUID(i, j);
    }

    /**
     * Generates a random UUID using the shared random
     */
    public static UUID getRandomUUID() {
        return getRandomUUID(RANDOM);
    }

    @SideOnly(Side.CLIENT)
    public static long getCoordinateRandom(int x, int y, int z) {
        long i = x * 3129871 ^ z * 116129781L ^ y;
        i = i * i * 42317861L + i * 11L;
        return i;
    }

    public static double pct(double p_181160_0_, double p_181160_2_, double p_181160_4_) {
        return (p_181160_0_ - p_181160_2_) / (p_181160_4_ - p_181160_2_);
    }

    public static double atan2(double p_181159_0_, double p_181159_2_) {
        double d0 = p_181159_2_ * p_181159_2_ + p_181159_0_ * p_181159_0_;

        if (Double.isNaN(d0)) {
            return Double.NaN;
        }
        else {
            boolean flag = p_181159_0_ < 0.0D;

            if (flag) {
                p_181159_0_ = -p_181159_0_;
            }

            boolean flag1 = p_181159_2_ < 0.0D;

            if (flag1) {
                p_181159_2_ = -p_181159_2_;
            }

            boolean flag2 = p_181159_0_ > p_181159_2_;

            if (flag2) {
                double d1 = p_181159_2_;
                p_181159_2_ = p_181159_0_;
                p_181159_0_ = d1;
            }

            double d9 = fastInvSqrt(d0);
            p_181159_2_ = p_181159_2_ * d9;
            p_181159_0_ = p_181159_0_ * d9;
            double d2 = FRAC_BIAS + p_181159_0_;
            int i = (int) Double.doubleToRawLongBits(d2);
            double d3 = ASINE_TAB[i];
            double d4 = COS_TAB[i];
            double d5 = d2 - FRAC_BIAS;
            double d6 = p_181159_0_ * d4 - p_181159_2_ * d5;
            double d7 = (6.0D + d6 * d6) * d6 * 0.16666666666666666D;
            double d8 = d3 + d7;

            if (flag2) {
                d8 = (Math.PI / 2D) - d8;
            }

            if (flag1) {
                d8 = Math.PI - d8;
            }

            if (flag) {
                d8 = -d8;
            }

            return d8;
        }
    }

    public static double fastInvSqrt(double p_181161_0_) {
        double d0 = 0.5D * p_181161_0_;
        long i = Double.doubleToRawLongBits(p_181161_0_);
        i = 6910469410427058090L - (i >> 1);
        p_181161_0_ = Double.longBitsToDouble(i);
        p_181161_0_ = p_181161_0_ * (1.5D - d0 * p_181161_0_ * p_181161_0_);
        return p_181161_0_;
    }

    @SideOnly(Side.CLIENT)
    public static int hsvToRGB(float hue, float saturation, float value) {
        int i = (int) (hue * 6.0F) % 6;
        float f = hue * 6.0F - i;
        float f1 = value * (1.0F - saturation);
        float f2 = value * (1.0F - f * saturation);
        float f3 = value * (1.0F - (1.0F - f) * saturation);
        float f4;
        float f5;
        float f6;

        switch (i) {
            case 0:
                f4 = value;
                f5 = f3;
                f6 = f1;
                break;
            case 1:
                f4 = f2;
                f5 = value;
                f6 = f1;
                break;
            case 2:
                f4 = f1;
                f5 = value;
                f6 = f3;
                break;
            case 3:
                f4 = f1;
                f5 = f2;
                f6 = value;
                break;
            case 4:
                f4 = f3;
                f5 = f1;
                f6 = value;
                break;
            case 5:
                f4 = value;
                f5 = f1;
                f6 = f2;
                break;
            default:
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }

        int j = clamp((int) (f4 * 255.0F), 0, 255);
        int k = clamp((int) (f5 * 255.0F), 0, 255);
        int l = clamp((int) (f6 * 255.0F), 0, 255);
        return j << 16 | k << 8 | l;
    }

    public static int hash(int p_188208_0_) {
        p_188208_0_ = p_188208_0_ ^ p_188208_0_ >>> 16;
        p_188208_0_ = p_188208_0_ * -2048144789;
        p_188208_0_ = p_188208_0_ ^ p_188208_0_ >>> 13;
        p_188208_0_ = p_188208_0_ * -1028477387;
        p_188208_0_ = p_188208_0_ ^ p_188208_0_ >>> 16;
        return p_188208_0_;
    }

    static {
        for (int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = (float) Math.sin(i * Math.PI * 2.0D / 65536.0D);
        }

        MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] {
                0,
                1,
                28,
                2,
                29,
                14,
                24,
                3,
                30,
                22,
                20,
                15,
                25,
                17,
                4,
                8,
                31,
                27,
                13,
                23,
                21,
                19,
                16,
                7,
                26,
                12,
                18,
                6,
                11,
                5,
                10,
                9
        };
        FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
        ASINE_TAB = new double[257];
        COS_TAB = new double[257];

        for (int j = 0; j < 257; ++j) {
            double d0 = j / 256.0D;
            double d1 = Math.asin(d0);
            COS_TAB[j] = Math.cos(d1);
            ASINE_TAB[j] = d1;
        }
    }

}
