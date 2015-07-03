package mas;

import java.nio.ByteBuffer;

import org.lwjgl.Sys;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

/**
 * @author SCAREX
 * 
 */
public class MAS
{
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private long window;

	public static void main(String[] args) {
		(new MAS()).run();
	}

	private void run() {
		System.out.println("LWJGL version : " + Sys.getVersion());

		try {
			init();
			loop();

			GLFW.glfwDestroyWindow(window);
			this.keyCallback.release();
		} finally {
			GLFW.glfwTerminate();
			this.errorCallback.release();
		}
	}

	private void init() {
		GLFW.glfwSetErrorCallback(this.errorCallback = Callbacks.errorCallbackPrint(System.err));

		if (GLFW.glfwInit() != GL11.GL_TRUE) throw new IllegalStateException("Couldn't initialize GLFW");

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);

		int width = 600;
		int height = 400;

		this.window = GLFW.glfwCreateWindow(width, height, "Model and Animator Software", 0, 0);
		if (this.window == 0) throw new RuntimeException("Couldn't create GLFW window");

		GLFW.glfwSetKeyCallback(this.window, this.keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) GLFW.glfwSetWindowShouldClose(window, 1);
			}
		});

		ByteBuffer vmod = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

		GLFW.glfwSetWindowPos(this.window, (GLFWvidmode.width(vmod) - width) / 2, (GLFWvidmode.height(vmod) - height) / 2);

		GLFW.glfwMakeContextCurrent(this.window);
		GLFW.glfwSwapInterval(1);

		GLFW.glfwShowWindow(this.window);
	}

	private void loop() {
		GLContext.createFromCurrent();

		GL11.glClearColor(0.0F, 0.0F, 1.0F, 0.0F); // Because blue is my
													// favorite color

		while (GLFW.glfwWindowShouldClose(this.window) == 0) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GLFW.glfwSwapBuffers(this.window);
			GLFW.glfwPollEvents();
		}
	}
}
