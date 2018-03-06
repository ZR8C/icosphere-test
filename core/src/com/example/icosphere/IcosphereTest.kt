package com.example.icosphere

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.example.icosphere.model.Icosphere
import ktx.math.vec3

class IcosphereTest : ApplicationAdapter() {

    lateinit var modelBatch: ModelBatch
    lateinit var cam: PerspectiveCamera
    lateinit var environment: Environment

    lateinit var uiStage: Stage
    lateinit var statsLabel: Label
    lateinit var font: BitmapFont

    private val icosphere by lazy { Icosphere(3) }

    override fun create() {
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.near = 1f
        cam.far = 300f
        cam.position.set(3f,3f,3f)
        cam.lookAt(0f, 0f, 0f)
        cam.update()

        modelBatch = ModelBatch()
        //env so models look a little nicer
        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))

        uiStage = Stage()
        font = BitmapFont()
        statsLabel = Label("FPS: ", Label.LabelStyle(font, Color.WHITE))
        uiStage.addActor(statsLabel)
    }

    var rotateTimer = 0f

    override fun render() {
        cam.update()
        Gdx.gl.glViewport(0,0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch.begin(cam)

        rotateTimer += Gdx.graphics.deltaTime

        icosphere.triangles.forEach {
            if (rotateTimer > 1 / 30f) {
                cam.rotateAround(Vector3.Zero, vec3(10f,10f,1f), 1f)
                rotateTimer = 0f
            }

            modelBatch.render(it.modelInstance(), environment)
        }

        modelBatch.end()

        statsLabel.setText("FPS: ${Gdx.graphics.framesPerSecond}")
        uiStage.draw()
    }

    override fun dispose() {
        modelBatch.dispose()
        uiStage.dispose()
        font.dispose()
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height)
        cam.viewportWidth = width.toFloat()
        cam.viewportHeight = height.toFloat()
        cam.update()
    }
}
