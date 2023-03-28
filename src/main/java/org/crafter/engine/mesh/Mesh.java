package org.crafter.engine.mesh;

import org.crafter.engine.texture.TextureStorage;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glVertexAttribIPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

/**
 * The actual Mesh object.
 * To interface into this you must talk to MeshStorage!
 */
public class Mesh {

    // Reserved invalidation token
    private static final int INVALID = Integer.MAX_VALUE;

    // Required VAO, VBO, vertex count, & texture ID
    private final int vaoID;

    private final int positionsVboID;

    private final int textureCoordinatesVboID;

    private final int indicesVboID;

    private final int vertexCount;

    private final int textureID;


    // Optional Vertex Buffer Objects
    private int bonesVboID = INVALID;

    private int colorsVboID = INVALID;

    // Not using builder pattern in Java because I'm trying out a new structure implementation
    Mesh(float[] positions, float[] textureCoordinates, int[] indices, int[] bones, float[] colors, String textureFileLocation) {

        // Before anything is sent to the GPU, let's check that texture
        try {
            textureID = TextureStorage.getID(textureFileLocation);
        } catch (RuntimeException e) {
            // We're going to throw a different, more specific error
            throw new RuntimeException("Mesh: Tried to use a nonexistent texture for a mesh! (" + textureFileLocation + ") does not exist! Did you add it to the TextureStorage?");
        }

        checkRequired(positions, textureCoordinates, indices);

        vertexCount = positions.length / 3;

        vaoID = glGenVertexArrays();

        // Bind into the Vertex Array Object context
        glBindVertexArray(vaoID);


        // Assign all Vertex buffer Objects
        positionsVboID = uploadFloatArray(positions, 0, 3);
        textureCoordinatesVboID = uploadFloatArray(textureCoordinates, 1, 2);
        indicesVboID = uploadIndices(indices);

        if (bones != null) {
            uploadIntArray(bones, 2, 1);
        }

        if (colors != null) {
            uploadFloatArray(colors, 2, 4);
        }

        // Now unbind the Vertex Array Object context
        glBindVertexArray(0);
    }

    // float[] automator method
    private int uploadFloatArray(float[] floatArray, int glslPosition, int componentsInStructure) {
        // Starts off as: float* var = nullptr;
        FloatBuffer buffer = null;

        final int returningID;

        try {

            buffer = MemoryUtil.memAllocFloat(floatArray.length);
            buffer.put(floatArray).flip();

            returningID = glGenBuffers();

            // Bind into the Vertex Buffer Object context
            glBindBuffer(GL_ARRAY_BUFFER, returningID);

            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
            // Not normalized (false), no stride (0), array starts at index 0 (0)
            glVertexAttribPointer(glslPosition, componentsInStructure, GL_FLOAT, false, 0, 0);

            // Now unbind the Vertex Buffer Object context
            glBindBuffer(GL_ARRAY_BUFFER, 0);

        } finally {
            // Free the C float*
            if (buffer != null) {
                MemoryUtil.memFree(buffer);
            }
        }
        return returningID;
    }

    // int[] automator method
    private int uploadIntArray(int[] intArray, int glslPosition, int componentsInStructure) {
        // Starts off as: int* var = nullptr;
        IntBuffer buffer = null;

        final int returningID;

        try {

            buffer = MemoryUtil.memAllocInt(intArray.length);
            buffer.put(intArray).flip();

            returningID = glGenBuffers();

            // Bind into the Vertex Buffer Object context
            glBindBuffer(GL_ARRAY_BUFFER, returningID);

            glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
            // Not normalized (false), no stride (0), array starts at index 0 (0)
            glVertexAttribIPointer(glslPosition, componentsInStructure, GL_INT, 0, 0);

            // Now unbind the Vertex Buffer Object context
            glBindBuffer(GL_ARRAY_BUFFER, 0);

        } finally {
            // Free the C int*
            if (buffer != null) {
                MemoryUtil.memFree(buffer);
            }
        }
        return returningID;
    }


    // This method is specialized, uploads the indices from an int[]
    private int uploadIndices(int[] indicesArray) {

        // Starts off as: int* var = nullptr;
        IntBuffer buffer = null;

        final int returningID;

        try {

            returningID = glGenBuffers();

            buffer = MemoryUtil.memAllocInt(indicesArray.length);
            buffer.put(indicesArray).flip();

            // Bind into the Vertex Buffer Object context
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, returningID);

            glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

            // Now unbind the Vertex Buffer Object context
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        } finally {
            if (buffer != null) {
                MemoryUtil.memFree(buffer);
            }
        }
        return returningID;
    }

    // This is a separate method to improve the constructor readability
    private void checkRequired(float[] positions, float[] textureCoordinates, int[] indices) {
        if (positions == null) {
            throw new RuntimeException("Mesh: Positions parameter CANNOT be null!");
        } else if (textureCoordinates == null) {
            throw new RuntimeException("Mesh: Texture coordinates parameter CANNOT be null!");
        } else if (indices == null) {
            throw new RuntimeException("Mesh: Indices parameter CANNOT be null!");
        }
        // Required data is all there, nice
    }

}
