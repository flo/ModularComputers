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
package org.terasology.computer.context;

import org.terasology.computer.system.server.lang.ComputerModule;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.geom.Vector3f;

public interface ComputerCallback {
    public ComputerConsole getConsole();
    public int getModuleSlotsCount();
    public ComputerModule getModule(int slot);
    public Vector3f getComputerLocation();
    public void suspendWithCondition(AwaitingCondition condition);
    public EntityRef getComputerEntity();
}
