package com.zanon.zanons_arsenal.weapon;

import java.util.function.Consumer;

import com.zanon.zanons_arsenal.registry.ModComponents;
import com.zanon.zanons_arsenal.registry.ModParticles;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MurasamaItem extends Item implements ArsenalItem {
    /** Tooltip attack damage. Vanilla adds the player's base 1 on top of the modifier. */
    public static final float MELEE_DAMAGE = 15.0F;
    /** Attacks per second. A vanilla sword is 1.6. */
    public static final double MELEE_SPEED = 2.2D;
    public static final float SKILL_DAMAGE = 50.0F;
    /** Ten seconds. */
    public static final int SKILL_COOLDOWN_TICKS = 200;
    public static final double SKILL_RANGE = 5.5D;
    /** About a 140 degree cone in front of the crosshair. */
    private static final double SKILL_CONE_DOT = 0.35D;

    private static final double PLAYER_BASE_ATTACK_DAMAGE = 1.0D;
    private static final double PLAYER_BASE_ATTACK_SPEED = 4.0D;

    private static final SwingAnimation SHEATHED_SWING = new SwingAnimation(SwingAnimationType.NONE, 6);
    private static final SwingAnimation DRAWN_SWING = new SwingAnimation(SwingAnimationType.WHACK, 5);

    private static final ItemAttributeModifiers SHEATHED_MODIFIERS = ItemAttributeModifiers.builder().build();
    private static final ItemAttributeModifiers DRAWN_MODIFIERS = ItemAttributeModifiers.builder()
            .add(
                    Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, MELEE_DAMAGE - PLAYER_BASE_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
            )
            .add(
                    Attributes.ATTACK_SPEED,
                    new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, MELEE_SPEED - PLAYER_BASE_ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
            )
            .build();

    public MurasamaItem(Properties properties) {
        super(properties);
    }

    public static boolean isSheathed(ItemStack stack) {
        return stack.getOrDefault(ModComponents.SHEATHED.get(), true);
    }

    public static void setSheathed(ItemStack stack, boolean sheathed) {
        stack.set(ModComponents.SHEATHED.get(), sheathed);
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, sheathed ? SHEATHED_MODIFIERS : DRAWN_MODIFIERS);
        stack.set(DataComponents.SWING_ANIMATION, sheathed ? SHEATHED_SWING : DRAWN_SWING);
    }

    @Override
    public WeaponDefinition definition() {
        return WeaponDefinition.MURASAMA;
    }

    @Override
    public boolean hasSkill(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasModes(ItemStack stack) {
        return true;
    }

    @Override
    public boolean allowsMelee(ItemStack stack) {
        return !isSheathed(stack);
    }

    @Override
    public double attackDamage(ItemStack stack) {
        return MELEE_DAMAGE;
    }

    @Override
    public double attackSpeed(ItemStack stack) {
        return MELEE_SPEED;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (isSheathed(player.getItemInHand(hand))) {
            return InteractionResult.PASS;
        }
        return super.use(level, player, hand);
    }

    @Override
    public void toggleMode(ServerPlayer player, ItemStack stack) {
        if (!isSheathed(stack)) {
            player.stopUsingItem();
        }
        setSheathed(stack, !isSheathed(stack));
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ARMOR_EQUIP_CHAIN.value(),
                SoundSource.PLAYERS,
                0.6F,
                isSheathed(stack) ? 0.85F : 1.25F
        );
    }

    @Override
    public void useSkill(ServerPlayer player, ItemStack stack) {
        if (!isSheathed(stack) || player.getCooldowns().isOnCooldown(stack)) {
            return;
        }
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(SKILL_RANGE))) {
            if (entity == player || !entity.isAlive() || !entity.isPickable()) {
                continue;
            }
            Vec3 center = entity.getBoundingBox().getCenter();
            double reach = SKILL_RANGE + entity.getBbWidth() * 0.5D;
            if (center.distanceToSqr(eye) > reach * reach) {
                continue;
            }
            Vec3 to = center.subtract(eye);
            if (to.lengthSqr() < 1.0E-4 || to.normalize().dot(look) < SKILL_CONE_DOT) {
                continue;
            }
            HitResult block = level.clip(new ClipContext(eye, center, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
            if (block.getType() != HitResult.Type.MISS) {
                continue;
            }
            if (entity.hurtServer(level, player.damageSources().playerAttack(player), SKILL_DAMAGE)) {
                spawnHitBurst(level, center);
            }
        }

        spawnSlash(level, player, look);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.1F, 1.45F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 0.7F, 1.6F);

        setSheathed(stack, false);
        player.swing(net.minecraft.world.InteractionHand.MAIN_HAND, true);
        player.getCooldowns().addCooldown(stack, SKILL_COOLDOWN_TICKS);
    }

    private static void spawnSlash(ServerLevel level, Player player, Vec3 look) {
        Vec3 flat = new Vec3(look.x, 0.0D, look.z);
        if (flat.lengthSqr() < 1.0E-4) {
            flat = new Vec3(0.0D, 0.0D, 1.0D);
        }
        flat = flat.normalize();
        Vec3 side = new Vec3(-flat.z, 0.0D, flat.x);
        Vec3 eye = player.getEyePosition().add(0.0D, -0.15D, 0.0D);

        Vec3 center = eye.add(look.scale(1.45D));
        level.sendParticles(ModParticles.RED_SLASH.get(), center.x, center.y, center.z, 0, side.x, 0.08D, side.z, 1.2D);
        level.sendParticles(
                ModParticles.RED_SLASH.get(),
                center.x + side.x * 0.35D,
                center.y + 0.22D,
                center.z + side.z * 0.35D,
                0,
                side.x * 0.55D,
                0.04D,
                side.z * 0.55D,
                0.62D
        );
    }

    private static void spawnHitBurst(ServerLevel level, Vec3 center) {
        level.sendParticles(ModParticles.RED_SLASH.get(), center.x, center.y, center.z, 0, 0.42D, 0.12D, 0.18D, 0.7D);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId()).withStyle(definition().rarity().color());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("rarity.zanons_arsenal.legendary").withStyle(definition().rarity().color()));
        tooltip.accept(Component.translatable(isSheathed(stack)
                ? "item.zanons_arsenal.murasama.sheathed"
                : "item.zanons_arsenal.murasama.drawn").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable(
                "item.zanons_arsenal.press_mode",
                Component.keybind("key.zanons_arsenal.mode")
        ).withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable(
                "item.zanons_arsenal.press_skill",
                Component.keybind("key.zanons_arsenal.skill")
        ).withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.zanons_arsenal.murasama.skill").withStyle(ChatFormatting.RED));
        if (isSheathed(stack)) {
            appendHeldStats(tooltip, flag);
        }
    }

    /** Sheathed stacks have no attack modifiers, so the held-stat lines are written here. Drawn stacks get them from the item component. */
    private static void appendHeldStats(Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(CommonComponents.EMPTY);
        tooltip.accept(Component.translatable("item.modifiers.mainhand").withStyle(ChatFormatting.GRAY));
        appendBaseStat(tooltip, Attributes.ATTACK_DAMAGE.value(), MELEE_DAMAGE, PLAYER_BASE_ATTACK_DAMAGE, flag);
        appendBaseStat(tooltip, Attributes.ATTACK_SPEED.value(), MELEE_SPEED, PLAYER_BASE_ATTACK_SPEED, flag);
    }

    private static void appendBaseStat(Consumer<Component> tooltip, Attribute attribute, double value, double entityBase, TooltipFlag flag) {
        tooltip.accept(Component.literal(" ")
                .append(attribute.toBaseComponent(value, entityBase, false, flag))
                .withStyle(ChatFormatting.DARK_GREEN));
    }
}
