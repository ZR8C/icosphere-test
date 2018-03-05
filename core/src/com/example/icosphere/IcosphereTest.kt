package com.example.icosphere

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.example.icosphere.model.Icosphere

class IcosphereTest : ApplicationAdapter() {

    lateinit var modelBatch: ModelBatch
    lateinit var cam: PerspectiveCamera
    lateinit var environment: Environment

    private val icosphere by lazy { Icosphere(6) }

    override fun create() {
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.near = 1f
        cam.far = 300f
        cam.position.set(5f,5f,5f)
        cam.lookAt(0f, 0f, 0f)
        cam.update()

        modelBatch = ModelBatch()
        //env so models look a little nicer
        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
    }

    override fun render() {
        cam.update()
        Gdx.gl.glViewport(0,0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch.begin(cam)

        icosphere.triangles.forEach {
            modelBatch.render(it.modelInstance(), environment)
        }

        modelBatch.end()

    }

    override fun dispose() {
        modelBatch.dispose()

    }

    override fun resize(width: Int, height: Int) {
//        stage.viewport.update(width, height)
        cam.viewportWidth = width.toFloat()
        cam.viewportHeight = height.toFloat()
        cam.update()
    }
}
