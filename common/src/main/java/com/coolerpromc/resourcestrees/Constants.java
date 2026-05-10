package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.client.pack.ResourcesTreesModelPack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

	public static Pack getInMemoryPack(){
		ResourcesTreesModelPack pack = new ResourcesTreesModelPack();
		TreeTypes.getTypes().forEach(treeType -> {
			pack.addLeavesModel(treeType.leavesTexture());
			pack.addSaplingBlockModel(treeType.saplingTexture());
			pack.addSaplingItemModel(treeType.saplingTexture());
		});

		Pack p = new Pack(
				pack.location(),
				new Pack.ResourcesSupplier() {
					@Override
					public PackResources openPrimary(PackLocationInfo info) { return pack; }
					@Override
					public PackResources openFull(PackLocationInfo info, Pack.Metadata meta) { return pack; }
				},
				new Pack.Metadata(pack.getDescription(), PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), List.of()),
				new PackSelectionConfig(true, Pack.Position.TOP, false)
		);
		return p;
	}
}