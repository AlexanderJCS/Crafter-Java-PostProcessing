//TODO: load up mods from JavaScript
/*
TODO: Use java classes as if they were compiled :D
TODO: Figure out more things like biomes
TODO:
*/

/*
API is the base of the API which loads up class variables into the crafter array.
This allows for mods to index DIRECTLY into the engine without needing to be compiled.
Limitations: Ecmascript 5 :( No constants, anything can be overridden.
*/

// Lua equivalent!
var doFile;
// Lua addition!
var readFileToString;

// Auto executing lambda localized variable scope discards
!function(){
    var FileReader = Java.type("org.crafter.engine.utility.FileReader");
    var API = Java.type("org.crafter.engine.api.API");
    doFile = API.runCode;
    readFileToString = FileReader.getFileString;
}()

var crafter = [];

doFile("api/testing.js");

var stringy = readFileToString("api/testing.js");
print(stringy);
