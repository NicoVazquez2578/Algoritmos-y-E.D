package ucu.edu.aed.ProblemSets.ProblemSet3.lib;

public class Formatter {
    private static final String[] SI = {"B", "kB", "MB", "GB", "TB", "PB", "EB"};

    private static final class UnitDef {
        final String name; final long factorNs; final boolean showDecimals;
        UnitDef(String n, long f, boolean s) { name = n; factorNs = f; showDecimals = s; }
    }

    private static final UnitDef[] UNITS = {
        new UnitDef("ns",  1L, false),
        new UnitDef("µs",  1_000L, true),
        new UnitDef("ms",  1_000_000L, true),
        new UnitDef("s",   1_000_000_000L, true),
        new UnitDef("min", 60_000_000_000L, true),
    };

    public static String formatMemory(long bytes) {
        double unit = 1000.0;
        if (bytes < unit) return bytes + " B";
        int exp = (int) Math.min((Math.log(bytes) / Math.log(unit)), SI.length - 1);
        return String.format("%.2f %s", bytes / Math.pow(unit, exp), SI[exp]);
    }

    public static String formatNanos(long nanos, int decimals) {
        long abs = Math.abs(nanos);
        UnitDef chosen = UNITS[0];
        for (int i = UNITS.length - 1; i >= 0; i--) {
            if (abs >= UNITS[i].factorNs) { chosen = UNITS[i]; break; }
        }
        if (chosen.showDecimals)
            return String.format("%." + decimals + "f %s", abs / (double) chosen.factorNs, chosen.name);
        return abs + " " + chosen.name;
    }
}
