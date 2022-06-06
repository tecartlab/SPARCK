/*
 * Gestalt
 *
 * Copyright (C) 2011 Patrick Kochlik + Dennis Paul
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * {@link http://www.gnu.org/licenses/lgpl.html}
 *
 */

package com.tecartlab.utils;

import java.io.Serializable;


public class Color
    implements Serializable {

    private static final long serialVersionUID = -7695130053845414343L;

    public float r;
    
    public float g;

    public float b;

    public float a;

    private final float[] _myArrayRepresentation = new float[4];

    public Color() {
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }


    public Color(final Color theColor) {
        r = theColor.r;
        g = theColor.g;
        b = theColor.b;
        a = theColor.a;
    }


    public Color(final float theR, final float theG, final float theB, final float theA) {
        r = theR;
        g = theG;
        b = theB;
        a = theA;
    }


    public Color(final float theR, final float theG, final float theB) {
        r = theR;
        g = theG;
        b = theB;
        a = 1;
    }


    public Color(final float theGrey) {
        r = theGrey;
        g = theGrey;
        b = theGrey;
        a = 1;
    }


    public Color(final float theGrey, final float theAlpha) {
        r = theGrey;
        g = theGrey;
        b = theGrey;
        a = theAlpha;
    }


    /* set */

    public void set(final Color theColor) {
        r = theColor.r;
        g = theColor.g;
        b = theColor.b;
        a = theColor.a;
    }


    public void set(final float[] theColor) {
        r = theColor[0];
        g = theColor[1];
        b = theColor[2];
        a = theColor[3];
    }


    public void set(final float theR, final float theG, final float theB, final float theA) {
        r = theR;
        g = theG;
        b = theB;
        a = theA;
    }


    public void set(final float theR, final float theG, final float theB) {
        r = theR;
        g = theG;
        b = theB;
    }


    public void set(final float theGrey) {
        r = theGrey;
        g = theGrey;
        b = theGrey;
    }


    public void set(final float theGrey, final float theAlpha) {
        r = theGrey;
        g = theGrey;
        b = theGrey;
        a = theAlpha;
    }


    /* add */

    public void add(final Color theColor) {
        r += theColor.r;
        r = Math.min(1f, r);
        g += theColor.g;
        g = Math.min(1f, g);
        b += theColor.b;
        b = Math.min(1f, b);
        a += theColor.a;
        a = Math.min(1f, a);
    }


    public void add(final float[] theColor) {
        r += theColor[0];
        g += theColor[1];
        b += theColor[2];
        a += theColor[3];
    }


    public void add(final float theR, final float theG, final float theB, final float theA) {
        r += theR;
        g += theG;
        b += theB;
        a += theA;
    }


    public void add(final float theR, final float theG, final float theB) {
        r += theR;
        g += theG;
        b += theB;
    }


    public void add(final float theGrey) {
        r += theGrey;
        g += theGrey;
        b += theGrey;
    }


    public void add(final float theGrey, final float theAlpha) {
        r += theGrey;
        g += theGrey;
        b += theGrey;
        a += theAlpha;
    }


    /* sub */


    public void sub(final Color theColor) {
        r -= theColor.r;
        g -= theColor.g;
        b -= theColor.b;
        a -= theColor.a;
    }


    public void sub(final float[] theColor) {
        r -= theColor[0];
        g -= theColor[1];
        b -= theColor[2];
        a -= theColor[3];
    }


    public void sub(final float theR, final float theG, final float theB, final float theA) {
        r -= theR;
        g -= theG;
        b -= theB;
        a -= theA;
    }


    public void sub(final float theR, final float theG, final float theB) {
        r -= theR;
        g -= theG;
        b -= theB;
    }


    public void sub(final float theGrey) {
        r -= theGrey;
        g -= theGrey;
        b -= theGrey;
    }


    public void sub(final float theGrey, final float theAlpha) {
        r -= theGrey;
        g -= theGrey;
        b -= theGrey;
        a -= theAlpha;
    }


    /* multiply */

    public void multiply(final Color theColor) {
        r *= theColor.r;
        g *= theColor.g;
        b *= theColor.b;
        a *= theColor.a;
    }


    public final void scale(final float theScalar) {
        r *= theScalar;
        g *= theScalar;
        b *= theScalar;
        a *= theScalar;
    }


    /* constrain */

    public void constrain() {
        if (r < 0) {
            r = 0;
        }
        if (r > 1) {
            r = 1;
        }
        if (g < 0) {
            g = 0;
        }
        if (g > 1) {
            g = 1;
        }
        if (b < 0) {
            b = 0;
        }
        if (b > 1) {
            b = 1;
        }
        if (a < 0) {
            a = 0;
        }
        if (a > 1) {
            a = 1;
        }
    }


    public void mix(float theRatio, Color theBegin, Color theEnd) {
        theRatio = Math.max(0, Math.min(1, theRatio));
        r = theBegin.r + (theEnd.r - theBegin.r) * theRatio;
        g = theBegin.g + (theEnd.g - theBegin.g) * theRatio;
        b = theBegin.b + (theEnd.b - theBegin.b) * theRatio;
        a = theBegin.a + (theEnd.a - theBegin.a) * theRatio;
    }


    public float[] toArray() {
        _myArrayRepresentation[0] = r;
        _myArrayRepresentation[1] = g;
        _myArrayRepresentation[2] = b;
        _myArrayRepresentation[3] = a;
        return _myArrayRepresentation;
    }


    public boolean equals(final Color theColor) {
        if (r == theColor.r &&
            g == theColor.g &&
            b == theColor.b &&
            a == theColor.a) {
            return true;
        } else {
            return false;
        }
    }


    public String toString() {
        return "r: " + r + " g: " + g + " b: " + b + " a: " + a;
    }


    public static  class HSL {
        public float h;

        public float s;

        public float l;
    }


    /*
       Calculate HSL from RGB
       Hue is in degrees
       Lightness is between 0 and 1
       Saturation is between 0 and 1
     */
    public static HSL RGB2HSL(final Color c1) {
        float themin;
        float themax;
        float delta;

        final HSL c2 = new HSL();

        themin = Math.min(c1.r, Math.min(c1.g, c1.b));
        themax = Math.max(c1.r, Math.max(c1.g, c1.b));
        delta = themax - themin;
        c2.l = (themin + themax) / 2;
        c2.s = 0;
        if (c2.l > 0 && c2.l < 1) {
            c2.s = delta / (c2.l < 0.5 ? (2 * c2.l) : (2 - 2 * c2.l));
        }
        c2.h = 0;
        if (delta > 0) {
            if (themax == c1.r && themax != c1.g) {
                c2.h += (c1.g - c1.b) / delta;
            }
            if (themax == c1.g && themax != c1.b) {
                c2.h += (2 + (c1.b - c1.r) / delta);
            }
            if (themax == c1.b && themax != c1.r) {
                c2.h += (4 + (c1.r - c1.g) / delta);
            }
            c2.h *= 60;
        }
        return (c2);
    }


    /*
       Calculate RGB from HSL, reverse of RGB2HSL()
       Hue is in degrees
       Lightness is between 0 and 1
       Saturation is between 0 and 1
     */
    public static Color HSL2RGB(final HSL c1) {

        final Color c2 = new Color();
        final Color sat = new Color();
        final Color ctmp = new Color();

        while (c1.h < 0) {
            c1.h += 360;
        } while (c1.h > 360) {
            c1.h -= 360;
        }

        if (c1.h < 120) {
            sat.r = (120 - c1.h) / 60.0f;
            sat.g = c1.h / 60.0f;
            sat.b = 0;
        } else if (c1.h < 240) {
            sat.r = 0;
            sat.g = (240 - c1.h) / 60.0f;
            sat.b = (c1.h - 120) / 60.0f;
        } else {
            sat.r = (c1.h - 240) / 60.0f;
            sat.g = 0;
            sat.b = (360 - c1.h) / 60.0f;
        }
        sat.r = Math.min(sat.r, 1);
        sat.g = Math.min(sat.g, 1);
        sat.b = Math.min(sat.b, 1);

        ctmp.r = 2 * c1.s * sat.r + (1 - c1.s);
        ctmp.g = 2 * c1.s * sat.g + (1 - c1.s);
        ctmp.b = 2 * c1.s * sat.b + (1 - c1.s);

        if (c1.l < 0.5) {
            c2.r = c1.l * ctmp.r;
            c2.g = c1.l * ctmp.g;
            c2.b = c1.l * ctmp.b;
        } else {
            c2.r = (1 - c1.l) * ctmp.r + 2 * c1.l - 1;
            c2.g = (1 - c1.l) * ctmp.g + 2 * c1.l - 1;
            c2.b = (1 - c1.l) * ctmp.b + 2 * c1.l - 1;
        }
        return (c2);
    }


    public java.awt.Color createAWTColor() {
        return new java.awt.Color(r, g, b, a);
    }
}
