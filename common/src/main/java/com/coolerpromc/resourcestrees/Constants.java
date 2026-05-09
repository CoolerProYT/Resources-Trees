package com.coolerpromc.resourcestrees;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String MODID = "resourcestrees";
	public static final Logger LOG = LoggerFactory.getLogger(MODID);

	public static Identifier id(String name){
		return Identifier.fromNamespaceAndPath(MODID, name);
	}

	public static String capitalizeWords(String str) {
		if (str == null || str.isEmpty()) return str;
		String[] words = str.split("_");
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			if (!word.isEmpty()) {
				sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
			}
			sb.append(" ");
		}
		return sb.toString().trim();
	}

	public static String getOrFallback(String translationKey, String fallback){
		return MutableComponent.create(new TranslatableContents(translationKey, capitalizeWords(fallback), TranslatableContents.NO_ARGS)).getString();
	}
}