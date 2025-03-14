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

/**
 * Charset recognizer for UTF-8
 */
class CharsetRecog_UTF8 extends CharsetRecognizer {

    String getName() {
        return "UTF-8";
    }

    /* (non-Javadoc)
     * @see com.ibm.icu.text.CharsetRecognizer#match(com.ibm.icu.text.CharsetDetector)
     */
    CharsetMatch match(CharsetDetector det) {
        boolean hasBOM = false;
        int numValid = 0;
        int numInvalid = 0;
        byte input[] = det.fRawInput;
        int i;
        int trailBytes = 0;
        int confidence;

        if (det.fRawLength >= 3 &&
                (input[0] & 0xFF) == 0xef && (input[1] & 0xFF) == 0xbb && (input[2] & 0xFF) == 0xbf) {
            hasBOM = true;
        }

        // Scan for multi-byte sequences
        for (i = 0; i < det.fRawLength; i++) {
            int b = input[i];
            if ((b & 0x80) == 0) {
                continue;   // ASCII
            }

            // Hi bit on char found.  Figure out how long the sequence should be
            if ((b & 0x0e0) == 0x0c0) {
                trailBytes = 1;
            } else if ((b & 0x0f0) == 0x0e0) {
                trailBytes = 2;
            } else if ((b & 0x0f8) == 0xf0) {
                trailBytes = 3;
            } else {
                numInvalid++;
                continue;
            }

            // Verify that we've got the right number of trail bytes in the sequence
            for (; ; ) {
                i++;
                if (i >= det.fRawLength) {
                    break;
                }
                b = input[i];
                if ((b & 0xc0) != 0x080) {
                    numInvalid++;
                    break;
                }
                if (--trailBytes == 0) {
                    numValid++;
                    break;
                }
            }
        }

        // Cook up some sort of confidence score, based on presense of a BOM
        //    and the existence of valid and/or invalid multi-byte sequences.
        confidence = 0;
        if (hasBOM && numInvalid == 0) {
            confidence = 100;
        } else if (hasBOM && numValid > numInvalid * 10) {
            confidence = 80;
        } else if (numValid > 3 && numInvalid == 0) {
            confidence = 100;
        } else if (numValid > 0 && numInvalid == 0) {
            confidence = 80;
        } else if (numValid == 0 && numInvalid == 0) {
            // Plain ASCII. Confidence must be > 10, it's more likely than UTF-16, which
            //              accepts ASCII with confidence = 10.
            // TODO: add plain ASCII as an explicitly detected type.
            confidence = 15;
        } else if (numValid > numInvalid * 10) {
            // Probably corruput utf-8 data.  Valid sequences aren't likely by chance.
            confidence = 25;
        }
        return confidence == 0 ? null : new CharsetMatch(det, this, confidence);
    }

}
