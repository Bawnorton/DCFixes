package com.bawnorton.dcfixes.super_early.extensions;

import com.bawnorton.dcfixes.super_early.reflection.FieldReference;
import com.bawnorton.dcfixes.super_early.reflection.MethodReference;
import net.minecraftforge.fml.earlydisplay.ElementShader;
import net.minecraftforge.fml.earlydisplay.RenderElement;
import net.minecraftforge.fml.earlydisplay.SimpleBufferBuilder;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Objects;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public class RenderElementExtension {
    private static final int INDEX_TEXTURE_OFFSET = 5;

    private static final FieldReference<Integer> globalAlphaReference = FieldReference.ofStatic(RenderElement.class, "globalAlpha", int.class);

    private static final MethodReference<RenderElement> ctorReference;

    public static RenderElement create(Object initializer) {
        return ctorReference.invoke(initializer);
    }

    public static Object reimplInitializeTexture(String textureFileName, int size, int textureNumber, Object textureRenderer) {
        return InitializerExtension.create(() -> {
            int[] imgSize = loadTextureFromClasspath(textureFileName, size, GL_TEXTURE0 + textureNumber + INDEX_TEXTURE_OFFSET);

            return RendererExtension.create((bb, ctx, frame) -> {
                TextureRendererExtension.tryAs(textureRenderer, extension -> {
                    ctx.elementShader().updateTextureUniform(textureNumber + INDEX_TEXTURE_OFFSET);
                    ctx.elementShader().updateRenderTypeUniform(ElementShader.RenderType.TEXTURE);
                    bb.begin(SimpleBufferBuilder.Format.POS_TEX_COLOR, SimpleBufferBuilder.Mode.QUADS);
                    extension.invokeAccept(bb, ctx, imgSize, frame);
                    bb.draw();
                });
            });
        });
    }

    public static ByteBuffer readFromClasspath(final String name, int initialCapacity) {
        ByteBuffer buf;
        try (var channel = Channels.newChannel(Objects.requireNonNull(RenderElementExtension.class.getClassLoader().getResourceAsStream(name), "The resource "+name+" cannot be found"))) {
            buf = BufferUtils.createByteBuffer(initialCapacity);
            while (true) {
                var readbytes = channel.read(buf);
                if (readbytes == -1) break;
                if (buf.remaining() == 0) {
                    var newBuf = BufferUtils.createByteBuffer(buf.capacity() * 3 / 2);
                    buf.flip();
                    newBuf.put(buf);
                    buf = newBuf;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        buf.flip();
        return MemoryUtil.memSlice(buf);
    }

    public static int[] loadTextureFromClasspath(String file, int size, int textureNumber) {
        int[] lw = new int[1];
        int[] lh = new int[1];
        int[] lc = new int[1];
        var img = loadImageFromClasspath(file, size, lw, lh, lc);
        var texid = glGenTextures();
        glActiveTexture(textureNumber);
        glBindTexture(GL_TEXTURE_2D, texid);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, lw[0], lh[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, img);
        glActiveTexture(GL_TEXTURE0);
        MemoryUtil.memFree(img);
        return new int[] {lw[0], lh[0]};
    }

    public static ByteBuffer loadImageFromClasspath(String file, int size, int[] width, int[] height, int[] channels) {
        ByteBuffer buf = readFromClasspath(file, size);
        return STBImage.stbi_load_from_memory(buf, width, height, channels, 4);
    }

    static {
        try {
            Class<?> initializerType = Class.forName("net.minecraftforge.fml.earlydisplay.RenderElement$Initializer");
            ctorReference = MethodReference.ofConstructor(RenderElement.class, initializerType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getGlobalAlpha() {
        return globalAlphaReference.get();
    }
}
