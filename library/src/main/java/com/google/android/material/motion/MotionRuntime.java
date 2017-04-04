/*
 * Copyright 2016-present The Material Motion Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.material.motion;

import android.util.Property;

import com.google.android.indefinite.observable.IndefiniteObservable.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * A MotionRuntime writes the output of streams to properties and observes their overall state.
 */
public final class MotionRuntime {

  private final List<Subscription> subscriptions = new ArrayList<>();

  /**
   * Subscribes to the stream, writes its output to the given property, and observes its state.
   */
  public <O, T> void write(
    MotionObservable<T> stream, final O target, final Property<O, T> property) {
    write(stream, ReactiveProperty.of(target, property));
  }

  /**
   * Subscribes to the stream, writes its output to the given property, and observes its state.
   */
  public <T> void write(MotionObservable<T> stream, final ReactiveWritable<T> property) {
    subscriptions.add(stream.subscribe(new MotionObserver<T>() {

      @Override
      public void next(T value) {
        property.write(value);
      }
    }));
  }

  @SafeVarargs
  public final <O, T> void addInteraction(
    Interaction<O, T> interaction, O target, Operation<T, T>... constraints) {
    addInteraction(interaction, target, new ConstraintApplicator<>(constraints));
  }

  public final <O, T> void addInteraction(
    Interaction<O, T> interaction, O target, ConstraintApplicator<T> constraints) {
    interaction.apply(this, target, constraints);
  }
}