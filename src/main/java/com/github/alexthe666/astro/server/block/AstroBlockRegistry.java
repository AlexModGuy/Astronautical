package com.github.alexthe666.astro.server.block;

import com.github.alexthe666.astro.Astronautical;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Astronautical.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstroBlockRegistry {

    public static final Block SQUID_MUCUS = new BlockSquidMucus();
    public static final Block BLOCK_OF_SQUID_GOOP = new BlockOfSquidGoop();
    public static final Block METEORITE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(7, 100).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("astro:meteorite");
    public static final Block METEORITE_IRON_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(8, 100).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("astro:meteorite_iron_ore");
    public static final Block POLISHED_METEORITE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(6, 100).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("astro:polished_meteorite");
    public static final Block METEORITE_PILLAR = new RotatedPillarBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(6, 100).harvestTool(ToolType.PICKAXE).harvestLevel(1)).setRegistryName("astro:meteorite_pillar");
    public static final Block BURNT_TORCH = new BlockBurntTorch(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.WOOD)).setRegistryName("astro:burnt_torch");
    public static final Block WALL_BURNT_TORCH = new ModWallTorchBlock(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.WOOD).lootFrom(BURNT_TORCH), "astro.wall_burnt_torch").setRegistryName("astro:wall_burnt_torch");
    public static final Block STARDUST_TORCH = new BlockStardustTorch(Block.Properties.create(Material.MISCELLANEOUS).func_235838_a_((p_235470_0_) -> { return 12; }).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.WOOD)).setRegistryName("astro:stardust_torch");
    public static final Block WALL_STARDUST_TORCH = new ModWallTorchBlock(Block.Properties.create(Material.MISCELLANEOUS).func_235838_a_((p_235470_0_) -> { return 12; }).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.WOOD).lootFrom(STARDUST_TORCH), "astro.wall_stardust_torch").setRegistryName("astro:wall_stardust_torch");
    public static final Block STARNACLE = new BlockStarnacle();
    public static final Block BLOCKIT_WORM_HOLE = new BlockBlockitWormHole();
    public static final Block PLANETOID_GAS_BLUE = new BlockPlanetoidGas("blue", 0X2F43F4);
    public static final Block PLANETOID_RING_BLUE = new BlockPlanetoidRing("blue", 0X0094FF);
    public static final Block PLANETOID_GAS_YELLOW = new BlockPlanetoidGas("yellow", 0XFFE566);
    public static final Block PLANETOID_RING_YELLOW = new BlockPlanetoidRing("yellow", 0XFFD800);
    public static final Block PLANETOID_GAS_ORANGE = new BlockPlanetoidGas("orange", 0XFF8D42);
    public static final Block PLANETOID_RING_ORANGE = new BlockPlanetoidRing("orange", 0XFF6A00);
    public static final Block PLANETOID_GAS_GREEN = new BlockPlanetoidGas("green", 0X99E444);
    public static final Block PLANETOID_RING_GREEN = new BlockPlanetoidRing("green", 0XDAFF7F);
    public static final Block PLANETOID_GAS_TEAL = new BlockPlanetoidGas("teal", 0X7FFFC5);
    public static final Block PLANETOID_RING_TEAL = new BlockPlanetoidRing("teal", 0X72C1A5);
    public static final Block PLANETOID_GAS_PURPLE = new BlockPlanetoidGas("purple", 0XC363FF);
    public static final Block PLANETOID_RING_PURPLE = new BlockPlanetoidRing("purple", 0XE5C1FF);
    public static final Block PLANETOID_CORE = new BlockPlanetoidCore();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : AstroBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    event.getRegistry().register((Block) obj);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        event.getRegistry().register(block);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : AstroBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    Item.Properties props = new Item.Properties();
                    if(!(obj instanceof INoTab)){
                        props.group(Astronautical.TAB);
                    }
                    if (obj instanceof IUsesTEISR) {
                        Astronautical.PROXY.setupISTER(props);
                    }
                    BlockItem blockItem;
                    if(obj instanceof IWallAndFloor){
                        blockItem = new WallOrFloorItem((Block)obj, ((IWallAndFloor)obj).wallBlock(), props);
                    }else{
                        blockItem = new BlockItem((Block) obj, props);
                    }
                    blockItem.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(blockItem);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
