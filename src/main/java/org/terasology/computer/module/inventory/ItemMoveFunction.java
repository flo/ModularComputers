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

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.ModuleFunctionExecutable;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.inventory.InventoryUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemMoveFunction implements ModuleFunctionExecutable {
    private InventoryManager inventoryManager;

    public ItemMoveFunction(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @Override
    public int getDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTicks() {
        return 0;
    }

    @Override
    public String[] getParameterNames() {
        return new String[] {"inventoryBindingFrom", "inventoryBindingTo", "slot"};
    }

    @Override
    public Object executeFunction(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        Variable inventoryBindingFrom = parameters.get("inventoryBindingFrom");
        if (inventoryBindingFrom.getType() != Variable.Type.CUSTOM_OBJECT
                || !((CustomObject) inventoryBindingFrom.getValue()).getType().equals("INVENTORY_BINDING")
                || ((InventoryBinding) inventoryBindingFrom.getValue()).isInput())
            throw new ExecutionException(line, "Invalid inventoryBindingFrom in itemMove()");

        Variable inventoryBindingTo = parameters.get("inventoryBindingTo");
        if (inventoryBindingTo.getType() != Variable.Type.CUSTOM_OBJECT
                || !((CustomObject) inventoryBindingTo.getValue()).getType().equals("INVENTORY_BINDING")
                || !((InventoryBinding) inventoryBindingTo.getValue()).isInput())
            throw new ExecutionException(line, "Invalid inventoryBindingTo in itemMove()");

        Variable slot = parameters.get("slot");
        if (slot.getType() != Variable.Type.NUMBER)
            throw new ExecutionException(line, "Invalid slot in itemMove()");

        int slotNo = ((Number) slot.getValue()).intValue();

        InventoryBinding bindingFrom = (InventoryBinding) inventoryBindingFrom.getValue();
        InventoryBinding.InventoryWithSlots inventoryFrom = bindingFrom.getInventoryEntity(line, computer);

        InventoryBinding bindingTo = (InventoryBinding) inventoryBindingTo.getValue();
        InventoryBinding.InventoryWithSlots inventoryTo = bindingTo.getInventoryEntity(line, computer);

        int slotFromCount = inventoryFrom.slots.size();

        if (slotNo<0 || slotFromCount<=slotNo)
            throw new ExecutionException(line, "Slot number out of range in itemMove()");

        int itemCountBefore = InventoryModuleUtils.getItemCount(InventoryUtils.getItemAt(inventoryFrom.inventory, inventoryFrom.slots.get(slotNo)));

        inventoryManager.moveItemToSlots(computer.getComputerEntity(), inventoryFrom.inventory, slotNo, inventoryTo.inventory, inventoryTo.slots);

        int itemCountAfter = InventoryModuleUtils.getItemCount(InventoryUtils.getItemAt(inventoryFrom.inventory, inventoryFrom.slots.get(slotNo)));

        return itemCountBefore-itemCountAfter;
    }
}
