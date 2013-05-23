/*
 * License: see src/main/resources/org/jenkinsci/plugins/scalajunitnamedecoder/scala-license
 */

package org.jenkinsci.plugins.scalajunitnamedecoder;

//Translated from the Scala compiler: https://github.com/scala/scala/blob/8cdf3b3f51adff8dbeff5217505f74cfbedb55cd/src/library/scala/reflect/NameTransformer.scala
//This code is meant to match the original Scala code as closely as possible,
//to make it easy to verify that they are equivalent
public class ScalaNameDecoder {
    private static final int nops = 128;
    private static final int ncodes = 26 * 26;

    static class OpCodes {
        public final char op;
        public final String code;
        public final OpCodes next;

        public OpCodes(char op, String code, OpCodes next) {
            this.op = op;
            this.code = code;
            this.next = next;
        }
    }

    private static String[] op2code = new String[nops];
    private static OpCodes[] code2op = new OpCodes[ncodes];

    private static void enterOp(char op, String code) {
        op2code[op] = code;
        int c = (code.charAt(1) - 'a') * 26 + code.charAt(2) - 'a';
        code2op[c] = new OpCodes(op, code, code2op[c]);
    }

    static {
        /* Note: decoding assumes opcodes are only ever lowercase. */
        enterOp('~', "$tilde");
        enterOp('=', "$eq");
        enterOp('<', "$less");
        enterOp('>', "$greater");
        enterOp('!', "$bang");
        enterOp('#', "$hash");
        enterOp('%', "$percent");
        enterOp('^', "$up");
        enterOp('&', "$amp");
        enterOp('|', "$bar");
        enterOp('*', "$times");
        enterOp('/', "$div");
        enterOp('+', "$plus");
        enterOp('-', "$minus");
        enterOp(':', "$colon");
        enterOp('\\', "$bslash");
        enterOp('?', "$qmark");
        enterOp('@', "$at");
    }

    /** Replace `\$opname` by corresponding operator symbol.
     * 
     *  @param name0 the string to decode
     *  @return      the string with all recognized operator symbol encodings replaced with their name
     */
    public static String decode(String name0) {
        final String name = (name0.endsWith("<init>")) ? name0.substring(0, name0.length() - "<init>".length()) + "this" : name0; //alternate: StringUtils.substringBefore(name0, "<init>"), but let's do this with no dependencies
        StringBuilder buf = null;
        final int len = name.length();
        int i = 0;
        while (i < len) {
            OpCodes ops = null;
            boolean unicode = false;
            final char c = name.charAt(i);
            if (c == '$' && i + 2 < len) {
                final char ch1 = name.charAt(i + 1);
                if ('a' <= ch1 && ch1 <= 'z') {
                    final char ch2 = name.charAt(i + 2);
                    if ('a' <= ch2 && ch2 <= 'z') {
                        ops = code2op[(ch1 - 'a') * 26 + ch2 - 'a'];
                        while ((ops != null) && !name.startsWith(ops.code, i))
                            ops = ops.next;
                        if (ops != null) {
                            if (buf == null) {
                                buf = new StringBuilder();
                                buf.append(name.substring(0, i));
                            }
                            buf.append(ops.op);
                            i += ops.code.length();
                        }
                        /* Handle the decoding of Unicode glyphs that are not
                         * valid Java/JVM identifiers */
                    } else if ((len - i) >= 6 && // Check that there are enough characters left
                               ch1 == 'u' && 
                               ((Character.isDigit(ch2)) || 
                               ('A' <= ch2 && ch2 <= 'F'))) {
                        /* Skip past "$u", next four should be hexadecimal */
                        final String hex = name.substring(i + 2, i + 6);
                        try {
                            final char str = (char) Integer.parseInt(hex, 16);
                            if (buf == null) {
                                buf = new StringBuilder();
                                buf.append(name.substring(0, i));
                            }
                            buf.append(str);
                            /* 2 for "$u", 4 for hexadecimal number */
                            i += 6;
                            unicode = true;
                        } catch (NumberFormatException e) {
                            /* `hex` did not decode to a hexadecimal number, so
                             * do nothing. */
                        }
                    }
                }
            }
            /* If we didn't see an opcode or encoded Unicode glyph, and the
             * buffer is non-empty, write the current character and advance
             * one */
            if ((ops == null) && !unicode) {
                if (buf != null)
                    buf.append(c);
                i += 1;
            }
        }
        return (buf == null) ? name : buf.toString();
    }
}
