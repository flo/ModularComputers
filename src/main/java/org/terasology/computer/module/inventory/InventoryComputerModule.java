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
package org.terasology.computer.module.inventory;

import org.terasology.computer.system.server.lang.ComputerModule;
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.world.BlockEntityRegistry;

import java.util.Collection;

public class InventoryComputerModule implements ComputerModule {
    private InventoryModuleConditionsRegister inventoryModuleConditionsRegister;
    private InventoryManager inventoryManager;
    private BlockEntityRegistry blockEntityRegistry;
    private String moduleType;
    private String moduleName;

    public InventoryComputerModule(InventoryModuleConditionsRegister inventoryModuleConditionsRegister,
                                   InventoryManager inventoryManager,
                                   BlockEntityRegistry blockEntityRegistry, String moduleType, String moduleName) {
        this.inventoryModuleConditionsRegister = inventoryModuleConditionsRegister;
        this.inventoryManager = inventoryManager;
        this.blockEntityRegistry = blockEntityRegistry;
        this.moduleType = moduleType;
        this.moduleName = moduleName;
    }

    @Override
    public String getModuleType() {
        return moduleType;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public boolean canBePlacedInComputer(Collection<ComputerModule> computerModulesInstalled) {
        return true;
    }

    @Override
    public boolean acceptsNewModule(ComputerModule computerModule) {
        return true;
    }

    @Override
    public ModuleMethodExecutable getFunctionByName(String name) {
        if (name.equals("getInputInventoryBinding"))
            return new InventoryBindingMethod("getInputInventoryBinding", blockEntityRegistry, true);
        if (name.equals("getOutputInventoryBinding"))
            return new InventoryBindingMethod("getOutputInventoryBinding", blockEntityRegistry, false);
        if (name.equals("getInventorySlotCount"))
            return new InventorySlotCountMethod("getInventorySlotCount");
        if (name.equals("getItemCount"))
            return new ItemCountMethod("getItemCount");
        if (name.equals("getItemName"))
            return new ItemNameMethod("getItemName");
        if (name.equals("getInventoryAndChangeCondition"))
            return new InventoryAndChangeConditionMethod("getInventoryAndChangeCondition", inventoryModuleConditionsRegister);
        if (name.equals("itemMove"))
            return new ItemMoveMethod("itemMove", inventoryManager);
        if (name.equals("dump"))
            return new DumpMethod("dump", inventoryManager);
        return null;
    }
}
