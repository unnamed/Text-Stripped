/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017-2019 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.text.serializer.plain;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.ComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;

/**
 * A plain component serializer.
 *
 * <p>Plain does <b>not</b> support more complex features such as, but not limited
 * to, colours, decorations, {@link ClickEvent}, and {@link HoverEvent}.</p>
 */
public class PlainComponentSerializer implements ComponentSerializer<Component, TextComponent, String> {
    /**
     * A component serializer for plain-based serialization and deserialization.
     */
    public static final PlainComponentSerializer INSTANCE = new PlainComponentSerializer();
    private final Function<TranslatableComponent, String> translatable;

    public PlainComponentSerializer() {
        this(component -> "");
    }

    public PlainComponentSerializer(final @NonNull Function<TranslatableComponent, String> translatable) {
        this.translatable = translatable;
    }

    @Override
    public @NonNull TextComponent deserialize(final @NonNull String input) {
        return TextComponent.of(input); //
    }

    @Override
    public @NonNull String serialize(final @NonNull Component component) {
        final StringBuilder sb = new StringBuilder();
        this.serialize(sb, component);
        return sb.toString();
    }

    public void serialize(final @NonNull StringBuilder sb, final @NonNull Component component) {
        if (component instanceof TextComponent) {
            sb.append(((TextComponent) component).content());
        } else if (component instanceof TranslatableComponent) {
            sb.append(this.translatable.apply((TranslatableComponent) component));
        } else {
            throw new IllegalArgumentException("Don't know how to turn " + component + " into a string");
        }

        for (final Component child : component.children()) {
            this.serialize(sb, child);
        }
    }
}
