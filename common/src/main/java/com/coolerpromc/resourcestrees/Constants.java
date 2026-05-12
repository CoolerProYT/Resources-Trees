package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.pack.ResourcesTreesModelPack;
import com.coolerpromc.resourcestrees.pack.ResourcesTreesTagPack;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
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

	public static Pack getInMemoryResourcePack(){
		ResourcesTreesModelPack pack = new ResourcesTreesModelPack();
		TreeTypes.getTypes().forEach(treeType -> {
			pack.addLeavesModel(treeType.leavesTexture());
			pack.addSaplingBlockModel(treeType.saplingTexture());
			pack.addSaplingItemModel(treeType.saplingTexture());
		});

		return getPack(pack, pack.getDescription());
	}

	public static Pack getInMemoryDataPack(){
		ResourcesTreesTagPack pack = new ResourcesTreesTagPack();
		pack.addItemTag(Constants.id("leaf_fragments"), ModItems.LEAF_FRAGMENTS.stream().map(RegistryHandler::id).toList());
		pack.addBlockTag(Constants.id("resources_saplings"), ModBlocks.SAPLINGS.stream().map(RegistryHandler::id).toList());
		pack.addItemTag(Constants.id("resources_saplings"), ModBlocks.SAPLINGS.stream().map(RegistryHandler::id).toList());
		pack.addBlockTag(Constants.id("resources_leaves"), ModBlocks.LEAVES.stream().map(RegistryHandler::id).toList());
		pack.addItemTag(Constants.id("resources_leaves"), ModBlocks.LEAVES.stream().map(RegistryHandler::id).toList());

		return getPack(pack, pack.getDescription());
	}

	private static <T extends AbstractPackResources> Pack getPack(T pack, Component description){
		return new Pack(
				pack.location(),
				new Pack.ResourcesSupplier() {
					@Override
					public PackResources openPrimary(PackLocationInfo info) { return pack; }
					@Override
					public PackResources openFull(PackLocationInfo info, Pack.Metadata meta) { return pack; }
				},
				new Pack.Metadata(description, PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), List.of()),
				new PackSelectionConfig(true, Pack.Position.TOP, false)
		);
	}
}