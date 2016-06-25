package io.vithor.yamvpframework.core

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.*

/**
 * General IO stream manipulation utilities.
 *
 *
 * This class provides static utility methods for input/output operations.
 *
 *  * closeQuietly - these methods close a stream ignoring nulls and exceptions
 *  * toXxx/read - these methods read data from a stream
 *  * write - these methods write data to a stream
 *  * copy - these methods copy all the data from one stream to another
 *  * contentEquals - these methods compare the content of two streams
 *
 *
 *
 * The byte-to-char methods and char-to-byte methods involve a conversion step.
 * Two methods are provided in each case, one that uses the platform default
 * encoding and the other which allows you to specify an encoding. You are
 * encouraged to always specify an encoding because relying on the platform
 * default can lead to unexpected results, for example when moving from
 * development to production.
 *
 *
 * All the methods in this class that read a stream are buffered internally.
 * This means that there is no cause to use a `BufferedInputStream`
 * or `BufferedReader`. The default buffer size of 4K has been shown
 * to be efficient in tests.
 *
 *
 * Wherever possible, the methods in this class do *not* flush or close
 * the stream. This is to avoid making non-portable assumptions about the
 * streams' origin and further use. Thus the caller is still responsible for
 * closing streams after use.
 *
 *
 * Origin of code: Excalibur.

 * @author Peter Donald
 * *
 * @author Jeff Turner
 * *
 * @author Matthew Hawthorne
 * *
 * @author Stephen Colebourne
 * *
 * @author Gareth Davis
 * *
 * @author Ian Springer
 * *
 * @author Niall Pemberton
 * *
 * @author Sandy McArthur
 * *
 * @version $Id: IOUtils.java 481854 2006-12-03 18:30:07Z scolebourne $
 */
/**
 * Created by Vithorio Polten on 1/4/16.
 */
object YAIOUtils {
    // NOTE: This class is focussed on InputStream, OutputStream, Reader and
    // Writer. Each method should take at least one of these as a parameter,
    // or return one of them.
    /**
     * The system line separator string.
     */
    val LINE_SEPARATOR: String

    init {
        // avoid security issues
        val buf = StringWriter(4)
        val out = PrintWriter(buf)
        out.println()
        LINE_SEPARATOR = buf.toString()
    }

    /**
     * The default buffer size to use.
     */
    private val DEFAULT_BUFFER_SIZE = 1024 * 4


    // copy from InputStream
    //-----------------------------------------------------------------------

