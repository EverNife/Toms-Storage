package com.tom.storagemod.gui;

import java.util.function.Supplier;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class PlatformButton extends Button {

	public PlatformButton(int x, int y, int w, int h, Component text, OnPress onPress) {
		super(x, y, w, h, text, onPress, Supplier::get);
	}
}
