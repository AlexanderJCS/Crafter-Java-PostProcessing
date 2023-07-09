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

//TODO: load up mods from JavaScript
/*
TODO: Use java classes as if they were compiled :D
TODO: Figure out more things like mobs & items & entities

TODO! VERY IMPORTANT! Add comments! See if there is a way to link the java documentation into javascript....somehow?
*/

/*
API is the base of the API which loads up class variables into the crafter array.
This allows for mods to index DIRECTLY into the engine without needing to be compiled.
Limitations: Ecmascript 6. Class architecture is not implemented into nashorn yet.
Possible implementations: Typescript (one day)
*/

// Lua equivalents!
const doFile = Java.type("org.crafter.engine.api.API").runCode;
const readFileToString = Java.type("org.crafter.engine.utility.FileReader").getFileString;

// Global java types assignment
const BlockDefinition = Java.type("org.crafter.engine.world.block.BlockDefinition");
const DrawType = Java.type("org.crafter.engine.world.block.DrawType");
const BiomeDefinition = Java.type("org.crafter.engine.world.biome.BiomeDefinition");

// Very similar to minetest's api table, basically a clone of it in JS.
const crafter = [];

// Auto executing lambda localized variable scope discards
!function(){

    // Classes from the engine which will disappear after this scope.
    const BlockDefinitionContainer = Java.type("org.crafter.engine.world.block.BlockDefinitionContainer");
    const BiomeDefinitionContainer = Java.type("org.crafter.engine.world.biome.BiomeDefinitionContainer");

    // Javascript level Block Definition registration function.
    crafter.registerBlock = function(newBlockDefinition) {
        BlockDefinitionContainer.getMainInstance().registerBlock(newBlockDefinition);
    }

    // Javascript level Biome Definition registration function.
    crafter.registerBiome = function(newBiomeDefinition) {
        BiomeDefinitionContainer.registerBiome(newBiomeDefinition);
    }
}()

// Air is reserved here
crafter.registerBlock(
    new BlockDefinition("air")
        .setDrawType(DrawType.AIR)
);