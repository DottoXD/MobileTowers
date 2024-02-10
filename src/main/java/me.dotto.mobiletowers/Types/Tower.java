package me.dotto.mobiletowers.Types;

import java.util.OptionalInt;

public class Tower {
    public Tower(String a, int b, int c, int d, String e, String f, int g) {
        id = a;
        x = b;
        y = c;
        z = d;
        type = e;
        world = f;
        trange = g;
    }
    public String id;
    public int x;
    public int y;
    public int z;
    public String type;
    public String world;
    public int trange;
    public OptionalInt score;
}