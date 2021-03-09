package com.tom.storagemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import com.tom.storagemod.NetworkHandler.IDataReceiver;
import com.tom.storagemod.block.BlockInventoryCable;
import com.tom.storagemod.block.BlockInventoryCableConnector;
import com.tom.storagemod.block.BlockInventoryCableConnectorFiltered;
import com.tom.storagemod.block.BlockInventoryCableFramed;
import com.tom.storagemod.block.BlockInventoryCableFramedPainted;
import com.tom.storagemod.block.BlockInventoryHopperBasic;
import com.tom.storagemod.block.BlockInventoryProxy;
import com.tom.storagemod.block.BlockLevelEmitter;
import com.tom.storagemod.block.BlockOpenCrate;
import com.tom.storagemod.block.BlockPaintedTrim;
import com.tom.storagemod.block.BlockTrim;
import com.tom.storagemod.block.CraftingTerminal;
import com.tom.storagemod.block.InventoryConnector;
import com.tom.storagemod.block.StorageTerminal;
import com.tom.storagemod.gui.ContainerCraftingTerminal;
import com.tom.storagemod.gui.ContainerFiltered;
import com.tom.storagemod.gui.ContainerLevelEmitter;
import com.tom.storagemod.gui.ContainerStorageTerminal;
import com.tom.storagemod.item.ItemBlockPainted;
import com.tom.storagemod.item.ItemPaintKit;
import com.tom.storagemod.item.ItemWirelessTerminal;
import com.tom.storagemod.tile.TileEntityCraftingTerminal;
import com.tom.storagemod.tile.TileEntityInventoryCableConnector;
import com.tom.storagemod.tile.TileEntityInventoryCableConnectorFiltered;
import com.tom.storagemod.tile.TileEntityInventoryConnector;
import com.tom.storagemod.tile.TileEntityInventoryHopperBasic;
import com.tom.storagemod.tile.TileEntityInventoryProxy;
import com.tom.storagemod.tile.TileEntityLevelEmitter;
import com.tom.storagemod.tile.TileEntityOpenCrate;
import com.tom.storagemod.tile.TileEntityPainted;
import com.tom.storagemod.tile.TileEntityStorageTerminal;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;

// The value here should match an entry in the META-INF/mods.toml file
//@Mod(StorageMod.modid)
public class StorageMod implements ModInitializer {
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String modid = "toms_storage";
	public static InventoryConnector connector;
	public static StorageTerminal terminal;
	public static BlockTrim inventoryTrim;
	public static BlockOpenCrate openCrate;
	public static BlockPaintedTrim paintedTrim;
	public static BlockInventoryCable invCable;
	public static BlockInventoryCableFramed invCableFramed;
	public static BlockInventoryCableFramedPainted invCablePainted;
	public static BlockInventoryCableConnector invCableConnector;
	public static BlockInventoryCableConnectorFiltered invCableConnectorFiltered;
	public static BlockInventoryProxy invProxy;
	public static CraftingTerminal craftingTerminal;
	public static BlockInventoryHopperBasic invHopperBasic;
	public static BlockLevelEmitter levelEmitter;

	public static ItemPaintKit paintingKit;
	public static ItemWirelessTerminal wirelessTerminal;

	public static BlockEntityType<TileEntityInventoryConnector> connectorTile;
	public static BlockEntityType<TileEntityStorageTerminal> terminalTile;
	public static BlockEntityType<TileEntityOpenCrate> openCrateTile;
	public static BlockEntityType<TileEntityPainted> paintedTile;
	public static BlockEntityType<TileEntityInventoryCableConnector> invCableConnectorTile;
	public static BlockEntityType<TileEntityInventoryCableConnectorFiltered> invCableConnectorFilteredTile;
	public static BlockEntityType<TileEntityInventoryProxy> invProxyTile;
	public static BlockEntityType<TileEntityCraftingTerminal> craftingTerminalTile;
	public static BlockEntityType<TileEntityInventoryHopperBasic> invHopperBasicTile;
	public static BlockEntityType<TileEntityLevelEmitter> levelEmitterTile;

	public static ScreenHandlerType<ContainerStorageTerminal> storageTerminal;
	public static ScreenHandlerType<ContainerCraftingTerminal> craftingTerminalCont;
	public static ScreenHandlerType<ContainerFiltered> filteredConatiner;
	public static ScreenHandlerType<ContainerLevelEmitter> levelEmitterConatiner;

	public static Config CONFIG = AutoConfig.register(Config.class, GsonConfigSerializer::new).getConfig();

	public StorageMod() {
	}

	public static final ItemGroup STORAGE_MOD_TAB = FabricItemGroupBuilder.build(id("tab"), () -> new ItemStack(terminal));


