package dev.wuason.mechanics.adapter;

import java.util.Locale;

public class Utils {

    public static String convert(String type, String id) {
        return type + ":" + id;
    }

    public static String[] process(String line) {
        if (line == null || line.isBlank() || !line.contains(":")) return null;
        String type = line.substring(0, line.indexOf(":")).toLowerCase(Locale.ENGLISH);
        String id = line.substring(line.indexOf(":") + 1);
        if (type.isBlank() || id.isBlank()) return null;
        AdapterComp adapter = Adapter.existAdapter(type) ? Adapter.getAdapter(type) : Adapter.getAdapterByAlias(type);
        return adapter == null ? null : new String[]{adapter.getType(), id};
    }

}
