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


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tecartlab.quescript.expression.Expression;
import com.tecartlab.quescript.expression.ExpressionNode;
import com.tecartlab.quescript.expression.RunTimeEnvironment;
import com.tecartlab.quescript.expression.Expression.ExpressionException;

public class TestBed2 {

	static public void main(String[] args){

		List<String> segmts = new ArrayList<String>();
		Matcher m = Pattern.compile("([^{]\\S*|.+?[{}])\\s*").matcher("/queOne color {sin($color_f1)} {'passed[' + ($global1) + ']'} {$global1} test");
		while (m.find()){
			System.out.println(m.group(1));
			segmts.add(m.group(1).trim()); // Add .replace("\"", "") to remove surrounding quotes.
		}


		try{
			double eff = 10.0;
			System.out.print(Double.toString(eff));

			ExpressionNode result = null;

			RunTimeEnvironment rt = new RunTimeEnvironment();

			Expression expression;

			ExpressionNode a2 = new ExpressionNode(5);
			ExpressionNode a = new ExpressionNode("test");
			ExpressionNode b = new ExpressionNode("test");
			ExpressionNode f = new ExpressionNode(4);

			rt.setVariable("a2",a2);

			rt.setVariable("a.1",a);
			rt.setVariable("b",b);
			rt.setVariable("f",f);

			expression = new Expression("??a2 = sqrt((a.1 == b) * 4)");
			result = expression.parse(rt);
			System.out.println("a2 = sqrt((a.1 == b) * 4) = " + result.eval());

			result = new Expression("('test' == 'test')/2").parse(rt);
			System.out.println("'test' == 'test'/2 = " + result.eval());

			result = new Expression("a.1 = (a2 * 2)/5").parse(rt);
			System.out.println("a.1 = (a2 * 2)/5 = " + result.eval());

			expression = new Expression("'prefix [' + (a.1) + '] postfix'");
			result = expression.parse(rt);
			System.out.println("1+1/3 = " + result.eval());

			a.setValue(3);
			b.setValue(4);

			long millis = System.currentTimeMillis();
			for(int i = 0; i < 10000; i++)
				result.eval();
			long millis2 = System.currentTimeMillis() - millis;

			System.out.println("sqrt((a == b) * 4) = " + result + " | time needed =" + millis2);


			result = new Expression("2.4/PI").parse(rt);
			System.out.println("2.4/PI = " + result.eval());


			a.setValue(1);
			result = new Expression("random() < 0.5 * $a").parse(rt);
			System.out.println("random() < 0.5 * $a= " + result.eval());
			result = new Expression("not($x<7||sqrt(max($x,9)) <= 3)").parse(rt);
			System.out.println("not(x<7||sqrt(max(x,9)) <= 3) = " + result.eval());

		} catch(ExpressionException e){
			System.err.println(e.getMessage());

		}

	}
}
