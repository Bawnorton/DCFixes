package com.bawnorton.dcfixes.mixin.refurbished_furniture;

import com.bawnorton.dcfixes.mixin_extensions.annotation.IfModLoaded;
import com.mrcrayfish.furniture.refurbished.Constants;
import com.mrcrayfish.furniture.refurbished.electricity.ElectricityTicker;
import com.mrcrayfish.furniture.refurbished.electricity.IElectricityNode;
import com.mrcrayfish.furniture.refurbished.electricity.IModuleNode;
import com.mrcrayfish.furniture.refurbished.electricity.ISourceNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IfModLoaded("refurbished_furniture")
@Mixin(value = ElectricityTicker.class, remap = false)
abstract class ElectricityTickerMixin {
    @Unique
    private static final int dcfixes$UNPOWERED_TICK_RATE = 20;

    @Shadow
    @Final
    private Level level;

    @Unique
    private final List<IModuleNode> dcfixes$poweredModules = new ArrayList<>();
    @Unique
    private final List<IModuleNode> dcfixes$unpoweredModules = new ArrayList<>();
    @Unique
    private final Set<BlockPos> dcfixes$modulePositions = new HashSet<>();

    @Unique
    private final List<ISourceNode> dcfixes$sources = new ArrayList<>();
    @Unique
    private final Set<BlockPos> dcfixes$sourcePositions = new HashSet<>();

    @Unique
    private int dcfixes$tickCount = 0;

    /**
     * @author Bawnorton
     * @reason optimisations
     */
    @Overwrite
    public void addElectricityNode(IElectricityNode node) {
        if(node instanceof IModuleNode module) {
            if(this.dcfixes$modulePositions.add(node.getNodePosition())) {
                (module.isNodePowered() ? this.dcfixes$poweredModules : this.dcfixes$unpoweredModules).add(module);
            }
        }
        else if(node instanceof ISourceNode source) {
            if(this.dcfixes$sourcePositions.add(node.getNodePosition())) {
                this.dcfixes$sources.add(source);
            }
        }
    }

    /**
     * @author Bawnorton
     * @reason optimisations
     */
    @Overwrite
    public void earlyTick() {
        this.dcfixes$tickCount = (this.dcfixes$tickCount + 1) % dcfixes$UNPOWERED_TICK_RATE;
        this.dcfixes$earlyTickList(this.dcfixes$poweredModules, this.dcfixes$modulePositions);
        this.dcfixes$earlyTickList(this.dcfixes$sources, this.dcfixes$sourcePositions);
    }

    /**
     * @author Bawnorton
     * @reason optimisations
     */
    @Overwrite
    public void tick() {
        this.dcfixes$tickModuleList(this.dcfixes$poweredModules, this.dcfixes$unpoweredModules, true);
        if(this.dcfixes$tickCount == 0) {
            this.dcfixes$tickModuleList(this.dcfixes$unpoweredModules, this.dcfixes$poweredModules, false);
        }
    }

    @Unique
    private <T extends IElectricityNode> void dcfixes$earlyTickList(List<T> nodes, Set<BlockPos> positions) {
        int i = 0;
        while(i < nodes.size()) {
            T node = nodes.get(i);
            if(!node.isNodeValid()) {
                BlockPos pos = node.getNodePosition();
                Constants.LOG.debug("Stopping ticking node at {}", pos);
                positions.remove(pos);
                dcfixes$swapRemove(nodes, i);
            }
            else {
                if(this.level.shouldTickBlocksAt(node.getNodePosition())) {
                    node.earlyNodeTick(this.level);
                }
                i++;
            }
        }
    }

    @Unique
    private void dcfixes$tickModuleList(List<IModuleNode> nodes, List<IModuleNode> other, boolean expectedPowered) {
        int i = 0;
        while(i < nodes.size()) {
            IModuleNode node = nodes.get(i);
            if(!node.isNodeValid()) {
                BlockPos pos = node.getNodePosition();
                Constants.LOG.debug("Stopping ticking node at {}", pos);
                this.dcfixes$modulePositions.remove(pos);
                dcfixes$swapRemove(nodes, i);
            } else if(this.level.shouldTickBlocksAt(node.getNodePosition())) {
                node.moduleTick(this.level);
                if(node.isNodePowered() != expectedPowered) {
                    dcfixes$swapRemove(nodes, i);
                    other.add(node);
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }
    }

    @Unique
    private static <T> void dcfixes$swapRemove(List<T> list, int index) {
        int last = list.size() - 1;
        list.set(index, list.get(last));
        list.remove(last);
    }
}
