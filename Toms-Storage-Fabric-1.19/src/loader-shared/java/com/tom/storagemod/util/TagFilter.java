package com.tom.storagemod.util;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.tom.storagemod.platform.Platform;

public class TagFilter implements ItemPredicate {
	private ItemStack stack;
	private boolean allowList;
	private List<TagKey<Item>> tags;

	public TagFilter(ItemStack stack) {
		this.stack = stack;
		CompoundTag tag = stack.getOrCreateTag();
		allowList = tag.getBoolean("allowlist");
		ListTag list = tag.getList("tags", Tag.TAG_STRING);
		tags = new ArrayList<>();
		try {
			for (int i = 0; i < list.size(); i++) {
				tags.add(Platform.getItemTag(new ResourceLocation(list.getString(i))));
			}
		} catch (Exception e) {
		}
	}

	@Override
	public boolean configMatch(ItemStack stack) {
		return stack == this.stack;
	}

	@Override
	public boolean test(ItemVariant stack) {
		return test0(stack) == allowList;
	}

	@SuppressWarnings("deprecation")
	private boolean test0(ItemVariant stack) {
		for (int i = 0; i < tags.size(); i++) {
			if(stack.getItem().builtInRegistryHolder().is(tags.get(i)))return true;
		}
		return false;
	}

	public boolean isAllowList() {
		return allowList;
	}

	public void setAllowList(boolean allowList) {
		this.allowList = allowList;
	}

	public void flush() {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean("allowlist", allowList);
		ListTag list = new ListTag();
		tags.forEach(t -> list.add(StringTag.valueOf(t.location().toString())));
		tag.put("tags", list);
	}

	public List<TagKey<Item>> getTags() {
		return tags;
	}

	public void setTags(List<ResourceLocation> tags) {
		this.tags = tags.stream().map(Platform::getItemTag).toList();
	}
}
