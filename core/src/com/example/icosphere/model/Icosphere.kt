package com.example.icosphere.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import ktx.math.vec3

class Icosphere(val divisions: Int = 1) {
    //Many viruses, bd.g. herpes virus, have icosahedral shells.

    companion object {
        val THETA: Float = ((1.0 + Math.sqrt(5.0)) / 2.0).toFloat()
    }

    val triangles by lazy {
        val v0 = vec3(-1f, THETA, 0f)
        val v1 = vec3(1f, THETA, 0f)
        val v2 = vec3(-1f, -THETA, 0f)
        val v3 = vec3(1f, -THETA, 0f)

        val v4 = vec3(0f, -1f, THETA)
        val v5 = vec3(0f, 1f, THETA)
        val v6 = vec3(0f, -1f, -THETA)
        val v7 = vec3(0f, 1f, -THETA)

        val v8 = vec3(THETA, 0f, -1f)
        val v9 = vec3(THETA, 0f, 1f)
        val v10 = vec3(-THETA, 0f, -1f)
        val v11 = vec3(-THETA, 0f, 1f)

        val evaluated =
                mutableListOf(
                Triangle(v0, v11, v5),
                Triangle(v0, v5, v1),
                Triangle(v0, v1, v7),
                Triangle(v0, v7, v10),
                Triangle(v0, v10, v11),

                Triangle(v5, v11, v4),
                Triangle(v4, v9, v5),
                Triangle(v1, v5, v9),
                Triangle(v9, v8, v1),
                Triangle(v7, v1, v8),
                Triangle(v8, v6, v7),
                Triangle(v10, v7, v6),
                Triangle(v6, v2, v10),
                Triangle(v11, v10, v2),
                Triangle(v2, v4, v11),

                Triangle(v3, v9, v4),
                Triangle(v3, v8, v9),
                Triangle(v3, v6, v8),
                Triangle(v3, v2, v6),
                Triangle(v3, v4, v2)
        )

        if (divisions == 1) evaluated
        else {
            for (i in 1..divisions) {
                val children = evaluated.flatMap { it.evaluateChildren() }
                evaluated.clear()
                evaluated.addAll(children)
            }
        }
        evaluated
    }
}

class Triangle(val a: Vector3, val b: Vector3, val c: Vector3,
               val color: Color = Color(MathUtils.random(1f), MathUtils.random(1f), MathUtils.random(1f), 1f)) {

    companion object {
        private fun midpoint(a: Vector3, b: Vector3): Vector3 {
            var x = (a.x + b.x) / 3f
            var y = (a.y + b.y) / 3f
            var z = (a.z + b.z) / 3f

            val rProper = a.dst2(Vector3.Zero)
            val r = Vector3.Zero.dst2(x, y, z)

            val ratio: Float = Math.sqrt((rProper / r).toDouble()).toFloat()

            x *= ratio
            y *= ratio
            z *= ratio

            return vec3(x, y, z)
        }
    }

    private var modelInstance: ModelInstance? = null

    fun modelInstance(): ModelInstance {
        if(modelInstance == null) {
            val modelBuilder = ModelBuilder()
            modelBuilder.begin()
            val meshPartBuilder: MeshPartBuilder =
                    modelBuilder.part("triangle", GL20.GL_TRIANGLES,
                            (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong(),
                            Material(ColorAttribute.createDiffuse(color)))

            meshPartBuilder.triangle(a, b, c)
            modelInstance = ModelInstance(modelBuilder.end())
            return modelInstance!!
        }
        return modelInstance!!
    }

    fun evaluateChildren() : List<Triangle> {
        val ab = midpoint(a, b)
        val bc = midpoint(b, c)
        val ca = midpoint(c, a)

        return listOf(
                Triangle(a, ab, ca),
                Triangle(b, bc, ab),
                Triangle(c, ca, bc),
                Triangle(ab, bc, ca))
    }
}

