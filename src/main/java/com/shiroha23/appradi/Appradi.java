package com.shiroha23.appradi;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.localization.GuiText;
import appeng.items.storage.StorageTier;
import me.ramidzkh.mekae2.AMItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Appradi.MODID)
public class Appradi {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "appradi";
    // Create a Deferred Register to hold Items which will all be registered under the "appradi" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "appradi" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Item> RADIOACTIVE_CHEMICAL_STORAGE_CELL = ITEMS.register(
        "radioactive_chemical_storage_cell",
        () -> new RadioactiveChemicalStorageCellItem(new Item.Properties().stacksTo(1), StorageTier.SIZE_1K,
            AMItems.CHEMICAL_CELL_HOUSING.get()));

    // Creates a creative tab with the id "appradi:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.appradi")).icon(() -> RADIOACTIVE_CHEMICAL_STORAGE_CELL.get().getDefaultInstance()).displayItems((parameters, output) -> {
        output.accept(RADIOACTIVE_CHEMICAL_STORAGE_CELL.get());
    }).build());

    public Appradi() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(this::initializeUpgrades));
    }

    private void initializeUpgrades() {
        var storageCellGroup = GuiText.StorageCells.getTranslationKey();

        Upgrades.add(AEItems.INVERTER_CARD, RADIOACTIVE_CHEMICAL_STORAGE_CELL::get, 1, storageCellGroup);
        Upgrades.add(AEItems.VOID_CARD, RADIOACTIVE_CHEMICAL_STORAGE_CELL::get, 1, storageCellGroup);
    }
}