	public static Identifier id(String id) {
		return new Identifier(modid, id);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Tom's Storage Setup starting");
		connector = new InventoryConnector();
		terminal = new StorageTerminal();
		openCrate = new BlockOpenCrate();
		inventoryTrim = new BlockTrim();
		paintedTrim = new BlockPaintedTrim();
		invCable = new BlockInventoryCable();
		invCableFramed = new BlockInventoryCableFramed();
		invCablePainted = new BlockInventoryCableFramedPainted();
		invCableConnector = new BlockInventoryCableConnector();
		invCableConnectorFiltered = new BlockInventoryCableConnectorFiltered();
		invProxy = new BlockInventoryProxy();
		craftingTerminal = new CraftingTerminal();
		invHopperBasic = new BlockInventoryHopperBasic();
		levelEmitter = new BlockLevelEmitter();

		paintingKit = new ItemPaintKit();
		wirelessTerminal = new ItemWirelessTerminal();

		connectorTile = BlockEntityType.Builder.create(TileEntityInventoryConnector::new, connector).build(null);
		terminalTile = BlockEntityType.Builder.create(TileEntityStorageTerminal::new, terminal).build(null);
		openCrateTile = BlockEntityType.Builder.create(TileEntityOpenCrate::new, openCrate).build(null);
		paintedTile = BlockEntityType.Builder.create(TileEntityPainted::new, paintedTrim, invCableFramed, invCablePainted).build(null);
		invCableConnectorTile = BlockEntityType.Builder.create(TileEntityInventoryCableConnector::new, invCableConnector).build(null);
		invCableConnectorFilteredTile = BlockEntityType.Builder.create(TileEntityInventoryCableConnectorFiltered::new, invCableConnectorFiltered).build(null);
		invProxyTile = BlockEntityType.Builder.create(TileEntityInventoryProxy::new, invProxy).build(null);
		craftingTerminalTile = BlockEntityType.Builder.create(TileEntityCraftingTerminal::new, craftingTerminal).build(null);
		invHopperBasicTile = BlockEntityType.Builder.create(TileEntityInventoryHopperBasic::new, invHopperBasic).build(null);
		levelEmitterTile = BlockEntityType.Builder.create(TileEntityLevelEmitter::new, levelEmitter).build(null);

		storageTerminal = ScreenHandlerRegistry.registerSimple(id("ts.storage_terminal.container"), ContainerStorageTerminal::new);
		craftingTerminalCont = ScreenHandlerRegistry.registerSimple(id("ts.crafting_terminal.container"), ContainerCraftingTerminal::new);
		filteredConatiner = ScreenHandlerRegistry.registerSimple(id("ts.filtered.container"), ContainerFiltered::new);
		levelEmitterConatiner = ScreenHandlerRegistry.registerSimple(id("ts.level_emitter.container"), ContainerLevelEmitter::new);

		Registry.register(Registry.BLOCK, id("ts.inventory_connector"), connector);
		Registry.register(Registry.BLOCK, id("ts.storage_terminal"), terminal);
		Registry.register(Registry.BLOCK, id("ts.open_crate"), openCrate);
		Registry.register(Registry.BLOCK, id("ts.trim"), inventoryTrim);
		Registry.register(Registry.BLOCK, id("ts.painted_trim"), paintedTrim);
		Registry.register(Registry.BLOCK, id("ts.inventory_cable"), invCable);
		Registry.register(Registry.BLOCK, id("ts.inventory_cable_framed"), invCableFramed);
		Registry.register(Registry.BLOCK, id("ts.inventory_cable_painted"), invCablePainted);
		Registry.register(Registry.BLOCK, id("ts.inventory_cable_connector"), invCableConnector);
		Registry.register(Registry.BLOCK, id("ts.inventory_cable_connector_filtered"), invCableConnectorFiltered);
		Registry.register(Registry.BLOCK, id("ts.inventory_proxy"), invProxy);
		Registry.register(Registry.BLOCK, id("ts.crafting_terminal"), craftingTerminal);
		Registry.register(Registry.BLOCK, id("ts.inventory_hopper_basic"), invHopperBasic);
		Registry.register(Registry.BLOCK, id("ts.level_emitter"), levelEmitter);

		Registry.register(Registry.ITEM, id("ts.paint_kit"), paintingKit);
		Registry.register(Registry.ITEM, id("ts.wireless_terminal"), wirelessTerminal);

		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.inventory_connector.tile"), connectorTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.storage_terminal.tile"), terminalTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.open_crate.tile"), openCrateTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.painted.tile"), paintedTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.inventory_cable_connector.tile"), invCableConnectorTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.inventory_cable_connector_filtered.tile"), invCableConnectorFilteredTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.inventory_proxy.tile"), invProxyTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.crafting_terminal.tile"), craftingTerminalTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.inventoty_hopper_basic.tile"), invHopperBasicTile);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, id("ts.level_emitter.tile"), levelEmitterTile);

		registerItemForBlock(connector);
		registerItemForBlock(terminal);
		registerItemForBlock(openCrate);
		registerItemForBlock(inventoryTrim);
		Registry.register(Registry.ITEM, Registry.BLOCK.getId(paintedTrim), new ItemBlockPainted(paintedTrim));
		registerItemForBlock(invCable);
		Registry.register(Registry.ITEM, Registry.BLOCK.getId(invCableFramed), new ItemBlockPainted(invCableFramed, new Item.Settings().group(STORAGE_MOD_TAB)));
		registerItemForBlock(invCableConnector);
		registerItemForBlock(invCableConnectorFiltered);
		//Registry.register(Registry.ITEM, Registry.BLOCK.getId(invProxy), new ItemBlockPainted(invProxy, new Item.Settings().group(STORAGE_MOD_TAB)));
		registerItemForBlock(invProxy);
		registerItemForBlock(craftingTerminal);
		registerItemForBlock(invHopperBasic);
		registerItemForBlock(levelEmitter);

		ServerSidePacketRegistry.INSTANCE.register(NetworkHandler.DATA_C2S, (ctx, buf) -> {
			CompoundTag tag = buf.method_30617();
			ctx.getTaskQueue().submit(() -> {
				ServerPlayerEntity sender = (ServerPlayerEntity) ctx.getPlayer();
				if(sender.currentScreenHandler instanceof IDataReceiver) {
					((IDataReceiver)sender.currentScreenHandler).receive(tag);
				}
			});
		});

		StorageTags.init();
	}

	private static void registerItemForBlock(Block block) {
		Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), new BlockItem(block, new Item.Settings().group(STORAGE_MOD_TAB)));
	}
}
