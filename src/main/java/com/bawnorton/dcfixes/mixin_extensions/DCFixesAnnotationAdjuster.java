package com.bawnorton.dcfixes.mixin_extensions;

import com.bawnorton.mixinsquared.adjuster.tools.*;
import com.bawnorton.mixinsquared.api.MixinAnnotationAdjuster;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public final class DCFixesAnnotationAdjuster implements MixinAnnotationAdjuster {
    @Override
    public @Nullable AdjustableAnnotationNode adjust(List<String> targetClassNames, String mixinClassName, MethodNode handlerNode, AdjustableAnnotationNode annotationNode) {
        return switch (mixinClassName) {
            case "ca.fxco.moreculling.mixin.renderers.ItemRenderer_faceCullingMixin" -> adjustItemRenderer_faceCullingMixin(mixinClassName, annotationNode, handlerNode);
            case "ca.fxco.moreculling.mixin.models.cullshape.JsonUnbakedModel_cullShapeMixin" -> adjustJsonUnbakedModel_cullShapeMixin(annotationNode);
            default -> annotationNode;
        };
    }

    private AdjustableAnnotationNode adjustJsonUnbakedModel_cullShapeMixin(AdjustableAnnotationNode annotationNode) {
        if (annotationNode.is(Redirect.class)) {
            AdjustableRedirectNode redirectNode = annotationNode.as(AdjustableRedirectNode.class);
            if (redirectNode.getAt().getTarget().equals("Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;")) {
                return null;
            }
        }
        if (annotationNode.is(WrapOperation.class)) {
            AdjustableWrapOperationNode wrapOpNode = annotationNode.as(AdjustableWrapOperationNode.class);
            if (wrapOpNode.getAt().stream().anyMatch(at -> at.getTarget().equals("Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))) {
                return null;
            }
        }
        return annotationNode;
    }

    private AdjustableAnnotationNode adjustItemRenderer_faceCullingMixin(String mixinClassName, AdjustableAnnotationNode annotationNode, MethodNode handlerNode) {
        if (annotationNode.is(Inject.class)) {
            return annotationNode.as(AdjustableInjectNode.class).withAt(ats -> ats.stream().map(at -> at.withTarget(target -> {
                if (target.equals("Lnet/minecraft/client/renderer/ItemBlockRenderTypes;getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;")
                        || target.equals("Lnet/minecraft/client/renderer/ItemBlockRenderTypes;m_109279_(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;")) {
                    return "net/minecraft/client/resources/model/BakedModel.getRenderTypes (Lnet/minecraft/world/item/ItemStack;Z)Ljava/util/List;";
                }
                return target;
            })).toList());
        }
        if (annotationNode.is(WrapOperation.class)) {
            AdjustableWrapOperationNode wrapOpNode = annotationNode.as(AdjustableWrapOperationNode.class);
            if (wrapOpNode.getAt().stream().anyMatch(at -> {
                String target = at.getTarget();
                return target.equals("Lnet/minecraft/client/renderer/block/model/ItemTransforms;getTransform(Lnet/minecraft/world/item/ItemDisplayContext;)Lnet/minecraft/client/renderer/block/model/ItemTransform;")
                        || target.equals("Lnet/minecraft/client/renderer/block/model/ItemTransforms;m_269404_(Lnet/minecraft/world/item/ItemDisplayContext;)Lnet/minecraft/client/renderer/block/model/ItemTransform;");
            })) {
                AdjustableModifyExpressionValueNode mevNode = AdjustableModifyExpressionValueNode.defaultNode(
                        wrapOpNode.getMethod(),
                        List.of(AdjustableAtNode.defaultNode(AdjustableAtNode.InjectionPoint.INVOKE)
                                .withTarget(target -> "net/minecraftforge/client/ForgeHooksClient.handleCameraTransforms (Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/item/ItemDisplayContext;Z)Lnet/minecraft/client/resources/model/BakedModel;")
                        )
                );

                Type bakedModelType = Type.getType("Lnet/minecraft/client/resources/model/BakedModel;");
                Type localRefType = Type.getType("Lcom/llamalad7/mixinextras/sugar/ref/LocalRef;");
                Type displayContextType = Type.getType("Lnet/minecraft/world/item/ItemDisplayContext;");
                handlerNode.desc = Type.getMethodDescriptor(bakedModelType, bakedModelType, displayContextType, localRefType);
                int modelIndex = (handlerNode.access & Opcodes.ACC_STATIC) != 0 ? 0 : 1;
                int displayContextIndex = modelIndex + 1;
                int transformationRefIndex = modelIndex + 2;

                handlerNode.parameters = new ArrayList<>();
                handlerNode.parameters.add(new ParameterNode("model", 0));
                handlerNode.parameters.add(new ParameterNode("displayContext", 0));
                handlerNode.parameters.add(new ParameterNode("transformationRef", 0));

                AnnotationNode localNode = new AnnotationNode("Lcom/llamalad7/mixinextras/sugar/Local;");
                localNode.values = new ArrayList<>();
                localNode.values.add("argsOnly");
                localNode.values.add(true);
                @SuppressWarnings("unchecked")
                List<AnnotationNode>[] invisibleParameterAnnotations = (List<AnnotationNode>[]) new List[3];
                if (handlerNode.invisibleParameterAnnotations != null && handlerNode.invisibleParameterAnnotations.length > 0) {
                    invisibleParameterAnnotations[0] = handlerNode.invisibleParameterAnnotations[0];
                }
                invisibleParameterAnnotations[1] = new ArrayList<>();
                invisibleParameterAnnotations[1].add(localNode);
                if (handlerNode.invisibleParameterAnnotations != null && handlerNode.invisibleParameterAnnotations.length > 3) {
                    invisibleParameterAnnotations[2] = handlerNode.invisibleParameterAnnotations[3];
                }
                handlerNode.invisibleParameterAnnotations = invisibleParameterAnnotations;

                if (handlerNode.visibleParameterAnnotations != null) {
                    @SuppressWarnings("unchecked")
                    List<AnnotationNode>[] visibleParameterAnnotations = (List<AnnotationNode>[]) new List[3];
                    System.arraycopy(handlerNode.visibleParameterAnnotations, 0, visibleParameterAnnotations, 0, Math.min(3, handlerNode.visibleParameterAnnotations.length));
                    handlerNode.visibleParameterAnnotations = visibleParameterAnnotations;
                }

                LocalVariableNode thisNode = null;
                if (handlerNode.localVariables != null) {
                    thisNode = handlerNode.localVariables.stream().filter(local -> local.index == 0).findFirst().orElse(null);
                    handlerNode.localVariables.clear();
                } else {
                    handlerNode.localVariables = new ArrayList<>();
                }

                InsnList instructions = new InsnList();
                LabelNode startLabel = new LabelNode();
                LabelNode elseLabel = new LabelNode();
                LabelNode endLabel = new LabelNode();
                instructions.add(startLabel);
                instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, Type.getType("Lca/fxco/moreculling/states/ItemRendererStates;").getInternalName(), "ITEM_FRAME", "Lnet/minecraft/world/entity/decoration/ItemFrame;"));
                instructions.add(new JumpInsnNode(Opcodes.IFNULL, elseLabel));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, transformationRefIndex));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, modelIndex));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/client/resources/model/BakedModel", "getTransforms", "()Lnet/minecraft/client/renderer/block/model/ItemTransforms;", true));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, displayContextIndex));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/block/model/ItemTransforms", "getTransform", "(Lnet/minecraft/world/item/ItemDisplayContext;)Lnet/minecraft/client/renderer/block/model/ItemTransform;", false));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "com/llamalad7/mixinextras/sugar/ref/LocalRef", "set", "(Ljava/lang/Object;)V", true));
                instructions.add(elseLabel);
                instructions.add(new VarInsnNode(Opcodes.ALOAD, modelIndex));
                instructions.add(new InsnNode(Opcodes.ARETURN));
                instructions.add(endLabel);
                handlerNode.instructions = instructions;

                if ((handlerNode.access & Opcodes.ACC_STATIC) == 0) {
                    String thisDescriptor = thisNode != null ? thisNode.desc : "L" + mixinClassName.replace('.', '/') + ";";
                    handlerNode.localVariables.add(new LocalVariableNode("this", thisDescriptor, null, startLabel, endLabel, 0));
                }
                handlerNode.localVariables.add(new LocalVariableNode("model", bakedModelType.getDescriptor(), null, startLabel, endLabel, modelIndex));
                handlerNode.localVariables.add(new LocalVariableNode("displayContext", displayContextType.getDescriptor(), null, startLabel, endLabel, displayContextIndex));
                handlerNode.localVariables.add(new LocalVariableNode("transformationRef", localRefType.getDescriptor(), null, startLabel, endLabel, transformationRefIndex));
                handlerNode.visibleLocalVariableAnnotations = null;
                handlerNode.invisibleLocalVariableAnnotations = null;
                handlerNode.tryCatchBlocks.clear();

                addNewHandler(mixinClassName, handlerNode);

                return mevNode;
            }
        }
        return annotationNode;
    }

    private void addNewHandler(String mixinClassName, MethodNode handlerNode) {
        ClassInfo info = ClassInfo.fromCache(mixinClassName);
        try {
            MethodUtils.invokeMethod(info, true, "addMethod", handlerNode);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error modifying ClassInfo for " + mixinClassName + '#' + handlerNode.name + handlerNode.desc, e.getCause());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error trying to modify ClassInfo for " + handlerNode + '#' + handlerNode.name + handlerNode.desc, e);
        }
    }
}
