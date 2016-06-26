package com.sonification.color;

import java.awt.Color;

public class ConvertRGBtoHSV {
	public static void main(String[] args) {
		Color color = new Color(100, 121, 12);
		float[] hsv = new float [3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);
		System.out.println("h="+hsv[0]);
		System.out.println("s="+hsv[1]);
		System.out.println("v="+hsv[2]);
	}
}
