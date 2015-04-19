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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.logic.inventory.InventoryAccessComponent;
import org.terasology.logic.inventory.ItemComponent;

import java.util.Collections;
import java.util.List;

public class InventoryModuleUtils {
    private InventoryModuleUtils() {}

    public static int getItemCount(EntityRef itemEntity) {
        ItemComponent item = itemEntity.getComponent(ItemComponent.class);
        if (item == null)
            return 0;

        return (int) item.stackCount;
    }

    public static String getItemName(EntityRef itemEntity) {
        ItemComponent item = itemEntity.getComponent(ItemComponent.class);
        if (item == null)
            return null;

        DisplayNameComponent displayName = itemEntity.getComponent(DisplayNameComponent.class);
        if (displayName == null)
            return "Unknown";

        return displayName.name;
    }
}
