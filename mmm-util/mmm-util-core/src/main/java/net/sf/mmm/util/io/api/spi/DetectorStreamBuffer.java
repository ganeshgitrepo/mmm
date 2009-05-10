/* $Id$
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.util.io.api.spi;

import net.sf.mmm.util.io.api.ByteArray;
import net.sf.mmm.util.io.api.ByteArrayBuffer;
import net.sf.mmm.util.io.api.ByteBuffer;

/**
 * This is the interface for a buffer of bytes from a stream. It allows to
 * consume such bytes in order to detect metadata or even manipulate the stream.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public interface DetectorStreamBuffer extends ByteBuffer {

  /**
   * This method removes the number of bytes given by <code>byteCount</code>
   * from the stream starting at the current position.
   * 
   * @see #skip(long)
   * 
   * @param byteCount is the number of bytes to remove. This value can be
   *        greater than the currently {@link #getBytesAvailable() available
   *        bytes}. You may supply {@link Long#MAX_VALUE} to remove the rest of
   *        the stream.
   */
  void remove(long byteCount);

  /**
   * This method skips the number of bytes given by <code>byteCount</code> in
   * the stream starting at the current position. The given number of bytes will
   * be untouched in stream (queued for the next {@link DetectorStreamProcessor
   * processor} in the chain).
   * 
   * @see java.io.InputStream#skip(long)
   * 
   * @param byteCount is the number of bytes to ignore. This value can be
   *        greater than the currently {@link #getBytesAvailable() available
   *        bytes}. You may supply {@link Long#MAX_VALUE} to ignore to the end
   *        of the stream.
   * @return the given <code>byteCount</code>.
   */
  long skip(long byteCount);

  /**
   * This method inserts the given <code>data</code> at the current position
   * into the stream.
   * 
   * @param data is a {@link ByteArray} with the data to insert.
   */
  void insert(ByteArray data);

  /* -- The following is to be reviewed/discussed/redesigned -- */

  /**
   * This method gets a buffer with
   * 
   * @return
   */
  ByteArrayBuffer getCurrentBuffer();

  /**
   * 
   * TODO: javadoc
   * 
   * @return <code>true</code> if a next {@link #getCurrentBuffer() buffer} is
   *         available and <code>false</code> if the last lookahead buffer has
   *         been consumed.
   */
  boolean stepNext();

  /**
   * 
   */
  void stepEnd();

  /**
   * This method gets the absolute position of the current pointer in bytes. The
   * value represents the number of bytes that have been consumed and/or
   * inserted. It excludes the number of bytes that have been removed.
   * 
   * TODO: change specification to position in the processed stream? Yep. Better
   * usability!
   * 
   * @return the current position in the stream.
   */
  long getStreamPosition();

}
