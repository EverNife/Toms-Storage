package com.tom.storagemod.gui;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

public class ItemFilterScreen extends AbstractFilteredScreen<ItemFilterMenu> {
	private static final ResourceLocation DISPENSER_GUI_TEXTURES = new ResourceLocation("textures/gui/container/dispenser.png");

	private GuiButton buttonAllowList, buttonMatchNBT;

	public ItemFilterScreen(ItemFilterMenu container, Inventory playerInventory, Component textComponent) {
		super(container, playerInventory, textComponent);
	}

	@Override
	protected void init() {
		super.init();
		//this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;

		buttonAllowList = addRenderableWidget(new GuiButton(leftPos - 18, topPos + 5, 0, b -> {
			click(1, buttonAllowList.state != 1);
		}));

		buttonMatchNBT = addRenderableWidget(new GuiButton(leftPos - 18, topPos + 5 + 18, 1, b -> {
			click(0, buttonMatchNBT.state != 1);
		}));
	}

	private void click(int id, boolean val) {
		this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, (id << 1) | (val ? 1 : 0));
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		buttonMatchNBT.state = menu.matchNBT ? 1 : 0;
		buttonAllowList.state = menu.allowList ? 1 : 0;
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);

		if (buttonAllowList.isHoveredOrFocused()) {
			renderTooltip(matrixStack, Component.translatable("tooltip.toms_storage.allowList_" + buttonAllowList.state), mouseX, mouseY);
		}

		if (buttonMatchNBT.isHoveredOrFocused()) {
			renderTooltip(matrixStack, Component.translatable("tooltip.toms_storage.matchNBT_" + buttonMatchNBT.state), mouseX, mouseY);
		}
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, DISPENSER_GUI_TEXTURES);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}
}
