package exercises.mandelbrot;

public class Complex {
	private final double real;
	private final double img;

	public Complex(double real, double img) {
		this.real = real;
		this.img = img;
	}

	public double abs() {
		return Math.sqrt(real * real + img * img);
	}

	public Complex plus(Complex b) {
		double real = this.real + b.real;
		double imag = this.img + b.img;
		return new Complex(real, imag);
	}

	public Complex times(Complex b) {
		Complex a = this;
		double real = a.real * b.real - a.img * b.img;
		double imag = a.real * b.img + a.img * b.real;
		return new Complex(real, imag);
	}
	public double getReal() {
		return real;
	}

	public double getImg() {
		return img;
	}






}
