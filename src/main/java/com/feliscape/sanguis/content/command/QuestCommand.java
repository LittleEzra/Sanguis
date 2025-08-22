package com.feliscape.sanguis.content.command;

import com.feliscape.sanguis.content.attachment.HunterData;
import com.feliscape.sanguis.content.quest.requirement.QuestType;
import com.feliscape.sanguis.registry.custom.SanguisRegistries;
import com.feliscape.sanguis.util.HunterUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class QuestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralCommandNode<CommandSourceStack> literalcommandnode = dispatcher
                .register(Commands.literal("sanguis").then(Commands.literal("quest")
                        .requires((commandSourceStack) -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.literal("addrandom")
                                        .then(Commands.argument("type", ResourceLocationArgument.id())
                                        .executes(context -> addRandom(
                                                context.getSource(),
                                                EntityArgument.getPlayers(context, "targets"),
                                                ResourceLocationArgument.getId(context, "type")
                                                )
                                        )))
                        )
                ));

    }

    private static int addRandom(CommandSourceStack source, Collection<ServerPlayer> targets, ResourceLocation id) {
        QuestType<?> type = source.registryAccess().registryOrThrow(SanguisRegistries.Keys.QUEST_TYPES).get(id);
        if (type == null){
            source.sendFailure(
                    Component.translatable("commands.sanguis.quest.addrandom.failure.invalid_type", id.toString())
            );
            return 0;
        }

        for (ServerPlayer serverplayer : targets) {
            if (HunterUtil.isHunter(serverplayer)){
                serverplayer.getData(HunterData.type()).addQuest(type.factory()
                        .create(serverplayer.serverLevel()));
            }
        }

        if (targets.size() == 1) {
            source.sendSuccess(
                    () -> Component.translatable(
                            "commands.sanguis.quest.addrandom.success.single", id.toString(), targets.iterator().next().getDisplayName()
                    ),
                    true
            );
        } else {
            source.sendSuccess(
                    () -> Component.translatable("commands.sanguis.quest.addrandom.success.multiple", id.toString(), targets.size()), true
            );
        }

        return targets.size();
    }
}
