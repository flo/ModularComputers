/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.computer.module.world;

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.asset.Assets;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.health.DestroyEvent;
import org.terasology.math.Direction;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

import java.util.Map;

public class DestroyMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;
    private Block replaceBlock;
    private WorldProvider worldProvider;
    private BlockEntityRegistry blockEntityRegistry;

    public DestroyMethod(String methodName, WorldProvider worldProvider, BlockEntityRegistry blockEntityRegistry,
                         Block replaceBlock) {
        super("Destroys the block in the specified direction. The resulting items from destroying the " +
                "block are scattered on the ground.", "Boolean", "Whether destroying the specified block was successful.");
        this.worldProvider = worldProvider;
        this.blockEntityRegistry = blockEntityRegistry;
        this.methodName = methodName;
        this.replaceBlock = replaceBlock;

        addParameter("direction", "Direction", "Direction in which to destroy the block.");

        addExample(
                "This example destroys the block below the computer. Please make sure " +
                        "this computer has a module of World Interaction type in any of its slots.",
                "var worldMod = computer.bindModuleOfType(\"" + WorldModuleCommonSystem.WORLD_MODULE_TYPE + "\");\n" +
                        "var worldMod.destroyBlock(\"down\");");
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        return 250;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        Direction direction = FunctionParamValidationUtil.validateDirectionParameter(line, parameters, "direction", methodName);

        Vector3f computerLocation = computer.getComputerLocation();
        Vector3i directionVector = direction.getVector3i();
        Vector3i harvestLocation = new Vector3i(
                computerLocation.x + directionVector.x,
                computerLocation.y + directionVector.y,
                computerLocation.z + directionVector.z);

        Block blockBeforeDestroy = worldProvider.getBlock(harvestLocation);
        if (blockBeforeDestroy != replaceBlock) {
            EntityRef harvestedEntity = blockEntityRegistry.getBlockEntityAt(harvestLocation);
            harvestedEntity.send(new DestroyEvent(computer.getComputerEntity(), EntityRef.NULL, Assets.getPrefab("ModularComputers:harvestDamage").get()));

            return worldProvider.getBlock(harvestLocation) != blockBeforeDestroy;
        } else {
            return true;
        }
    }
}
