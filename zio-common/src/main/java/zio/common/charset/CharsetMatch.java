/*
 * Copyright 2023-2025 Alberto Paro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zio.common.charset;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * This class represents a charset that has been identified by a CharsetDetector
 * as a possible encoding for a set of input data.  From an instance of this
 * class, you can ask for a confidence level in the charset identification,
 * or for Java Reader or String to access the original byte data in Unicode form.
 * <p>
 * Instances of this class are created only by CharsetDetectors.
 * <p>
 * Note:  this class has a natural ordering that is inconsistent with equals.
 * The natural ordering is based on the match confidence value.
 *
 * @stable ICU 3.4
 */
public class CharsetMatch implements Comparable<CharsetMatch> {


    //
    //   Private Data
    //
    private int fConfidence;
    private byte[] fRawInput = null;     // Original, untouched input bytes.
    //  If user gave us a byte array, this is it.
    private int fRawLength;           // Length of data in fRawInput array.
    private InputStream fInputStream = null;  // User's input stream, or null if the user
    private String fCharsetName;         // The name of the charset this CharsetMatch
    //   represents.  Filled in by the recognizer.
    private String fLang;                // The language, if one was determined by

    /*
     *  Constructor.  Implementation internal
     */
    CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf) {
        fConfidence = conf;

        // The references to the original application input data must be copied out
        //   of the charset recognizer to here, in case the application resets the
        //   recognizer before using this CharsetMatch.
        if (det.fInputStream == null) {
            // We only want the existing input byte data if it came straight from the user,
            //   not if is just the head of a stream.
            fRawInput = det.fRawInput;
            fRawLength = det.fRawLength;
        }
        fInputStream = det.fInputStream;
        fCharsetName = rec.getName();
        fLang = rec.getLanguage();
    }

    /*
     *  Constructor.  Implementation internal
     */
    CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf, String csName, String lang) {
        fConfidence = conf;

        // The references to the original application input data must be copied out
        //   of the charset recognizer to here, in case the application resets the
        //   recognizer before using this CharsetMatch.
        if (det.fInputStream == null) {
            // We only want the existing input byte data if it came straight from the user,
            //   not if is just the head of a stream.
            fRawInput = det.fRawInput;
            fRawLength = det.fRawLength;
        }
        fInputStream = det.fInputStream;
        fCharsetName = csName;
        fLang = lang;
    }

    /**
     * Create a java.io.Reader for reading the Unicode character data corresponding
     * to the original byte data supplied to the Charset detect operation.
     * <p>
     * CAUTION:  if the source of the byte data was an InputStream, a Reader
     * can be created for only one matching char set using this method.  If more
     * than one charset needs to be tried, the caller will need to reset
     * the InputStream and create InputStreamReaders itself, based on the charset name.
     *
     * @return the Reader for the Unicode character data.
     * @stable ICU 3.4
     */
    public Reader getReader() {
        InputStream inputStream = fInputStream;

        if (inputStream == null) {
            inputStream = new ByteArrayInputStream(fRawInput, 0, fRawLength);
        }

        try {
            inputStream.reset();
            return new InputStreamReader(inputStream, getName());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Create a Java String from Unicode character data corresponding
     * to the original byte data supplied to the Charset detect operation.
     *
     * @return a String created from the converted input data.
     * @stable ICU 3.4
     */
    public String getString() throws java.io.IOException {
        return getString(-1);

    }

    /**
     * Create a Java String from Unicode character data corresponding
     * to the original byte data supplied to the Charset detect operation.
     * The length of the returned string is limited to the specified size;
     * the string will be trunctated to this length if necessary.  A limit value of
     * zero or less is ignored, and treated as no limit.
     *
     * @param maxLength The maximium length of the String to be created when the
     *                  source of the data is an input stream, or -1 for
     *                  unlimited length.
     * @return a String created from the converted input data.
     * @stable ICU 3.4
     */
    public String getString(int maxLength) throws java.io.IOException {
        String result = null;
        if (fInputStream != null) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            Reader reader = getReader();
            int max = maxLength < 0 ? Integer.MAX_VALUE : maxLength;
            int bytesRead = 0;

            while ((bytesRead = reader.read(buffer, 0, Math.min(max, 1024))) >= 0) {
                sb.append(buffer, 0, bytesRead);
                max -= bytesRead;
            }

            reader.close();

            return sb.toString();
        } else {
            String name = getName();
            /*
             * getName() may return a name with a suffix 'rtl' or 'ltr'. This cannot
             * be used to open a charset (e.g. IBM424_rtl). The ending '_rtl' or 'ltr'
             * should be stripped off before creating the string.
             */
            int startSuffix = name.indexOf("_rtl") < 0 ? name.indexOf("_ltr") : name.indexOf("_rtl");
            if (startSuffix > 0) {
                name = name.substring(0, startSuffix);
            }
            result = new String(fRawInput, name);
        }
        return result;

    }

    /**
     * Get an indication of the confidence in the charset detected.
     * Confidence values range from 0-100, with larger numbers indicating
     * a better match of the input data to the characteristics of the
     * charset.
     *
     * @return the confidence in the charset match
     * @stable ICU 3.4
     */
    public int getConfidence() {
        return fConfidence;
    }

    /**
     * Get the name of the detected charset.
     * The name will be one that can be used with other APIs on the
     * platform that accept charset names.  It is the "Canonical name"
     * as defined by the class java.nio.charset.Charset; for
     * charsets that are registered with the IANA charset registry,
     * this is the MIME-preferred registerd name.
     *
     * @return The name of the charset.
     * @stable ICU 3.4
     * @see java.nio.charset.Charset
     * @see java.io.InputStreamReader
     */
    public String getName() {
        return fCharsetName;
    }
    //   gave us a byte array.

    /**
     * Get the ISO code for the language of the detected charset.
     *
     * @return The ISO code for the language or <code>null</code> if the language cannot be determined.
     * @stable ICU 3.4
     */
    public String getLanguage() {
        return fLang;
    }

    /**
     * Compare to other CharsetMatch objects.
     * Comparison is based on the match confidence value, which
     * allows CharsetDetector.detectAll() to order its results.
     *
     * @param other the CharsetMatch object to compare against.
     * @return a negative integer, zero, or a positive integer as the
     * confidence level of this CharsetMatch
     * is less than, equal to, or greater than that of
     * the argument.
     * @throws ClassCastException if the argument is not a CharsetMatch.
     * @stable ICU 4.4
     */
    public int compareTo(CharsetMatch other) {
        int compareResult = 0;
        if (this.fConfidence > other.fConfidence) {
            compareResult = 1;
        } else if (this.fConfidence < other.fConfidence) {
            compareResult = -1;
        }
        return compareResult;
    }

    /**
     * compare this CharsetMatch to another based on confidence value
     * @param o the CharsetMatch object to compare against
     * @return true if equal
     */
    public boolean equals(Object o) {
        if (o instanceof CharsetMatch) {
            CharsetMatch that = (CharsetMatch) o;
            return (this.fConfidence == that.fConfidence);
        }

        return false;
    }

    /**
     * generates a hashCode based on the confidence value
     * @return the hashCode
     */
    public int hashCode() {
        return fConfidence;
    }
    //   gave us a byte array.

    public String toString() {
        String s = "Match of " + fCharsetName;
        if (getLanguage() != null) {
            s += " in " + getLanguage();
        }
        s += " with confidence " + fConfidence;
        return s;
    }
}
