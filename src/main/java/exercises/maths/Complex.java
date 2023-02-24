package exercises.maths;

public class Complex {
	private final double re;
	private final double im;

	public Complex(double real, double imag) {
		re = real;
		im = imag;
	}

	public double abs() {
		return Math.sqrt(re*re + im*im);
	}

	public Complex plus(Complex b) {
		Complex a = this;
		double real = a.re + b.re;
		double imag = a.im + b.im;
		return new Complex(real, imag);
	}

	public Complex times(Complex b) {
		Complex a = this;
		double real = a.re * b.re - a.im * b.im;
		double imag = a.re * b.im + a.im * b.re;
		return new Complex(real, imag);
	}
	public double getRe() {
		return re;
	}

	public double getIm() {
		return im;
	}






}
