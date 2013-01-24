/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BenchCommonUtils;

/**
 *
 * @author kamalnath_ng
 */

public class CalcSupport {
    public static double quantile(double[] numbers, int k, int q) throws IllegalArgumentException {
		if (numbers.length == 1) return numbers[0];
		
		double p = ((double) k) / q;
		double index = (numbers.length - 1) * p;
		int j = (int) Math.floor(index);
		double g = index - j;
		return numbers[j] + (g * (numbers[j + 1] - numbers[j]));	// use j and j + 1 instead of j + 1 and j + 2 of wikipedia because of 0-offset java arrays
	}
    
    public static double median(double[] numbers) throws IllegalArgumentException {
		return quantile(numbers, 1, 2);
	}
    
    public static double mean(double[] numbers) throws IllegalArgumentException {
		double sum = 0.0;
		for (int i = 0; i < numbers.length; i++) {
			sum += numbers[i];
		}
		return sum / numbers.length;
	}
    public static double sum(double[] numbers) throws IllegalArgumentException {
		double sum = 0.0;
		for (double d : numbers) sum += d;
		return sum;
	}
   
}
