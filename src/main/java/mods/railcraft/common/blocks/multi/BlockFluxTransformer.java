/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/

package mods.railcraft.common.blocks.multi;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.IBatteryBlock;
import mods.railcraft.api.charge.IChargeBlock;
import mods.railcraft.common.blocks.BlockMeta;
import mods.railcraft.common.blocks.logic.ChargeSourceLogic;
import mods.railcraft.common.blocks.logic.Logic;
import mods.railcraft.common.items.ItemCharge;
import mods.railcraft.common.items.Metal;
import mods.railcraft.common.items.RailcraftItems;
import mods.railcraft.common.plugins.forge.CraftingPlugin;
import mods.railcraft.common.plugins.forge.WorldPlugin;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@BlockMeta.Tile(TileFluxTransformer.class)
public final class BlockFluxTransformer extends BlockStructure<TileFluxTransformer> implements IChargeBlock {
    public static final PropertyInteger ICON = PropertyInteger.create("icon", 0, 1);
    private static final Map<Charge, ChargeSpec> CHARGE_SPECS = ChargeSpec.make(Charge.distribution, ConnectType.BLOCK, 0.5,
            new IBatteryBlock.Spec(IBatteryBlock.State.DISABLED, 500, 500, 1.0));

    public BlockFluxTransformer() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setTickRandomly(true);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ICON);
    }

    @Override
    public Charge.IAccess getMeterAccess(Charge network, IBlockState state, World world, BlockPos pos) {
        Optional<TileFluxTransformer> tile = WorldPlugin.getTileEntity(world, pos, TileFluxTransformer.class);
        BlockPos accessPos = tile.flatMap(t -> t.getLogic(ChargeSourceLogic.class)).map(Logic::getPos).orElse(pos);
        return network.network(world).access(accessPos);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand) {
        Charge.effects().throwSparks(state, worldIn, pos, rand, 50);
    }

    @Override
    public Map<Charge, ChargeSpec> getChargeSpecs(IBlockState state, IBlockAccess world, BlockPos pos) {
        return CHARGE_SPECS;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        deregisterNode(worldIn, pos);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World worldIn, BlockPos pos) {
        return Charge.distribution.network(worldIn).access(pos).getComparatorOutput();
    }

    @Override
    public void defineRecipes() {
        ItemStack stack = new ItemStack(this, 2);
        CraftingPlugin.addShapedRecipe("railcraft:flux_transformer",
                stack,
                "CGC",
                "GRG",
                "CTC",
                'G', RailcraftItems.PLATE, Metal.GOLD,
                'C', RailcraftItems.CHARGE, ItemCharge.EnumCharge.SPOOL_SMALL,
                'T', RailcraftItems.CHARGE, ItemCharge.EnumCharge.TERMINAL,
                'R', "blockRedstone");
    }
}
