package pl.iceclient.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import pl.iceclient.core.GfxMath;
import pl.iceclient.core.RubikCube;

/** Rysuje animowaną kostkę Rubika z czworokątów przesyłanych wprost do bufora GUI. */
public final class CubeRenderer {
    public static final RubikCube CUBE = new RubikCube(7, 7);

    private CubeRenderer() {}

    public static void draw(DrawContext ctx, float cx, float cy, float scale, float alpha, long cubeTimeMs, double assemble) {
        VertexConsumer vc = ctx.getVertexConsumers().getBuffer(RenderLayer.getGui());
        Matrix4f m = ctx.getMatrices().peek().getPositionMatrix();
        for (RubikCube.Quad q : CUBE.build(cubeTimeMs, assemble)) {
            int color = GfxMath.mulAlpha(q.color(), alpha);
            for (int i = 0; i < 4; i++) {
                vc.vertex(m, cx + q.x()[i] * scale, cy + q.y()[i] * scale, 0f).color(color);
            }
        }
        ctx.draw();
    }
}
