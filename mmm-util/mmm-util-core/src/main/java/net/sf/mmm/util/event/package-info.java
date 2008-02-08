/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
/**
 * Contains utilities for event-handling.
 * <h2>Event Utilities</h2>
 * It is a common pattern to register a listener for events that notify of a 
 * specific situation. This pattern has been re-implemented thousands of times.
 * Java generics allow to stop to re-invent the wheel and implement the pattern
 * once in a generic way. This prevents you from typical mistakes as dead-locks
 * in listeners that want to de-register themselves during event-handling.<br>
 * Create your custom event by implementing the marker interface 
 * {@link net.sf.mmm.util.event.Event} or if suitable implementing
 * {@link net.sf.mmm.util.event.ChangeEvent}. Then the producer of the events 
 * only needs to extends {@link net.sf.mmm.util.event.AbstractEventSource} or
 * one of its sub-classes.
 */
package net.sf.mmm.util.event;