    /**
     * Copy bytes from an `InputStream` to an
     * `OutputStream`.
     *
     *
     * This method buffers the input internally, so there is no need to use a
     * `BufferedInputStream`.
     *
     *
     * Large streams (over 2GB) will return a bytes copied value of
     * `-1` after the copy has completed since the correct
     * number of bytes cannot be returned as an int. For large streams
     * use the `copyLarge(InputStream, OutputStream)` method.

     * @param input  the `InputStream` to read from
     * *
     * @param output the `OutputStream` to write to
     * *
     * @return the number of bytes copied
     * *
     * @throws NullPointerException if the input or output is null
     * *
     * @throws IOException          if an I/O error occurs
     * *
     * @throws ArithmeticException  if the byte count is too large
     * *
     * @since Commons IO 1.1
     */
    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream): Int {
        val count = copyLarge(input, output)
        if (count > Integer.MAX_VALUE) {
            return -1
        }
        return count.toInt()
    }

    @Throws(IOException::class)
    fun copy(src: File, dst: File): Long {
        val inStream = FileInputStream(src)
        val outStream = FileOutputStream(dst)
        try {
            val inChannel = inStream.channel
            val outChannel = outStream.channel
            return inChannel.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inStream.close()
            outStream.close()
        }
    }

    /**
     * Copy bytes from a large (over 2GB) `InputStream` to an
     * `OutputStream`.
     *
     *
     * This method buffers the input internally, so there is no need to use a
     * `BufferedInputStream`.

     * @param input  the `InputStream` to read from
     * *
     * @param output the `OutputStream` to write to
     * *
     * @return the number of bytes copied
     * *
     * @throws NullPointerException if the input or output is null
     * *
     * @throws IOException          if an I/O error occurs
     * *
     * @since Commons IO 1.3
     */
    @Throws(IOException::class)
    fun copyLarge(input: InputStream, output: OutputStream): Long {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var count: Long = 0
        var n = 0
        while (-1 != n) {
            output.write(buffer, 0, n)
            count += n.toLong()
            n = input.read(buffer)
        }
        return count
    }

    /**
     * Copy bytes from an `InputStream` to chars on a
     * `Writer` using the default character encoding of the platform.
     *
     *
     * This method buffers the input internally, so there is no need to use a
     * `BufferedInputStream`.
     *
     *
     * This method uses [InputStreamReader].

     * @param input  the `InputStream` to read from
     * *
     * @param output the `Writer` to write to
     * *
     * @throws NullPointerException if the input or output is null
     * *
     * @throws IOException          if an I/O error occurs
     * *
     * @since Commons IO 1.1
     */
    @Throws(IOException::class)
    fun copy(input: InputStream, output: Writer) {
        val `in` = InputStreamReader(input)
        copy(`in`, output)
    }

    /**
     * Copy bytes from an `InputStream` to chars on a
     * `Writer` using the specified character encoding.
     *
     *
     * This method buffers the input internally, so there is no need to use a
     * `BufferedInputStream`.
     *
     *
     * Character encoding names can be found at
     * [IANA](http://www.iana.org/assignments/character-sets).
     *
     *
     * This method uses [InputStreamReader].

     * @param input    the `InputStream` to read from
     * *
     * @param output   the `Writer` to write to
     * *
     * @param encoding the encoding to use, null means platform default
     * *
     * @throws NullPointerException if the input or output is null
     * *
     * @throws IOException          if an I/O error occurs
     * *
     * @since Commons IO 1.1
     */
    @Throws(IOException::class)
    fun copy(input: InputStream, output: Writer, encoding: String?) {
        if (encoding == null) {
            copy(input, output)
        } else {
            val `in` = InputStreamReader(input, encoding)
            copy(`in`, output)
        }
    }

    // copy from Reader
    //-----------------------------------------------------------------------

    /**
     * Copy chars from a `Reader` to a `Writer`.
     *
     *
     * This method buffers the input internally, so there is no need to use a
     * `BufferedReader`.
     *
     *
     * Large streams (over 2GB) will return a chars copied value of
     * `-1` after the copy has completed since the correct
     * number of chars cannot be returned as an int. For large streams
     * use the `copyLarge(Reader, Writer)` method.

     * @param input  the `Reader` to read from
     * *
     * @param output the `Writer` to write to
     * *
     * @return the number of characters copied
     * *
     * @throws NullPointerException if the input or output is null
     * *
     * @throws IOException          if an I/O error occurs
     * *
     * @throws ArithmeticException  if the character count is too large
     * *
     * @since Commons IO 1.1
     */
    @Throws(IOException::class)
    fun copy(input: Reader, output: Writer): Int {
        val count = copyLarge(input, output)
        if (count > Integer.MAX_VALUE) {
            return -1
        }
        return count.toInt()
    }

    /**
     * Copy chars from a large (over 2GB) `Reader` to a `Writer`.
     *
     *
     * This method buffers the input internally, so there is no need to use a
     * `BufferedReader`.

     * @param input  the `Reader` to read from
     * *
     * @param output the `Writer` to write to
     * *
     * @return the number of characters copied
     * *
     * @throws NullPointerException if the input or output is null
     * *
     * @throws IOException          if an I/O error occurs
     * *
     * @since Commons IO 1.3
     */
    @Throws(IOException::class)
    fun copyLarge(input: Reader, output: Writer): Long {
        val buffer = CharArray(DEFAULT_BUFFER_SIZE)
        var count: Long = 0
        var n = 0
        while (-1 != n) {
            output.write(buffer, 0, n)
            count += n.toLong()
            n = input.read(buffer)
        }
        return count
    }

    /**
     * Copy chars from a `Reader` to bytes on an
     * `OutputStream` using the default character encoding of the
     * platform, and calling flush.
     *
     *
     * This method buffers the input internally, so there is no need to use a
     * `BufferedReader`.
     *
     *
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     *
     *
     * This method uses [OutputStreamWriter].

     * @param input  the `Reader` to read from
     * *
     * @param output the `OutputStream` to write to
     * *
     * @throws NullPointerException if the input or output is null
     * *
     * @throws IOException          if an I/O error occurs
     * *
     * @since Commons IO 1.1
     */
    @Throws(IOException::class)
    fun copy(input: Reader, output: OutputStream) {
        val out = OutputStreamWriter(output)
        copy(input, out)
        // XXX Unless anyone is planning on rewriting OutputStreamWriter, we
        // have to flush here.
        out.flush()
    }

    /**
     * Copy chars from a `Reader` to bytes on an
     * `OutputStream` using the specified character encoding, and
     * calling flush.
     *
     *
     * This method buffers the input internally, so there is no need to use a
     * `BufferedReader`.
     *
     *
     * Character encoding names can be found at
     * [IANA](http://www.iana.org/assignments/character-sets).
     *
     *
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     *
     *
     * This method uses [OutputStreamWriter].

     * @param input    the `Reader` to read from
     * *
     * @param output   the `OutputStream` to write to
     * *
     * @param encoding the encoding to use, null means platform default
     * *
     * @throws NullPointerException if the input or output is null
     * *
     * @throws IOException          if an I/O error occurs
     * *
     * @since Commons IO 1.1
     */
    @Throws(IOException::class)
    fun copy(input: Reader, output: OutputStream, encoding: String?) {
        if (encoding == null) {
            copy(input, output)
        } else {
            val out = OutputStreamWriter(output, encoding)
            copy(input, out)
            // XXX Unless anyone is planning on rewriting OutputStreamWriter,
            // we have to flush here.
            out.flush()
        }
    }

}
