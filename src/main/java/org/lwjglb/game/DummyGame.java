package org.lwjglb.game;

import com.google.common.collect.Sets;
import org.apache.commons.lang.ArrayUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.IGameLogic;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Texture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DummyGame implements IGameLogic
{

  private static final float MOUSE_SENSITIVITY = 0.2f;

  private final Vector3f cameraInc;

  private final Renderer renderer;

  private final Camera camera;

  private GameItem[] gameItems;

  private static final float CAMERA_POS_STEP = 0.05f;

  public DummyGame()
  {
    renderer = new Renderer();
    camera = new Camera();
    cameraInc = new Vector3f(0, 0, 0);
  }

  @Override
  public void init(Window window) throws Exception
  {
    renderer.init(window);
    // Create the Mesh
    File f = new File(Objects.requireNonNull(Main.class.getClassLoader().getResource("geometry.xml")).getFile());

    List<XmlParser.ThreeDVertex> geometryRow = XmlParser.parseVertices(f);
    List<Integer> vertIndexes = deleteSamePointsInTriangle(XmlParser.parseVertIndexes(f));
    List<XmlParser.TextureCoordinate> textures = XmlParser.parseTextures(f);
    List<Integer> textIndexes = deleteSamePointsInTriangle(XmlParser.parseTextureIndexes(f));

    int[] indexesArray = vertIndexes.stream().mapToInt(i -> i).toArray();

    List<XmlParser.ThreeDVertex> geometryNew = vertIndexes.stream().map(geometryRow::get).collect(Collectors.toList());
    List<XmlParser.TextureCoordinate> texturesNew = textIndexes.stream().map(textures::get).collect(Collectors.toList());

    Texture texture = new Texture("/textures/texture.png");

    List<Float> coords = geometryNew.stream()
        .flatMap(threeDVertex -> Stream.of(threeDVertex.longitude, threeDVertex.latitude, threeDVertex.height))
        .collect(Collectors.toList());

    float[] floatsCoords = ArrayUtils.toPrimitive(coords.toArray(new Float[0]), 0.0F);

    List<Float> textCoords = texturesNew.stream()
        .flatMap(threeDVertex -> Stream.of(threeDVertex.u, threeDVertex.v))
        .collect(Collectors.toList());

    float[] floatsText = ArrayUtils.toPrimitive(textCoords.toArray(new Float[0]), 0.0F);

    Mesh mesh = new Mesh(floatsCoords, floatsText, indexesArray, texture);
    GameItem gameItem1 = new GameItem(mesh);
    gameItem1.setScale(0.5f);
    gameItem1.setRotation(0, 0, 0);
    gameItem1.setPosition(0, 0, -1);

    gameItems = new GameItem[] {gameItem1};
  }

  private List<Integer> deleteSamePointsInTriangle(List<Integer> points)
  {
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < points.size() - 2; i++)
    {
      Set<Integer> localSet = Sets.newHashSet(points.get(i), points.get(i + 1), points.get(i + 2));

      if (localSet.size() == 3)
      {
        result.add(points.get(i));
      }
    }
    return result;
  }

  @Override
  public void input(Window window, MouseInput mouseInput)
  {
    cameraInc.set(0, 0, 0);
    if (window.isKeyPressed(GLFW_KEY_W))
    {
      cameraInc.z = -1;
    }
    else if (window.isKeyPressed(GLFW_KEY_S))
    {
      cameraInc.z = 1;
    }
    if (window.isKeyPressed(GLFW_KEY_A))
    {
      cameraInc.x = -1;
    }
    else if (window.isKeyPressed(GLFW_KEY_D))
    {
      cameraInc.x = 1;
    }
    if (window.isKeyPressed(GLFW_KEY_Z))
    {
      cameraInc.y = -1;
    }
    else if (window.isKeyPressed(GLFW_KEY_X))
    {
      cameraInc.y = 1;
    }
  }

  @Override
  public void update(float interval, MouseInput mouseInput)
  {
    // Update camera position
    camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

    // Update camera based on mouse
    if (mouseInput.isRightButtonPressed())
    {
      Vector2f rotVec = mouseInput.getDisplVec();
      camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
    }
  }

  @Override
  public void render(Window window)
  {
    renderer.render(window, camera, gameItems);
  }

  @Override
  public void cleanup()
  {
    renderer.cleanup();
    for (GameItem gameItem : gameItems)
    {
      gameItem.getMesh().cleanUp();
    }
  }
}
