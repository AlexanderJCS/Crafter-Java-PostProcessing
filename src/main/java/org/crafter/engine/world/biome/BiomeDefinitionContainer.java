/*
 Crafter - A blocky game (engine) written in Java with LWJGL.
 Copyright (C) 2023  jordan4ibanez

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.crafter.engine.world.biome;

import java.util.HashMap;

import static org.crafter.engine.utility.UtilityPrinter.println;

public final class BiomeDefinitionContainer {

    private static final HashMap<String, BiomeDefinition> container = new HashMap<>();
    private BiomeDefinitionContainer(){}


    public static void registerBiome(String name, BiomeDefinition definition) {
        // TODO: maybe overrides aren't a good idea? I dunno. See if checking or clearing is a more concise way to do this maybe.
        println("BiomeDefinitionContainer: Registered biome: (" + name + ")");
        container.put(name, definition);
    }

    public static BiomeDefinition getBiome(String name) {
        return container.get(name);
    }
}
