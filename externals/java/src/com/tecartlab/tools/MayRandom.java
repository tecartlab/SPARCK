/* MIT License
 *
 * Copyright (c) 2012-2020 tecartlab.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author maybites
 *
 */

package com.tecartlab.tools;

import java.util.Random;

public class MayRandom extends Random {

	Random internalRandom;

	public MayRandom() {
	}

	/**
	 * Return a random number in the range [0, howbig).
	 * <P>
	 * The number returned will range from zero up to (but not including)
	 * 'howbig'.
	 */
	public final float create(float howbig) {
		// for some reason (rounding error?) Math.random() * 3
		// can sometimes return '3' (once in ~30 million tries)
		// so a check was added to avoid the inclusion of 'howbig'

		// avoid an infinite loop
		if (howbig == 0)
			return 0;

		// internal random number object
		if (internalRandom == null)
			internalRandom = new Random();

		float value = 0;
		do {
			// value = (float)Math.random() * howbig;
			value = internalRandom.nextFloat() * howbig;
		} while (value == howbig);
		return value;
	}

	/**
	 * Return a random number in the range [howsmall, howbig).
	 * <P>
	 * The number returned will range from 'howsmall' up to (but not including
	 * 'howbig'.
	 * <P>
	 * If howsmall is >= howbig, howsmall will be returned, meaning that
	 * random(5, 5) will return 5 (useful) and random(7, 4) will return 7 (not
	 * useful.. better idea?)
	 */
	public final float create(float howsmall, float howbig) {
		if (howsmall >= howbig)
			return howsmall;
		float diff = howbig - howsmall;
		return create(diff) + howsmall;
	}

}
