/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2016
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.items;

import mods.railcraft.common.plugins.forge.LootPlugin;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ItemSpikeMaulIron extends ItemSpikeMaul {

    public ItemSpikeMaulIron() {
        super(ItemMaterials.Material.IRON, ToolMaterial.IRON);
    }

    @Override
    public void defineRecipes() {
        // TODO: Add recipe
//        CraftingPlugin.addRecipe(new ItemStack(this),
//                " RI",
//                "RIR",
//                "IR ",
//                'I', "ingotIron",
//                'R', "dyeRed");
    }

    @Override
    public void initializeDefinintion() {
        super.initializeDefinintion();
        LootPlugin.addLoot(RailcraftItems.SPIKE_MAUL_IRON, 1, 1, LootPlugin.Type.TOOL);
        LootPlugin.addLoot(RailcraftItems.SPIKE_MAUL_IRON, 1, 1, LootPlugin.Type.WORKSHOP);
    }

}
